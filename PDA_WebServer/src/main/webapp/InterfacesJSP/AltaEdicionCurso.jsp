<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Alta Edición de Curso</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Alta Edición de Curso</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/AltaEdicionServlet" method="POST">
            <label>Curso:</label><br>
            <select name="curso" required>
                <!-- Cursos -->
            </select><br>
            <label>Nombre de la Edición:</label><br>
            <input type="text" name="nombre" required><br>
            <label>Fecha de Inicio:</label><br>
            <input type="date" name="fechaInicio" required><br>
            <label>Fecha de Fin:</label><br>
            <input type="date" name="fechaFin" required><br>
            <label>Cupo (Opcional):</label><br>
            <input type="number" name="cupo"><br>
            <label>Docentes Participantes:</label><br>
            <select name="docentes" multiple required>
                <!-- Lista de docentes -->
            </select><br><br>
            <button type="submit">Crear Edición</button>
        </form>
    </div>
</body>
</html>