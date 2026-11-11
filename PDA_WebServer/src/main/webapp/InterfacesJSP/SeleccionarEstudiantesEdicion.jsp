<%--
    Document   : SeleccionarEstudiantesEdicion
    Caso de uso: Seleccionar estudiantes para una edición de curso (con video)
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
    <title>Seleccionar Estudiantes de Edición de Curso</title>
    <style>
        #contenido { transition: opacity .15s; }
        #contenido.cargando { opacity: .5; pointer-events: none; }
        #reproductor iframe, #reproductor video { width: 100%; height: 100%; border: 0; background: #000; }
        /* css/style.css limita todo <form> a 500px en columna: acá los formularios ocupan todo el ancho */
        #formSeleccion, #formFiltros { max-width: none; margin-bottom: 0; }
        #formSeleccion { display: block; }
        #formFiltros { flex-direction: row; gap: 0; }
        /* css/style.css pinta de rojo todo button[type=button]: se respetan los colores de Bootstrap */
        button.btn-video, #modalVideo .btn { margin-top: 0; background-color: var(--bs-btn-bg); color: var(--bs-btn-color); }
        button.btn-video:hover, #modalVideo .btn:hover { background-color: var(--bs-btn-hover-bg); color: var(--bs-btn-hover-color); }
        button.btn-close { background-color: transparent; margin-top: 0; }
    </style>
</head>
<body>
    <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>
        <div class="card border-dark shadow p-3 p-md-4 w-100 bg-white" style="max-width: 1150px;">
                <h2 class="card-title text-center mb-4 fw-bold fs-3">Seleccionar Estudiantes para una Edición</h2>

                <!-- Todo lo que cambia dinámicamente vive dentro de #contenido -->
                <div id="contenido">

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

                    <!-- FILTROS: instituto -> curso (se envían por fetch) -->
                    <form id="formFiltros" method="GET" action="SeleccionarEstudiantesEdicionServlet" class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label for="instituto" class="form-label fw-bold">1. Instituto:</label>
                            <select id="instituto" name="instituto" class="form-select border-dark">
                                <option value="">-- Seleccionar Instituto --</option>
                                <c:forEach var="inst" items="${institutos}">
                                    <option value="${fn:escapeXml(inst.nombre)}" ${inst.nombre == instSeleccionado ? 'selected' : ''}>
                                        <c:out value="${inst.nombre}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="curso" class="form-label fw-bold">2. Curso:</label>
                            <select id="curso" name="curso" class="form-select border-dark" ${empty cursos ? 'disabled' : ''}>
                                <option value="">-- Seleccionar Curso --</option>
                                <c:forEach var="c" items="${cursos}">
                                    <option value="${fn:escapeXml(c.nombre)}" ${c.nombre == cursoSeleccionado ? 'selected' : ''}>
                                        <c:out value="${c.nombre}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>

                    <c:if test="${not empty mensajeInfo}">
                        <div class="alert alert-secondary text-center"><c:out value="${mensajeInfo}"/></div>
                    </c:if>

                    <c:if test="${not empty dtEdicion}">
                        <!-- Datos básicos de la edición vigente -->
                        <label class="form-label fw-bold">3. Edición vigente</label>
                        <div class="card border-secondary mb-4">
                            <div class="card-body">
                                <h5 class="card-title fw-bold text-break mb-3">
                                    <c:url var="urlEdicion" value="/ConsultaEdicionServlet"><c:param name="edicion" value="${dtEdicion.nombre}"/></c:url>
                                    <a href="${urlEdicion}" class="text-dark"><c:out value="${dtEdicion.nombre}"/></a>
                                </h5>
                                <div class="row g-2 small">
                                    <div class="col-md-4"><strong>Curso:</strong> <c:out value="${dtEdicion.curso}"/></div>
                                    <div class="col-md-4"><strong>Fecha inicio:</strong> <fmt:formatDate value="${dtEdicion.FInicio}" pattern="dd/MM/yyyy"/></div>
                                    <div class="col-md-4"><strong>Fecha fin:</strong> <fmt:formatDate value="${dtEdicion.FFin}" pattern="dd/MM/yyyy"/></div>
                                    <div class="col-md-4"><strong>Fecha de alta:</strong> <fmt:formatDate value="${dtEdicion.fechaAlta}" pattern="dd/MM/yyyy"/></div>
                                    <div class="col-md-4"><strong>Cupo:</strong> ${dtEdicion.cupo > 0 ? dtEdicion.cupo : 'Sin límite'}</div>
                                    <div class="col-md-4"><strong>Estado:</strong> <c:out value="${fn:replace(dtEdicion.estado, '_', ' ')}"/></div>
                                    <div class="col-12"><strong>Docentes:</strong>
                                        <c:forEach var="d" items="${dtEdicion.docentes}" varStatus="st"><c:out value="${d}"/>${st.last ? '' : ', '}</c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
                            <label class="form-label fw-bold m-0">4. Inscripciones</label>
                            <span id="contadorCupo" class="badge fs-6 bg-dark" data-cupo="${dtEdicion.cupo}">
                                Aceptados: <span id="cantAceptados">${cantAceptados}</span><c:if test="${dtEdicion.cupo > 0}"> / ${dtEdicion.cupo}</c:if>
                            </span>
                        </div>
                        <div id="avisoCupo" class="alert alert-warning text-center d-none">
                            La cantidad de estudiantes aceptados supera el cupo de la edición (${dtEdicion.cupo}).
                        </div>

                        <form id="formSeleccion" method="POST" action="SeleccionarEstudiantesEdicionServlet">
                            <!-- Se reenvían los filtros para mantener la pantalla tras guardar -->
                            <input type="hidden" name="instituto" value="${fn:escapeXml(instSeleccionado)}">
                            <input type="hidden" name="curso" value="${fn:escapeXml(cursoSeleccionado)}">
                            <input type="hidden" name="nomEdicion" value="${fn:escapeXml(dtEdicion.nombre)}">

                            <div class="table-responsive">
                                <table class="table table-bordered table-hover align-middle mb-4">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>Estudiante</th>
                                            <th>Fecha inscripción</th>
                                            <th>Estado</th>
                                            <th class="text-center">Video</th>
                                            <th class="text-center">Selección</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:choose>
                                            <c:when test="${empty dtEdicion.misInscripciones}">
                                                <tr>
                                                    <td colspan="5" class="text-center text-muted py-3">
                                                        La edición vigente no tiene inscripciones.
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach var="ins" items="${dtEdicion.misInscripciones}" varStatus="st">
                                                    <c:set var="nick" value="${fn:escapeXml(ins.nickEstudiante)}"/>
                                                    <c:set var="estado" value="${fn:toUpperCase(ins.estadoIns)}"/>
                                                    <tr>
                                                        <td>
                                                            <strong>${nick}</strong>
                                                            <input type="hidden" name="estudiantes[]" value="${nick}">
                                                        </td>
                                                        <td><fmt:formatDate value="${ins.fInscripcion}" pattern="dd/MM/yyyy"/></td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${estado == 'ACEPTADO'}"><span class="badge bg-success">Aceptada</span></c:when>
                                                                <c:when test="${estado == 'RECHAZADO'}"><span class="badge bg-danger">Rechazada</span></c:when>
                                                                <c:otherwise><span class="badge bg-warning text-dark">Inscripta</span></c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class="text-center">
                                                            <c:choose>
                                                                <c:when test="${not empty ins.urlVideo}">
                                                                    <button type="button" class="btn btn-dark btn-sm fw-bold btn-video text-nowrap"
                                                                            data-video-url="${fn:escapeXml(ins.urlVideo)}"
                                                                            data-estudiante="${nick}">&#9654; Ver video</button>
                                                                </c:when>
                                                                <c:otherwise><span class="text-muted small">Sin video</span></c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class="text-center text-nowrap">
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="estado_${nick}"
                                                                       id="acep_${st.index}" value="ACEPTADO" ${estado == 'ACEPTADO' ? 'checked' : ''}>
                                                                <label class="form-check-label" for="acep_${st.index}">Aceptada</label>
                                                            </div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="estado_${nick}"
                                                                       id="rech_${st.index}" value="RECHAZADO" ${estado == 'RECHAZADO' ? 'checked' : ''}>
                                                                <label class="form-check-label" for="rech_${st.index}">Rechazada</label>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                            </div>

                            <c:if test="${not empty dtEdicion.misInscripciones}">
                                <div class="d-flex flex-column flex-sm-row gap-2">
                                    <button type="submit" id="btnRegistrar" class="btn btn-primary fw-bold flex-fill">Registrar selección</button>
                                    <a href="${pageContext.request.contextPath}/MainMenu" class="btn btn-outline-secondary flex-fill">Cancelar</a>
                                </div>
                            </c:if>
                        </form>
                    </c:if>

                </div><!-- /#contenido -->
        </div>
    </div><%-- cierra la columna de contenido abierta en InterfazBase --%>

    <!-- Reproductor del video de motivación -->
    <div class="modal fade" id="modalVideo" tabindex="-1" aria-labelledby="tituloVideo" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="tituloVideo">Video de motivación</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body p-0">
                    <div id="reproductor" class="ratio ratio-16x9"></div>
                    <div id="sinReproductor" class="p-4 text-center d-none">
                        Este video no se puede reproducir dentro de la página.
                    </div>
                </div>
                <div class="modal-footer">
                    <a id="linkVideo" href="#" target="_blank" rel="noopener noreferrer" class="btn btn-outline-secondary btn-sm">Abrir en una pestaña nueva</a>
                    <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    (function () {
        const contenido = document.getElementById('contenido');
        const BASE = '${pageContext.request.contextPath}/SeleccionarEstudiantesEdicionServlet';

        // ---------- Carga dinámica sin recargar la página ----------
        async function cargar(url, opciones) {
            contenido.classList.add('cargando');
            try {
                const resp = await fetch(url, opciones);
                if (!resp.ok) throw new Error('HTTP ' + resp.status);
                const doc = new DOMParser().parseFromString(await resp.text(), 'text/html');
                const nuevo = doc.getElementById('contenido');
                if (!nuevo) throw new Error('Respuesta inesperada del servidor');
                contenido.innerHTML = nuevo.innerHTML;
            } catch (e) {
                const div = document.createElement('div');
                div.className = 'alert alert-danger';
                div.textContent = 'Error al cargar los datos: ' + e.message;
                contenido.prepend(div);
            } finally {
                contenido.classList.remove('cargando');
                actualizarCupo();
            }
        }

        contenido.addEventListener('change', function (ev) {
            if (ev.target.matches('#formSeleccion input[type=radio]')) {
                actualizarCupo();
                return;
            }
            if (!ev.target.matches('#formFiltros select')) return;
            const form = document.getElementById('formFiltros');
            if (ev.target.id === 'instituto') form.querySelector('#curso').value = '';
            const params = new URLSearchParams(new FormData(form));
            for (const [k, v] of [...params.entries()]) if (v === '') params.delete(k);
            const url = BASE + '?' + params.toString();
            history.replaceState(null, '', url);
            cargar(url);
        });

        contenido.addEventListener('submit', function (ev) {
            if (ev.target.id !== 'formSeleccion') return;
            ev.preventDefault();
            cargar(BASE, {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' },
                body: new URLSearchParams(new FormData(ev.target)).toString()
            });
        });

        // ---------- Control del cupo (el SC también lo valida) ----------
        function actualizarCupo() {
            const contador = document.getElementById('contadorCupo');
            if (!contador) return;
            const cupo = parseInt(contador.dataset.cupo, 10) || 0;
            const aceptados = contenido.querySelectorAll('#formSeleccion input[value=ACEPTADO]:checked').length;
            const excede = cupo > 0 && aceptados > cupo;
            document.getElementById('cantAceptados').textContent = aceptados;
            contador.classList.toggle('bg-dark', !excede);
            contador.classList.toggle('bg-danger', excede);
            document.getElementById('avisoCupo').classList.toggle('d-none', !excede);
            const btn = document.getElementById('btnRegistrar');
            if (btn) btn.disabled = excede;
        }
        actualizarCupo();

        // ---------- Reproducción de video ----------
        // Devuelve cómo reproducir la URL dentro de la página, o null si no se puede.
        function reproductorPara(url) {
            let u;
            try { u = new URL(url); } catch (e) { return null; }
            if (u.protocol !== 'http:' && u.protocol !== 'https:') return null;
            const host = u.hostname.replace(/^(www|m)\./, '');
            let id = null;

            if (host === 'youtu.be') {
                id = u.pathname.split('/')[1];
            } else if (host === 'youtube.com' || host === 'youtube-nocookie.com') {
                if (u.pathname === '/watch') id = u.searchParams.get('v');
                else { const m = u.pathname.match(/^\/(embed|shorts|live|v)\/([^/]+)/); if (m) id = m[2]; }
            }
            if (id && /^[\w-]{6,20}$/.test(id)) {
                return { tipo: 'iframe', src: 'https://www.youtube.com/embed/' + id + '?autoplay=1&rel=0' };
            }
            if (host === 'vimeo.com' || host === 'player.vimeo.com') {
                const m = u.pathname.match(/(\d{5,})/);
                if (m) return { tipo: 'iframe', src: 'https://player.vimeo.com/video/' + m[1] + '?autoplay=1' };
            }
            if (host === 'drive.google.com') {
                const m = u.pathname.match(/\/file\/d\/([\w-]+)/);
                if (m) return { tipo: 'iframe', src: 'https://drive.google.com/file/d/' + m[1] + '/preview' };
            }
            if (/\.(mp4|webm|ogg|ogv|mov)$/i.test(u.pathname)) {
                return { tipo: 'video', src: u.href };
            }
            return null;
        }

        const modalEl = document.getElementById('modalVideo');
        const modal = new bootstrap.Modal(modalEl);
        const reproductor = document.getElementById('reproductor');
        const sinReproductor = document.getElementById('sinReproductor');

        document.addEventListener('click', function (ev) {
            const btn = ev.target.closest('.btn-video');
            if (!btn) return;
            const url = btn.dataset.videoUrl;
            const r = reproductorPara(url);

            document.getElementById('tituloVideo').textContent = 'Video de motivación de ' + btn.dataset.estudiante;
            document.getElementById('linkVideo').href = url;
            reproductor.replaceChildren();
            if (r) {
                const el = document.createElement(r.tipo);
                el.src = r.src;
                if (r.tipo === 'iframe') {
                    el.allow = 'autoplay; encrypted-media; picture-in-picture; fullscreen';
                    el.allowFullscreen = true;
                    el.title = 'Video de motivación';
                } else {
                    el.controls = true;
                    el.autoplay = true;
                }
                reproductor.appendChild(el);
            }
            reproductor.classList.toggle('d-none', !r);
            sinReproductor.classList.toggle('d-none', !!r);
            modal.show();
        });

        // Al cerrar se quita el reproductor para que el video deje de sonar
        modalEl.addEventListener('hidden.bs.modal', function () { reproductor.replaceChildren(); });
    })();
    </script>
</body>
</html>
