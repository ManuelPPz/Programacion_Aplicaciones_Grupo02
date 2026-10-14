<%@page import="Logica.Classes.Curso"%>
<%@page import="Logica.Manejadores.ManejadorCursos"%>
<%@page import="Logica.util.ImagenUtil"%>
<%@page import="Logica.DTsClasses.DTProgramaForm"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consulta de Programa de Formacion</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <div class="col-md-9 col-lg-10 ps-md-4 mb-5">
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>

            <%
                DTProgramaForm programaDetalle = (DTProgramaForm) request.getAttribute("programaDetalle");
                
                if (programaDetalle == null) {
                    // CASO 1: Listado general de programas
            %>
                    <div class="card border-dark shadow p-4 w-100 mx-auto" style="max-width: 850px;">
                        <h2 class="card-title text-center mb-4 fw-bold">Programas de Formacion</h2>
                        
                        <div class="list-group">
                            <% 
                                List<DTMaster> listaMaster = (List<DTMaster>) request.getAttribute("programas");
                                if (listaMaster != null && !listaMaster.isEmpty()) {
                                    for (DTMaster master : listaMaster) {
                                        if (master instanceof DTProgramaForm) {
                                            DTProgramaForm prog = (DTProgramaForm) master;
                                            String linkConsulta = request.getContextPath() + "/ConsultaProgramaServlet?programa=" + java.net.URLEncoder.encode(prog.getNombre(), "UTF-8");
                            %>
                                            <a href="<%= linkConsulta %>" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-3">
                                                <div>
                                                    <h5 class="mb-1 fw-bold text-dark"><%= prog.getNombre() %></h5>
                                                    <small class="text-muted"><%= prog.getDescripcion() != null && prog.getDescripcion().length() > 100 ? prog.getDescripcion().substring(0, 100) + "..." : prog.getDescripcion() %></small>
                                                </div>
                                                <span class="btn btn-outline-primary btn-sm fw-bold">Consultar</span>
                                            </a>
                            <% 
                                        }
                                    }
                                } else { 
                            %>
                                <div class="alert alert-secondary text-center mb-0" role="alert">
                                    No hay programas de formacion registrados en el sistema.
                                </div>
                            <% } %>
                        </div>
                    </div>

            <% 
                } else {
                    // CASO 2: Detalle del programa seleccionado
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    
                    String fInicio = "N/A";
                    String fFin = "N/A";
                    if (programaDetalle.getVigenciaProg() != null) {
                        try {
                            fInicio = (programaDetalle.getVigenciaProg().getFechaInicio() != null) ? sdf.format(programaDetalle.getVigenciaProg().getFechaInicio()) : "N/A";
                            fFin = (programaDetalle.getVigenciaProg().getFechaFin() != null) ? sdf.format(programaDetalle.getVigenciaProg().getFechaFin()) : "N/A";
                        } catch (Exception e) {}
                    }
                    
                    // Validación de imagen del programa
                    String imgNombreProg = programaDetalle.getImage();
                    String imgPrograma = (imgNombreProg != null && !imgNombreProg.trim().isEmpty()) 
                        ? ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PROGRAMAS, imgNombreProg, request.getContextPath())
                        : request.getContextPath() + "/Images/ImagenUserDefault.png";
            %>
                    <h2>Consulta de Programa de Formacion</h2>

                    <div class="row g-4 mt-1">
                        <%-- COLUMNA IZQUIERDA: Imagen, Datos básicos, Categorías y Descripción --%>
                        <div class="col-md-7 col-lg-8">
                            <div class="d-flex align-items-start gap-3 mb-3">
                                <img src="<%= imgPrograma %>" 
                                     alt="Logo Programa" 
                                     width="250" 
                                     height="150" 
                                     class="rounded border border-2 border-dark shadow-sm flex-shrink-0"
                                     style="object-fit: cover;"
                                     onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">

                                <div>
                                    <h3 class="fw-bold mb-1"><%= programaDetalle.getNombre() %></h3>
                                    <p class="text-muted mb-1"><strong>Inicio:</strong> <%= fInicio %> | <strong>Fin:</strong> <%= fFin %></p>
                                    
                                    <div class="mt-2">
                                        <span class="fw-bold text-dark d-block mb-1">Categorias:</span>
                                        <% 
                                            List<String> categorias = programaDetalle.getMisCategorias();
                                            if (categorias != null && !categorias.isEmpty()) {
                                                for (String cat : categorias) {
                                        %>
                                                    <span class="badge bg-secondary me-1"><%= cat %></span>
                                        <% 
                                                }
                                            } else { 
                                        %>
                                            <small class="text-muted">Sin categorias asociadas.</small>
                                        <% } %>
                                    </div>
                                </div>
                            </div>

                            <div class="card p-3 border-dark bg-white shadow-sm mt-3">
                                <h5 class="fw-bold border-bottom pb-2">Descripcion</h5>
                                <p class="mb-0 text-break" style="white-space: pre-line; word-wrap: break-word;">
                                    <%= programaDetalle.getDescripcion() != null ? programaDetalle.getDescripcion() : "Sin descripcion disponible." %>
                                </p>
                            </div>

                            <div class="mt-3">
                                <a href="${pageContext.request.contextPath}/ConsultaProgramaServlet" class="btn btn-outline-secondary fw-bold">Volver al listado</a>
                            </div>
                        </div>

                        <%-- COLUMNA DERECHA: Cursos integrantes --%>
                        <div class="col-md-5 col-lg-4">
                            <div class="card p-3 border-dark shadow-sm bg-white">
                                <h4 class="fw-bold mb-3 border-bottom pb-2">Cursos Integrantes</h4>

                                <ul class="list-group list-group-flush">
                                    <%
                                        List<String> listaCursosNom = programaDetalle.getCursos();
                                        if (listaCursosNom != null && !listaCursosNom.isEmpty()) {
                                            for (String nomCurso : listaCursosNom) {
                                                String linkCurso = request.getContextPath() + "/ConsultaCursoServlet?nombre=" + java.net.URLEncoder.encode(nomCurso, "UTF-8");
                                                
                                                // Se busca la entidad Curso para obtener el nombre real de su imagen
                                                String imgCurso = request.getContextPath() + "/Images/ImagenUserDefault.png";
                                                Curso cAux = ManejadorCursos.GetInstance().BuscarCurso(nomCurso);
                                                if (cAux != null && cAux.getImage() != null && !cAux.getImage().trim().isEmpty()) {
                                                    imgCurso = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, cAux.getImage(), request.getContextPath());
                                                }
                                    %>
                                                <li class="list-group-item d-flex align-items-center justify-content-between gap-2 px-0 py-2 bg-transparent">
                                                    <div class="d-flex align-items-center gap-2 text-truncate">
                                                        <img src="<%= imgCurso %>" 
                                                             alt="Logo Curso" 
                                                             width="40" 
                                                             height="40"
                                                             class="rounded border flex-shrink-0"
                                                             style="object-fit: cover;"
                                                             onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
                                                        <div class="text-truncate">
                                                            <h6 class="mb-0 fw-semibold text-truncate"><%= nomCurso %></h6>
                                                        </div>
                                                    </div>
                                                    <a href="<%= linkCurso %>" class="btn btn-outline-dark btn-sm flex-shrink-0 fw-bold">
                                                        Ver
                                                    </a>
                                                </li>
                                    <%
                                            }
                                        } else {
                                    %>
                                        <li class="list-group-item text-muted px-0 bg-transparent text-center">
                                            Este programa no tiene cursos asociados actualmente.
                                        </li>
                                    <% } %>
                                </ul>
                            </div>
                        </div>
                    </div>
            <% 
                } 
            %>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>