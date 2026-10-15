package Logica.Servlets;

import Logica.DTsClasses.DTCurso;
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
import java.lang.reflect.Method;
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

            // 1. Búsqueda general en los Enums estándar (Usuarios, Cursos, Programas, etc.)
            for (EnumDT enumType : EnumDT.values()) {
                try {
                    List<DTMaster> elementos = control.ListarClase(enumType);
                    if (elementos != null) {
                        for (DTMaster m : elementos) {
                            if (m == null) continue;
                            
                            // Si es un curso, aprovechamos para buscar también sus ediciones asociadas
                            if (m instanceof DTCurso curso) {
                                procesarItem(curso, q, request, json, first);
                                if (json.length() > 1 && first) first = false;
                                
                                // Buscar ediciones de este curso (igual que en MainMenu)
                                try {
                                    List<DTMaster> listaEdiciones = control.ListarEdiciones(curso.getNombre());
                                    if (listaEdiciones != null) {
                                        for (DTMaster edMaster : listaEdiciones) {
                                            if (edMaster != null) {
                                                procesarItemEdicion(edMaster, q, request, json, first);
                                                if (json.length() > 1 && first) first = false;
                                            }
                                        }
                                    }
                                } catch (Exception ignored) {}
                            } else {
                                procesarItem(m, q, request, json, first);
                                if (json.length() > 1 && first) first = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignorar errores individuales por enum
                }
            }

            json.append("]");
            out.print(json.toString());
        }
    }

    private void procesarItem(DTMaster m, String q, HttpServletRequest request, StringBuilder json, boolean first) {
        try {
            String nombreItem = "";
            String tipoItem = "Elemento";
            String urlItem = "#";

            if (m instanceof DTUsuarioBase u) {
                String nombreCompleto = u.getNombre() + " " + u.getApellido() + " (" + u.getNickname() + ")";
                if (nombreCompleto.toLowerCase().contains(q) || u.getNickname().toLowerCase().contains(q)) {
                    tipoItem = "Usuario";
                    nombreItem = u.getNombre() + " " + u.getApellido() + " (@" + u.getNickname() + ")";
                    urlItem = request.getContextPath() + "/ConsultaUsuarioIndividualServlet?nickname=" + java.net.URLEncoder.encode(u.getNickname(), "UTF-8");
                }
            } else {
                String nombre = obtenerNombreObjeto(m);
                if (nombre != null && nombre.toLowerCase().contains(q)) {
                    nombreItem = nombre;
                    String classSimpleName = m.getClass().getSimpleName().toLowerCase();
                    
                    if (classSimpleName.contains("curso")) {
                        tipoItem = "Curso";
                        urlItem = request.getContextPath() + "/ConsultaCursoServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                    } else if (classSimpleName.contains("programa") || classSimpleName.contains("dtprogram")) {
                        tipoItem = "Programa";
                        urlItem = request.getContextPath() + "/ConsultaProgramaServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                    } else {
                        tipoItem = "General";
                        urlItem = "#";
                    }
                }
            }

            if (!nombreItem.isEmpty()) {
                String itemJson = "{\"tipo\":\"" + tipoItem + "\", \"nombre\":\"" + escapeJson(nombreItem) + "\", \"url\":\"" + urlItem + "\"}";
                if (!json.toString().contains(itemJson)) {
                    if (!first && json.length() > 2) json.append(",");
                    json.append(itemJson);
                }
            }
        } catch (Exception ignored) {}
    }

    private void procesarItemEdicion(DTMaster edMaster, String q, HttpServletRequest request, StringBuilder json, boolean first) {
        try {
            String nombre = obtenerNombreObjeto(edMaster);
            if (nombre != null && nombre.toLowerCase().contains(q)) {
                String urlItem = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                String itemJson = "{\"tipo\":\"Edición\", \"nombre\":\"" + escapeJson(nombre) + "\", \"url\":\"" + urlItem + "\"}";
                if (!json.toString().contains(itemJson)) {
                    if (!first && json.length() > 2) json.append(",");
                    json.append(itemJson);
                }
            }
        } catch (Exception ignored) {}
    }

    private String obtenerNombreObjeto(Object m) {
        String[] metodosAGestionar = {"getNombre", "getNombreEdicion", "getTitulo"};
        for (String nombreMetodo : metodosAGestionar) {
            try {
                Method method = m.getClass().getMethod(nombreMetodo);
                Object resultado = method.invoke(m);
                if (resultado != null) {
                    return resultado.toString();
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private String escapeJson(String str) {
        return str.replace("\"", "\\\"").replace("\n", " ").replace("\r", " ");
    }
}