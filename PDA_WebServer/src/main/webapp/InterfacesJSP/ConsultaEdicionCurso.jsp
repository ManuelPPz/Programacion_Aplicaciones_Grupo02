<%@page import="Logica.util.ImagenUtil"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTUsuario"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Detección de dispositivo móvil por User-Agent
    String ua = request.getHeader("User-Agent");
    boolean esMovil = ua != null && ua.matches("(?i).*(android|iphone|ipad|ipod|mobile|blackberry|opera mini).*");

    if (esMovil) {
        // ===================================================================
        // VISTA MÓVIL PURA (Sin el sidebar de PC, manteniendo estética limpia)
        // ===================================================================
        DTMaster dtmUserLogueado = (DTMaster) session.getAttribute("usuarioLogueado");
        String nombreUsuarioMostrar = "Usuario";
        if (dtmUserLogueado instanceof DTUsuarioBase) {
            DTUsuarioBase uBase = (DTUsuarioBase) dtmUserLogueado;
            if (uBase.getNombre() != null && !uBase.getNombre().isBlank()) {
                nombreUsuarioMostrar = uBase.getNombre();
            } else if (uBase.getNickname() != null && !uBase.getNickname().isBlank()) {
                nombreUsuarioMostrar = uBase.getNickname();
            }
        }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Consulta de Edición - Móvil</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <style>
            .mobile-header {
                background-color: #f8f9fa;
                border-bottom: 1px solid #e9ecef;
            }
            .mobile-menu-link {
                color: #6c757d;
                text-decoration: none;
                font-weight: 500;
                display: block;
                padding: 8px 0;
            }
            .mobile-menu-link:hover {
                color: #212529;
            }
            .btn-hamburger-clean {
                color: #212529 !important;
                border: none !important;
                background: transparent !important;
            }
        </style>
    </head>
    <body class="bg-light">

        <!-- Header Superior Móvil -->
        <div class="mobile-header p-3 mb-3 shadow-sm">
            <div class="d-flex align-items-center justify-content-between">
                <button class="btn btn-hamburger-clean p-1 shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#mobileMenu" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="bi bi-list fs-2"></i>
                </button>
                <span class="fs-6 fw-normal text-dark text-truncate px-2">Bienvenido <%= nombreUsuarioMostrar %></span>
                <img src="<%= request.getContextPath() %>/Images/Logo%20edEXT%20sin%20fondo.png" alt="edEXT Logo" style="height: 35px; width: auto;">
                <%-- Foto de perfil del usuario logueado (se pide al Servidor Central vía /imagenes) --%>
                <% if (dtmUserLogueado instanceof Logica.DTsClasses.DTUsuarioBase) {
                       Logica.DTsClasses.DTUsuarioBase usuMovil = (Logica.DTsClasses.DTUsuarioBase) dtmUserLogueado;
                       String fotoMovil = Logica.util.ImagenUtil.versionedUrl(Logica.util.ImagenUtil.CARPETA_PERFILES,
                               usuMovil.getImg(), request.getContextPath());
                %>
                    <a href="<%= request.getContextPath() %>/ConsultaUsuarioIndividualServlet?nickname=<%= java.net.URLEncoder.encode(usuMovil.getNickname(), "UTF-8") %>" title="<%= usuMovil.getNickname() %>">
                        <img src="<%= fotoMovil %>" alt="Perfil" width="40" height="40" class="rounded-circle border" style="object-fit: cover;"
                             onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
                    </a>
                <% } else { %>
                    <span style="width: 40px;"></span>
                <% } %>
            </div>
            
            <!-- Menú Desplegable -->
            <div class="collapse pt-3 ps-1 border-top mt-2" id="mobileMenu">
                <ul class="list-unstyled mb-0">
                    <li><a href="<%= request.getContextPath() %>/MainMenu" class="mobile-menu-link text-dark fw-semibold"><i class="bi bi-house-door me-2"></i>Inicio</a></li>
                    <li><a href="<%= request.getContextPath() %>/ConsultaCursoServlet" class="mobile-menu-link text-dark fw-semibold"><i class="bi bi-book me-2"></i>Ver Cursos</a></li>
                    <li><a href="<%= request.getContextPath() %>/ConsultaEdicionServlet" class="mobile-menu-link text-dark fw-semibold"><i class="bi bi-calendar-event me-2"></i>Ver Ediciones</a></li>
                    <li><a href="<%= request.getContextPath() %>/LogoutServlet" class="mobile-menu-link text-danger"><i class="bi bi-box-arrow-right me-2"></i>Salir</a></li>
                </ul>
            </div>
        </div>

        <div class="container px-3 pb-5">
            <h4 class="mb-3 fw-bold text-center">Consulta de Edición de Curso</h4>

            <% if (request.getAttribute("error") != null) {%>
                <div class="alert alert-danger"><%= request.getAttribute("error")%></div>
            <% } %>

            <% if (request.getAttribute("exito") != null) {%>
                <div class="alert alert-success"><%= request.getAttribute("exito")%></div>
            <% } %>

            <%
                DTEdicionCurso detalleEdicion = (DTEdicionCurso) request.getAttribute("edicionDetalle");
                if (detalleEdicion == null) {
                    // CASO 1: Listado general de ediciones para móvil
            %>
                <div class="card border-dark shadow-sm p-3 bg-white">
                    <h6 class="fw-bold border-bottom pb-2 mb-2 small">Listado de Ediciones</h6>
                    <div class="list-group list-group-flush">
                        <%
                            List<DTMaster> listaEdiciones = (List<DTMaster>) request.getAttribute("ediciones");
                            if (listaEdiciones != null && !listaEdiciones.isEmpty()) {
                                for (DTMaster master : listaEdiciones) {
                                    if (master instanceof DTEdicionCurso) {
                                        DTEdicionCurso ed = (DTEdicionCurso) master;
                                        String linkConsultaEd = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(ed.getNombre(), "UTF-8");
                        %>
                                        <a href="<%= linkConsultaEd %>" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-2 px-1 bg-transparent border-bottom">
                                            <div class="text-truncate">
                                                <h6 class="mb-0 fw-bold text-dark text-truncate small"><%= ed.getNombre() %></h6>
                                                <small class="text-muted" style="font-size: 0.7rem;">Curso: <%= ed.getCurso() %> | Inst: <%= ed.getInstituto() %></small>
                                            </div>
                                            <span class="btn btn-outline-dark btn-sm fw-bold px-2 py-1 flex-shrink-0" style="font-size: 0.75rem;">Ver</span>
                                        </a>
                        <%
                                    }
                                }
                            } else {
                        %>
                            <div class="alert alert-secondary text-center mb-0 small" role="alert">
                                No hay ediciones de cursos registradas.
                            </div>
                        <% } %>
                    </div>
                </div>
            <%
                } else {
                    // CASO 2: Detalle específico de la edición seleccionada en móvil
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

                <!-- Volver al listado -->
                <div class="mb-3">
                    <a href="${pageContext.request.contextPath}/ConsultaEdicionServlet" class="btn btn-outline-dark btn-sm fw-bold">
                        ← Volver al listado
                    </a>
                </div>

                <!-- Tarjeta de Detalles de la Edición -->
                <div class="card border-dark shadow-sm p-3 bg-white mb-3">
                    <div class="text-center mb-3">
                        <img src="<%= imgEdicion%>" alt="Imagen de Edición" width="100" height="100" class="rounded-circle border border-2 border-dark shadow-sm" style="object-fit: cover;">
                        <h4 class="fw-bold mt-2 text-dark fs-5"><%= detalleEdicion.getNombre()%></h4>
                        <span class="badge bg-primary">Edición</span>
                    </div>

                    <div class="d-flex flex-column gap-1 small border-top pt-2">
                        <p class="mb-1"><strong>Instituto:</strong> <span class="text-muted"><%= detalleEdicion.getInstituto()%></span></p>
                        <p class="mb-1"><strong>Fecha de Inicio:</strong> <span class="text-muted"><%= fInicio%></span></p>
                        <p class="mb-1"><strong>Fecha de Fin:</strong> <span class="text-muted"><%= fFin%></span></p>
                        <p class="mb-1"><strong>Cupo Máximo:</strong> <span class="text-muted"><%= detalleEdicion.getCupo()%></span></p>
                        <p class="mb-1"><strong>Cupo Actual:</strong> <span class="text-muted"><%= detalleEdicion.getCupoActual()%></span></p>
                        <p class="mb-1"><strong>Fecha de Alta:</strong> <span class="text-muted"><%= fAlta%></span></p>
                    </div>

                    <!-- Botón de Inscripción -->
                    <%
                        DTMaster usuarioActual = (DTMaster) session.getAttribute("usuarioLogueado");
                        if (usuarioActual != null) {
                            String tipoClase = usuarioActual.getClass().getSimpleName().toLowerCase();
                            if (!tipoClase.contains("docente")) {
                                Boolean yaInscriptoObj = (Boolean) request.getAttribute("yaInscripto");
                                boolean yaInscripto = (yaInscriptoObj != null && yaInscriptoObj);
                    %>
                    <div class="mt-3 pt-3 border-top" id="contenedorInscripcion">
                        <% if (!yaInscripto) {%>
                        <button type="button" id="btnInscribir" onclick="inscribirDirectamente('<%= detalleEdicion.getCurso()%>', '<%= detalleEdicion.getNombre()%>')" class="btn fw-bold w-100 py-2 shadow-sm text-white" style="background-color: #198754 !important; border-color: #198754 !important; font-size: 0.9rem;">
                            Solicitar Inscripción
                        </button>
                        <% } else { %>
                        <button type="button" class="btn btn-secondary fw-bold w-100 py-2 shadow-sm" disabled style="font-size: 0.9rem;">
                            Ya estás inscripto
                        </button>
                        <% } %>
                    </div>

                    <script>
                        function inscribirDirectamente(curso, edicion) {
                            const btn = document.getElementById("btnInscribir");
                            btn.disabled = true;
                            btn.innerText = "Inscribiendo...";

                            fetch('${pageContext.request.contextPath}/ConsultaEdicionServlet', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/x-www-form-urlencoded',
                                },
                                body: 'action=inscribirDirecto&curso=' + encodeURIComponent(curso) + '&edicion=' + encodeURIComponent(edicion)
                            })
                            .then(response => {
                                if (response.ok) {
                                    const contenedor = document.getElementById("contenedorInscripcion");
                                    contenedor.innerHTML = '<button type="button" class="btn btn-secondary fw-bold w-100 py-2 shadow-sm" disabled style="font-size: 0.9rem;">Ya estás inscripto</button>';
                                } else {
                                    response.text().then(msg => {
                                        alert(msg && msg.trim() !== "" ? msg : "Ocurrió un error al procesar la inscripción.");
                                    });
                                    btn.disabled = false;
                                    btn.innerText = "Solicitar Inscripción";
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                alert("Error de conexión al intentar inscribir.");
                                btn.disabled = false;
                                btn.innerText = "Solicitar Inscripción";
                            });
                        }
                    </script>
                    <% 
                            }
                        } 
                    %>
                </div>

                <!-- Tarjeta de Docentes Asociados (SIN BOTÓN DE VER PERFIL EN MÓVIL) -->
                <div class="card border-dark shadow-sm p-3 bg-white mb-3">
                    <h6 class="fw-bold border-bottom pb-2 mb-2">Docentes Asociados</h6>
                    <ul class="list-group list-group-flush">
                        <%
                            List<String> docentesAsociados = null;
                            try {
                                docentesAsociados = detalleEdicion.getDocentes();
                            } catch (Exception e) {}

                            if (docentesAsociados != null && !docentesAsociados.isEmpty()) {
                                for (String docNick : docentesAsociados) {
                        %>
                                    <li class="list-group-item px-0 py-2 bg-transparent">
                                        <span class="fw-semibold text-dark small"><%= docNick %></span>
                                    </li>
                        <%
                                }
                            } else {
                        %>
                            <li class="list-group-item text-muted px-0 bg-transparent small">No hay docentes asociados.</li>
                        <% } %>
                    </ul>
                </div>

                <!-- Tarjeta de Cursos Asociados -->
                <div class="card border-dark shadow-sm p-3 bg-white mb-4">
                    <h6 class="fw-bold border-bottom pb-2 mb-2">Curso Asociado</h6>
                    <ul class="list-group list-group-flush">
                        <% if (detalleEdicion.getCurso() != null && !detalleEdicion.getCurso().trim().isEmpty()) {
                                String nombreCurso = detalleEdicion.getCurso();
                        %>
                            <li class="list-group-item d-flex justify-content-between align-items-center px-0 py-2 bg-transparent">
                                <div class="text-truncate pe-2">
                                    <span class="fw-bold text-dark small"><%= nombreCurso %></span>
                                </div>
                                <a href="${pageContext.request.contextPath}/ConsultaCursoServlet?nombre=<%= java.net.URLEncoder.encode(nombreCurso, "UTF-8")%>" class="btn btn-dark btn-sm fw-bold flex-shrink-0" style="font-size: 0.75rem; padding: 2px 8px;">
                                    Ver Curso
                                </a>
                            </li>
                        <% } else { %>
                            <li class="list-group-item text-muted px-0 bg-transparent small">No hay cursos asociados.</li>
                        <% } %>
                    </ul>
                </div>
            <% } %>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

<%
    } else {
        // ===================================================================
        // VISTA DE ESCRITORIO ORIGINAL (Mantiene tu InterfazBase.jsp de PC)
        // ===================================================================
%>
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

                <% if (request.getAttribute("error") != null) {%>
                <div class="alert alert-danger"><%= request.getAttribute("error")%></div>
                <% } %>

                <% if (request.getAttribute("exito") != null) {%>
                <div class="alert alert-success"><%= request.getAttribute("exito")%></div>
                <% } %>

                <%
                    DTEdicionCurso detalleEdicion = (DTEdicionCurso) request.getAttribute("edicionDetalle");
                    if (detalleEdicion == null) {
                        // CASO 1: Listado general de ediciones si no hay una seleccionada
                %>
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
                        // CASO 2: Detalle específico de la edición seleccionada
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

                <%-- TARJETA DE DETALLE E IMAGEN DE LA EDICIÓN --%>
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

                            <%-- BOTÓN DINÁMICO CON AJAX (Verde y separado) --%>
                            <%
                                DTMaster usuarioActual = (DTMaster) session.getAttribute("usuarioLogueado");
                                if (usuarioActual != null) {
                                    String tipoClase = usuarioActual.getClass().getSimpleName().toLowerCase();
                                    if (!tipoClase.contains("docente")) {

                                        Boolean yaInscriptoObj = (Boolean) request.getAttribute("yaInscripto");
                                        boolean yaInscripto = (yaInscriptoObj != null && yaInscriptoObj);
                            %>
                            <div class="mt-4 pt-3 border-top d-flex align-items-center" id="contenedorInscripcion">
                                <% if (!yaInscripto) {%>
                                <button type="button" id="btnInscribir" onclick="inscribirDirectamente('<%= detalleEdicion.getCurso()%>', '<%= detalleEdicion.getNombre()%>')" class="btn fw-bold w-100 py-2 shadow-sm text-white" style="background-color: #198754 !important; border-color: #198754 !important;">
                                    Solicitar Inscripción a esta Edición
                                </button>
                                <% } else { %>
                                <button type="button" class="btn btn-secondary fw-bold w-100 py-2 shadow-sm" disabled>
                                    Ya estás inscripto en esta Edición
                                </button>
                                <% } %>
                            </div>

                            <%-- SCRIPT AJAX PARA PROCESAR LA INSCRIPCIÓN AL INSTANTE --%>
                            <script>
                                function inscribirDirectamente(curso, edicion) {
                                    const btn = document.getElementById("btnInscribir");
                                    btn.disabled = true;
                                    btn.innerText = "Inscribiendo...";

                                    fetch('${pageContext.request.contextPath}/ConsultaEdicionServlet', {
                                        method: 'POST',
                                        headers: {
                                            'Content-Type': 'application/x-www-form-urlencoded',
                                        },
                                        body: 'action=inscribirDirecto&curso=' + encodeURIComponent(curso) + '&edicion=' + encodeURIComponent(edicion)
                                    })
                                    .then(response => {
                                        if (response.ok) {
                                            const contenedor = document.getElementById("contenedorInscripcion");
                                            contenedor.innerHTML = '<button type="button" class="btn btn-secondary fw-bold w-100 py-2 shadow-sm" disabled>Ya estás inscripto en esta Edición</button>';
                                        } else {
                                            response.text().then(msg => {
                                                alert(msg && msg.trim() !== "" ? msg : "Ocurrió un error al procesar la inscripción.");
                                            });
                                            btn.disabled = false;
                                            btn.innerText = "Solicitar Inscripción a esta Edición";
                                        }
                                    })
                                    .catch(error => {
                                        console.error('Error:', error);
                                        alert("Error de conexión al intentar inscribir.");
                                        btn.disabled = false;
                                        btn.innerText = "Solicitar Inscripción a esta Edición";
                                    });
                                }
                            </script>
                            <% 
                                    }
                                } 
                            %>
                        </div>
                    </div>
                </div>

                <%-- TARJETA DE DOCENTES ASOCIADOS A LA EDICIÓN (Mantiene botón en Escritorio) --%>
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

                <%-- TARJETA DE CURSOS ASOCIADOS --%>
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

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
<% } %>