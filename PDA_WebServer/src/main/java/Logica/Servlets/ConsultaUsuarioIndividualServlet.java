package Logica.Servlets;

import Logica.DTsClasses.DTUsuarioBase;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ConsultaUsuarioIndividualServlet", urlPatterns = {"/ConsultaUsuarioIndividualServlet"})
public class ConsultaUsuarioIndividualServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        boolean isJavaClient = request.getHeader("User-Agent") != null && request.getHeader("User-Agent").contains("Java");

        String nickname = request.getParameter("nickname");

        if (nickname != null && !nickname.isBlank()) {
            DTUsuarioBase usuario = control.ConsultarUsuario(nickname.trim());
            
            if (usuario == null) {
                if (isJavaClient) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado.");
                    return;
                }
                request.setAttribute("error", "No se encontró el usuario '" + nickname + "'.");
            } else {
                request.setAttribute("usuarioConsultado", usuario);
            }
        } else {
            if (isJavaClient) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro nickname requerido.");
                return;
            }
            request.setAttribute("error", "No se especificó un nickname para consultar.");
        }

        if (isJavaClient) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaUsuarioIndividual.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}