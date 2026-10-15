package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTProgramaForm;
import Logica.DTsClasses.EnumDT;
import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * ED-26 – Consulta de Programa de Formación
 */
@WebServlet(name = "ConsultaProgramaServlet", urlPatterns = {"/ConsultaProgramaServlet"})
public class ConsultaProgramaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IController control = Fabric.GetInstance().GetIController();
        request.setAttribute("programas", control.ListarClase(EnumDT.DT_PROGRAMA));

        // Obtenemos tanto "programa" como "nombre" por seguridad y decodificamos caracteres especiales
        String programaRaw = request.getParameter("programa");
        if (programaRaw == null || programaRaw.isBlank()) {
            programaRaw = request.getParameter("nombre");
        }

        if (programaRaw != null && !programaRaw.isBlank()) {
            // Decodificar por si viene con codificación URL (%20, etc.) y hacer trim
            String programa = URLDecoder.decode(programaRaw, StandardCharsets.UTF_8).trim();
            
            System.out.println(">>> [DEBUG SERVLET] Buscando detalle para el programa: [" + programa + "]");
            
            DTMaster detalle = control.ConsultaProgramaFormacion(programa);
            
            if (detalle instanceof DTProgramaForm dtp) {
                System.out.println(">>> [DEBUG SERVLET] ¡Programa encontrado con éxito!");
                request.setAttribute("programaDetalle", dtp);
            } else {
                System.out.println(">>> [DEBUG SERVLET] control.ConsultaProgramaFormacion devolvió null o un tipo incorrecto.");
                request.setAttribute("error", "No se encontró el programa '" + programa + "'.");
            }
        }

        request.getRequestDispatcher("InterfacesJSP/ConsultaProgramaFormacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}