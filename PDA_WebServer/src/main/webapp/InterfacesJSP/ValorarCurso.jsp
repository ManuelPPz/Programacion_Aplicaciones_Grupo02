<%--
    Document   : ValorarCurso
    Caso de uso: Valorar un Curso (y resumen de puntajes, Requerimiento 7.6)
    Author     : mateo
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Valorar Curso</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/StyleValoracion.css">
    <style>
        /* css/style.css limita todo <form> a 500px en columna: acá los formularios ocupan todo el ancho */
        #formCurso, #formValorar { max-width: none; margin-bottom: 0; }
        #formCurso { display: block; }
        button.btn-close { background-color: transparent; margin-top: 0; }
    </style>
</head>
<body>
    <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>
        <div class="card border-dark shadow p-3 p-md-4 w-100 bg-white" style="max-width: 900px;">
            <h2 class="card-title text-center mb-4 fw-bold fs-3">Valorar un Curso</h2>

            <c:if test="${not empty mensajeError}">
                <div class="alert alert-danger alert-dismissible fade show text-center" role="alert">
                    <c:out value="${mensajeError}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty mensajeExito}">
                <div class="alert alert-success alert-dismissible fade show text-center" role="alert">
                    <c:out value="${mensajeExito}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty mensajeInfo}">
                <div class="alert alert-info text-center"><c:out value="${mensajeInfo}"/></div>
            </c:if>

            <c:choose>
                <c:when test="${empty cursos}">
                    <div class="alert alert-secondary text-center mb-0">
                        Todavía no participás en ningún curso. Podrás valorarlo cuando te acepten en alguna de sus ediciones.
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- 1. Curso a valorar -->
                    <form id="formCurso" method="GET" action="ValorarCursoServlet" class="mb-4">
                        <label for="curso" class="form-label fw-bold">Curso en el que participás:</label>
                        <select id="curso" name="curso" class="form-select border-dark" onchange="this.form.submit()">
                            <option value="">-- Seleccionar Curso --</option>
                            <c:forEach var="cur" items="${cursos}">
                                <option value="${fn:escapeXml(cur)}" ${cur == cursoSeleccionado ? 'selected' : ''}><c:out value="${cur}"/></option>
                            </c:forEach>
                        </select>
                        <noscript><button type="submit" class="btn btn-dark mt-2">Ver</button></noscript>
                    </form>

                    <c:if test="${not empty resumen}">
                        <!-- 2. Resumen de puntajes (promedio + desglose por estrellas) -->
                        <div class="row align-items-center g-4 mb-4">
                            <div class="col-md-4 text-center border-end-md">
                                <div class="display-4 fw-bold text-dark lh-1">${resumen.promedioTexto}</div>
                                <div class="estrellas fs-4 my-2" aria-label="Promedio ${resumen.promedioTexto} de 5 estrellas">
                                    <c:forEach var="ico" items="${resumen.iconos}">
                                        <i class="bi ${ico == 'full' ? 'bi-star-fill' : (ico == 'half' ? 'bi-star-half' : 'bi-star')}"></i>
                                    </c:forEach>
                                </div>
                                <div class="text-muted small">
                                    ${resumen.total} ${resumen.total == 1 ? 'valoración' : 'valoraciones'}
                                </div>
                            </div>
                            <div class="col-md-8">
                                <c:forEach var="f" items="${resumen.filas}">
                                    <div class="d-flex align-items-center mb-2">
                                        <span class="small fw-semibold text-secondary text-nowrap" style="width: 4.5rem;">
                                            ${f[0]} <i class="bi bi-star-fill text-warning"></i>
                                        </span>
                                        <div class="progress flex-grow-1 mx-2" style="height: 10px;" role="progressbar"
                                             aria-label="${f[0]} estrellas" aria-valuenow="${f[2]}" aria-valuemin="0" aria-valuemax="100">
                                            <div class="progress-bar ${f[0] >= 4 ? 'bg-success' : (f[0] == 3 ? 'bg-warning' : 'bg-danger')}" style="width: ${f[2]}%"></div>
                                        </div>
                                        <span class="small text-muted text-end" style="width: 2.5rem;">${f[1]}</span>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <hr>

                        <!-- 3. Valoración del estudiante -->
                        <div class="bg-light p-3 rounded-3 border">
                            <c:choose>
                                <c:when test="${resumen.valorado}">
                                    <h5 class="fw-bold mb-2">Tu valoración</h5>
                                    <div class="estrellas fs-3" aria-label="${resumen.miPuntaje} de 5 estrellas">
                                        <c:forEach var="i" begin="1" end="5">
                                            <i class="bi ${i <= resumen.miPuntaje ? 'bi-star-fill' : 'bi-star'}"></i>
                                        </c:forEach>
                                    </div>
                                    <p class="text-muted small mb-0 mt-2">Ya valoraste este curso; cada curso se puede valorar una sola vez.</p>
                                </c:when>
                                <c:otherwise>
                                    <h5 class="fw-bold mb-1">Tu valoración</h5>
                                    <p class="text-muted small mb-3">Elegí de 1 a 5 estrellas según tu satisfacción con el curso (solo se puede valorar una vez).</p>
                                    <form id="formValorar" method="POST" action="ValorarCursoServlet">
                                        <input type="hidden" name="curso" value="${fn:escapeXml(cursoSeleccionado)}">
                                        <div class="star-rating fs-2 mb-3">
                                            <c:forEach var="n" begin="1" end="5">
                                                <c:set var="s" value="${6 - n}"/>
                                                <input type="radio" id="star${s}" name="puntaje" value="${s}" required>
                                                <label for="star${s}" title="${s} ${s == 1 ? 'estrella' : 'estrellas'}"><i class="bi bi-star-fill"></i></label>
                                            </c:forEach>
                                        </div>
                                        <button type="submit" class="btn btn-primary px-4 fw-semibold">Enviar valoración</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
    </div><%-- cierra la columna de contenido abierta en InterfazBase --%>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
