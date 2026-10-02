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
                  <a class="navbar-brand" href="#">Navbar</a>
                  <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                  </button>
                  <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                      <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="#">Home</a>
                      </li>
                      <li class="nav-item">
                        <a class="nav-link" href="#">Link</a>
                      </li>
                      <li class="nav-item">
                        <a class="nav-link disabled" aria-disabled="true">Disabled</a>
                      </li>
                    </ul>

                    <!-- Buscador -->
                    <form class="d-flex me-lg-3 mb-2 mb-lg-0" role="search">
                      <input class="form-control me-2" type="search" placeholder="Buscar..." aria-label="Search"/>
                      <button class="btn btn-outline-success" type="submit">Buscar</button>
                    </form>
                        <%-- Muestra el usuario autenticado --%>
                        <% 
                            DTMaster usuario = (DTMaster) request.getAttribute("usuario");
                            if (usuario != null) {
                                if (usuario instanceof DTDocente) {
                                    DTDocente dtd = (DTDocente) usuario;
                        %>
                                    <!-- Dropdown con Imagen de Usuario -->
                                    <ul class="navbar-nav">
                                      <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                          <img src="<%= request.getContextPath() %><%= versionedUrl(application, "/Images/ImagenUserDefault.png") %>" 
                                               alt="Usuario" 
                                               width="32" 
                                               height="32" 
                                               class="rounded-circle me-1 border">
                                        </a>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                          <li><a class="dropdown-item" href="#">Mi Perfil</a></li>
                                          <li><a class="dropdown-item" href="#">Crear Curso</a></li>
                                          <li><hr class="dropdown-divider"></li>
                                          <li><a class="dropdown-item text-danger" href="#">Cerrar Sesión</a></li>
                                        </ul>
                                      </li>
                                    </ul>
                        <% 
                                } else if (usuario instanceof DTUsuario) { 
                                    DTUsuario dtu = (DTUsuario) usuario;
                        %>            
                                    <!-- Dropdown con Imagen de Usuario -->
                                    <ul class="navbar-nav">
                                      <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                          <img src="<%= request.getContextPath() %><%= versionedUrl(application, "/Images/ImagenUserDefault.png") %>" 
                                               alt="Usuario" 
                                               width="32" 
                                               height="32" 
                                               class="rounded-circle me-1 border">
                                        </a>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                          <li><a class="dropdown-item" href="#">Mi Perfil</a></li>
                                          <li><a class="dropdown-item" href="#">Inscribirme</a></li>
                                          <li><hr class="dropdown-divider"></li>
                                          <li><a class="dropdown-item text-danger" href="#">Cerrar Sesión</a></li>
                                        </ul>
                                      </li>
                                    </ul>
                        <% 
                                }
                            } else {
                        %>
                                <!-- Dropdown con Imagen de Usuario -->
                                    <ul class="navbar-nav">
                                      <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                          <img src="<%= request.getContextPath() %><%= versionedUrl(application, "/Images/ImagenUserDefault.png") %>" 
                                               alt="Usuario" 
                                               width="32" 
                                               height="32" 
                                               class="rounded-circle me-1 border">
                                        </a>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                          <li><a class="dropdown-item" href="#">Iniciar Sesion</a></li>
                                          <li><a class="dropdown-item" href="#">Registrarme</a></li>
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