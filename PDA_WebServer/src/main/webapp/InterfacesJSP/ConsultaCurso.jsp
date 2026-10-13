<%@page import="Logica.DTsClasses.DTCurso"%>
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
        <title>Consulta Curso</title>
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
                    <% } 
                    
                    DTMaster dtmaster = (DTMaster)request.getAttribute("cursoElegido");
                    if(dtmaster !=null){
                        if(dtmaster instanceof DTCurso){
                            DTCurso dtc = (DTCurso)dtmaster;
                            if(dtc!=null){
                                String relPath = "/uploads/cursos/" + dtc.getImage();
                                String urlConVersion = versionedUrl(relPath);
                                %>                          
                                    <%-- CONTENIDO DE CONSULTA DE CURSO --%>
                                    <h2>Consulta de Curso</h2>

                                    <div class="row g-4 mt-1">

                                        <%-- COLUMNA IZQUIERDA: Detalles del Curso y Descripción --%>
                                        <div class="col-md-7 col-lg-8">

                                            <%-- Cabecera con Imagen + Título e Instituto --%>
                                            <div class="d-flex align-items-start gap-3 mb-3">
                                                <% if(dtc.getImage() != null) { %>
                                                    <img src="<%= request.getContextPath() %><%= urlConVersion %>" 
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
                                            <% if(dtc.getURL() != null && !dtc.getURL().isBlank()) { %>
                                                        <a href="<%= dtc.getURL() %>" class="btn-solid theme-primary stretched-link"><%= dtc.getURL() %></a>
                                                    <% } %>
                                            <%-- Descripción del Curso (Control de Ancho y Salto de Línea) --%>
                                            <div class="card p-3 border-dark bg-white shadow-sm mt-3">
                                                <h5 class="fw-bold border-bottom pb-2">Descripción</h5>
                                                <p class="mb-0 text-break" style="white-space: pre-line; word-wrap: break-word;">
                                                    <%= dtc.getDescripcion() != null ? dtc.getDescripcion() : "Sin descripción disponible." %>
                                                </p>
                                            </div>

                                        </div>

                                        <%-- COLUMNA DERECHA: Ediciones de Curso --%>
                                        <div class="col-md-5 col-lg-4">
                                            <div class="card p-3 border-dark shadow-sm bg-white">
                                                <h4 class="fw-bold mb-3 border-bottom pb-2">Ediciones de Cursos</h4>

                                                <ul class="list-group list-group-flush">
                                                    <li class="list-group-item d-flex align-items-center gap-2 px-0">
                                                        <img src="<%= request.getContextPath() %>/Images/Logo edEXT sin fondo.png" 
                                                             alt="Logo Edicion" 
                                                             width="45" 
                                                             height="45"
                                                             class="rounded border">
                                                        <div class="text-truncate">
                                                            <h6 class="mb-0 fw-semibold text-truncate">Taller de robotica educativa 2026/2</h6>
                                                        </div>
                                                    </li>

                                                    <li class="list-group-item d-flex align-items-center gap-2 px-0">
                                                        <img src="<%= request.getContextPath() %>/Images/Logo edEXT sin fondo.png" 
                                                             alt="Logo Edicion" 
                                                             width="45" 
                                                             height="45"
                                                             class="rounded border">
                                                        <div class="text-truncate">
                                                            <h6 class="mb-0 fw-semibold text-truncate">Taller de robotica educativa 2026/2</h6>
                                                        </div>
                                                    </li>

                                                    <li class="list-group-item d-flex align-items-center gap-2 px-0">
                                                        <img src="<%= request.getContextPath() %>/Images/Logo edEXT sin fondo.png" 
                                                             alt="Logo Edicion" 
                                                             width="45" 
                                                             height="45"
                                                             class="rounded border">
                                                        <div class="text-truncate">
                                                            <h6 class="mb-0 fw-semibold text-truncate">Taller de robotica educativa 2026/2</h6>
                                                        </div>
                                                    </li>
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

            </div>
        </div>
        
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>