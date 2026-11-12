<%--
    Resumen de los puntajes de un curso (Requerimiento 7.6, Figura 7): promedio grande con
    estrellas y una barra por cada cantidad de estrellas.
    Uso: <edext:resumenValoraciones valoracion="${dtCurso.valoraciones}"/>
--%>
<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="valoracion" type="Logica.DTsClasses.DTValoracionCurso" required="false" %>
<c:choose>
    <c:when test="${empty valoracion or valoracion.total == 0}">
        <p class="text-muted mb-0">Este curso todavía no tiene valoraciones de estudiantes.</p>
    </c:when>
    <c:otherwise>
        <div class="row align-items-center g-3">
            <div class="col-sm-4 text-center">
                <div class="display-5 fw-bold text-dark lh-1">${valoracion.promedioTexto}</div>
                <div class="text-warning fs-5 my-1" role="img" aria-label="${valoracion.promedioTexto} de 5 estrellas">
                    <c:forEach var="ico" items="${valoracion.iconos}"><i class="bi ${ico == 'full' ? 'bi-star-fill' : (ico == 'half' ? 'bi-star-half' : 'bi-star')}"></i></c:forEach>
                </div>
                <div class="text-muted small">${valoracion.total} ${valoracion.total == 1 ? 'valoración' : 'valoraciones'}</div>
            </div>
            <div class="col-sm-8">
                <c:forEach var="f" items="${valoracion.filas}">
                    <div class="d-flex align-items-center mb-1">
                        <span class="small fw-semibold text-secondary text-nowrap" style="width: 2.6rem;">${f[0]} <i class="bi bi-star-fill text-warning"></i></span>
                        <div class="progress flex-grow-1 mx-2" style="height: 10px;" role="progressbar"
                             aria-label="${f[0]} estrellas" aria-valuenow="${f[2]}" aria-valuemin="0" aria-valuemax="100">
                            <div class="progress-bar ${f[0] >= 4 ? 'bg-success' : (f[0] == 3 ? 'bg-warning' : 'bg-danger')}" style="width: ${f[2]}%"></div>
                        </div>
                        <span class="small text-muted text-end" style="width: 2rem;">${f[1]}</span>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:otherwise>
</c:choose>
