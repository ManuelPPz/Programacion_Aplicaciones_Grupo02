package Logica.Servlets;

import Logica.Logica.Fabric;
import Logica.Logica.IController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Requerimiento 7.5: verificación en segundo plano (AJAX) de la disponibilidad
 * del nickname y del correo mientras el usuario completa el Alta de Usuario.
 *
 * GET ?tipo=nickname|correo&valor=X
 * Responde JSON: {"disponible": true|false, "mensaje": "...", "sugerencia": "nick2"|null}
 */
@WebServlet(name = "VerificarDisponibilidadServlet", urlPatterns = {"/VerificarDisponibilidadServlet"})
public class VerificarDisponibilidadServlet extends HttpServlet {

    /** Cuántas variantes "nick2", "nick3", ... se prueban para sugerir un nickname libre. */
    static final int MAX_SUGERENCIAS = 20;

    /** Punto de acceso al Servidor Central (se sobreescribe en los tests). */
    protected IController control() {
        return Fabric.GetInstance().GetIController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        String valor = request.getParameter("valor");
        valor = (valor == null) ? "" : valor.trim();

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");

        if (!"nickname".equals(tipo) && !"correo".equals(tipo)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(json(false, "Tipo de verificación no válido.", null));
            return;
        }

        try {
            String resultado = "nickname".equals(tipo) ? verificarNickname(valor) : verificarCorreo(valor);
            response.getWriter().write(resultado);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(json(false, "No se pudo verificar en este momento.", null));
        }
    }

    private String verificarNickname(String nick) {
        if (nick.isEmpty()) {
            return json(false, "El nickname es obligatorio.", null);
        }
        if (nick.matches(".*\\s.*")) {
            return json(false, "El nickname no puede contener espacios en blanco.", null);
        }
        IController ic = control();
        if (!ic.VerificarNickname(nick)) {
            return json(true, "El nickname \"" + nick + "\" está disponible.", null);
        }
        String sugerencia = null;
        for (int i = 2; i < 2 + MAX_SUGERENCIAS && sugerencia == null; i++) {
            if (!ic.VerificarNickname(nick + i)) {
                sugerencia = nick + i;
            }
        }
        return json(false, "El nickname \"" + nick + "\" ya está en uso.", sugerencia);
    }

    private String verificarCorreo(String correo) {
        if (correo.isEmpty()) {
            return json(false, "El correo electrónico es obligatorio.", null);
        }
        if (!correo.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            return json(false, "El correo electrónico no tiene un formato válido.", null);
        }
        if (control().VerificarCorreo(correo)) {
            return json(false, "El correo \"" + correo + "\" ya está registrado.", null);
        }
        return json(true, "El correo \"" + correo + "\" está disponible.", null);
    }

    static String json(boolean disponible, String mensaje, String sugerencia) {
        return "{\"disponible\":" + disponible
                + ",\"mensaje\":" + texto(mensaje)
                + ",\"sugerencia\":" + (sugerencia == null ? "null" : texto(sugerencia)) + "}";
    }

    /** Cadena JSON escapada. */
    private static String texto(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20 || c == '<' || c == '>') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.append('"').toString();
    }
}
