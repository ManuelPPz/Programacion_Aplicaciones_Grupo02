<%@page import="Logica.DTsClasses.DTProgramaForm"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTUsuario"%>
<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTCategoria"%>
<%@page import="java.util.List"%>
<%@page import="Logica.DTsClasses.DTDocente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="Logica.util.ImagenUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consulta de Curso</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
        
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <%-- FORMULARIO PRINCIPAL DE CONSULTA DE CURSO --%>
        <div class="col-md-9 col-lg-10 ps-md-4 mb-5">
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } 
            
            DTMaster dtmaster = (DTMaster) request.getAttribute("cursoElegido");
            if (dtmaster != null) {
                if (dtmaster instanceof DTCurso) {
                    DTCurso dtc = (DTCurso) dtmaster;
                    if (dtc != null) {
                        String urlConVersion = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, dtc.getImage(), request.getContextPath());
            %>          
                        <h2>Consulta de Curso</h2>

                        <div class="row g-4 mt-1">

                            <%-- COLUMNA IZQUIERDA: Detalles del Curso y Descripción --%>
                            <div class="col-md-7 col-lg-8">

                                <%-- Cabecera con Imagen + Título e Instituto --%>
                                <div class="d-flex align-items-start gap-3 mb-3">
                                    <% if (dtc.getImage() != null && !dtc.getImage().trim().isEmpty()) { %>
                                        <img src="<%= urlConVersion %>" 
                                             alt="Logo Curso" 
                                             width="250" 
                                             height="150" 
                                             class="rounded border border-2 border-dark shadow-sm flex-shrink-0"
                                             style="object-fit: cover;">
                                    <% } else { %>
                                        <img src="<%= request.getContextPath() %>/Images/ImagenUserDefault.png" 
                                             alt="Logo Curso" 
                                             width="250" 
                                             height="150" 
                                             class="rounded border border-2 border-dark shadow-sm flex-shrink-0"
                                             style="object-fit: cover;">
                                    <% } %>

                                    <div>
                                        <h3 class="fw-bold mb-1"><%= dtc.getNombre() %></h3>
                                        <p class="text-muted fs-5 mb-2"><%= dtc.getInstituto() %></p>
                                    </div>
                                </div>

                                <%-- URL del Curso --%>
                                <% if (dtc.getURL() != null && !dtc.getURL().isBlank()) { %>
                                    <div class="position-relative d-inline-block my-2">
                                        <a href="<%= dtc.getURL() %>" target="_blank" class="text-primary text-decoration-underline"><%= dtc.getURL() %></a>
                                    </div>
                                <% } %>

                                <%-- Descripción del Curso --%>
                                <div class="card p-3 border-dark bg-white shadow-sm mt-3">
                                    <h5 class="fw-bold border-bottom pb-2">Descripcion</h5>
                                    <p class="mb-0 text-break" style="white-space: pre-line; word-wrap: break-word;">
                                        <%= dtc.getDescripcion() != null ? dtc.getDescripcion() : "Sin descripcion disponible." %>
                                    </p>
                                </div>

                            </div>

                            <%-- COLUMNA DERECHA: Ediciones y Programas --%>
                            <div class="col-md-5 col-lg-4">
                                <div class="card p-3 border-dark shadow-sm bg-white">
                                    <h4 class="fw-bold mb-3 border-bottom pb-2">Ediciones de Cursos</h4>

                                    <ul class="list-group list-group-flush">
                                        <%
                                        // Soportamos ambos nombres por si el servlet usa "edicionesDelCurso" o simplemente "ediciones"
                                        List<DTMaster> dtEdiciones = (List<DTMaster>) request.getAttribute("edicionesDelCurso");
                                        if (dtEdiciones == null) {
                                            dtEdiciones = (List<DTMaster>) request.getAttribute("ediciones");
                                        }

                                        if (dtEdiciones != null && !dtEdiciones.isEmpty()) {
                                            for (DTMaster edMaster : dtEdiciones) {
                                                if (edMaster instanceof DTEdicionCurso) {
                                                    DTEdicionCurso dte = (DTEdicionCurso) edMaster;
                                                    String urlConVersionEdi = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_EDICIONCURSOS, dte.getImage(), request.getContextPath());
                                        %>
                                                    <li class="list-group-item d-flex align-items-center justify-content-between gap-2 px-0 py-2 bg-transparent">
                                                        <div class="d-flex align-items-center gap-2 text-truncate">
                                                            <img src="<%= urlConVersionEdi %>" 
                                                                 alt="Logo Edicion" 
                                                                 width="40" 
                                                                 height="40"
                                                                 class="rounded border flex-shrink-0"
                                                                 style="object-fit: cover;">
                                                            <div class="text-truncate">
                                                                <h6 class="mb-0 fw-semibold text-truncate"><%= dte.getNombre() %></h6>
                                                                <small class="text-muted">Inicio: <%= dte.getFInicio() != null ? new SimpleDateFormat("dd/MM/yyyy").format(dte.getFInicio()) : "" %></small>
                                                            </div>
                                                        </div>
                                                        <a href="${pageContext.request.contextPath}/ConsultaEdicionServlet?nombre=<%= java.net.URLEncoder.encode(dte.getNombre(), "UTF-8") %>" class="btn btn-outline-dark btn-sm flex-shrink-0 fw-bold">
                                                            Ver
                                                        </a>
                                                    </li>
                                        <%
                                                }
                                            }
                                        } else {
                                        %>
                                            <li class="list-group-item text-muted px-0 bg-transparent text-center">
                                                No hay ediciones registradas para este curso.
                                            </li>
                                        <% } %>
                                    </ul>
                                </div>

                                <br>

                                <div class="card p-3 border-dark shadow-sm bg-white">
                                    <h4 class="fw-bold mb-3 border-bottom pb-2">Programas de Formacion</h4>
                                    <ul class="list-group list-group-flush">
                                        <%
                                        // Soportamos ambos nombres por si el servlet usa "programasDelCurso" o simplemente "programas"
                                        List<DTMaster> dtProgramas = (List<DTMaster>) request.getAttribute("programasDelCurso");
                                        if (dtProgramas == null) {
                                            dtProgramas = (List<DTMaster>) request.getAttribute("programas");
                                        }

                                        if (dtProgramas != null && !dtProgramas.isEmpty()) {
                                            for (DTMaster progMaster : dtProgramas) {
                                                if (progMaster instanceof DTProgramaForm) {
                                                    DTProgramaForm dtp = (DTProgramaForm) progMaster;
                                                    String urlConVersionPro = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PROGRAMAS, dtp.getImage(), request.getContextPath());
                                        %>
                                                    <li class="list-group-item d-flex align-items-center gap-2 px-0">
                                                        <img src="<%= urlConVersionPro %>" 
                                                             alt="Logo Programa" 
                                                             width="60" 
                                                             height="40"
                                                             class="rounded border"
                                                             style="object-fit: cover;">
                                                        <div class="text-truncate">
                                                            <h6 class="mb-0 fw-semibold text-truncate"><%= dtp.getNombre() %></h6>
                                                        </div>
                                                    </li>
                                        <%
                                                }
                                            }
                                        } else {
                                        %>
                                            <li class="list-group-item text-muted px-0 bg-transparent text-center">
                                                No hay programas vinculados a este curso.
                                            </li>
                                        <% } %>
                                    </ul>
                                </div>
                            </div>

                        </div>
            <%
                    }
                }
            }
            %>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>