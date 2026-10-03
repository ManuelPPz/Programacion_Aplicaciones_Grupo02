<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Crear Programa de Formación</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Crear Programa de Formación</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/CrearProgramaServlet" method="POST">
            <label>Nombre:</label><br>
            <input type="text" name="nombre" required><br>
            <label>Descripción:</label><br>
            <textarea name="descripcion" required></textarea><br>
            <label>Fecha de Inicio:</label><br>
            <input type="date" name="fechaInicio" required><br>
            <label>Fecha de Fin:</label><br>
            <input type="date" name="fechaFin" required><br><br>
            <button type="submit">Crear Programa</button>
        </form>
    </div>
</body>
</html>