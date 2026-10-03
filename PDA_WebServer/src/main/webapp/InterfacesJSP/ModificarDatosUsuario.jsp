<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modificar Datos</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Modificar Datos de Usuario</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/ModificarUsuarioServlet" method="POST">
            <label>Nickname (No editable):</label><br>
            <input type="text" name="nickname" value="${usuario.nickname}" readonly><br>
            <label>Correo (No editable):</label><br>
            <input type="email" name="correo" value="${usuario.correo}" readonly><br>
            <label>Nombre:</label><br>
            <input type="text" name="nombre" value="${usuario.nombre}" required><br>
            <label>Apellido:</label><br>
            <input type="text" name="apellido" value="${usuario.apellido}" required><br>
            <label>Fecha de Nacimiento:</label><br>
            <input type="date" name="fechaNacimiento" value="${usuario.fechaNacimiento}" required><br><br>
            <button type="submit">Guardar Cambios</button>
        </form>
    </div>
</body>
</html>