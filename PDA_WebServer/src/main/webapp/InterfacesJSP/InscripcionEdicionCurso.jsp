<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscripción a Edición de Curso</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Inscripción a Edición de Curso</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/InscripcionEdicionServlet" method="POST">
            <label>Seleccionar Curso:</label><br>
            <select name="curso" required>
                <!-- Cursos -->
            </select><br>
            <label>Edición Vigente:</label><br>
            <input type="text" name="edicion" value="${edicionVigente.nombre}" readonly required><br><br>
            <button type="submit">Inscribirme</button>
        </form>
    </div>
</body>
</html>