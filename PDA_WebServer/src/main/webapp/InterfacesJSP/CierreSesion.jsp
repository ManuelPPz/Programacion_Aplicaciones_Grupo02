<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cierre de Sesión</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>¿Está seguro que desea cerrar sesión?</h2>
        <form action="${pageContext.request.contextPath}/CierreSesionServlet" method="POST">
            <button type="submit">Cerrar Sesión</button>
            <button type="button" onclick="window.history.back();">Cancelar</button>
        </form>
    </div>
</body>
</html>