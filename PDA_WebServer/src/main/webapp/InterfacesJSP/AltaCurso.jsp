<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTCategoria"%>
<%@page import="java.util.List"%>

<%
    request.setAttribute("titlePage", "Alta de Curso");
    String instSel = (String) request.getAttribute("institutoSeleccionado");
%>

<%-- Cargamos toda la estructura visual y el menú lateral desde la base --%>
<%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

<%-- CONTENIDO ESPECÍFICO DEL FORMULARIO DE ALTA DE CURSO --%>
<div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
    <h2 class="card-title text-center mb-4 fw-bold">Alta de Curso</h2>

    <% if (request.getAttribute("error") != null) {%>
    <div class="alert alert-danger"><%= request.getAttribute("error")%></div>
    <% } %>
    <% if (request.getAttribute("exito") != null) {%>
    <div class="alert alert-success"><%= request.getAttribute("exito")%></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/AltaCursoServlet" method="POST" enctype="multipart/form-data">
        <div class="row g-3">

            <!-- Instituto -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Instituto:</label>
                <select name="instituto" class="form-select" required onchange="cargarCursosPorInstituto(this.value)">
                    <option value="" disabled selected>Seleccione un instituto...</option>
                    <%
                        List<DTMaster> auxListIns = (List<DTMaster>) request.getAttribute("institutos");
                        if (auxListIns == null) {
                            auxListIns = (List<DTMaster>) session.getAttribute("AuxListInstituto");
                        }

                        if (auxListIns != null) {
                            for (int i = 0; i < auxListIns.size(); i++) {
                                if (auxListIns.get(i) instanceof DTInstituto) {
                                    DTInstituto dti = (DTInstituto) auxListIns.get(i);
                                    boolean selected = (instSel != null && instSel.equals(dti.getNombre()));
                    %>
                    <option value="<%= dti.getNombre()%>" <%= selected ? "selected" : ""%>>
                        <%= dti.getNombre()%>
                    </option>
                    <%
                                }
                            }
                        }
                    %>
                </select>
            </div>

            <!-- Nombre del Curso -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Nombre del Curso:</label>
                <input type="text" name="nombre" class="form-control" placeholder="Nombre del curso" required>
            </div>

            <!-- Descripción -->
            <div class="col-12">
                <label class="form-label fw-bold">Descripción:</label>
                <textarea name="descripcion" class="form-control" rows="3" placeholder="Descripción detallada del curso" required></textarea>
            </div>

            <!-- Duración -->
            <div class="col-md-4">
                <label class="form-label fw-bold">Duración (Semanas):</label>
                <input type="number" name="duracion" class="form-control" min="1" required>
            </div>

            <!-- Horas -->
            <div class="col-md-4">
                <label class="form-label fw-bold">Horas:</label>
                <input type="number" name="horas" class="form-control" min="1" required>
            </div>

            <!-- Créditos -->
            <div class="col-md-4">
                <label class="form-label fw-bold">Créditos:</label>
                <input type="number" name="creditos" class="form-control" min="0" required>
            </div>

            <!-- URL -->
            <div class="col-12">
                <label class="form-label fw-bold">URL:</label>
                <input type="url" name="url" class="form-control" placeholder="https://ejemplo.com/curso" required>
            </div>

            <!-- Imagen -->
            <div class="col-12">
                <label class="form-label fw-bold">Imagen del curso:</label><br>
                <input type="file" name="imagen" class="form-control" accept="image/png, image/jpeg">
            </div>

            <!-- Categorías -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Categorías:</label>
                <select name="categorias" class="form-select" multiple required style="height: 120px;">
                    <%
                        List<DTMaster> auxListCat = (List<DTMaster>) request.getAttribute("categorias");
                        if (auxListCat == null) {
                            auxListCat = (List<DTMaster>) session.getAttribute("AuxListCategoria");
                        }

                        if (auxListCat != null) {
                            for (int i = 0; i < auxListCat.size(); i++) {
                                if (auxListCat.get(i) instanceof DTCategoria) {
                                    DTCategoria dtc = (DTCategoria) auxListCat.get(i);
                    %>
                    <option value="<%= dtc.getNombre()%>"><%= dtc.getNombre()%></option>
                    <%
                                }
                            }
                        }
                    %>
                </select>
                <div class="form-text">Usa Ctrl + Clic para selección múltiple.</div>
            </div>

            <!-- Previas -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Previas (Opcional):</label>
                <select name="previas" class="form-select" multiple style="height: 120px;">
                    <option value="" disabled selected>Seleccione primero un instituto...</option>
                </select>
                <div class="form-text">Usa Ctrl + Clic para selección múltiple.</div>
            </div>

            <!-- Botón Submit -->
            <div class="col-12 text-center mt-4">
                <button type="submit" class="btn btn-primary btn-lg px-5 shadow-sm fw-bold">Registrar Curso</button>
            </div>
        </div>
    </form>
</div>

<%-- Script de carga dinámica para previas --%>
<script>
    function cargarCursosPorInstituto(nombreInstituto) {
        const selectPrevias = document.querySelector('select[name="previas"]');
        selectPrevias.innerHTML = '';

        if (!nombreInstituto || nombreInstituto.trim() === "" || nombreInstituto === "null") {
            selectPrevias.innerHTML = '<option value="" disabled selected>Seleccione primero un instituto...</option>';
            return;
        }

        fetch("${pageContext.request.contextPath}/AltaCursoServlet?instituto=" + encodeURIComponent(nombreInstituto))
                .then(response => response.json())
                .then(cursos => {
                    if (cursos.length === 0) {
                        selectPrevias.innerHTML = '<option value="" disabled>Sin cursos previos disponibles en este instituto</option>';
                        return;
                    }

                    cursos.forEach(curso => {
                        const option = document.createElement('option');
                        option.value = curso.nombre;
                        option.textContent = curso.nombre;
                        selectPrevias.appendChild(option);
                    });
                })
                .catch(error => {
                    console.error('Error al cargar cursos:', error);
                    selectPrevias.innerHTML = '<option value="" disabled>Error al cargar cursos previos</option>';
                });
    }
</script>

<%-- Cerramos aquí las etiquetas que quedaron abiertas en InterfazBase.jsp (la columna central, el .row, el .container-fluid y el body) --%>
</div>

</div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>