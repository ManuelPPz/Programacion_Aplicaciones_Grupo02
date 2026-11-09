<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<%@page import="Logica.ws.ClienteWS"%>
<%
    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    log("edEXT: no se pudo contactar al Servidor Central en " + ClienteWS.getUrl(), exception);
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>edEXT - Servidor Central no disponible</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light d-flex align-items-center justify-content-center" style="min-height: 100vh;">
        <div class="card border-dark shadow p-4 text-center" style="max-width: 520px;">
            <h2 class="fw-bold mb-3">Servidor Central no disponible</h2>
            <p class="mb-2">No se pudo establecer la conexión con el Servidor Central de edEXT.</p>
            <p class="text-muted small mb-4">
                Verificá que esté en ejecución y que la dirección configurada (<code>ws.url</code> en
                <code>config.properties</code>, o <code>edext.ws.url</code> en <code>web.xml</code>) sea correcta:
                <code><%= ClienteWS.getUrl() != null ? ClienteWS.getUrl() : "(sin configurar)" %></code>
            </p>
            <a class="btn btn-dark" href="javascript:location.reload()">Reintentar</a>
        </div>
    </body>
</html>
