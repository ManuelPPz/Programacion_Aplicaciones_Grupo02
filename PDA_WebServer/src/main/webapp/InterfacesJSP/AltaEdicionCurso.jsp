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

<%
    // Definimos el título que aparecerá en la pestaña del navegador
    request.setAttribute("titlePage", "Alta Edición de Curso");
%>

<%-- Cargamos la estructura global (Barra de navegación + Menú lateral izquierdo) --%>
<%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

<%-- FORMULARIO PRINCIPAL DE ALTA DE EDICIÓN (Se ubica automáticamente al lado del menú lateral) --%>
<div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
    <h2 class="card-title text-center mb-4 fw-bold">Alta Edición de Curso</h2>
    
    <% if(request.getAttribute("error") != null) { %>
        <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
    <% } %>
    <% if(request.getAttribute("exito") != null) { %>
        <div class="alert alert-success"><%= request.getAttribute("exito") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/AltaEdicionServlet" method="POST" enctype="multipart/form-data">
        <div class="row g-3">
            
            <!-- Seleccionar Curso -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Curso:</label>
                <select name="curso" class="form-select" required>
                    <option value="" disabled selected>Seleccione un curso...</option>
                    <%
                        List<DTMaster> auxListCur = (List<DTMaster>) request.getAttribute("cursos");
                        if (auxListCur != null) {
                            for (DTMaster m : auxListCur) {
                                if (m instanceof DTCurso) {
                                    DTCurso dtc = (DTCurso) m;
                    %>
                                    <option value="<%= dtc.getNombre() %>"><%= dtc.getNombre() %></option>
                    <%
                                }
                            }
                        }
                    %>
                </select>
            </div>

            <!-- Nombre de la Edición -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Nombre de la Edición:</label>
                <input type="text" name="nombre" class="form-control" placeholder="Ej: Edición Verano 2026" required>
            </div>

            <!-- Fecha de Inicio -->
            <div class="col-md-4">
                <label class="form-label fw-bold">Fecha de Inicio:</label>
                <input type="date" name="fechaInicio" class="form-control" required>
            </div>

            <!-- Fecha de Fin -->
            <div class="col-md-4">
                <label class="form-label fw-bold">Fecha de Fin:</label>
                <input type="date" name="fechaFin" class="form-control" required>
            </div>

            <!-- Cupo -->
            <div class="col-md-4">
                <label class="form-label fw-bold">Cupo (Opcional):</label>
                <input type="number" name="cupo" class="form-control" min="1" placeholder="Ej: 30">
            </div>

            <!-- Docentes Participantes -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Docentes Participantes:</label>
                <select name="docentes" class="form-select" multiple required style="height: 120px;">
                    <%
                        List<DTMaster> auxListDoc = (List<DTMaster>) request.getAttribute("docentes");
                        if (auxListDoc != null) {
                            for (DTMaster elem : auxListDoc) {
                                if (elem instanceof DTDocente) {
                                    DTDocente dtd = (DTDocente) elem;
                    %>
                                    <option value="<%= dtd.getNickname() %>">
                                        <%= dtd.getNombre() %> (<%= dtd.getNickname() %>)
                                    </option>
                    <%
                                }
                            }
                        }
                    %>
                </select>
                <div class="form-text">Usa Ctrl + Clic para selección múltiple.</div>
            </div>

            <!-- Imagen de la Edición -->
            <div class="col-md-6">
                <label class="form-label fw-bold">Imagen de la Edición:</label>
                <input type="file" name="imagen" class="form-control" accept="image/png, image/jpeg">
            </div>

            <!-- Botón Submit -->
            <div class="col-12 text-center mt-4">
                <button type="submit" class="btn btn-primary btn-lg px-5 shadow-sm fw-bold">Crear Edición</button>
            </div>

        </div>
    </form>
</div>

<%-- Cerramos aquí de forma limpia las etiquetas abiertas en InterfazBase.jsp (la columna central, el .row, el .container-fluid y el body) --%>
                </div>

            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>