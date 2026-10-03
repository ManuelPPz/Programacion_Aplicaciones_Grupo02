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
</html>