<%@page import="Logica.util.ImagenUtil"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consulta de Edición de Curso</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
        <%-- Cargamos la estructura global (Barra de navegación + Menú lateral izquierdo) --%>
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <%-- CONTENIDO PRINCIPAL: CONSULTA DE EDICIÓN DE CURSO --%>
        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
            <div class="w-100 px-3" style="max-width: 900px;">
                <h2 class="mb-4 fw-bold text-center">Consulta de Edición de Curso</h2>

                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                <% } %>

                <%
                    DTEdicionCurso detalleEdicion = (DTEdicionCurso) request.getAttribute("edicionDetalle");
                    if (detalleEdicion != null) {
                        SimpleDateFormat sdfEd = new SimpleDateFormat("dd/MM/yyyy");
                        String fInicio = (detalleEdicion.getFInicio() != null) ? sdfEd.format(detalleEdicion.getFInicio()) : "";
                        String fFin = (detalleEdicion.getFFin() != null) ? sdfEd.format(detalleEdicion.getFFin()) : "";
                        String fAlta = (detalleEdicion.getFechaAlta() != null) ? sdfEd.format(detalleEdicion.getFechaAlta()) : "";
                        
                        // Obtenemos la imagen usando getImage() y CARPETA_EDICIONCURSOS
                        String nombreImagen = null;
                        try {
                            nombreImagen = detalleEdicion.getImage(); 
                        } catch (Exception e) {
                            nombreImagen = null;
                        }
                        
                        String imgEdicion = (nombreImagen != null && !nombreImagen.trim().isEmpty()) 
                            ? ImagenUtil.versionedUrl(ImagenUtil.CARPETA_EDICIONCURSOS, nombreImagen, request.getContextPath()) 
                            : request.getContextPath() + "/Images/ImagenUserDefault.png";
                %>
                
                <%-- TARJETA DE DETALLE E IMAGEN DE LA EDICIÓN --%>
                <div class="card border-dark shadow p-4 bg-white mb-4">
                    <div class="row align-items-center">
                        <div class="col-md-4 text-center mb-3 mb-md-0">
                            <img src="<%= imgEdicion %>" alt="Imagen de Edición" width="140" height="140" class="rounded-circle border border-2 border-dark shadow-sm" style="object-fit: cover;">
                            <h4 class="fw-bold mt-3 text-dark"><%= detalleEdicion.getNombre() %></h4>
                            <span class="badge bg-primary fs-6">Edición</span>
                        </div>
                        <div class="col-md-8 border-start ps-md-4">
                            <div class="d-flex flex-column gap-2 fs-6">
                                <p class="mb-1"><strong>Instituto:</strong> <span class="text-muted"><%= detalleEdicion.getInstituto() %></span></p>
                                <p class="mb-1"><strong>Fecha de Inicio:</strong> <span class="text-muted"><%= fInicio %></span></p>
                                <p class="mb-1"><strong>Fecha de Fin:</strong> <span class="text-muted"><%= fFin %></span></p>
                                <p class="mb-1"><strong>Cupo Máximo:</strong> <span class="text-muted"><%= detalleEdicion.getCupo() %></span></p>
                                <p class="mb-1"><strong>Cupo Actual:</strong> <span class="text-muted"><%= detalleEdicion.getCupoActual() %></span></p>
                                <p class="mb-1"><strong>Fecha de Alta:</strong> <span class="text-muted"><%= fAlta %></span></p>
                            </div>
                        </div>
                    </div>
                </div>

                <%-- TARJETA DE CURSOS ASOCIADOS --%>
                <div class="card border-dark shadow p-4 bg-white mb-4">
                    <h4 class="fw-bold border-bottom pb-2 mb-3">Cursos Asociados</h4>
                    <ul class="list-group list-group-flush">
                        <% if (detalleEdicion.getCurso() != null && !detalleEdicion.getCurso().trim().isEmpty()) { 
                            String nombreCurso = detalleEdicion.getCurso();
                        %>
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                                <div>
                                    <span class="fw-bold fs-5 text-dark"><%= nombreCurso %></span>
                                    <p class="text-muted small mb-0">Curso principal asociado a esta edición.</p>
                                </div>
                                <a href="${pageContext.request.contextPath}/ConsultaCursoServlet?nombre=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8") %>&curso=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8") %>" class="btn btn-dark btn-sm fw-bold">
                                    Ver Curso
                                </a>
                            </li>
                        <% } else { %>
                            <li class="list-group-item text-muted px-0 bg-transparent">No hay cursos asociados registrados.</li>
                        <% } %>
                    </ul>
                </div>

                <% } else if (request.getAttribute("error") == null) { %>
                <div class="card border-dark shadow p-4 bg-white">
                    <div class="alert alert-warning mb-0 text-center">No se ha seleccionado ninguna edición para consultar.</div>
                </div>
                <% } %>
            </div>
        </div>

    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>