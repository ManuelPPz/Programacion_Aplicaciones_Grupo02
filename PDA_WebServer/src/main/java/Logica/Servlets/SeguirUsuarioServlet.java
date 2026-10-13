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

@WebServlet(name = "SeguirUserServlet", urlPatterns = {"/SeguirUserServlet"})
public class SeguirUsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Verificamos quién está logueado en la sesión
        HttpSession session = request.getSession(false);
        DTMaster dtm = (session != null) ? (DTMaster) session.getAttribute("usuarioLogueado") : null;
        
        System.out.println("--------------------------------------------------");
        System.out.println("🔍 [DEBUG] Intentando procesar 'Seguir Usuario'");
        System.out.println("🔍 [DEBUG] Objeto dtm en sesión: " + (dtm != null ? dtm.getClass().getName() : "NULL"));

        if (dtm instanceof DTUsuarioBase usuarioLogueado) {
            System.out.println("🔍 [DEBUG] Nickname del usuario LOGUEADO: " + usuarioLogueado.getNickname());
            
            // 2. Verificamos el parámetro que viene del formulario HTML
            String nicknameAseguir = request.getParameter("nickname");
            System.out.println("🔍 [DEBUG] Nickname a SEGUIR recibido por parámetro: [" + nicknameAseguir + "]");

            if (nicknameAseguir != null && !nicknameAseguir.isBlank()) {
                try {
                    IController control = Fabric.GetInstance().GetIController();
                    
                    // Llamamos a la lógica
                    control.SeguirUsuario(usuarioLogueado.getNickname(), nicknameAseguir.trim());
                    System.out.println("✅ [DEBUG] control.SeguirUsuario() ejecutado correctamente.");
                    
                } catch (Exception e) {
                    System.out.println("❌ [DEBUG] Error al ejecutar control.SeguirUsuario:");
                    e.printStackTrace();
                }
                
                response.sendRedirect(request.getContextPath() + "/ConsultaUsuarioIndividualServlet?nickname=" + URLEncoder.encode(nicknameAseguir, StandardCharsets.UTF_8));
                return;
            } else {
                System.out.println("❌ [DEBUG] ¡El parámetro 'nickname' llegó NULO o VACÍO desde el formulario!");
            }
        } else {
            System.out.println("❌ [DEBUG] ¡No hay un usuario logueado válido en la sesión o expiró!");
        }
        System.out.println("--------------------------------------------------");
        
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}