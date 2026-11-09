<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.util.ImagenUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    // Escapa texto para mostrarlo dentro del HTML
    private static String html(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Cursos más visitados</title>
    </head>
    <body>
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>
        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
            <div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
                <h2 class="card-title text-center mb-1 fw-bold">Cursos más visitados</h2>
                <p class="text-center text-muted mb-4">Ordenados por la cantidad de veces que fueron consultados</p>
                <%
                    List<DTMaster> tendencias = (List<DTMaster>) request.getAttribute("tendencias");
                    if (tendencias == null || tendencias.isEmpty()) {
                %>
                <div class="alert alert-secondary text-center mb-0">
                    Todavía no se consultó ningún curso.
                </div>
                <%
                    } else {
                %>
                <ol class="list-group list-group-numbered">
                    <%
                        for (DTMaster dt : tendencias) {
                            if (!(dt instanceof DTCurso)) {
                                continue;
                            }
                            DTCurso c = (DTCurso) dt;
                            String nombreUrl = URLEncoder.encode(c.getNombre(), "UTF-8");
                            String link = request.getContextPath() + "/ConsultaCursoServlet?curso=" + nombreUrl;
                            String imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, c.getImage(), request.getContextPath());
                            int visitas = c.getVisitas();
                    %>
                    <li class="list-group-item d-flex align-items-center gap-3">
                        <img src="<%= imagenUrl %>" alt="" class="rounded border flex-shrink-0"
                             style="width: 56px; height: 56px; object-fit: cover;">
                        <div class="me-auto text-break">
                            <a href="<%= link %>" class="fw-bold text-decoration-none"><%= html(c.getNombre()) %></a>
                            <% if (c.getInstituto() != null && !c.getInstituto().isBlank()) { %>
                            <div class="small text-muted"><%= html(c.getInstituto()) %></div>
                            <% } %>
                        </div>
                        <span class="badge bg-primary rounded-pill text-nowrap">
                            <%= visitas %> <%= visitas == 1 ? "visita" : "visitas" %>
                        </span>
                    </li>
                    <%
                        }
                    %>
                </ol>
                <%
                    }
                %>
            </div>
        </div>
    </body>
</html>
