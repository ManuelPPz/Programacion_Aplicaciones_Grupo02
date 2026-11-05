<%@page import="Logica.Classes.Enum_Estado_inscripcion"%>
<%@page import="Logica.util.ImagenUtil"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTUsuario"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- Cargamos la estructura global (que ya trae <html>, <head>, <body> y el menú) --%>
<%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

<!-- Carga del JavaScript externo para esta vista -->
<script src="${pageContext.request.contextPath}/JavasScripts/ConsultaEdicion.js"></script>

<%-- CONTENIDO PRINCIPAL --%>
<div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
    <div class="w-100 px-3" style="max-width: 900px;">
        <h2 class="mb-4 fw-bold text-center">Consulta de Edición de Curso</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger"><%= request.getAttribute("error")%></div>
        <% } %>

        <% if (request.getAttribute("exito") != null) { %>
            <div class="alert alert-success"><%= request.getAttribute("exito")%></div>
        <% } %>

        <%
            DTEdicionCurso detalleEdicion = (DTEdicionCurso) request.getAttribute("edicionDetalle");
            if (detalleEdicion == null) {
        %>
        <%-- CASO 1: Listado general de ediciones --%>
        <div class="card border-dark shadow p-4 w-100 mx-auto bg-white" style="max-width: 850px;">
            <h4 class="card-title text-center mb-4 fw-bold">Listado de Ediciones de Cursos</h4>
            <div class="list-group">
                <%
                    List<DTMaster> listaEdiciones = (List<DTMaster>) request.getAttribute("ediciones");
                    if (listaEdiciones != null && !listaEdiciones.isEmpty()) {
                        for (DTMaster master : listaEdiciones) {
                            if (master instanceof DTEdicionCurso) {
                                DTEdicionCurso ed = (DTEdicionCurso) master;
                                String linkConsultaEd = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(ed.getNombre(), "UTF-8");
                %>
                                <a href="<%= linkConsultaEd %>" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-3">
                                    <div>
                                        <h5 class="mb-1 fw-bold text-dark"><%= ed.getNombre() %></h5>
                                        <small class="text-muted">Curso: <%= ed.getCurso() %> | Instituto: <%= ed.getInstituto() %></small>
                                    </div>
                                    <span class="btn btn-outline-dark btn-sm fw-bold">Ver</span>
                                </a>
                <%
                            }
                        }
                    } else {
                %>
                    <div class="alert alert-secondary text-center mb-0" role="alert">
                        No hay ediciones de cursos registradas en el sistema.
                    </div>
                <% } %>
            </div>
        </div>
        <%
            } else {
                SimpleDateFormat sdfEd = new SimpleDateFormat("dd/MM/yyyy");
                String fInicio = (detalleEdicion.getFInicio() != null) ? sdfEd.format(detalleEdicion.getFInicio()) : "";
                String fFin = (detalleEdicion.getFFin() != null) ? sdfEd.format(detalleEdicion.getFFin()) : "";
                String fAlta = (detalleEdicion.getFechaAlta() != null) ? sdfEd.format(detalleEdicion.getFechaAlta()) : "";

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

        <%-- CASO 2: Tarjeta de detalle de la edición --%>
        <div class="card border-dark shadow p-4 bg-white mb-4">
            <div class="row align-items-center">
                <div class="col-md-4 text-center mb-3 mb-md-0">
                    <img src="<%= imgEdicion%>" alt="Imagen de Edición" width="140" height="140" class="rounded-circle border border-2 border-dark shadow-sm" style="object-fit: cover;">
                    <h4 class="fw-bold mt-3 text-dark"><%= detalleEdicion.getNombre()%></h4>
                    <span class="badge bg-primary fs-6">Edición</span>
                </div>
                <div class="col-md-8 border-start ps-md-4">
                    <div class="d-flex flex-column gap-2 fs-6">
                        <p class="mb-1"><strong>Instituto:</strong> <span class="text-muted"><%= detalleEdicion.getInstituto()%></span></p>
                        <p class="mb-1"><strong>Fecha de Inicio:</strong> <span class="text-muted"><%= fInicio%></span></p>
                        <p class="mb-1"><strong>Fecha de Fin:</strong> <span class="text-muted"><%= fFin%></span></p>
                        <p class="mb-1"><strong>Cupo Máximo:</strong> <span class="text-muted"><%= detalleEdicion.getCupo()%></span></p>
                        <p class="mb-1"><strong>Cupo Actual:</strong> <span class="text-muted"><%= detalleEdicion.getCupoActual()%></span></p>
                        <p class="mb-1"><strong>Fecha de Alta:</strong> <span class="text-muted"><%= fAlta%></span></p>
                    </div>

                    <%-- BOTÓN CON ATRIBUTOS DATA-* --%>
                    <%
                        DTMaster usuarioActual = (DTMaster) session.getAttribute("usuarioLogueado");
                        if (usuarioActual != null) {
                            String tipoClase = usuarioActual.getClass().getSimpleName().toLowerCase();
                            if (!tipoClase.contains("docente")) {
                                Enum_Estado_inscripcion yaInscripto = (Enum_Estado_inscripcion) request.getAttribute("yaInscripto");
                                String estadoStr = (yaInscripto != null) ? yaInscripto.name() : "NORMAL";
                    %>
                    <div class="mt-4 pt-3 border-top d-flex align-items-center" id="contenedorInscripcion">
                        <% if ("NORMAL".equals(estadoStr)) { %>
                            <button type="button" 
                                    id="btnInscribir" 
                                    data-curso="<%= detalleEdicion.getCurso() %>" 
                                    data-edicion="<%= detalleEdicion.getNombre() %>"
                                    data-contextpath="${pageContext.request.contextPath}"
                                    class="btn fw-bold w-100 py-2 shadow-sm text-white" 
                                    style="background-color: #198754 !important; border-color: #198754 !important;">
                                Solicitar Inscripción a esta Edición
                            </button>
                        <% } else if ("INSCRIPTO".equals(estadoStr)) { %>
                            <button type="button" 
                                    id="btnCancelar" 
                                    data-curso="<%= detalleEdicion.getCurso() %>" 
                                    data-edicion="<%= detalleEdicion.getNombre() %>"
                                    data-contextpath="${pageContext.request.contextPath}"
                                    class="btn btn-danger fw-bold w-100 py-2 shadow-sm">
                                Cancelar
                            </button>
                        <% } else if ("ACEPTADO".equals(estadoStr)) { %>
                            <button type="button" class="btn btn-secondary fw-bold w-100 py-2 shadow-sm" disabled>
                                Inscripto a esta edición de curso
                            </button>
                        <% } else if("RECHAZADO".equals(estadoStr)){%>
                            <button type="button" class="btn btn-secondary fw-bold w-100 py-2 shadow-sm" disabled>
                                Rechazado
                            </button>
                        <%}%>
                    </div>
                    <% 
                            }
                        } 
                    %>
                </div>
            </div>
        </div>

        <%-- Tarjeta de Docentes Asociados --%>
        <div class="card border-dark shadow p-4 bg-white mb-4">
            <h4 class="fw-bold border-bottom pb-2 mb-3">Docentes Asociados</h4>
            <ul class="list-group list-group-flush">
                <%
                    List<String> docentesAsociados = null;
                    try {
                        docentesAsociados = detalleEdicion.getDocentes();
                    } catch (Exception e) {}

                    if (docentesAsociados != null && !docentesAsociados.isEmpty()) {
                        for (String docNick : docentesAsociados) {
                            String linkDocente = request.getContextPath() + "/ConsultaUsuarioIndividualServlet?nickname=" + java.net.URLEncoder.encode(docNick, "UTF-8");
                %>
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                                <div class="d-flex align-items-center gap-2">
                                    <span class="fw-semibold text-dark"><%= docNick %></span>
                                </div>
                                <a href="<%= linkDocente %>" class="btn btn-outline-dark btn-sm fw-bold">
                                    Ver Perfil
                                </a>
                            </li>
                <%
                        }
                    } else {
                %>
                    <li class="list-group-item text-muted px-0 bg-transparent">No hay docentes asociados registrados para esta edición.</li>
                <% } %>
            </ul>
        </div>

        <%-- Tarjeta de Cursos Asociados --%>
        <div class="card border-dark shadow p-4 bg-white mb-4">
            <h4 class="fw-bold border-bottom pb-2 mb-3">Cursos Asociados</h4>
            <ul class="list-group list-group-flush">
                <% if (detalleEdicion.getCurso() != null && !detalleEdicion.getCurso().trim().isEmpty()) {
                        String nombreCurso = detalleEdicion.getCurso();
                %>
                <li class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                    <div>
                        <span class="fw-bold fs-5 text-dark"><%= nombreCurso%></span>
                        <p class="text-muted small mb-0">Curso principal asociado a esta edición.</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/ConsultaCursoServlet?nombre=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8")%>&curso=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8")%>" class="btn btn-dark btn-sm fw-bold">
                        Ver Curso
                    </a>
                </li>
                <% } else { %>
                <li class="list-group-item text-muted px-0 bg-transparent">No hay cursos asociados registrados.</li>
                <% } %>
            </ul>
        </div>

        <% } %>
    </div>
</div>