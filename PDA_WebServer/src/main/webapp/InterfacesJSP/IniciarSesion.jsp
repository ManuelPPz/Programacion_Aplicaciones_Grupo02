<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Iniciar Sesión</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getParameter("mensaje") != null) { %><div class="alert-success"><%= request.getParameter("mensaje") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/IniciarSesionServlet" method="POST">
            <label>Nickname o Correo:</label><br>
            <input type="text" name="id" required><br><br>
            <label>Contraseña:</label><br>
            <input type="password" name="password" required><br><br>
            <button type="submit">Ingresar</button>
            <button type="button" onclick="window.history.back();">Cancelar</button>
        </form>
    </div>
</body>
</html>