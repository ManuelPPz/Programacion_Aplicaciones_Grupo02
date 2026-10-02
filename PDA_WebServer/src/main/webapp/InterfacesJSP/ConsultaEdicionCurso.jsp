<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta Edición de Curso</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Consulta de Edición de Curso</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/ConsultaEdicionServlet" method="GET">
            <label>Seleccionar Curso:</label>
            <select name="curso">
                 <!-- Opciones -->
            </select>
            <button type="submit">Ver Ediciones</button>
        </form>
        <form action="${pageContext.request.contextPath}/ConsultaEdicionServlet" method="GET">
            <label>Seleccionar Edición:</label>
            <select name="edicion">
                 <!-- Opciones de ediciones -->
            </select>
            <button type="submit">Ver Detalles</button>
        </form>
    </div>
</body>
</html>