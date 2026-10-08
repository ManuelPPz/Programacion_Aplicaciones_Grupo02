<%@page import="Logica.DTsClasses.DTUsuario"%>
<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTCategoria"%>
<%@page import="java.util.List"%>
<%@page import="Logica.DTsClasses.DTDocente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%!
    // Función helper para evadir caché apuntando a la carpeta física real
    public String versionedUrl(String relativePath) {
        String discoPath = "C:/mi_proyecto_data" + relativePath;
        java.io.File file = new java.io.File(discoPath);
        
        if (file.exists()) {
            return relativePath + "?v=" + file.lastModified();
        }
        return relativePath;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consulta de Perfil</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body>
        <%
            DTMaster dtm = (DTMaster)session.getAttribute("usuarioLogueado");
        %>
        <div class="container-fluid px-4 mt-3">
            <%-- BARRA DE NAVEGACIÓN --%>
            <nav class="navbar bg-body-tertiary px-3 rounded border">
                <div class="container-fluid d-flex flex-nowrap align-items-center justify-content-between">

                    <!-- Logo -->
                    <a class="navbar-brand me-3 d-flex align-items-center" href="${pageContext.request.contextPath}/MainMenu">
                        <img src="<%= request.getContextPath() %>/Images/Logo edEXT sin fondo.png" 
                             alt="LogoApp" 
                             width="52" 
                             height="52" 
                             class="rounded-circle border">
                    </a>

                    <!-- Buscador -->
                    <form class="navbar-search-form my-0" role="search">
                        <input class="form-control" type="search" placeholder="Cursos, Programas" aria-label="Search"/>
                        <button class="btn btn-outline-success" type="submit">Buscar</button>
                    </form>

                    <!-- Perfil / Sesión -->
                    <%
                        if (dtm instanceof DTUsuarioBase) {
                            DTUsuarioBase dtu = (DTUsuarioBase) dtm;
                            String nombreImagen = dtu.getImg(); 
                    %>
                        <div class="d-flex align-items-center ms-3">
                            <span class="fw-bold me-3 text-nowrap text-dark fs-5"><%=dtu.getNickname()%></span>    
                            <a class="navbar-brand p-0 m-0 d-flex align-items-center" href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname() %>">
                                <% if (nombreImagen != null && !nombreImagen.trim().isEmpty()) { 
                                       String relPath = "/uploads/perfiles/" + nombreImagen;
                                       String urlConVersion = versionedUrl(relPath);
                                %>
                                    <img src="<%= request.getContextPath() %><%= urlConVersion %>" 
                                         alt="Perfil" 
                                         width="52" 
                                         height="52" 
                                         class="rounded-circle border">
                                <% } else { %>
                                    <img src="<%= request.getContextPath() %>/Images/ImagenUserDefault.png" 
                                         alt="Perfil" 
                                         width="52" 
                                         height="52" 
                                         class="rounded-circle border">
                                <% } %>
                            </a>
                        </div>
                    <%
                        } else {
                    %>
                        <div class="d-flex align-items-center ms-3">
                            <a class="link-opacity-100-hover me-3 text-nowrap" href="IniciarSesionServlet">Iniciar Sesion</a>
                            <div class="vr text-dark opacity-100 me-3" style="height: 1.5rem; width: 2px;"></div>
                            <a class="link-opacity-100-hover text-nowrap" href="AltaUsuarioServlet">Registrarse</a>
                        </div>
                    <%
                        }
                    %>
                </div>
            </nav>
            <hr class="border border-dark border-1 opacity-100 mb-4">
            
            <div class="row">

                <div class="col-md-3 col-lg-2">
                    <div class="card border-dark w-100">
                        <ul class="list-group list-group-flush">
                            <%
                            if (dtm != null) {
                                if (dtm instanceof DTDocente) {
                                    DTDocente dtd = (DTDocente) dtm;
                                    %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtd.getNickname() %>" class="text-dark text-decoration-none d-block">
                                            Mi Perfil
                                        </a>
                                    </li>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">CURSOS</li>
                                    <li class="list-group-item text-dark fw-semibold">Alta Curso</li>
                                    <li class="list-group-item text-dark fw-semibold">Alta Edicion</li>
                                    <li class="list-group-item text-dark fw-semibold">Alta Programa de Formacion</li>
                                    <%
                                } else if (dtm instanceof DTUsuario) {
                                    DTUsuario dtu = (DTUsuario) dtm;
                                    %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname() %>" class="text-dark text-decoration-none d-block">
                                            Mi Perfil
                                        </a>
                                    </li>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">Inscripciones</li>
                                    <li class="list-group-item text-dark fw-semibold">Inscribirme</li>
                                    <li class="list-group-item text-dark fw-semibold">Ver Resultados</li>
                                    <%
                                }
                            }
                            %>
                        </ul>
                        <div class="card-header text-center fw-bold border-top border-dark fs-5">
                            Institutos
                        </div>
                        <ul class="list-group list-group-flush">
                            <%
                                List<DTMaster> listInstitutos = (List<DTMaster>) session.getAttribute("AuxListInstituto");
                                if (listInstitutos != null && !listInstitutos.isEmpty()) {
                                    for (int i = 0; i < listInstitutos.size(); i++) {
                                        if (listInstitutos.get(i) != null) {
                                            DTInstituto auxDt = (DTInstituto) listInstitutos.get(i);
                                            %>
                                            <li class="list-group-item text-dark fw-semibold"><%= auxDt.getNombre() %></li>
                                            <%
                                        }
                                    }
                                } else {
                                    %>
                                    <li class="list-group-item text-muted small fw-semibold">Sin institutos</li>
                                    <%
                                }
                            %>
                        </ul>

                        <div class="card-header text-center fw-bold border-top border-dark fs-5">
                            Categorias
                        </div>
                        <ul class="list-group list-group-flush">
                            <%
                                List<DTMaster> listCategorias = (List<DTMaster>) session.getAttribute("AuxListCategoria");
                                if (listCategorias != null && !listCategorias.isEmpty()) {
                                    for (int i = 0; i < listCategorias.size(); i++) {
                                        if (listCategorias.get(i) != null) {
                                            DTCategoria auxDt = (DTCategoria) listCategorias.get(i);
                                            %>
                                            <li class="list-group-item text-dark fw-semibold"><%= auxDt.getNombre() %></li>
                                            <%
                                        }
                                    }
                                } else {
                                    %>
                                    <li class="list-group-item text-muted small">Sin Categorias</li>
                                    <%
                                }
                            %>
                        </ul>
                        <ul class="list-group list-group-flush">
                            <%
                            if (dtm != null) {
                                if (dtm instanceof DTUsuarioBase) {
                                    DTUsuarioBase dtu = (DTUsuarioBase) dtm;
                                    %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        
                                        <a href="#" class="text-dark text-decoration-none d-block">
                                            <img src="<%= request.getContextPath() %>/Images/Exit_Simbol.png" 
                                                alt="Perfil" 
                                                width="22" 
                                                height="22" 
                                                class="border border-0">
                                            Salir
                                        </a>
                                    </li>
                                    <%
                                }
                            }
                            %>
                        </ul>
                    </div>
                </div>

                        
                <!---Columna despues de user interface default de la pagina-->
                <div class="col-md-9 col-lg-10 ps-md-4">
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                    <% } %>

                    <% 
                        DTUsuarioBase usuarioConsultado = (DTUsuarioBase) request.getAttribute("usuarioConsultado");
                        if (usuarioConsultado != null) { 
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaFormateada = (usuarioConsultado.getFNac() != null) ? sdf.format(usuarioConsultado.getFNac()) : "";
                    %>
                        <h3 class="fw-bold mb-3"><%= usuarioConsultado.getNombre() %> <%= usuarioConsultado.getApellido() %> (<%= usuarioConsultado.getNickname() %>)</h3>

                        <%-- Imagen de Perfil del Usuario Consultado --%>
                        <div class="foto-perfil-container mb-3">
                            <% if (usuarioConsultado.getImg() != null && !usuarioConsultado.getImg().trim().isEmpty()) { 
                                   String relPath = "/uploads/perfiles/" + usuarioConsultado.getImg();
                                   String urlConVersion = versionedUrl(relPath);
                            %>
                                <img src="<%= request.getContextPath() %><%= urlConVersion %>" 
                                     alt="Perfil" 
                                     width="110" 
                                     height="110" 
                                     style="object-fit: cover;"
                                     class="rounded-circle border">
                            <% } else { %>
                                <img src="<%= request.getContextPath() %>/Images/ImagenUserDefault.png" 
                                     alt="Perfil Default" 
                                     width="110" 
                                     height="110" 
                                     style="object-fit: cover;"
                                     class="rounded-circle border">
                            <% } %>
                        </div>

                        <div class="tab-header mb-3">
                            <button class="btn btn-outline-primary active me-2" onclick="openTab(event, 'tabInfo')">Información General</button>
                            <button class="btn btn-outline-primary" onclick="openTab(event, 'tabActividad')">Actividad (Cursos/Programas)</button>
                        </div>

                        <div id="tabInfo" class="tab-content active" style="display: block;">
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
                            <% } %>
                        </div>

                        <div id="tabActividad" class="tab-content" style="display: none;">
                            <% if (usuarioConsultado instanceof DTDocente) { 
                                DTDocente doc = (DTDocente) usuarioConsultado;
                            %>
                                <h4>Ediciones de Cursos Impartidas:</h4>
                                <ul>
                                    <% if (doc.getEdiciones() != null && !doc.getEdiciones().isEmpty()) { 
                                        for (String ed : doc.getEdiciones()) { %>
                                            <li><%= ed %></li>
                                        <% } 
                                    } else { %>
                                        <li>No imparte ediciones actualmente.</li>
                                    <% } %>
                                </ul>
                            <% } else { %>
                                <h4>Inscripciones a Ediciones:</h4>
                                <ul>
                                    <% if (usuarioConsultado.getEdiciones() != null && !usuarioConsultado.getEdiciones().isEmpty()) { 
                                        for (String ed : usuarioConsultado.getEdiciones()) { %>
                                            <li><%= ed %></li>
                                        <% } 
                                    } else { %>
                                        <li>No está inscripto a ninguna edición.</li>
                                    <% } %>
                                </ul>
                            <% } %>

                            <h4>Programas de Formación:</h4>
                            <ul>
                                <% if (usuarioConsultado.getProgramas() != null && !usuarioConsultado.getProgramas().isEmpty()) { 
                                    for (String prog : usuarioConsultado.getProgramas()) { %>
                                        <li><%= prog %></li>
                                    <% } 
                                } else { %>
                                    <li>No está vinculado a programas de formación.</li>
                                <% } %>
                            </ul>
                        </div>
                    <% } %>
                </div>

            </div>
        </div>
        
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>