<%@page import="java.io.File"%>
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
<%@ page import="jakarta.servlet.ServletContext" %>
<%@ page import="jakarta.servlet.http.HttpServletRequest" %>
<%@page import="Logica.util.ImagenUtil"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consulta Usuario</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
        
        <%-- Script para el funcionamiento de las pestañas --%>
        <script>
            function openTab(evt, tabName) {
                // Ocultar todos los contenidos de pestañas
                var tabcontent = document.getElementsByClassName("tab-content");
                for (var i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                }

                // Quitar la clase 'active' de todos los botones de las pestañas
                var tablinks = document.getElementsByClassName("tab-btn");
                for (var i = 0; i < tablinks.length; i++) {
                    tablinks[i].classList.remove("active", "btn-primary");
                    tablinks[i].classList.add("btn-outline-primary");
                }

                // Mostrar la pestaña actual y agregar la clase active al botón seleccionado
                document.getElementById(tabName).style.display = "block";
                evt.currentTarget.classList.add("active", "btn-primary");
                evt.currentTarget.classList.remove("btn-outline-primary");
            }
        </script>
    </head>
    <body class="bg-light">
        <%-- Cargamos toda la estructura visual y el menú lateral desde la base --%>
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

        <%-- FORMULARIO PRINCIPAL DE CONSULTA DE USUARIO --%>
        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">

            <div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
                <h2 class="card-title text-center mb-4 fw-bold">Consulta Usuario</h2>

                <% if (request.getAttribute("error") != null) {%>
                <div class="alert alert-danger"><%= request.getAttribute("error")%></div>
                <% } %>

                <%
                    DTUsuarioBase usuarioConsultado = (DTUsuarioBase) request.getAttribute("usuarioConsultado");
                    if (usuarioConsultado != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String fechaFormateada = (usuarioConsultado.getFNac() != null) ? sdf.format(usuarioConsultado.getFNac()) : "";
                %>
                <h3 class="fw-bold mb-3"><%= usuarioConsultado.getNombre()%> <%= usuarioConsultado.getApellido()%> (<%= usuarioConsultado.getNickname()%>)</h3>

                <%-- Imagen de Perfil del Usuario Consultado --%>
                <div class="foto-perfil-container mb-3">
                    <% if (usuarioConsultado.getImg() != null && !usuarioConsultado.getImg().trim().isEmpty()) {%>
                    <img src="<%= ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PERFILES, usuarioConsultado.getImg(), request.getContextPath())%>" 
                         alt="Perfil" 
                         width="110" 
                         height="110" 
                         style="object-fit: cover;"
                         class="rounded-circle border">
                    <% } else {%>
                    <img src="<%= request.getContextPath()%>/Images/ImagenUserDefault.png" 
                         alt="Perfil Default" 
                         width="110" 
                         height="110" 
                         style="object-fit: cover;"
                         class="rounded-circle border">
                    <% }%>
                </div>

                <div class="tab-header mb-3">
                    <button class="tab-btn btn btn-primary active me-2" onclick="openTab(event, 'tabInfo')">Información General</button>
                    <button class="tab-btn btn btn-outline-primary" onclick="openTab(event, 'tabActividad')">Actividad (Cursos/Programas)</button>
                </div>

                <div id="tabInfo" class="tab-content" style="display: block;">
                    <p><strong>Nickname:</strong> <%= usuarioConsultado.getNickname()%></p>
                    <p><strong>Nombre:</strong> <%= usuarioConsultado.getNombre()%></p>
                    <p><strong>Apellido:</strong> <%= usuarioConsultado.getApellido()%></p>
                    <p><strong>Email:</strong> <%= usuarioConsultado.getCorreo()%></p>
                    <p><strong>Fecha Nacimiento:</strong> <%= fechaFormateada%></p>
                    <% if (usuarioConsultado instanceof DTDocente) {
                            DTDocente docente = (DTDocente) usuarioConsultado;
                    %>
                    <p><strong>Tipo:</strong> Docente</p>
                    <p><strong>Instituto:</strong> <%= docente.getInstituto() != null && !docente.getInstituto().isEmpty() ? docente.getInstituto() : "N/A"%></p>
                    <% } else { %>
                    <p><strong>Tipo:</strong> Estudiante</p>
                    <% }
                        if (dtm != null) {
                            if (dtm instanceof DTUsuarioBase) {
                                DTUsuarioBase dtu = (DTUsuarioBase) dtm;
                                if (usuarioConsultado.getNickname().equals(dtu.getNickname())) {
                    %>
                    <form action="${pageContext.request.contextPath}/ModificarUsuarioServlet" method="GET" class="d-inline">
                        <button type="submit" class="btn-solid btn-styled theme-primary">Modificar Datos</button>
                    </form>
                    <%
                                }
                            }
                        }
                    %>
                </div>

                <div id="tabActividad" class="tab-content" style="display: none;">
                    <% if (usuarioConsultado instanceof DTDocente) {
                            DTDocente doc = (DTDocente) usuarioConsultado;
                    %>
                    <h4 class="fw-bold mt-2">Ediciones de Cursos Impartidas:</h4>
                    <ul>
                        <% if (doc.getEdiciones() != null && !doc.getEdiciones().isEmpty()) {
                                for (String ed : doc.getEdiciones()) {%>
                        <li>
                            <a href="${pageContext.request.contextPath}/ConsultaEdicionServlet?nombre=<%= java.net.URLEncoder.encode(ed, "UTF-8") %>" class="text-decoration-none">
                                <%= ed %>
                            </a>
                        </li>
                        <% }
                            } else { %>
                        <li>No imparte ediciones actualmente.</li>
                        <% } %>
                    </ul>
                    <% } else { %>
                    <h4 class="fw-bold mt-2">Inscripciones a Ediciones:</h4>
                    <ul>
                        <% if (usuarioConsultado.getEdiciones() != null && !usuarioConsultado.getEdiciones().isEmpty()) {
                                for (String ed : usuarioConsultado.getEdiciones()) {%>
                        <li>
                            <a href="${pageContext.request.contextPath}/ConsultaEdicionServlet?nombre=<%= java.net.URLEncoder.encode(ed, "UTF-8") %>" class="text-decoration-none">
                                <%= ed %>
                            </a>
                        </li>
                        <% }
                            } else { %>
                        <li>No está inscripto a ninguna edición.</li>
                        <% } %>
                    </ul>
                    <% } %>

                    <h4 class="fw-bold mt-3">Programas de Formación:</h4>
                    <ul>
                        <% if (usuarioConsultado.getProgramas() != null && !usuarioConsultado.getProgramas().isEmpty()) {
                                for (String prog : usuarioConsultado.getProgramas()) {%>
                        <li>
                            <a href="${pageContext.request.contextPath}/ConsultaProgramaServlet?nombre=<%= java.net.URLEncoder.encode(prog, "UTF-8") %>" class="text-decoration-none">
                                <%= prog %>
                            </a>
                        </li>
                        <% }
                            } else { %>
                        <li>No está vinculado a programas de formación.</li>
                        <% } %>
                    </ul>
                </div>
                <% }%>
            </div>
        </div>

    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>