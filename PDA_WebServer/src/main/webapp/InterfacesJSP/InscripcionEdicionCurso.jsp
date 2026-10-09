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
<%!
    // Función helper para evadir caché apuntando a la carpeta física real
    public String versionedUrl(String relativePath) {
        String discoPath = "C:/mi_proyecto_data" + relativePath;
        java.io.File file = new java.io.File(discoPath);
        
        if (file.exists()) {
            return relativePath + "?v=" + file.lastModified();
        }
        return relativePath;
    }
%>
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
        <%
            DTMaster dtm = (DTMaster)session.getAttribute("usuarioLogueado");
        %>

        <%-- BARRA DE NAVEGACIÓN FIJA (sticky-top) --%>
        <nav class="navbar bg-body-tertiary px-4 border-bottom sticky-top shadow-sm z-3">
            <div class="container-fluid d-flex flex-nowrap align-items-center justify-content-between">

                <!-- Logo -->
                <a class="navbar-brand me-3 d-flex align-items-center" href="${pageContext.request.contextPath}/MainMenu">
                    <img src="<%= request.getContextPath() %>/Images/Logo edEXT sin fondo.png" 
                         alt="LogoApp" 
                         width="52" 
                         height="52" 
                         class="rounded-circle border">
                </a>

                <!-- Buscador -->
                <form class="navbar-search-form my-0 d-flex" role="search">
                    <input class="form-control me-2" type="search" placeholder="Cursos, Programas" aria-label="Search"/>
                    <button class="btn btn-outline-success" type="submit">Buscar</button>
                </form>

                <!-- Perfil / Sesión -->
                <%
                    if (dtm instanceof DTUsuarioBase) {
                        DTUsuarioBase dtu = (DTUsuarioBase) dtm;
                        String nombreImagen = dtu.getImg(); 
                %>
                    <div class="d-flex align-items-center ms-3">
                        <span class="fw-bold me-3 text-nowrap text-dark fs-5"><%=dtu.getNickname()%></span>    
                        <a class="navbar-brand p-0 m-0 d-flex align-items-center" href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname() %>">
                            <% if (nombreImagen != null && !nombreImagen.trim().isEmpty()) { 
                                   String relPath = "/uploads/perfiles/" + nombreImagen;
                                   String urlConVersion = versionedUrl(relPath);
                            %>
                                <img src="<%= request.getContextPath() %><%= urlConVersion %>" 
                                     alt="Perfil" 
                                     width="52" 
                                     height="52" 
                                     class="rounded-circle border">
                            <% } else { %>
                                <img src="<%= request.getContextPath() %>/Images/ImagenUserDefault.png" 
                                     alt="Perfil" 
                                     width="52" 
                                     height="52" 
                                     class="rounded-circle border">
                            <% } %>
                        </a>
                    </div>
                <%
                    } else {
                %>
                    <div class="d-flex align-items-center ms-3">
                        <a class="link-opacity-100-hover me-3 text-nowrap" href="IniciarSesionServlet">Iniciar Sesion</a>
                        <div class="vr text-dark opacity-100 me-3" style="height: 1.5rem; width: 2px;"></div>
                        <a class="link-opacity-100-hover text-nowrap" href="AltaUsuarioServlet">Registrarse</a>
                    </div>
                <%
                    }
                %>
            </div>
        </nav>

        <%-- CONTENIDO PRINCIPAL --%>
        <div class="container-fluid px-4 mt-4">
            <div class="row">

                <%-- MENÚ LATERAL IZQUIERDO --%>
                <div class="col-md-3 col-lg-2 mb-4">
                    <div class="card border-dark w-100 shadow-sm">
                        <ul class="list-group list-group-flush">
                            <%
                            if (dtm != null) {
                                if (dtm instanceof DTDocente) {
                                    DTDocente dtd = (DTDocente) dtm;
                                    %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtd.getNickname() %>" class="text-dark text-decoration-none d-block">
                                            Mi Perfil
                                        </a>
                                    </li>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">CURSOS</li>
                                    <li class="list-group-item text-dark fw-semibold">Alta Curso</li>
                                    <li class="list-group-item text-dark fw-semibold">Alta Edicion</li>
                                    <li class="list-group-item text-dark fw-semibold">Alta Programa de Formacion</li>
                                    <%
                                } else if (dtm instanceof DTUsuario) {
                                    DTUsuario dtu = (DTUsuario) dtm;
                                    %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname() %>" class="text-dark text-decoration-none d-block">
                                            Mi Perfil
                                        </a>
                                    </li>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">Inscripciones</li>
                                    <li class="list-group-item text-dark fw-semibold">Inscribirme</li>
                                    <li class="list-group-item text-dark fw-semibold">Ver Resultados</li>
                                    <%
                                }
                            }
                            %>
                        </ul>
                        <div class="card-header text-center fw-bold border-top border-dark fs-5">
                            Institutos
                        </div>
                        <ul class="list-group list-group-flush">
                            <%
                                List<DTMaster> listInstitutos = (List<DTMaster>) session.getAttribute("AuxListInstituto");
                                if (listInstitutos != null && !listInstitutos.isEmpty()) {
                                    for (int i = 0; i < listInstitutos.size(); i++) {
                                        if (listInstitutos.get(i) != null) {
                                            DTInstituto auxDt = (DTInstituto) listInstitutos.get(i);
                                            %>
                                            <li class="list-group-item text-dark fw-semibold"><%= auxDt.getNombre() %></li>
                                            <%
                                        }
                                    }
                                } else {
                                    %>
                                    <li class="list-group-item text-muted small fw-semibold">Sin institutos</li>
                                    <%
                                }
                            %>
                        </ul>

                        <div class="card-header text-center fw-bold border-top border-dark fs-5">
                            Categorias
                        </div>
                        <ul class="list-group list-group-flush">
                            <%
                                List<DTMaster> listCategorias = (List<DTMaster>) session.getAttribute("AuxListCategoria");
                                if (listCategorias != null && !listCategorias.isEmpty()) {
                                    for (int i = 0; i < listCategorias.size(); i++) {
                                        if (listCategorias.get(i) != null) {
                                            DTCategoria auxDt = (DTCategoria) listCategorias.get(i);
                                            %>
                                            <li class="list-group-item text-dark fw-semibold"><%= auxDt.getNombre() %></li>
                                            <%
                                        }
                                    }
                                } else {
                                    %>
                                    <li class="list-group-item text-muted small">Sin Categorias</li>
                                    <%
                                }
                            %>
                        </ul>
                        <ul class="list-group list-group-flush">
                            <%
                            if (dtm != null) {
                                if (dtm instanceof DTUsuarioBase) {
                                    %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        <a href="#" class="text-dark text-decoration-none d-block">
                                            <img src="<%= request.getContextPath() %>/Images/Exit_Simbol.png" 
                                                alt="Perfil" 
                                                width="22" 
                                                height="22" 
                                                class="border border-0 me-1">
                                            Salir
                                        </a>
                                    </li>
                                    <%
                                }
                            }
                            %>
                        </ul>
                    </div>
                </div>

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