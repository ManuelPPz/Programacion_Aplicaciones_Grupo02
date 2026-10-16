<%-- 
    Document   : SeleccionarEstudiantesEdicion
    Created on : Oct 12, 2026, 7:29:27 PM
    Author     : leoli
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seleccionar Estudiantes de Edición de Curso</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        #contenido { transition: opacity .15s; }
        #contenido.cargando { opacity: .5; pointer-events: none; }
    </style>
</head>
<body class="bg-light">
    <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

    <div class="container my-4">

        <div class="card shadow-sm mb-4">
            <div class="card-header bg-success text-white">
                <h3 class="card-title m-0">Seleccionar estudiantes para una edición de curso</h3>
            </div>
            <div class="card-body">

                <!-- Todo lo que cambia dinámicamente vive dentro de #contenido -->
                <div id="contenido">

                    <!-- MENSAJE DE ERROR -->
                    <c:if test="${not empty mensajeError}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong>Error:</strong> ${mensajeError}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- MENSAJE DE ÉXITO -->
                    <c:if test="${not empty mensajeExito}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            ${mensajeExito}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- FORMULARIO DE FILTROS EN CASCADA (se envía por fetch) -->
                    <form id="formFiltros" method="GET" action="SeleccionarEstudiantesEdicionServlet" class="row g-3 mb-4">

                        <!-- 1. Instituto -->
                        <div class="col-md-4">
                            <label for="instituto" class="form-label fw-bold">1. Instituto:</label>
                            <select id="instituto" name="instituto" class="form-select">
                                <option value="">-- Seleccionar Instituto --</option>
                                <c:forEach var="inst" items="${institutos}">
                                    <option value="${inst.nombre}" ${inst.nombre == instSeleccionado ? 'selected' : ''}>
                                        ${inst.nombre}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- 2. Curso -->
                        <div class="col-md-4">
                            <label for="curso" class="form-label fw-bold">2. Curso:</label>
                            <select id="curso" name="curso" class="form-select" ${empty cursos ? 'disabled' : ''}>
                                <option value="">-- Seleccionar Curso --</option>
                                <c:forEach var="c" items="${cursos}">
                                    <option value="${c.nombre}" ${c.nombre == cursoSeleccionado ? 'selected' : ''}>
                                        ${c.nombre}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- 3. Edición -->
                        <div class="col-md-4">
                            <label for="edicion" class="form-label fw-bold">3. Edición de Curso:</label>
                            <select id="edicion" name="edicion" class="form-select" ${empty ediciones ? 'disabled' : ''}>
                                <option value="">-- Seleccionar Edición --</option>
                                <c:forEach var="ed" items="${ediciones}">
                                    <option value="${ed.nombre}" ${ed.nombre == edicionSeleccionada ? 'selected' : ''}>
                                        ${ed.nombre}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>

                    <!-- DATOS DE LA EDICIÓN + INSCRIPCIONES -->
                    <c:if test="${not empty dtEdicion}">

                        <!-- Ficha resumen -->
                        <div class="card mb-4 border-success">
                            <div class="card-header bg-success text-white fw-bold">
                                Datos Básicos de la Edición
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4"><strong>Nombre:</strong> ${dtEdicion.nombre}</div>
                                    <div class="col-md-4"><strong>Fecha Inicio:</strong> <fmt:formatDate value="${dtEdicion.FInicio}" pattern="dd/MM/yyyy" /></div>
                                    <div class="col-md-4"><strong>Cupo Máximo:</strong> ${dtEdicion.cupo}</div>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4 class="m-0 text-secondary">Inscripciones de la Edición</h4>
                            <c:url var="urlRanking" value="SeleccionarEstudiantesEdicionServlet">
                                <c:param name="instituto" value="${instSeleccionado}" />
                                <c:param name="curso" value="${cursoSeleccionado}" />
                                <c:param name="edicion" value="${edicionSeleccionada}" />
                                <c:param name="orden" value="ranking" />
                            </c:url>
                            <a href="${urlRanking}" data-ajax class="btn btn-outline-success btn-sm">Ordenar por ranking</a>
                        </div>

                        <!-- FORMULARIO DE SELECCIÓN (POST por fetch) -->
                        <form id="formSeleccion" method="POST" action="SeleccionarEstudiantesEdicionServlet">

                            <!-- Se reenvían los filtros para mantener la pantalla tras guardar -->
                            <input type="hidden" name="instituto" value="${instSeleccionado}">
                            <input type="hidden" name="curso" value="${cursoSeleccionado}">
                            <input type="hidden" name="edicion" value="${edicionSeleccionada}">
                            <input type="hidden" name="nomEdicion" value="${edicionSeleccionada}">

                            <div class="table-responsive">
                                <table class="table table-bordered table-striped align-middle">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>Estudiante (Nickname)</th>
                                            <th>Fecha Inscripción</th>
                                            <th>Estado actual</th>
                                            <th class="text-center">Nuevo estado</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty dtEdicion.inscripciones}">
                                                <tr>
                                                    <td colspan="4" class="text-center text-muted py-3">
                                                        Esta edición de curso no tiene inscripciones.
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="ins" items="${dtEdicion.inscripciones}">
                                                    <tr>
                                                        <td>
                                                            <strong>${ins.nicknameEstudiante}</strong>
                                                            <input type="hidden" name="estudiantes[]" value="${ins.nicknameEstudiante}">
                                                        </td>
                                                        <td>${ins.fechaInscripcion}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${ins.estado == 'Aceptada'}">
                                                                    <span class="badge bg-success">Aceptada</span>
                                                                </c:when>
                                                                <c:when test="${ins.estado == 'Rechazada'}">
                                                                    <span class="badge bg-danger">Rechazada</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge bg-warning text-dark">${ins.estado}</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class="text-center">
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio"
                                                                       name="estado_${ins.nicknameEstudiante}"
                                                                       id="acep_${ins.nicknameEstudiante}"
                                                                       value="Aceptada" ${ins.estado == 'Aceptada' ? 'checked' : ''}>
                                                                <label class="form-check-label" for="acep_${ins.nicknameEstudiante}">Aceptar</label>
                                                            </div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio"
                                                                       name="estado_${ins.nicknameEstudiante}"
                                                                       id="rech_${ins.nicknameEstudiante}"
                                                                       value="Rechazada" ${ins.estado == 'Rechazada' ? 'checked' : ''}>
                                                                <label class="form-check-label" for="rech_${ins.nicknameEstudiante}">Rechazar</label>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <c:if test="${not empty dtEdicion.inscripciones}">
                                <div class="text-end">
                                    <button type="submit" class="btn btn-success">Registrar Selección</button>
                                </div>
                            </c:if>
                        </form>

                    </c:if>

                </div><!-- /#contenido -->

            </div>
        </div>

    </div>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Carga dinámica sin recargar la página -->
    <script>
    (function () {
        const contenido = document.getElementById('contenido');
        // Ruta absoluta al servlet, con el context path real de la aplicación
        const BASE = '${pageContext.request.contextPath}/SeleccionarEstudiantesEdicionServlet';

        // Pide la página al servlet y reemplaza solo el contenido dinámico
        async function cargar(url, opciones) {
            contenido.classList.add('cargando');
            try {
                const resp = await fetch(url, opciones);
                if (!resp.ok) throw new Error('HTTP ' + resp.status);
                const html = await resp.text();
                const doc = new DOMParser().parseFromString(html, 'text/html');
                const nuevo = doc.getElementById('contenido');
                if (!nuevo) throw new Error('Respuesta inesperada del servidor');
                contenido.innerHTML = nuevo.innerHTML;
            } catch (e) {
                contenido.insertAdjacentHTML('afterbegin',
                    '<div class="alert alert-danger">Error al cargar los datos: ' + e.message + '</div>');
            } finally {
                contenido.classList.remove('cargando');
            }
        }

        // Cambio en un combo de filtros
        contenido.addEventListener('change', function (ev) {
            const sel = ev.target;
            if (!sel.matches('#formFiltros select')) return;

            const form = document.getElementById('formFiltros');

            // Al cambiar un combo, se reinician los dependientes
            if (sel.id === 'instituto') {
                form.querySelector('#curso').value = '';
                form.querySelector('#edicion').value = '';
            } else if (sel.id === 'curso') {
                form.querySelector('#edicion').value = '';
            }

            const params = new URLSearchParams(new FormData(form));
            // Los combos deshabilitados no se envían; limpiamos vacíos
            for (const [k, v] of [...params.entries()]) {
                if (v === '') params.delete(k);
            }
            const url = BASE + '?' + params.toString();
            history.replaceState(null, '', url);
            cargar(url);
        });

        // Link "Ordenar por ranking"
        contenido.addEventListener('click', function (ev) {
            const a = ev.target.closest('a[data-ajax]');
            if (!a) return;
            ev.preventDefault();
            history.replaceState(null, '', a.href);
            cargar(a.href);
        });

        // Envío de "Registrar Selección"
        contenido.addEventListener('submit', function (ev) {
            const form = ev.target;
            if (form.id !== 'formSeleccion') return;
            ev.preventDefault();
            cargar(BASE, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
                body: new URLSearchParams(new FormData(form)).toString()
            });
        });
    })();
    </script>
</body>
</html>
