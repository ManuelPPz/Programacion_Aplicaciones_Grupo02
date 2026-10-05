<<<<<<< HEAD
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="Logica.DTsClasses.DTDocente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>edEXT – Menú Principal</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <%
            DTUsuarioBase usuario = (DTUsuarioBase) session.getAttribute("usuarioLogueado");
            boolean esDocente = (usuario instanceof DTDocente);
        %>
        <h1>edEXT – Plataforma de Formación Extendida
            <a class="logout" href="${pageContext.request.contextPath}/CierreSesionServlet" style="float: right; color: #e74c3c; font-size: 16px;">Cerrar Sesión</a>
        </h1>

        <p class="bienvenida">Bienvenido/a, <strong><%= usuario != null ? usuario.getNickname() : "" %></strong></p>

        <!-- Sección: Usuario -->
        <div class="seccion" style="background:#fff; border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 5px;">
            <h3>👤 Usuarios</h3>
            <ul style="list-style: none; padding: 0;">
                <li><a href="${pageContext.request.contextPath}/AltaUsuarioServlet">Alta de Usuario (ED-15)</a></li>
                <li><a href="${pageContext.request.contextPath}/ConsultaUsuarioServlet">Consulta de Usuario (ED-16/17)</a></li>
                <li><a href="${pageContext.request.contextPath}/ModificarUsuarioServlet">Modificar Mis Datos (ED-18)</a></li>
            </ul>
        </div>

        <!-- Sección: Cursos – solo Docente -->
        <% if (esDocente) { %>
        <div class="seccion" style="background:#fff; border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 5px;">
            <h3>📚 Cursos (Docente)</h3>
            <ul style="list-style: none; padding: 0;">
                <li><a href="${pageContext.request.contextPath}/AltaCursoServlet">Alta de Curso (ED-19)</a></li>
                <li><a href="${pageContext.request.contextPath}/ConsultaCursoServlet">Consulta de Curso (ED-20)</a></li>
                <li><a href="${pageContext.request.contextPath}/AltaEdicionServlet">Alta Edición de Curso (ED-21)</a></li>
                <li><a href="${pageContext.request.contextPath}/ConsultaEdicionServlet">Consulta Edición de Curso (ED-22)</a></li>
            </ul>
        </div>
        <% } %>

        <!-- Sección: Inscripciones – Estudiante -->
        <div class="seccion" style="background:#fff; border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 5px;">
            <h3>✍️ Inscripciones</h3>
            <ul style="list-style: none; padding: 0;">
                <li><a href="${pageContext.request.contextPath}/InscripcionEdicionServlet">Inscripción a Edición de Curso (ED-23)</a></li>
                <li><a href="${pageContext.request.contextPath}/InscripcionProgramaServlet">Inscripción a Programa de Formación (ED-27)</a></li>
            </ul>
        </div>

        <!-- Sección: Programas de Formación -->
        <div class="seccion" style="background:#fff; border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 5px;">
            <h3>🎓 Programas de Formación</h3>
            <ul style="list-style: none; padding: 0;">
                <% if (esDocente) { %>
                <li><a href="${pageContext.request.contextPath}/CrearProgramaServlet">Crear Programa de Formación (ED-24)</a></li>
                <li><a href="${pageContext.request.contextPath}/AgregarCursoProgramaServlet">Agregar Curso a Programa (ED-25)</a></li>
                <% } %>
                <li><a href="${pageContext.request.contextPath}/ConsultaProgramaServlet">Consulta de Programa (ED-26)</a></li>
            </ul>
        </div>
    </div>
</body>
=======
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
>>>>>>> v2.0.1
</html>