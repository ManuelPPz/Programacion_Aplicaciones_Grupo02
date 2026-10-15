<%-- 
    Document   : SeleccionarEstudiantesEdicion
    Created on : Oct 12, 2026, 7:29:27 PM
    Author     : leoli
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscripciones Aceptadas a Edición de Curso</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

    <div class="container my-4">
        
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-success text-white">
                <h3 class="card-title m-0">Seleccionar estudiantes para una edición de curso</h3>
            </div>
            <div class="card-body">

                <!-- MENSAJE DE ERROR (SI OCURRE) -->
                <c:if test="${not empty mensajeError}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong>Error:</strong> ${mensajeError}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- FORMULARIO DE FILTROS EN CASCADA (GET) -->
                <form method="GET" action="ListarAceptadosEdicionServlet" class="row g-3 mb-4">
                    
                    <!-- 1. Selección Instituto -->
                    <div class="col-md-4">
                        <label for="instituto" class="form-label fw-bold">1. Instituto:</label>
                        <select id="instituto" name="instituto" class="form-select" onchange="this.form.submit()">
                            <option value="">-- Seleccionar Instituto --</option>
                            <c:forEach var="inst" items="${institutos}">
                                <option value="${inst.nombre}" ${inst.nombre == instSeleccionado ? 'selected' : ''}>
                                    ${inst.nombre}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- 2. Selección Curso -->
                    <div class="col-md-4">
                        <label for="curso" class="form-label fw-bold">2. Curso:</label>
                        <select id="curso" name="curso" class="form-select" onchange="this.form.submit()" ${empty cursos ? 'disabled' : ''}>
                            <option value="">-- Seleccionar Curso --</option>
                            <c:forEach var="c" items="${cursos}">
                                <option value="${c.nombre}" ${c.nombre == cursoSeleccionado ? 'selected' : ''}>
                                    ${c.nombre}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- 3. Selección Edición -->
                    <div class="col-md-4">
                        <label for="edicion" class="form-label fw-bold">3. Edición de Curso:</label>
                        <select id="edicion" name="edicion" class="form-select" onchange="this.form.submit()" ${empty ediciones ? 'disabled' : ''}>
                            <option value="">-- Seleccionar Edición --</option>
                            <c:forEach var="ed" items="${ediciones}">
                                <option value="${ed.nombre}" ${ed.nombre == edicionSeleccionada ? 'selected' : ''}>
                                    ${ed.nombre}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </form>

                <!-- INFORMACIÓN DE LA EDICIÓN Y TABLA DE ACEPTADOS -->
                <c:if test="${not empty dtEdicion}">
                    
                    <!-- Ficha resumen de la Edición -->
                    <div class="card mb-4 border-success">
                        <div class="card-header bg-success text-white fw-bold">
                            Datos Básicos de la Edición
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-4"><strong>Nombre:</strong> ${dtEdicion.nombre}</div>
                                <div class="col-md-4"><strong>Fecha Inicio:</strong> ${dtEdicion.fechaInicio}</div>
                                <div class="col-md-4"><strong>Cupo Máximo:</strong> ${dtEdicion.cupo}</div>
                            </div>
                        </div>
                    </div>

                    <!-- LISTADO EXCLUSIVO DE INSCRIPCIONES ACEPTADAS -->
                    <h4 class="mb-3 text-secondary">Estudiantes Inscriptos</h4>
                    
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped align-middle">
                            <thead class="table-dark">
                                <tr>
                                    <th>Estudiante (Nickname)</th>
                                    <th>Fecha Inscripción</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- Contador auxiliar para identificar si hay resultados -->
                                <c:set var="cantAceptados" value="0" />

                                <c:forEach var="ins" items="${dtEdicion.inscripciones}">
                                    <c:if test="${ins.estado == 'Aceptada'}">
                                        <c:set var="cantAceptados" value="${cantAceptados + 1}" />
                                        <tr>
                                            <td><strong>${ins.nicknameEstudiante}</strong></td>
                                            <td>${ins.fechaInscripcion}</td>
                                            <td>
                                                <span class="badge bg-success">Aceptada</span>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                                
                                <c:if test="${cantAceptados == 0}">
                                    <tr>
                                        <td colspan="3" class="text-center text-muted py-3">
                                            No existen estudiantes en estado <strong>Aceptada</strong> para esta edición de curso.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>

                </c:if>

            </div>
        </div>

    </div>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>