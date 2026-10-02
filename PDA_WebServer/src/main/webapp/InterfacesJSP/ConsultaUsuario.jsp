<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Consulta de Usuario</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/ConsultaUsuarioServlet" method="GET">
            <label>Seleccionar Usuario:</label>
            <select name="nickname">
                <!-- Iterar usuarios con JSTL -->
            </select>
            <button type="submit">Ver Detalles</button>
        </form>
        <hr>
        
        <div id="detallesUsuario">
            <h3>Detalles del Perfil</h3>
            <div class="tab-header">
                <button class="tab-button active" onclick="openTab(event, 'tabInfo')">Información General</button>
                <button class="tab-button" onclick="openTab(event, 'tabActividad')">Actividad (Cursos/Programas)</button>
            </div>
            
            <div id="tabInfo" class="tab-content active">
                <p><strong>Nickname:</strong> <!-- Nombre --></p>
            </div>
            
            <div id="tabActividad" class="tab-content">
                <p>Aquí se listarán las Ediciones o Inscripciones...</p>
            </div>
        </div>
    </div>
</body>
</html>