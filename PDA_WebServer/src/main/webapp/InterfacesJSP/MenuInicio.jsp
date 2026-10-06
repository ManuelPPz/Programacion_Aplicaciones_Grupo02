<%@page import="Logica.DTsClasses.DTUsuario"%>
<%@page import="Logica.DTsClasses.DTDocente"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    // Función helper para evadir caché de la imagen si se actualiza
    public String versionedUrl(ServletContext app, String relativePath) {
        String realPath = app.getRealPath(relativePath);
        if (realPath != null) {
            java.io.File file = new java.io.File(realPath);
            if (file.exists()) {
                return relativePath + "?v=" + file.lastModified();
            }
        }
        return relativePath;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menú Principal</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-5">
            <nav class="navbar navbar-expand-lg bg-body-tertiary">
                <div class="container-fluid">
                  <a class="navbar-brand" href="${pageContext.request.contextPath}/MainMenu">edEXT</a>
                  <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                  </button>
                  <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                      <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/MainMenu">Inicio</a>
                      </li>
                      <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/ConsultaCursoServlet">Cursos</a>
                      </li>
                      <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/ConsultaProgramaServlet">Programas</a>
                      </li>
                    </ul>

                    <!-- Buscador -->
                    <form class="d-flex me-lg-3 mb-2 mb-lg-0" role="search" action="${pageContext.request.contextPath}/BuscarServlet" method="GET">
                      <input class="form-control me-2" type="search" name="query" placeholder="Buscar..." aria-label="Search"/>
                      <button class="btn btn-outline-success" type="submit">Buscar</button>
                    </form>
                    
                    <%-- Muestra el usuario autenticado desde la Sesión HTTP --%>
                    <% 
                        DTMaster usuario = (DTMaster) session.getAttribute("usuarioLogueado");
                        if (usuario != null) {
                            if (usuario instanceof DTDocente) {
                                DTDocente dtd = (DTDocente) usuario;
                    %>
                                <!-- Dropdown para DOCENTE -->
                                <ul class="navbar-nav">
                                  <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                      <img src="<%= request.getContextPath() %><%= versionedUrl(application, "/Images/ImagenUserDefault.png") %>" 
                                           alt="Usuario" 
                                           width="32" 
                                           height="32" 
                                           class="rounded-circle me-1 border">
                                      <span class="ms-1 fw-semibold"><%= dtd.getNickname() %></span>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                      <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ConsultaUsuarioServlet">Mi Perfil</a></li>
                                      <li><a class="dropdown-item" href="${pageContext.request.contextPath}/AltaCursoServlet">Crear Curso</a></li>
                                      <li><hr class="dropdown-divider"></li>
                                      <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/MainMenu">Cerrar Sesión</a></li>
                                    </ul>
                                  </li>
                                </ul>
                    <% 
                            } else if (usuario instanceof DTUsuario) { 
                                DTUsuario dtu = (DTUsuario) usuario;
                    %>            
                                <!-- Dropdown para ESTUDIANTE / USUARIO BASE -->
                                <ul class="navbar-nav">
                                  <li class="nav-item dropdown">
                                    <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                      <img src="<%= request.getContextPath() %><%= versionedUrl(application, "/Images/ImagenUserDefault.png") %>" 
                                           alt="Usuario" 
                                           width="32" 
                                           height="32" 
                                           class="rounded-circle me-1 border">
                                      <span class="ms-1 fw-semibold"><%= dtu.getNickname() %></span>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                      <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ConsultaUsuarioServlet">Mi Perfil</a></li>
                                      <li><a class="dropdown-item" href="${pageContext.request.contextPath}/InscripcionEdicionServlet">Inscribirme</a></li>
                                      <li><hr class="dropdown-divider"></li>
                                      <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/MainMenu">Cerrar Sesión</a></li>
                                    </ul>
                                  </li>
                                </ul>
                    <% 
                            }
                        } else {
                    %>
                            <!-- Dropdown para VISITANTE / INVITADO -->
                            <ul class="navbar-nav">
                              <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                  <img src="<%= request.getContextPath() %><%= versionedUrl(application, "/Images/ImagenUserDefault.png") %>" 
                                       alt="Invitado" 
                                       width="32" 
                                       height="32" 
                                       class="rounded-circle me-1 border">
                                  <span class="ms-1 text-muted">Invitado</span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end">
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/index.jsp">Iniciar Sesión</a></li>
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/AltaUsuarioServlet">Registrarme</a></li>
                                </ul>
                              </li>
                            </ul>
                    <% 
                        } 
                    %>
                  </div>
                </div>
            </nav>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>