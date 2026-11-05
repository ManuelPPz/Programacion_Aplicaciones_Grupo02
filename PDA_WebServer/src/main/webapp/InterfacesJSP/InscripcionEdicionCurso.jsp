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
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alta de Curso</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
       <!-- Navbar / Interfaz Base -->
    <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>
                <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center">

        <div class="card border-dark shadow-sm p-4 w-100" style="max-width: 550px;">
            <h2 class="text-center fw-bold mb-4 fs-3">Inscripción a Edición de Curso</h2>

            <%-- Mensajes de Error y Éxito --%>
            <% if(request.getAttribute("error") != null) { %>
                <div class="alert alert-danger text-center mb-3"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if(request.getAttribute("exito") != null) { %>
                <div class="alert alert-success text-center mb-3"><%= request.getAttribute("exito") %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/InscripcionEdicionServlet" method="POST">

                <%-- Cargar lista de Cursos --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Seleccionar Curso:</label>
                    <select name="curso" class="form-select border-dark" required onchange="cargarEdiciones(this.value)">
                        <option value="" disabled <%= (request.getAttribute("cursoSeleccionado") == null) ? "selected" : "" %>>-- Seleccione un curso --</option>
                        <%
                            List<DTMaster> listCursos = (List<DTMaster>) request.getAttribute("cursos");
                            String cursoSel = (String) request.getAttribute("cursoSeleccionado");
                            if (listCursos != null && !listCursos.isEmpty()) {
                                for (DTMaster dtma : listCursos) {
                                    if (dtma != null) {
                                        // Asumiendo que DTCurso o DTMaster expone getNombre()
                                        DTCurso c = (DTCurso)dtma;
                                        String nombreCurso = c.getNombre();
                                        boolean isSelected = cursoSel != null && cursoSel.equals(nombreCurso);
                                        %>
                                        <option value="<%= nombreCurso %>" <%= isSelected ? "selected" : "" %>><%= nombreCurso %></option>
                                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div>

                <%-- Cargar Ediciones del Curso Seleccionado --%>
                <div class="mb-4">
                    <label class="form-label fw-bold">Edición Vigente / Disponible:</label>
                    <select name="edicion" class="form-select border-dark" required>
                        <option value="" disabled selected>Seleccione una edicion...</option>
                        <%
                            List<DTMaster> listEdiciones = (List<DTMaster>) request.getAttribute("ediciones");
                            if (listEdiciones != null && !listEdiciones.isEmpty()) {
                                for (DTMaster ed : listEdiciones) {
                                    if (ed instanceof DTEdicionCurso) {
                                        DTEdicionCurso dtEd = (DTEdicionCurso) ed;
                                        %>
                                        <option value="<%= dtEd.getNombre() %>"><%= dtEd.getNombre() %></option>
                                        <%
                                    }
                                }
                            } else {
                                %>
                                <option value="" disabled selected>
                                    <%= (request.getAttribute("cursoSeleccionado") != null) ? "Sin ediciones vigentes para este curso" : "-- Seleccione un curso primero --" %>
                                </option>
                                <%
                            }
                        %>
                    </select>
                </div>

                <div class="d-grid">
                    <button type="submit" class="btn btn-primary fw-bold" <%= (listEdiciones == null || listEdiciones.isEmpty()) ? "disabled" : "" %>>
                        Inscribirme
                    </button>
                </div>
            </form>
        </div>

    </div>

    <%-- Script JavaScript para recargar la página al elegir un curso --%>
    <script>
        function cargarEdiciones(nombreCurso) {
            if (nombreCurso) {
                window.location.href = "${pageContext.request.contextPath}/InscripcionEdicionServlet?curso=" + encodeURIComponent(nombreCurso);
            }
        }
    </script>

            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>