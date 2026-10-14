<%@page import="Logica.util.ImagenUtil"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTUsuario"%>
<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTCategoria"%>
<%@page import="java.util.List"%>
<%@page import="Logica.DTsClasses.DTDocente"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crear Programa de Formacion</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
        <%-- Cargamos toda la estructura visual y el menu lateral desde la base --%>
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">

            <div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
                <h2 class="card-title text-center mb-4 fw-bold">Crear Programa de Formacion</h2>

                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger" role="alert"><%= request.getAttribute("error") %></div>
                <% } %>
                
                <% if (request.getAttribute("exito") != null) { %>
                    <div class="alert alert-success" role="alert"><%= request.getAttribute("exito") %></div>
                <% } %>

                <form action="${pageContext.request.contextPath}/CrearProgramaServlet" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                    
                    <div class="mb-3">
                        <label for="nombre" class="form-label fw-bold">Nombre del Programa:</label>
                        <input type="text" class="form-control" id="nombre" name="nombre" required>
                    </div>

                    <div class="mb-3">
                        <label for="descripcion" class="form-label fw-bold">Descripcion:</label>
                        <textarea class="form-control" id="descripcion" name="descripcion" rows="4" required></textarea>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="fechaInicio" class="form-label fw-bold">Fecha de Inicio:</label>
                            <input type="date" class="form-control" id="fechaInicio" name="fechaInicio" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="fechaFin" class="form-label fw-bold">Fecha de Fin:</label>
                            <input type="date" class="form-control" id="fechaFin" name="fechaFin" required>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="imagen" class="form-label fw-bold">Imagen del Programa:</label>
                        <input type="file" class="form-control" id="imagen" name="imagen" accept="image/png, image/jpeg">
                        <div class="form-text">Formatos permitidos: PNG o JPEG.</div>
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary fw-bold py-2">Crear Programa</button>
                    </div>

                </form>
            </div>

        </div>

    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>