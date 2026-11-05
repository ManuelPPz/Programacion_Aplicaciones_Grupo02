<%@page import="Logica.DTsClasses.DTProgramaForm"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>
        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
            <div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
                <h2 class="card-title text-center mb-4 fw-bold">Curso, ediciones y programas</h2>
                <!--A partir de aca es la lista de cursos, ediciones y programas por la categoria seleccionda o instituto-->
                <%                    List<DTMaster> auxList = (List<DTMaster>) request.getAttribute("ListaFinal");
                %>
                <div class="text-center"><%= auxList.size()%> Resultados</div>
                <ol class="list-group list-group-numbered">
                    <%

                        for (DTMaster dt : auxList) {
                            if (dt instanceof DTCurso) {
                                DTCurso dtc = (DTCurso) dt;
                    %>
                    <li class="list-group-item d-flex justify-content-between align-items-start">
                        <div class="ms-2 me-auto">
                            <a href="${pageContext.request.contextPath}/ConsultaCursoServlet?nombre=<%= java.net.URLEncoder.encode(dtc.getNombre(), "UTF-8")%>&curso=<%= java.net.URLEncoder.encode(dtc.getNombre(), "UTF-8")%>">
                                <div class="fw-bold"><%= dtc.getNombre()%></div>
                            </a>

                        </div>

                        <span class="badge bg-primary text-white rounded-pill">Curso</span>
                    </li>
                    <%
                    } else if (dt instanceof DTEdicionCurso) {
                        DTEdicionCurso dte = (DTEdicionCurso) dt;
                        String linkEdicion = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(dte.getNombre(), "UTF-8");
                    %>
                    <li class="list-group-item d-flex justify-content-between align-items-start">
                        <div class="ms-2 me-auto">
                            <a href="<%= linkEdicion%>">

                                <div class="fw-bold"><%= dte.getNombre()%></div>
                            </a>
                        </div>

                        <span class="badge bg-primary text-white rounded-pill">Edicion</span>
                    </li>
                    <%
                    } else if (dt instanceof DTProgramaForm) {
                        DTProgramaForm dtp = (DTProgramaForm) dt;
                        String linkProg = request.getContextPath() + "/ConsultaProgramaServlet?nombre=" + java.net.URLEncoder.encode(dtp.getNombre(), "UTF-8");
                    %>
                    <li class="list-group-item d-flex justify-content-between align-items-start"> 

                        <div class="ms-2 me-auto">
                            <a href="<%= linkProg%>">
                                <div class="fw-bold"><%= dtp.getNombre()%></div>
                            </a>
                        </div>
                        <span class="badge bg-primary text-white rounded-pill">Programa</span>
                    </li>
                    <%
                            }
                        }
                    %>

                </ol>
            </div>
        </div>
    </body>
</html>