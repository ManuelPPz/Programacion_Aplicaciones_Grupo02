package Logica.Servlets;

import Logica.DTsClasses.DTEdicionCurso;
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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * ED-22 – Consulta de Edición de Curso GET con ?curso=X: Trae las ediciones de
 * ese curso. GET con ?edicion=X o nombre: Trae el detalle completo de la
 * edición seleccionada y precarga su curso.
 */
@WebServlet(name = "ConsultaEdicionServlet", urlPatterns = {"/ConsultaEdicionServlet"})
public class ConsultaEdicionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        HttpSession session = request.getSession(false);
        DTMaster usuarioLogueado = (session != null) ? (DTMaster) session.getAttribute("usuarioLogueado") : null;

        // 1. Siempre disponemos la lista completa de cursos para el primer selector
        request.setAttribute("cursos", control.ListarClase(EnumDT.DT_CURSO));

        String curso = request.getParameter("curso");
        String edicion = request.getParameter("edicion");

        // Soporte adicional por si viene como ?nombre=... desde los enlaces de perfil
        if (edicion == null || edicion.isBlank()) {
            edicion = request.getParameter("nombre");
        }

        // 2. Si viene una edición directamente
        if (edicion != null && !edicion.isBlank()) {
            DTMaster detalle = control.ConsultaEdicionCurso(edicion.trim());
            if (detalle instanceof DTEdicionCurso dte) {
                // Depuración para verificar en la consola si la imagen viaja desde el controlador
                System.out.println("--- CONSULTA EDICIÓN SERVLET ---");
                System.out.println("Edición consultada: " + dte.getNombre());
                System.out.println("Imagen obtenida del DTE: [" + dte.getImage() + "]");
                System.out.println("---------------------------------");

                request.setAttribute("edicionDetalle", dte);
                request.setAttribute("edicionSeleccionada", dte.getNombre());

                curso = dte.getCurso();
                request.setAttribute("cursoSeleccionado", curso);

                if (usuarioLogueado != null) {
                    try {
                        boolean yaInscripto = false; // Ajusta aquí tu lógica de verificación si la tienes
                        request.setAttribute("yaInscripto", yaInscripto);
                    } catch (Exception e) {
                        request.setAttribute("yaInscripto", false);
                    }
                }
            } else {
                request.setAttribute("error", "No se encontró la edición '" + edicion + "'.");
            }
        }

        // 3. Carga de ediciones para la vista
        if (curso != null && !curso.isBlank()) {
            // Si hay un curso específico, listamos solo sus ediciones
            List<DTMaster> ediciones = control.ListarEdiciones(curso.trim());
            request.setAttribute("ediciones", ediciones);
            request.setAttribute("cursoSeleccionado", curso.trim());
        } else {
            // SI NO HAY CURSO NI EDICIÓN: Listamos TODAS las ediciones de todos los cursos para el menú general
            List<DTMaster> todasLasEdiciones = new java.util.ArrayList<>();
            List<DTMaster> listaCursos = control.ListarClase(EnumDT.DT_CURSO);
            if (listaCursos != null) {
                for (DTMaster m : listaCursos) {
                    if (m instanceof Logica.DTsClasses.DTCurso c) {
                        List<DTMaster> edCurso = control.ListarEdiciones(c.getNombre());
                        if (edCurso != null) {
                            todasLasEdiciones.addAll(edCurso);
                        }
                    }
                }
            }
            request.setAttribute("ediciones", todasLasEdiciones);
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaEdicionCurso.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("inscribirDirecto".equals(action)) {
            response.setContentType("text/plain; charset=UTF-8");

            HttpSession session = request.getSession(false);
            DTMaster usuario = (session != null) ? (DTMaster) session.getAttribute("usuarioLogueado") : null;
            String edicion = request.getParameter("edicion");

            if (usuario == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Debes iniciar sesión para inscribirte.");
                return;
            }
            if (usuario.getClass().getSimpleName().toLowerCase().contains("docente")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Los docentes no pueden inscribirse a ediciones.");
                return;
            }
            if (edicion == null || edicion.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Falta la edición.");
                return;
            }

            try {
                IController control = Fabric.GetInstance().GetIController();
                String nickname = ((DTUsuarioBase) usuario).getNickname();
                control.InscripcionAEdicionCurso(edicion.trim(), nickname, new Date());
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("OK");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage() != null ? e.getMessage() : "No se pudo completar la inscripción.");
            }
            return;
        }

        doGet(request, response);
    }
}