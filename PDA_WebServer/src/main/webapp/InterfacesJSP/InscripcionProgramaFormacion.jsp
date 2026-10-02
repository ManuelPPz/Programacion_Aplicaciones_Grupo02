<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscripción a Programa de Formación</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Inscripción a Programa de Formación</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/InscripcionProgramaServlet" method="POST">
            <label>Seleccionar Programa:</label><br>
            <select name="programa" required>
                <!-- Programas existentes -->
            </select><br><br>
            <button type="submit">Inscribirme</button>
        </form>
    </div>
</body>
</html>