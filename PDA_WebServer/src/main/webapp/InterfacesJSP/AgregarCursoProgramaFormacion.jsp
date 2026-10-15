<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTProgramaForm"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Agregar Curso a Programa</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
        <%-- Cargamos la estructura global (Barra de navegación + Menú lateral izquierdo) --%>
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <%-- CONTENIDO PRINCIPAL: AGREGAR CURSO A PROGRAMA --%>
        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
            <div class="w-100 px-3" style="max-width: 700px;">
                <h2 class="mb-4 fw-bold text-center">Agregar Curso a Programa de Formación</h2>

                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger shadow-sm"><%= request.getAttribute("error") %></div>
                <% } %>
                
                <% if (request.getAttribute("exito") != null) { %>
                    <div class="alert alert-success shadow-sm"><%= request.getAttribute("exito") %></div>
                <% } %>

                <div class="card border-dark shadow p-4 bg-white">
                    <form action="${pageContext.request.contextPath}/AgregarCursoProgramaServlet" method="POST">
                        
                        <%-- Selección de Programa de Formación --%>
                        <div class="mb-3">
                            <label for="programa" class="form-label fw-bold text-dark">Programa de Formación:</label>
                            <select name="programa" id="programa" class="form-select border-dark shadow-sm" required>
                                <option value="" disabled selected>-- Seleccione un programa --</option>
                                <%
                                    List<DTMaster> programas = (List<DTMaster>) request.getAttribute("programas");
                                    if (programas != null && !programas.isEmpty()) {
                                        for (DTMaster pMaster : programas) {
                                            if (pMaster instanceof DTProgramaForm) {
                                                DTProgramaForm dtp = (DTProgramaForm) pMaster;
                                %>
                                                <option value="<%= dtp.getNombre() %>"><%= dtp.getNombre() %></option>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <%-- Selección de Curso (name="cursos" para coincidir exactamente con el Servlet) --%>
                        <div class="mb-4">
                            <label for="cursos" class="form-label fw-bold text-dark">Curso a agregar:</label>
                            <select name="cursos" id="cursos" class="form-select border-dark shadow-sm" required>
                                <option value="" disabled selected>-- Seleccione un curso --</option>
                                <%
                                    List<DTMaster> cursos = (List<DTMaster>) request.getAttribute("cursos");
                                    if (cursos != null && !cursos.isEmpty()) {
                                        for (DTMaster cMaster : cursos) {
                                            if (cMaster instanceof DTCurso) {
                                                DTCurso dtc = (DTCurso) cMaster;
                                %>
                                                <option value="<%= dtc.getNombre() %>"><%= dtc.getNombre() %> (<%= dtc.getInstituto() %>)</option>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </div>

                        <%-- Botón de envío --%>
                        <div class="d-grid pt-2 border-top">
                            <button type="submit" class="btn btn-dark fw-bold py-2 shadow-sm">
                                Agregar Curso al Programa
                            </button>
                        </div>

                    </form>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>