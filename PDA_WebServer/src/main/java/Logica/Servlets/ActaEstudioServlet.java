package Logica.Servlets;

import Logica.DTsClasses.DTActaEstudio;
import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuario;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import Logica.util.ActaPdf;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Caso de uso: Tramitar Acta de Estudio. Actor: Estudiante.
 *
 * GET ?programa=X : genera (en el momento) el PDF del acta del estudiante logueado
 * para el programa X y lo devuelve como descarga. Si no se puede, vuelve al perfil
 * con el error.
 */
@WebServlet(name = "ActaEstudioServlet", urlPatterns = {"/ActaEstudioServlet"})
public class ActaEstudioServlet extends HttpServlet {

    private static final String LOGO = "/Images/Logo edEXT sin fondo.png";

    /** Punto de acceso al Servidor Central (se sobreescribe en los tests). */
    protected IController control() {
        return Fabric.GetInstance().GetIController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object u = (session != null) ? session.getAttribute("usuarioLogueado") : null;
        if (!(u instanceof DTUsuario estudiante)) {
            response.sendRedirect(request.getContextPath() + "/IniciarSesionServlet");
            return;
        }

        String programa = request.getParameter("programa");
        try {
            if (programa == null || programa.isBlank()) {
                throw new Exception("Elegí un programa de formación.");
            }
            DTMaster dt = control().ActaDeEstudio(estudiante.getNickname(), programa.trim());
            if (!(dt instanceof DTActaEstudio acta)) {
                throw new Exception("No se pudo obtener el acta del programa.");
            }
            // Se arma completo en memoria: si falla, todavía se puede redirigir con el error.
            ByteArrayOutputStream pdf = new ByteArrayOutputStream();
            ActaPdf.escribir(acta, logo(), pdf);

            String archivo = ActaPdf.nombreArchivo(acta);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + archivo + "\"");
            response.setHeader("Cache-Control", "no-store");
            response.setContentLength(pdf.size());
            pdf.writeTo(response.getOutputStream());
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/ConsultaUsuarioIndividualServlet?nickname="
                    + url(estudiante.getNickname()) + "&errorActa=" + url(e.getMessage()));
        }
    }

    private byte[] logo() {
        try (InputStream in = getServletContext().getResourceAsStream(LOGO)) {
            return (in != null) ? in.readAllBytes() : null;
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static String url(String s) {
        return URLEncoder.encode(s != null ? s : "", StandardCharsets.UTF_8);
    }
}
