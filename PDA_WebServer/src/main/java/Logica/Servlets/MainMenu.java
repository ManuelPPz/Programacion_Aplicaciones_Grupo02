package Logica.Servlets;

import Logica.DTsClasses.DTCurso;
import Logica.DTsClasses.DTEdicionCurso;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet(name = "MainMenu", urlPatterns = {"/MainMenu"})
public class MainMenu extends HttpServlet {
    IController ico;
    
    /*----------------Funciones de carga de lista de datos para el request-----------------*/
    private void CargarInstitutosEnSesion(HttpServletRequest request) {
        ico = Fabric.GetInstance().GetIController();
        List<DTMaster> auxListInstituto = ico.ListarClase(EnumDT.DT_INSTITUTO);
        if (auxListInstituto != null) {
            request.getSession().setAttribute("AuxListInstituto", auxListInstituto);
        }
    }
    
    private void CargarCategoriasEnSesion(HttpServletRequest request){
        ico = Fabric.GetInstance().GetIController();
        List<DTMaster> auxListCategoria = ico.ListarClase(EnumDT.DT_CATEGORIA);
        if (auxListCategoria != null) {
            request.getSession().setAttribute("AuxListCategoria", auxListCategoria);
        }
    }

    /**
     * Filtra los elementos (cursos, ediciones y programas) creados en los últimos 30 días
     */
    private void CargarNovedadesRecientes(HttpServletRequest request) {
        ico = Fabric.GetInstance().GetIController();
        List<DTMaster> novedades = new ArrayList<>();
        long treintaDiasMillis = 30L * 24L * 60L * 60L * 1000L;
        long ahora = System.currentTimeMillis();

        // 1. Filtrar Cursos recientes (< 30 días)
        List<DTMaster> listaCursos = ico.ListarClase(EnumDT.DT_CURSO);
        if (listaCursos != null) {
            for (DTMaster m : listaCursos) {
                if (m instanceof DTCurso curso) {
                    Date fAlta = curso.getFechaAlta();
                    if (fAlta != null && (ahora - fAlta.getTime()) <= treintaDiasMillis) {
                        novedades.add(curso);
                    }
                }
            }
        }

        // 2. Filtrar Ediciones recientes recorriendo los cursos
        if (listaCursos != null) {
            for (DTMaster m : listaCursos) {
                if (m instanceof DTCurso curso) {
                    List<DTMaster> listaEdicionesCurso = ico.ListarEdiciones(curso.getNombre());
                    if (listaEdicionesCurso != null) {
                        for (DTMaster edMaster : listaEdicionesCurso) {
                            if (edMaster instanceof DTEdicionCurso edicion) {
                                Date fAltaEd = edicion.getFechaAlta();
                                if (fAltaEd != null && (ahora - fAltaEd.getTime()) <= treintaDiasMillis) {
                                    if (!novedades.contains(edicion)) {
                                        novedades.add(edicion);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Filtrar Programas de Formación recientes (< 30 días)
        try {
            EnumDT enumProg = null;
            for (EnumDT e : EnumDT.values()) {
                if (e.name().contains("PROGRAMA")) {
                    enumProg = e;
                    break;
                }
            }
            if (enumProg != null) {
                List<DTMaster> listaProgramas = ico.ListarClase(enumProg);
                if (listaProgramas != null) {
                    for (DTMaster progMaster : listaProgramas) {
                        if (progMaster != null) {
                            Date fAltaProg = null;
                            for (String mFecha : new String[]{"getFechaAlta", "getFechaCreacion", "getFechaIngresado", "getFecha"}) {
                                try {
                                    Object fObj = progMaster.getClass().getMethod(mFecha).invoke(progMaster);
                                    if (fObj instanceof Date) {
                                        fAltaProg = (Date) fObj;
                                        break;
                                    }
                                } catch (Exception ex) {}
                            }

                            if (fAltaProg == null || (ahora - fAltaProg.getTime()) <= treintaDiasMillis) {
                                if (!novedades.contains(progMaster)) {
                                    novedades.add(progMaster);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("novedadesRecientes", novedades);
    }
    /*--------------------------------------------------------------------------------------*/
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("ingVisitantes".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        
        // Cargar listas y novedades en la sesión/request
        CargarInstitutosEnSesion(request);
        CargarCategoriasEnSesion(request);
        CargarNovedadesRecientes(request);

        // Despachar al JSP respetando la sesión actual
        request.getRequestDispatcher("/InterfacesJSP/MenuInicio.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ico = Fabric.GetInstance().GetIController();
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        DTMaster dt = ico.ConsultaUsuarioAvanzada(email, password);
        
        if (dt != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", dt);
            
            CargarInstitutosEnSesion(request);
            CargarCategoriasEnSesion(request);
            CargarNovedadesRecientes(request);
            
            request.getRequestDispatcher("/InterfacesJSP/MenuInicio.jsp").forward(request, response);
        } else {
            request.setAttribute("errorLogin", "El usuario o la contraseña son incorrectos.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet del Menú Principal";
    }
}