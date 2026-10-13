<%@page import="java.io.File"%>
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
<%@ page import="jakarta.servlet.ServletContext" %>
<%@ page import="jakarta.servlet.http.HttpServletRequest" %>
<%@page import="Logica.util.ImagenUtil"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consulta Usuario</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
        
        <script>
            function openTab(evt, tabName) {
                var tabcontent = document.getElementsByClassName("tab-content");
                for (var i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }

                var tablinks = document.getElementsByClassName("tab-btn");
                for (var i = 0; i < tablinks.length; i++) {
                    tablinks[i].classList.remove("active", "btn-primary");
                    tablinks[i].classList.add("btn-outline-primary");
                }

                document.getElementById(tabName).style.display = "block";
                evt.currentTarget.classList.add("active", "btn-primary");
                evt.currentTarget.classList.remove("btn-outline-primary");
            }
        </script>
    </head>
    <body class="bg-light">
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">

            <div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
                <h2 class="card-title text-center mb-4 fw-bold">Consulta Usuario</h2>

                <% if (request.getAttribute("error") != null) {%>
                <div class="alert alert-danger"><%= request.getAttribute("error")%></div>
                <% } %>

                <%
                    DTUsuarioBase usuarioConsultado = (DTUsuarioBase) request.getAttribute("usuarioConsultado");
                    if (usuarioConsultado != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaFormateada = (usuarioConsultado.getFNac() != null) ? sdf.format(usuarioConsultado.getFNac()) : "";
                        
                        DTMaster dtmLogueado = (DTMaster) session.getAttribute("usuarioLogueado");
                        boolean esElMismoUsuario = false;

                        if (dtmLogueado != null && dtmLogueado instanceof DTUsuarioBase) {
                            DTUsuarioBase dtuLogueado = (DTUsuarioBase) dtmLogueado;
                            if (dtuLogueado.getNickname().equals(usuarioConsultado.getNickname())) {
                                esElMismoUsuario = true;
                            }
                        }

                        Boolean loSigue = (Boolean) request.getAttribute("loSigue");
                        if (loSigue == null) loSigue = false;
                %>
                
                <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-3">
                    <div>
                        <h3 class="fw-bold mb-0"><%= usuarioConsultado.getNombre() %> <%= usuarioConsultado.getApellido() %> (<%= usuarioConsultado.getNickname() %>)</h3>
                    </div>

                    <% if (dtmLogueado != null && !esElMismoUsuario) { %>
                        <div>
                            <% if (loSigue) { %>
                                <form action="${pageContext.request.contextPath}/DejarDeSeguirServlet" method="POST" class="d-inline">
                                    <input type="hidden" name="nickname" value="<%= usuarioConsultado.getNickname() %>">
                                    <button type="submit" class="btn btn-outline-danger fw-bold px-4">Dejar de seguir</button>
                                </form>
                            <% } else { %>
                                <form action="${pageContext.request.contextPath}/SeguirUserServlet" method="POST" class="d-inline">
                                    <input type="hidden" name="nickname" value="<%= usuarioConsultado.getNickname() %>">
                                    <button type="submit" class="btn btn-primary fw-bold px-4">Seguir</button>
                                </form>
                            <% } %>
                        </div>
                    <% } %>
                </div>

                <div class="foto-perfil-container mb-3">
                    <% if (usuarioConsultado.getImg() != null && !usuarioConsultado.getImg().trim().isEmpty()) {%>
                    <img src="<%= ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PERFILES, usuarioConsultado.getImg(), request.getContextPath()) %>" 
                         alt="Perfil" width="110" height="110" style="object-fit: cover;" class="rounded-circle border">
                    <% } else {%>
                    <img src="<%= request.getContextPath() %>/Images/ImagenUserDefault.png" 
                         alt="Perfil Default" width="110" height="110" style="object-fit: cover;" class="rounded-circle border">
                    <% }%>
                </div>

                <div class="tab-header mb-3 d-flex flex-wrap gap-2">
                    <button class="tab-btn btn btn-primary active" onclick="openTab(event, 'tabInfo')">Informacion General</button>
                    <button class="tab-btn btn btn-outline-primary" onclick="openTab(event, 'tabActividad')">Actividad (Cursos/Programas)</button>
                    <button class="tab-btn btn btn-outline-primary" onclick="openTab(event, 'tabSeguidores')">Seguidores</button>
                </div>

                <div id="tabInfo" class="tab-content" style="display: block;">
                    <p><strong>Nickname:</strong> <%= usuarioConsultado.getNickname() %></p>
                    <p><strong>Nombre:</strong> <%= usuarioConsultado.getNombre() %></p>
                    <p><strong>Apellido:</strong> <%= usuarioConsultado.getApellido() %></p>
                    <p><strong>Email:</strong> <%= usuarioConsultado.getCorreo() %></p>
                    <p><strong>Fecha Nacimiento:</strong> <%= fechaFormateada %></p>
                    <% if (usuarioConsultado instanceof DTDocente) {
                            DTDocente docente = (DTDocente) usuarioConsultado;
                    %>
                    <p><strong>Tipo:</strong> Docente</p>
                    <p><strong>Instituto:</strong> <%= docente.getInstituto() != null && !docente.getInstituto().isEmpty() ? docente.getInstituto() : "N/A" %></p>
                    <% } else { %>
                    <p><strong>Tipo:</strong> Estudiante</p>
                    <% }
                        if (esElMismoUsuario) {
                    %>
                    <form action="${pageContext.request.contextPath}/ModificarUsuarioServlet" method="GET" class="d-inline mt-2">
                        <button type="submit" class="btn-solid btn-styled theme-primary">Modificar Datos</button>
                    </form>
                    <% } %>
                </div>

                <div id="tabActividad" class="tab-content" style="display: none;">
                    
                    <div class="mb-4">
                        <h4 class="fw-bold text-dark border-bottom pb-2 mb-3">
                            <%= (usuarioConsultado instanceof DTDocente) ? "Ediciones de Cursos Impartidas" : "Mis Inscripciones a Ediciones" %>
                        </h4>
                        
                        <div class="row g-3">
                            <% 
                                List<String> ediciones = usuarioConsultado.getEdiciones();
                                if (ediciones != null && !ediciones.isEmpty()) {
                                    for (String ed : ediciones) {
                                        String linkEdicion = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(ed, "UTF-8");
                                        String imgEdicion = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_EDICIONCURSOS, ed, request.getContextPath());
                            %>
                            <div class="col-md-6">
                                <div class="card border-dark shadow-sm h-100 bg-white position-relative">
                                    <div class="card-body d-flex gap-3 align-items-center p-3">
                                        <img src="<%= imgEdicion %>" alt="Logo Edicion" width="65" height="65" class="rounded border object-fit-cover shadow-sm flex-shrink-0" onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
                                        <div class="flex-grow-1 overflow-hidden">
                                            <span class="badge bg-success mb-1">Edicion</span>
                                            <h5 class="card-title mb-0 text-truncate">
                                                <a href="<%= linkEdicion %>" class="text-dark text-decoration-none fw-bold stretched-link">
                                                    <%= ed %>
                                                </a>
                                            </h5>
                                            <small class="text-muted">Ver detalles de la edicion</small>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% 
                                    }
                                } else { 
                            %>
                            <div class="col-12">
                                <div class="alert alert-secondary text-center mb-0" role="alert">
                                    <%= (usuarioConsultado instanceof DTDocente) ? "Este docente no imparte ediciones actualmente." : "Este estudiante no esta inscripto a ninguna edicion." %>
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>

                    <div>
                        <h4 class="fw-bold text-dark border-bottom pb-2 mb-3">Programas de Formacion</h4>
                        
                        <div class="row g-3">
                            <% 
                                List<String> programas = usuarioConsultado.getProgramas();
                                if (programas != null && !programas.isEmpty()) {
                                    for (String prog : programas) {
                                        String linkPrograma = request.getContextPath() + "/ConsultaProgramaServlet?nombre=" + java.net.URLEncoder.encode(prog, "UTF-8");
                                        String imgPrograma = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PROGRAMAS, prog, request.getContextPath());
                            %>
                            <div class="col-md-6">
                                <div class="card border-dark shadow-sm h-100 bg-white position-relative">
                                    <div class="card-body d-flex gap-3 align-items-center p-3">
                                        <img src="<%= imgPrograma %>" alt="Logo Programa" width="65" height="65" class="rounded border object-fit-cover shadow-sm flex-shrink-0" onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
                                        <div class="flex-grow-1 overflow-hidden">
                                            <span class="badge bg-info text-dark mb-1">Programa</span>
                                            <h5 class="card-title mb-0 text-truncate">
                                                <a href="<%= linkPrograma %>" class="text-dark text-decoration-none fw-bold stretched-link">
                                                    <%= prog %>
                                                </a>
                                            </h5>
                                            <small class="text-muted">Ver detalles del programa</small>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <% 
                                    }
                                } else { 
                            %>
                            <div class="col-12">
                                <div class="alert alert-secondary text-center mb-0" role="alert">
                                    No esta vinculado a ningun programa de formacion.
                                </div>
                            </div>
                            <% } %>
                        </div>
                    </div>

                </div>

                <div id="tabSeguidores" class="tab-content" style="display: none;">
                    <h4 class="fw-bold mt-2">Usuarios que lo siguen:</h4>
                    <ul class="list-group list-group-flush mt-3">
                        <% 
                            List<DTUsuarioBase> listaSeguidores = (List<DTUsuarioBase>) request.getAttribute("listaSeguidores"); 
                            
                            if (listaSeguidores != null && !listaSeguidores.isEmpty()) {
                                for (DTUsuarioBase seg : listaSeguidores) {
                                    String imgSeg = request.getContextPath() + "/Images/ImagenUserDefault.png";
                                    if (seg.getImg() != null && !seg.getImg().isBlank()) {
                                        imgSeg = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PERFILES, seg.getImg(), request.getContextPath());
                                    }
                        %>
                        <li class="list-group-item d-flex align-items-center justify-content-between px-0 bg-transparent">
                            <div class="d-flex align-items-center gap-3">
                                <img src="<%= imgSeg %>" alt="Avatar" width="40" height="40" class="rounded-circle border object-fit-cover">
                                <div>
                                    <h6 class="mb-0 fw-bold"><%= seg.getNombre() %> <%= seg.getApellido() %></h6>
                                    <small class="text-muted">@<%= seg.getNickname() %></small>
                                </div>
                            </div>
                            <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= java.net.URLEncoder.encode(seg.getNickname(), "UTF-8") %>" class="btn btn-outline-dark btn-sm fw-bold">
                                Ver Perfil
                            </a>
                        </li>
                        <% 
                                }
                            } else { 
                        %>
                        <li class="list-group-item text-muted px-0 bg-transparent">Este usuario aun no tiene seguidores registrados.</li>
                        <% } %>
                    </ul>
                </div>

                <% } %>
            </div>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>