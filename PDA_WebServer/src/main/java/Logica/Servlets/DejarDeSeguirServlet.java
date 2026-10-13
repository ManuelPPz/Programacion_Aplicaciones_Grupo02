package Logica.Servlets;

import Logica.DTsClasses.DTUsuarioBase;
import Logica.DTsClasses.DTMaster;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "DejarDeSeguirServlet", urlPatterns = {"/DejarDeSeguirServlet"})
public class DejarDeSeguirServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        DTMaster dtm = (session != null) ? (DTMaster) session.getAttribute("usuarioLogueado") : null;

        if (dtm instanceof DTUsuarioBase usuarioLogueado) {
            String nicknameDejar = request.getParameter("nickname");

            if (nicknameDejar != null && !nicknameDejar.isBlank()) {
                try {
                    IController control = Fabric.GetInstance().GetIController();
                    control.DejarDeSeguir(usuarioLogueado.getNickname(), nicknameDejar.trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                response.sendRedirect(request.getContextPath() + "/ConsultaUsuarioIndividualServlet?nickname=" + URLEncoder.encode(nicknameDejar, StandardCharsets.UTF_8));
                return;
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}