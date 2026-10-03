<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Programa de Formación</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Consulta de Programa de Formación</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/ConsultaProgramaServlet" method="GET">
            <label>Seleccionar Programa:</label><br>
            <select name="programa" required>
                <!-- Programas -->
            </select><br><br>
            <button type="submit">Ver Detalles</button>
        </form>
    </div>
</body>
</html>