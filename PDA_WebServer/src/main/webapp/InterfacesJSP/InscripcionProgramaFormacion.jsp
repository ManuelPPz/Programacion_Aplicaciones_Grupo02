<%@page import="Logica.DTsClasses.DTProgramaForm"%>
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
        <title>Alta Edición de Curso</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
    <!-- Navbar / Interfaz Base -->
    <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

                <%-- FORMULARIO PRINCIPAL DE ALTA DE EDICIÓN DE CURSO --%>
                <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
                    <div class="card border-dark shadow-sm p-4 w-100" style="max-width: 550px;">
                        <h2>Inscripción a Programa de Formación</h2>
                        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
                        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>

                        <form action="${pageContext.request.contextPath}/InscripcionProgramaServlet" method="POST">
                            <label>Seleccionar Programa:</label><br>
                            <select name="programa" required>
                                <option value="" disabled selected>Seleccione un programa...</option>
                                <%
                                List<DTMaster> auxListProg = (List<DTMaster>)request.getAttribute("programas");
                                if(auxListProg!=null){
                                    for(int i = 0;i<auxListProg.size();i++){
                                        DTProgramaForm dtp = (DTProgramaForm)auxListProg.get(i);
                                        if(dtp!=null){
                                            %>
                                                    <option value="<%= dtp.getNombre() %>">
                                                        <%= dtp.getNombre() %>
                                                    </option>
                                            <%
                                        }
                                    }
                                }
                                %>
                            </select><br><br>
                            <button type="submit">Inscribirme</button>
                        </form>
                    </div>
                </div>

            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>