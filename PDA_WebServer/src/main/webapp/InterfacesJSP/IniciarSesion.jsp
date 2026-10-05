<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    public String versionedUrl(ServletContext app, String relativePath) {
        String realPath = app.getRealPath(relativePath);
        if (realPath != null) {
            java.io.File file = new java.io.File(realPath);
            if (file.exists()) {
                return relativePath + "?v=" + file.lastModified();
            }
        }
        return relativePath;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    
</head>
<body>
    <div class="container">
        <h2>Iniciar Sesión</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getParameter("mensaje") != null) { %><div class="alert-success"><%= request.getParameter("mensaje") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/IniciarSesionServlet" method="POST">
            <label>Nickname o Correo:</label><br>
            <input type="text" name="id" required><br><br>
            <label>Contraseña:</label><br>
            <input type="password" name="password" required><br><br>
            <button type="submit">Ingresar</button>
            <button type="button" onclick="window.history.back();">Cancelar</button>
        </form>
    </div>
    <script src="<%= request.getContextPath() %><%= versionedUrl(application, "/JavasScripts/main.js") %>"></script>
</body>
</html>