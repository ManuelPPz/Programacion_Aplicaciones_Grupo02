<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Curso</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Consulta de Curso</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/ConsultaCursoServlet" method="GET">
            <label>Filtrar por Instituto o Categoría:</label>
            <select name="filtro">
                 <!-- Opciones -->
            </select>
            <button type="submit">Buscar Cursos</button>
        </form>
        <hr>
        <form action="${pageContext.request.contextPath}/ConsultaCursoServlet" method="GET">
            <label>Seleccionar Curso:</label>
            <select name="curso">
                 <!-- Opciones de cursos filtrados -->
            </select>
            <button type="submit">Ver Información</button>
        </form>
    </div>
</body>
</html>