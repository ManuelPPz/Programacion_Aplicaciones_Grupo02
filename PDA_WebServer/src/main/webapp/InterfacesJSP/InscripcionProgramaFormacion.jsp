<%@page import="Logica.DTsClasses.DTProgramaForm"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    // Escapa texto para mostrarlo dentro del HTML
    private static String html(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    String ctx = request.getContextPath();
    List<DTMaster> programas = (List<DTMaster>) request.getAttribute("programas");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Inscripción a Programa de Formación</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
            <div class="card border-dark shadow p-3 p-md-4 w-100" style="max-width: 550px;">
                <h2 class="card-title text-center mb-4 fw-bold fs-3">Inscripción a Programa de Formación</h2>

                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger text-center"><%= html(request.getAttribute("error")) %></div>
                <% } %>
                <% if (request.getAttribute("exito") != null) { %>
                    <div class="alert alert-success text-center"><%= html(request.getAttribute("exito")) %></div>
                <% } %>

                <% if (programas == null || programas.isEmpty()) { %>
                    <div class="alert alert-secondary text-center mb-0">
                        No hay programas de formación registrados en el sistema.
                    </div>
                <% } else { %>
                    <form action="<%= ctx %>/InscripcionProgramaServlet" method="POST">
                        <label for="programa" class="form-label fw-bold">Seleccionar programa</label>
                        <select name="programa" id="programa" class="form-select border-dark mb-4" required>
                            <option value="" disabled selected>-- Seleccione un programa --</option>
                            <% for (DTMaster dt : programas) {
                                   if (dt instanceof DTProgramaForm) { DTProgramaForm p = (DTProgramaForm) dt; %>
                                <option value="<%= html(p.getNombre()) %>"><%= html(p.getNombre()) %></option>
                            <%     }
                               } %>
                        </select>
                        <button type="submit" class="btn btn-dark fw-bold w-100 py-2 shadow-sm">Inscribirme</button>
                    </form>
                <% } %>
            </div>
        </div>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
