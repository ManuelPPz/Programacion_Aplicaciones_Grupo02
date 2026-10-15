<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar Curso a Programa</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Agregar Curso a Programa de Formación</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/AgregarCursoProgramaServlet" method="POST">
            <label>Programa de Formación:</label><br>
            <select name="programa" required>
                <!-- Programas -->
            </select><br>
            <label>Curso a agregar:</label><br>
            <select name="curso" required>
                <!-- Cursos -->
            </select><br><br>
            <button type="submit">Agregar Curso</button>
        </form>
    </div>
</body>
</html>