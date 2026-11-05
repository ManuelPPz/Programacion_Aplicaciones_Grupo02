/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica.Servlets;

import Logica.DTsClasses.DTMaster;
import Logica.DTsClasses.DTUsuarioBase;
import Logica.WebServer.LabService;
import Logica.WebServer.Server;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.ws.Service;
import java.io.IOException;
import java.util.List;

/**
 * Servlet para el Caso de Uso: Listar resultados de inscripciones a ediciones de cursos
 * Actor: Estudiante
 */
@WebServlet(name = "ListarResultadosInscripcionesServlet", urlPatterns = {"/ListarResultadosInscripcionesServlet"})
public class ListarResultadosInscripcionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Server server = new Server();
        Service service = server.getService();
        LabService cliente = server.getPortLabService(service);
        HttpSession session = request.getSession();

        try {
            // "usuarioLogueado" guarda un objeto de usuario (DT), no un String.
            Object usuario = session.getAttribute("usuarioLogueado");
            String nicknameEstudiante = null;

            if (usuario instanceof String) {
                nicknameEstudiante = (String) usuario;
            } else if (usuario instanceof DTUsuarioBase) {
                // Ajustá el nombre del getter si en tu DTUsuarioBase se llama distinto
                nicknameEstudiante = ((DTUsuarioBase) usuario).getNickname();
            }

            if (nicknameEstudiante != null && !nicknameEstudiante.trim().isEmpty()) {
                List<DTMaster> inscripciones = cliente.ListarInscripciones(nicknameEstudiante);
                request.setAttribute("inscripciones", inscripciones);
            } else {
                request.setAttribute("mensajeError", "Debe iniciar sesión como estudiante para ver sus inscripciones.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al cargar las inscripciones: " + e.getMessage());
        }

        request.getRequestDispatcher("/InterfacesJSP/ListarResultadosInscripciones.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}