<%--
    Promedio de un curso con la escala de 5 estrellas (Requerimiento 7.6).
    Uso: <edext:estrellas valoracion="${dtCurso.valoraciones}"/>  (tamano: clase CSS opcional, p. ej. "fs-5")
--%>
<%@ tag pageEncoding="UTF-8" body-content="empty" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ attribute name="valoracion" type="Logica.DTsClasses.DTValoracionCurso" required="false" %>
<%@ attribute name="tamano" required="false" %>
<span class="d-inline-flex align-items-center gap-1 text-nowrap ${tamano}">
    <c:choose>
        <c:when test="${empty valoracion or valoracion.total == 0}">
            <span class="text-secondary" style="opacity: .5;" aria-hidden="true"><c:forEach begin="1" end="5"><i class="bi bi-star"></i></c:forEach></span>
            <small class="text-muted">Sin valoraciones</small>
        </c:when>
        <c:otherwise>
            <span class="text-warning" role="img" aria-label="${valoracion.promedioTexto} de 5 estrellas">
                <c:forEach var="ico" items="${valoracion.iconos}"><i class="bi ${ico == 'full' ? 'bi-star-fill' : (ico == 'half' ? 'bi-star-half' : 'bi-star')}"></i></c:forEach>
            </span>
            <small class="fw-semibold text-dark">${valoracion.promedioTexto}</small>
            <small class="text-muted">(${valoracion.total})</small>
        </c:otherwise>
    </c:choose>
</span>
