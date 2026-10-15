package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "BuscadorLiveServlet", urlPatterns = {"/BuscadorLiveServlet"})
public class BuscadorLiveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String query = request.getParameter("q");

        try (PrintWriter out = response.getWriter()) {
            if (query == null || query.trim().length() < 2) {
                out.print("[]");
                return;
            }

            String q = query.trim().toLowerCase();
            IController control = Fabric.GetInstance().GetIController();
            
            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            // Recorremos los enums disponibles en EnumDT que maneja tu controlador con ListarClase
            // (Ej: Buscamos usuarios, cursos y programas usando los listados generales del IController)
            for (EnumDT enumType : EnumDT.values()) {
                try {
                    List<DTMaster> elementos = control.ListarClase(enumType);
                    if (elementos != null) {
                        for (DTMaster m : elementos) {
                            if (m == null) continue;
                            
                            String nombreItem = "";
                            String tipoItem = "Elemento";
                            String urlItem = "#";

                            // Identificamos el tipo de objeto según sus propiedades o instancia
                            if (m instanceof DTUsuarioBase u) {
                                String nombreCompleto = u.getNombre() + " " + u.getApellido() + " (" + u.getNickname() + ")";
                                if (nombreCompleto.toLowerCase().contains(q) || u.getNickname().toLowerCase().contains(q)) {
                                    tipoItem = "Usuario";
                                    nombreItem = u.getNombre() + " " + u.getApellido() + " (@" + u.getNickname() + ")";
                                    urlItem = request.getContextPath() + "/ConsultaUsuarioIndividualServlet?nickname=" + java.net.URLEncoder.encode(u.getNickname(), "UTF-8");
                                }
                            } else {
                                // Para Cursos, Ediciones o Programas que tengan el método getNombre()
                                try {
                                    java.lang.reflect.Method getNombreMethod = m.getClass().getMethod("getNombre");
                                    String nombre = (String) getNombreMethod.invoke(m);
                                    
                                    if (nombre != null && nombre.toLowerCase().contains(q)) {
                                        nombreItem = nombre;
                                        String className = m.getClass().getSimpleName();
                                        
                                        if (className.contains("Curso")) {
                                            tipoItem = "Curso";
                                            urlItem = request.getContextPath() + "/ConsultaCursoServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                        } else if (className.contains("Edicion")) {
                                            tipoItem = "Edición";
                                            urlItem = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                        } else if (className.contains("Programa") || className.contains("DTProgram")) {
                                            tipoItem = "Programa";
                                            urlItem = request.getContextPath() + "/ConsultaProgramaServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                        } else {
                                            tipoItem = "General";
                                            urlItem = "#";
                                        }
                                    }
                                } catch (Exception ex) {
                                    // Si el DTMaster no tiene getNombre, se ignora de forma segura
                                }
                            }

                            // Si encontró coincidencia y no se ha agregado antes en este ciclo
                            if (!nombreItem.isEmpty()) {
                                if (!first) json.append(",");
                                json.append("{\"tipo\":\"").append(tipoItem).append("\", \"nombre\":\"").append(escapeJson(nombreItem)).append("\", \"url\":\"").append(urlItem).append("\"}");
                                first = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignora si un Enum específico da error en ListarClase
                }
            }

            // También aseguramos incluir los programas mediante ListarProgramaDeForm()
            try {
                List<DTMaster> programas = control.ListarClase(EnumDT.DT_PROGRAMA);
                if (programas != null) {
                    for (DTMaster m : programas) {
                        try {
                            java.lang.reflect.Method getNombreMethod = m.getClass().getMethod("getNombre");
                            String nombre = (String) getNombreMethod.invoke(m);
                            if (nombre != null && nombre.toLowerCase().contains(q)) {
                                if (!first) json.append(",");
                                json.append("{\"tipo\":\"Programa\", \"nombre\":\"").append(escapeJson(nombre)).append("\", \"url\":\"").append(request.getContextPath()).append("/ConsultaProgramaServlet?nombre=").append(java.net.URLEncoder.encode(nombre, "UTF-8")).append("\"}");
                                first = false;
                            }
                        } catch (Exception e) {}
                    }
                }
            } catch (Exception e) {}

            json.append("]");
            out.print(json.toString());
        }
    }

    private String escapeJson(String str) {
        return str.replace("\"", "\\\"").replace("\n", " ").replace("\r", " ");
    }
}