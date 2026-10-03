<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Alta de Curso</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Alta de Curso</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/AltaCursoServlet" method="POST">
            <label>Instituto:</label><br>
            <select name="instituto" required>
                <!-- Llenar con institutos -->
            </select><br>
            <label>Nombre del Curso:</label><br>
            <input type="text" name="nombre" required><br>
            <label>Descripción:</label><br>
            <textarea name="descripcion" required></textarea><br>
            <label>Duración (meses):</label><br>
            <input type="number" name="duracion" required><br>
            <label>Horas:</label><br>
            <input type="number" name="horas" required><br>
            <label>Créditos:</label><br>
            <input type="number" name="creditos" required><br>
            <label>URL:</label><br>
            <input type="url" name="url" required><br>
            <label>Categorías:</label><br>
            <select name="categorias" multiple required>
                <!-- Llenar con categorías -->
            </select><br>
            <label>Previas (Opcional):</label><br>
            <select name="previas" multiple>
                <!-- Llenar con cursos -->
            </select><br><br>
            <button type="submit">Registrar Curso</button>
        </form>
    </div>
</body>
</html>