<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="Logica.util.ImagenUtil"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= request.getAttribute("titlePage") != null ? request.getAttribute("titlePage") : "EdEXT" %></title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body class="bg-light">
        <%
            DTMaster dtm = (DTMaster) session.getAttribute("usuarioLogueado");
        %>

        <%-- BARRA DE NAVEGACIÓN FIJA --%>
        <nav class="navbar bg-body-tertiary px-4 border-bottom sticky-top shadow-sm z-3">
            <div class="container-fluid d-flex flex-nowrap align-items-center justify-content-between">
                <a class="navbar-brand me-3 d-flex align-items-center" href="${pageContext.request.contextPath}/MainMenu">
                    <img src="<%= request.getContextPath() %>/Images/Logo edEXT sin fondo.png" alt="LogoApp" width="52" height="52" class="rounded-circle border">
                </a>
                <form class="navbar-search-form my-0 d-flex" role="search">
                    <input class="form-control me-2" type="search" placeholder="Cursos, Programas" aria-label="Search"/>
                    <button class="btn btn-outline-success" type="submit">Buscar</button>
                </form>
                <%
                    if (dtm instanceof DTUsuarioBase) {
                        DTUsuarioBase dtu = (DTUsuarioBase) dtm;
                        String nombreImagen = dtu.getImg();
                %>
                    <div class="d-flex align-items-center ms-3">
                        <span class="fw-bold me-3 text-nowrap text-dark fs-5"><%=dtu.getNickname()%></span>    
                        <a class="navbar-brand p-0 m-0 d-flex align-items-center" href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname()%>">
                            <% if (nombreImagen != null && !nombreImagen.trim().isEmpty()) {%>
                            <img src="<%= ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PERFILES, nombreImagen, request.getContextPath())%>" alt="Perfil" width="52" height="52" class="rounded-circle border">
                            <% } else {%>
                            <img src="<%= request.getContextPath()%>/Images/ImagenUserDefault.png" alt="Perfil" width="52" height="52" class="rounded-circle border">
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

        <%-- CONTENEDOR Y FILA PRINCIPAL QUE AGRUPA TODO --%>
        <div class="container-fluid px-4 mt-4">
            <div class="row">

<%-- 1. MENÚ LATERAL IZQUIERDO --%>
                <div class="col-md-3 col-lg-2 mb-4">
                    <div class="card border-dark w-100 shadow-sm">
                        
                        <%-- BOTÓN VOLVER ATRÁS CON ICONO EN EL MENÚ LATERAL --%>
                        <div class="p-2 border-bottom bg-white text-center">
                            <button onclick="history.back()" class="btn btn-outline-dark btn-sm w-100 fw-bold d-flex align-items-center justify-content-center gap-2">
                                <img src="<%= request.getContextPath() %>/Images/Exit_Simbol.png" alt="Volver" width="18" height="18" style="transform: scaleX(-1);">
                                Volver Atrás
                            </button>
                        </div>

                        <ul class="list-group list-group-flush">
                            <%
                            if (dtm != null) {
                                if (dtm instanceof Logica.DTsClasses.DTDocente) {
                                    Logica.DTsClasses.DTDocente dtd = (Logica.DTsClasses.DTDocente) dtm;
                            %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtd.getNickname() %>" class="text-dark text-decoration-none">Mi Perfil</a>
                                    </li>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">CURSOS</li>

                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Usuarios</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="${pageContext.request.contextPath}/AltaCursoServlet" class="text-dark text-decoration-none d-block p-2">Alta Curso</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="${pageContext.request.contextPath}/AltaEdicionServlet" class="text-dark text-decoration-none d-block p-2">Alta Edición</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="${pageContext.request.contextPath}/CrearProgramaServlet" class="text-dark text-decoration-none d-block p-2">Alta Programa Formación</a>
                                    </li>
                            <%
                                } else if (dtm instanceof Logica.DTsClasses.DTUsuario) {
                                    Logica.DTsClasses.DTUsuario dtu = (Logica.DTsClasses.DTUsuario) dtm;
                            %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname() %>" class="text-dark text-decoration-none">Mi Perfil</a>
                                    </li>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">CONSULTAS</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="${pageContext.request.contextPath}/ConsultaCursoServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Curso</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="${pageContext.request.contextPath}/ConsultaEdicionServlet" class="text-dark text-decoration-none d-block p-2">Consulta Edición</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Usuarios</a>
                                    </li>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">Inscripciones</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <span class="d-block p-2">Inscribirme</span>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <span class="d-block p-2">Ver Resultados</span>
                                    </li>
                            <%
                                }
                            } else {
                            %>
                                <li class="list-group-item text-secondary fw-bold text-center fs-5">NAVEGACIÓN</li>
                                <li class="list-group-item text-dark fw-semibold p-0">
                                    <a href="${pageContext.request.contextPath}/ConsultaCursoServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Curso</a>
                                </li>
                                <li class="list-group-item text-dark fw-semibold p-0">
                                    <a href="${pageContext.request.contextPath}/ConsultaEdicionServlet" class="text-dark text-decoration-none d-block p-2">Consulta Edición</a>
                                </li>
                                <li class="list-group-item text-dark fw-semibold p-0">
                                    <a href="${pageContext.request.contextPath}/ConsultaUsuarioServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Usuarios</a>
                                </li>
                            <%
                            }
                            %>
                        </ul>

                        <!-- Sección Institutos -->
                        <div class="card-header text-center fw-bold border-top border-dark fs-5">
                            Institutos
                        </div>
                        <ul class="list-group list-group-flush">
                            <%
                                java.util.List<Logica.DTsClasses.DTMaster> listInstitutos = (java.util.List<Logica.DTsClasses.DTMaster>) session.getAttribute("AuxListInstituto");
                                if (listInstitutos != null && !listInstitutos.isEmpty()) {
                                    for (int i = 0; i < listInstitutos.size(); i++) {
                                        if (listInstitutos.get(i) != null) {
                                            Logica.DTsClasses.DTInstituto auxDt = (Logica.DTsClasses.DTInstituto) listInstitutos.get(i);
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

                        <!-- Sección Categorías -->
                        <div class="card-header text-center fw-bold border-top border-dark fs-5">
                            Categorias
                        </div>
                        <ul class="list-group list-group-flush">
                            <%
                                java.util.List<Logica.DTsClasses.DTMaster> listCategorias = (java.util.List<Logica.DTsClasses.DTMaster>) session.getAttribute("AuxListCategoria");
                                if (listCategorias != null && !listCategorias.isEmpty()) {
                                    for (int i = 0; i < listCategorias.size(); i++) {
                                        if (listCategorias.get(i) != null) {
                                            Logica.DTsClasses.DTCategoria auxDt = (Logica.DTsClasses.DTCategoria) listCategorias.get(i);
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
                    </div>
                </div>

                <%-- 2. ÁREA DE CONTENIDO CENTRAL --%>
                <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">