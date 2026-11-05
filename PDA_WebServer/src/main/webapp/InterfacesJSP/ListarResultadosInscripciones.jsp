<%-- 
    Document   : ListarResultadosInscripciones
    Created on : Oct 15, 2026, 8:00:35 PM
    Author     : leoli
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resultados de Inscripciones</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <!-- Navbar -->
    <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

    <div class="container my-4">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h3 class="card-title m-0">Mis Inscripciones a Ediciones de Cursos</h3>
            </div>

            <div class="card-body">
                <c:if test="${not empty mensajeError}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong>Atención:</strong> ${mensajeError}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <p class="text-secondary mb-4">A continuación se listan todas tus inscripciones y su estado actual.</p>

                <div class="table-responsive">
                    <table class="table table-bordered table-striped align-middle text-center">
                        <thead class="table-dark">
                            <tr>
                                <th>Edición de Curso</th>
                                <th>Fecha de Inscripción</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${empty inscripciones}">
                                <tr>
                                    <td colspan="3" class="text-muted py-4">
                                        No se encontraron inscripciones registradas.
                                    </td>
                                </tr>
                            </c:if>

                            <c:forEach var="ins" items="${inscripciones}">
                                <c:set var="est" value="${fn:toUpperCase(ins.estadoIns)}" />
                                <tr>
                                    <td class="fw-bold">${ins.nomEdicion}</td>
                                    <td><fmt:formatDate value="${ins.fInscripcion}" pattern="dd/MM/yyyy" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${fn:startsWith(est, 'ACEPT')}">
                                                <span class="badge bg-success fs-6">Aceptada</span>
                                            </c:when>
                                            <c:when test="${fn:startsWith(est, 'RECHAZ')}">
                                                <span class="badge bg-danger fs-6">Rechazada</span>
                                            </c:when>
                                            <c:otherwise>
                                                <!-- INSCRIPTO / Pendiente de aprobación -->
                                                <span class="badge bg-warning text-dark fs-6">Inscripto</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

