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

<%
    // Detección de dispositivo móvil por User-Agent
    String ua = request.getHeader("User-Agent");
    boolean esMovil = ua != null && ua.matches("(?i).*(android|iphone|ipad|ipod|mobile|blackberry|opera mini).*");

    if (esMovil) {
        // ===================================================================
        // VISTA MÓVIL PURA (Muestra programas pero SIN botón "Ver")
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
        <title>Consulta de Curso - Móvil</title>
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
                    <li><a href="<%= request.getContextPath() %>/ConsultaEdicionServlet" class="mobile-menu-link"><i class="bi bi-calendar-event me-2"></i>Ver Ediciones</a></li>
                    <li><a href="<%= request.getContextPath() %>/CerrarSesionServlet" class="mobile-menu-link text-danger"><i class="bi bi-box-arrow-right me-2"></i>Salir</a></li>
                </ul>
            </div>
        </div>

        <div class="container px-3 pb-5">
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>
            
            <%  
            DTMaster dtmaster = (DTMaster) request.getAttribute("cursoElegido");
            if (dtmaster != null) {
                if (dtmaster instanceof DTCurso) {
                    DTCurso dtc = (DTCurso) dtmaster;
                    if (dtc != null) {
                        String urlConVersion = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, dtc.getImage(), request.getContextPath());  
            %>  
                        <!-- Cabecera de Detalle en Móvil -->
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4 class="fw-bold m-0">Consulta de Curso</h4>
                            <a href="${pageContext.request.contextPath}/ConsultaCursoServlet" class="btn btn-outline-dark btn-sm fw-bold">
                                ← Volver
                            </a>
                        </div>

                        <!-- Información Principal del Curso -->
                        <div class="card p-3 border-dark bg-white shadow-sm mb-3">
                            <div class="text-center mb-3">
                                <% if (dtc.getImage() != null && !dtc.getImage().trim().isEmpty()) { %>
                                    <img src="<%= urlConVersion %>" alt="Logo Curso" class="rounded border border-2 border-dark shadow-sm w-100" style="max-height: 200px; object-fit: cover;">
                                <% } else { %>
                                    <img src="<%= request.getContextPath() %>/Images/ImagenUserDefault.png" alt="Logo Curso" class="rounded border border-2 border-dark shadow-sm w-100" style="max-height: 200px; object-fit: cover;">
                                <% } %>
                            </div>
                            <h3 class="fw-bold mb-1 fs-4"><%= dtc.getNombre() %></h3>
                            <p class="text-muted mb-2"><%= dtc.getInstituto() %></p>

                            <% if (dtc.getURL() != null && !dtc.getURL().isBlank()) { %>
                                <div class="my-2">
                                    <a href="<%= dtc.getURL() %>" target="_blank" class="text-primary text-decoration-underline small"><%= dtc.getURL() %></a>
                                </div>
                            <% } %>
                        </div>

                        <!-- Descripción -->
                        <div class="card p-3 border-dark bg-white shadow-sm mb-3">
                            <h5 class="fw-bold border-bottom pb-2 fs-6">Descripción</h5>
                            <p class="mb-0 text-break small" style="white-space: pre-line; word-wrap: break-word;">
                                <%= dtc.getDescripcion() != null ? dtc.getDescripcion() : "Sin descripción disponible." %>
                            </p>
                        </div>

                        <!-- Ediciones del Curso -->
                        <div class="card p-3 border-dark shadow-sm bg-white mb-3">
                            <h5 class="fw-bold mb-3 border-bottom pb-2 fs-6">Ediciones de Cursos</h5>
                            <ul class="list-group list-group-flush">
                                <%
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
                                                        <img src="<%= urlConVersionEdi %>" alt="Edición" width="35" height="35" class="rounded border flex-shrink-0" style="object-fit: cover;">
                                                        <div class="text-truncate">
                                                            <h6 class="mb-0 fw-semibold text-truncate small"><%= dte.getNombre() %></h6>
                                                            <small class="text-muted" style="font-size: 0.75rem;">Inicio: <%= dte.getFInicio() != null ? new SimpleDateFormat("dd/MM/yyyy").format(dte.getFInicio()) : "" %></small>
                                                        </div>
                                                    </div>
                                                    <a href="${pageContext.request.contextPath}/ConsultaEdicionServlet?nombre=<%= java.net.URLEncoder.encode(dte.getNombre(), "UTF-8") %>" class="btn btn-outline-dark btn-sm flex-shrink-0 fw-bold" style="font-size: 0.75rem; padding: 2px 8px;">
                                                        Ver
                                                    </a>
                                                </li>
                                <%
                                            }
                                        }
                                    } else {
                                %>
                                        <li class="list-group-item text-muted px-0 bg-transparent text-center small">
                                            No hay ediciones registradas.
                                        </li>
                                <% } %>
                            </ul>
                        </div>

                        <!-- Programas de Formación (Visualización Únicamente, SIN BOTÓN DE VER) -->
                        <div class="card p-3 border-dark shadow-sm bg-white mb-4">
                            <h5 class="fw-bold mb-3 border-bottom pb-2 fs-6">Programas de Formación</h5>
                            <ul class="list-group list-group-flush">
                                <%
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
                                                <li class="list-group-item d-flex align-items-center gap-2 px-0 py-2 bg-transparent">
                                                    <img src="<%= urlConVersionPro %>" alt="Programa" width="45" height="30" class="rounded border flex-shrink-0" style="object-fit: cover;">
                                                    <div class="text-truncate">
                                                        <h6 class="mb-0 fw-semibold text-truncate small text-dark"><%= dtp.getNombre() %></h6>
                                                    </div>
                                                </li>
                                <%
                                            }
                                        }
                                    } else {
                                %>
                                        <li class="list-group-item text-muted px-0 bg-transparent text-center small">
                                            No hay programas vinculados.
                                        </li>
                                <% } %>
                            </ul>
                        </div>
            <%  
                    }
                }
            } else {
                // Listado general de cursos para móvil
            %>
                <h4 class="mb-3 fw-bold text-center">Listado de Cursos</h4>
                <div class="card border-dark shadow-sm p-3 bg-white">
                    <h6 class="fw-bold border-bottom pb-2 mb-2 small">Seleccione un curso:</h6>
                    <ul class="list-group list-group-flush">
                        <%
                            List<DTMaster> listaCursos = (List<DTMaster>) request.getAttribute("cursos");
                            if (listaCursos != null && !listaCursos.isEmpty()) {
                                for (DTMaster mCurso : listaCursos) {
                                    if (mCurso instanceof DTCurso) {
                                        DTCurso cursoItem = (DTCurso) mCurso;
                                        String imgCurso = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, cursoItem.getImage(), request.getContextPath());
                        %>
                                        <li class="list-group-item d-flex align-items-center justify-content-between gap-2 px-2 py-2 bg-transparent border-bottom">
                                            <div class="d-flex align-items-center gap-2 text-truncate">
                                                <img src="<%= imgCurso %>" alt="Curso" width="40" height="40" class="rounded-circle border border-dark flex-shrink-0" style="object-fit: cover;">
                                                <div class="text-truncate">
                                                    <h6 class="fw-bold text-dark mb-0 text-truncate small"><%= cursoItem.getNombre() %></h6>
                                                    <small class="text-muted" style="font-size: 0.7rem;">Inst: <%= cursoItem.getInstituto() %></small>
                                                </div>
                                            </div>
                                            <a href="${pageContext.request.contextPath}/ConsultaCursoServlet?nombre=<%= java.net.URLEncoder.encode(cursoItem.getNombre(), "UTF-8") %>" class="btn btn-dark btn-sm fw-bold px-2 py-1 flex-shrink-0" style="font-size: 0.75rem;">
                                                Ver
                                            </a>
                                        </li>
                        <%
                                    }
                                }
                            } else {
                        %>
                                <li class="list-group-item text-muted text-center py-3 small">No hay cursos disponibles.</li>
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
            <div class="w-100 px-3" style="max-width: 1000px;">
                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                <% } %>
                
                <%  
                DTMaster dtmaster = (DTMaster) request.getAttribute("cursoElegido");
                if (dtmaster != null) {
                    if (dtmaster instanceof DTCurso) {
                        DTCurso dtc = (DTCurso) dtmaster;
                        if (dtc != null) {
                            String urlConVersion = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, dtc.getImage(), request.getContextPath());  
                %>        
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2 class="fw-bold m-0">Consulta de Curso</h2>
                                <a href="${pageContext.request.contextPath}/ConsultaCursoServlet" class="btn btn-outline-dark btn-sm fw-bold">
                                    ← Volver al listado de cursos
                                </a>
                            </div>

                            <div class="row g-4 mt-1">
                                <%-- COLUMNA IZQUIERDA: Detalles del Curso y Descripción --%>
                                <div class="col-md-7 col-lg-8">
                                    <div class="d-flex align-items-start gap-3 mb-3">
                                        <% if (dtc.getImage() != null && !dtc.getImage().trim().isEmpty()) { %>
                                            <img src="<%= urlConVersion %>" alt="Logo Curso" width="250" height="150" class="rounded border border-2 border-dark shadow-sm flex-shrink-0" style="object-fit: cover;">
                                        <% } else { %>
                                            <img src="<%= request.getContextPath() %>/Images/ImagenUserDefault.png" alt="Logo Curso" width="250" height="150" class="rounded border border-2 border-dark shadow-sm flex-shrink-0" style="object-fit: cover;">
                                        <% } %>
                                        <div>
                                            <h3 class="fw-bold mb-1"><%= dtc.getNombre() %></h3>
                                            <p class="text-muted fs-5 mb-2"><%= dtc.getInstituto() %></p>
                                        </div>
                                    </div>

                                    <% if (dtc.getURL() != null && !dtc.getURL().isBlank()) { %>
                                        <div class="position-relative d-inline-block my-2">
                                            <a href="<%= dtc.getURL() %>" target="_blank" class="text-primary text-decoration-underline"><%= dtc.getURL() %></a>
                                        </div>
                                    <% } %>

                                    <div class="card p-3 border-dark bg-white shadow-sm mt-3">
                                        <h5 class="fw-bold border-bottom pb-2">Descripcion</h5>
                                        <p class="mb-0 text-break" style="white-space: pre-line; word-wrap: break-word;">
                                            <%= dtc.getDescripcion() != null ? dtc.getDescripcion() : "Sin descripcion disponible." %>
                                        </p>
                                    </div>
                                </div>

                                <%-- COLUMNA DERECHA: Ediciones y Programas --%>
                                <div class="col-md-5 col-lg-4">
                                    <div class="card p-3 border-dark shadow-sm bg-white mb-4">
                                        <h4 class="fw-bold mb-3 border-bottom pb-2">Ediciones de Cursos</h4>
                                        <ul class="list-group list-group-flush">
                                            <%
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
                                                                    <img src="<%= urlConVersionEdi %>" alt="Logo Edicion" width="40" height="40" class="rounded border flex-shrink-0" style="object-fit: cover;">
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

                                    <div class="card p-3 border-dark shadow-sm bg-white">
                                        <h4 class="fw-bold mb-3 border-bottom pb-2">Programas de Formacion</h4>
                                        <ul class="list-group list-group-flush">
                                            <%
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
                                                            <li class="list-group-item d-flex align-items-center justify-content-between gap-2 px-0 py-2 bg-transparent">
                                                                <div class="d-flex align-items-center gap-2 text-truncate">
                                                                    <img src="<%= urlConVersionPro %>" alt="Logo Programa" width="60" height="40" class="rounded border flex-shrink-0" style="object-fit: cover;">
                                                                    <div class="text-truncate">
                                                                        <h6 class="mb-0 fw-semibold text-truncate"><%= dtp.getNombre() %></h6>
                                                                    </div>
                                                                </div>
                                                                <a href="${pageContext.request.contextPath}/ConsultaProgramaServlet?nombre=<%= java.net.URLEncoder.encode(dtp.getNombre(), "UTF-8") %>" class="btn btn-outline-dark btn-sm flex-shrink-0 fw-bold">
                                                                    Ver
                                                                </a>
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
                } else {
                %>
                    <h2 class="mb-4 fw-bold text-center">Listado de Cursos</h2>
                    <div class="card border-dark shadow p-4 bg-white">
                        <h4 class="fw-bold border-bottom pb-3 mb-3">Seleccione un curso para ver su información completa</h4>
                        <ul class="list-group list-group-flush">
                            <%
                                List<DTMaster> listaCursos = (List<DTMaster>) request.getAttribute("cursos");
                                if (listaCursos != null && !listaCursos.isEmpty()) {
                                    for (DTMaster mCurso : listaCursos) {
                                        if (mCurso instanceof DTCurso) {
                                            DTCurso cursoItem = (DTCurso) mCurso;
                                            String imgCurso = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, cursoItem.getImage(), request.getContextPath());
                            %>
                                            <li class="list-group-item d-flex align-items-center justify-content-between gap-3 px-3 py-3 bg-transparent border-bottom">
                                                <div class="d-flex align-items-center gap-3">
                                                    <img src="<%= imgCurso %>" alt="Curso" width="60" height="60" class="rounded-circle border border-2 border-dark shadow-sm" style="object-fit: cover;">
                                                    <div>
                                                        <h5 class="fw-bold text-dark mb-1"><%= cursoItem.getNombre() %></h5>
                                                        <p class="text-muted small mb-0">Instituto: <%= cursoItem.getInstituto() %></p>
                                                    </div>
                                                </div>
                                                <a href="${pageContext.request.contextPath}/ConsultaCursoServlet?nombre=<%= java.net.URLEncoder.encode(cursoItem.getNombre(), "UTF-8") %>" class="btn btn-dark btn-sm fw-bold px-4">
                                                    Ver Curso
                                                </a>
                                            </li>
                            <%
                                        }
                                    }
                                } else {
                            %>
                                    <li class="list-group-item text-muted text-center py-4">No hay cursos disponibles en el sistema.</li>
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