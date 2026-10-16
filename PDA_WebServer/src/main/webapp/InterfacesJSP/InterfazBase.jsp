<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="Logica.util.ImagenUtil"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= request.getAttribute("titlePage") != null ? request.getAttribute("titlePage") : "EdEXT" %></title>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/style.css">
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="<%= request.getContextPath() %>/js/main.js"></script>
        
        <%-- Estilos para la cajita flotante de autocompletado del buscador --%>
        <style>
            .search-container {
                position: relative;
            }
            #searchResults {
                position: absolute;
                top: 100%;
                left: 0;
                right: 0;
                z-index: 2000;
                max-height: 380px;
                overflow-y: auto;
                background: #ffffff;
                border: 1px solid rgba(0,0,0,.15);
                border-radius: 0.375rem;
                box-shadow: 0 0.5rem 1rem rgba(0,0,0,.2);
                display: none;
            }
            .search-item {
                padding: 10px 15px;
                border-bottom: 1px solid #f1f1f1;
                cursor: pointer;
                text-decoration: none;
                display: block;
                color: #333;
            }
            .search-item:hover {
                background-color: #f8f9fa;
                color: #000;
            }
            .search-badge {
                font-size: 0.75rem;
                padding: 3px 6px;
                border-radius: 4px;
                font-weight: bold;
                float: right;
            }
        </style>
    </head>
    <body class="bg-light">
        <%
            DTMaster dtm = (DTMaster) session.getAttribute("usuarioLogueado");
        %>

        <%-- BARRA DE NAVEGACIÓN FIJA --%>
        <nav class="navbar bg-body-tertiary px-4 border-bottom sticky-top shadow-sm z-3">
            <div class="container-fluid d-flex flex-nowrap align-items-center justify-content-between">
                <a class="navbar-brand me-3 d-flex align-items-center" href="<%= request.getContextPath() %>/MainMenu">
                    <img src="<%= request.getContextPath() %>/Images/Logo edEXT sin fondo.png" alt="LogoApp" width="52" height="52" class="rounded-circle border">
                </a>
                
                <%-- BARRA DE BÚSQUEDA INTELIGENTE EN TIEMPO REAL --%>
                <div class="search-container my-0 flex-grow-1 mx-4" style="max-width: 500px;">
                    <form class="d-flex" role="search" onsubmit="return false;">
                        <input id="searchInput" class="form-control me-2" type="search" placeholder="Buscar cursos, programas, ediciones, usuarios..." aria-label="Search" autocomplete="off"/>
                    </form>
                    <div id="searchResults"></div>
                </div>

                <%
                    if (dtm instanceof DTUsuarioBase) {
                        DTUsuarioBase dtu = (DTUsuarioBase) dtm;
                        String nombreImagen = dtu.getImg();
                %>
                    <div class="d-flex align-items-center ms-3">
                        <span class="fw-bold me-3 text-nowrap text-dark fs-5"><%=dtu.getNickname()%></span>    
                        <a class="navbar-brand p-0 m-0 d-flex align-items-center" href="<%= request.getContextPath() %>/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname()%>">
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

        <%-- SCRIPT JAVASCRIPT PARA EL AUTOCOMPLETADO DINÁMICO --%>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const searchInput = document.getElementById("searchInput");
                const searchResults = document.getElementById("searchResults");
                let timeout = null;

                function ejecutarBusqueda() {
                    clearTimeout(timeout);
                    const query = searchInput.value.trim();

                    if (query.length < 1) {
                        searchResults.style.display = "none";
                        searchResults.innerHTML = "";
                        return;
                    }

                    timeout = setTimeout(() => {
                        fetch("<%= request.getContextPath() %>/BuscadorLiveServlet?q=" + encodeURIComponent(query))
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error("Respuesta HTTP con estado " + response.status);
                                }
                                return response.json();
                            })
                            .then(data => {
                                searchResults.innerHTML = "";
                                if (data && data.length > 0) {
                                    data.forEach(item => {
                                        let nombre = item.nombre || item.titulo || item.label || item.nickname || "Resultado";
                                        let tipo = item.tipo || item.type || "Elemento";
                                        let rawUrl = item.url || item.link || item.href || "#";

                                        let finalUrl = rawUrl;
                                        if (rawUrl !== "#" && !rawUrl.startsWith("http") && !rawUrl.startsWith("<%= request.getContextPath() %>")) {
                                            if (!rawUrl.startsWith("/")) rawUrl = "/" + rawUrl;
                                            finalUrl = "<%= request.getContextPath() %>" + rawUrl;
                                        }

                                        let badgeClass = "bg-secondary";
                                        let tipoLower = tipo.toLowerCase();
                                        if (tipoLower.includes("curso")) badgeClass = "bg-primary text-white";
                                        else if (tipoLower.includes("programa")) badgeClass = "bg-info text-dark";
                                        else if (tipoLower.includes("edicion") || tipoLower.includes("edición")) badgeClass = "bg-success text-white";
                                        else if (tipoLower.includes("usuario") || tipoLower.includes("docente") || tipoLower.includes("estudiante")) badgeClass = "bg-warning text-dark";

                                        const link = document.createElement("a");
                                        link.className = "search-item";
                                        link.href = finalUrl;
                                        link.innerHTML = "<span>" + nombre + "</span> <span class=\"search-badge " + badgeClass + "\">" + tipo + "</span>";
                                        searchResults.appendChild(link);
                                    });
                                    searchResults.style.display = "block";
                                } else {
                                    searchResults.innerHTML = '<div class="p-3 text-muted small text-center">No se encontraron resultados</div>';
                                    searchResults.style.display = "block";
                                }
                            })
                            .catch(err => {
                                console.error("Error en la búsqueda en vivo:", err);
                                searchResults.style.display = "none";
                            });
                    }, 250);
                }

                searchInput.addEventListener("input", ejecutarBusqueda);
                searchInput.addEventListener("focus", function() {
                    if (this.value.trim().length >= 1) {
                        ejecutarBusqueda();
                    }
                });

                document.addEventListener("click", function (e) {
                    if (!searchInput.contains(e.target) && !searchResults.contains(e.target)) {
                        searchResults.style.display = "none";
                    }
                });
            });
        </script>

        <%-- CONTENEDOR Y FILA PRINCIPAL QUE AGRUPA TODO --%>
        <div class="container-fluid px-4 mt-4">
            <div class="row">

                <%-- 1. MENÚ LATERAL IZQUIERDO --%>
                <div class="col-md-3 col-lg-2 mb-4">
                    <div class="card border-dark w-100 shadow-sm">
                        
                        <%-- BOTÓN VOLVER ATRÁS --%>
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
                                    <%-- SECCIÓN USUARIOS --%>
                                    <li class="list-group-item text-secondary fw-bold text-center fs-5">USUARIOS</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ConsultaUsuarioIndividualServlet?nickname=<%= dtd.getNickname() %>" class="text-dark text-decoration-none d-block p-2">Mi Perfil</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ConsultaUsuarioServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Usuarios</a>
                                    </li>

                                    <%-- SECCIÓN CURSOS --%>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">CURSOS</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/AltaCursoServlet" class="text-dark text-decoration-none d-block p-2">Alta Curso</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/AltaEdicionServlet" class="text-dark text-decoration-none d-block p-2">Alta Edición</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/CrearProgramaServlet" class="text-dark text-decoration-none d-block p-2">Alta Programa Formación</a>
                                    </li>

                                    <%-- NUEVO: SECCIÓN INSCRIPCIONES (DOCENTE) --%>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">INSCRIPCIONES</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/SeleccionarEstudiantesEdicionServlet" class="text-dark text-decoration-none d-block p-2">Seleccionar Estudiantes</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ListarAceptadosEdicionServlet" class="text-dark text-decoration-none d-block p-2">Listar Aceptados</a>
                                    </li>
                            <%
                                } else if (dtm instanceof Logica.DTsClasses.DTUsuario) {
                                    Logica.DTsClasses.DTUsuario dtu = (Logica.DTsClasses.DTUsuario) dtm;
                            %>
                                    <%-- SECCIÓN USUARIOS --%>
                                    <li class="list-group-item text-secondary fw-bold text-center fs-5">USUARIOS</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ConsultaUsuarioIndividualServlet?nickname=<%= dtu.getNickname() %>" class="text-dark text-decoration-none d-block p-2">Mi Perfil</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ConsultaUsuarioServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Usuarios</a>
                                    </li>

                                    <%-- SECCIÓN CONSULTAS DE CURSOS --%>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">CURSOS</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ConsultaCursoServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Curso</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ConsultaEdicionServlet" class="text-dark text-decoration-none d-block p-2">Consulta Edición</a>
                                    </li>

                                    <%-- SECCIÓN INSCRIPCIONES --%>
                                    <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">INSCRIPCIONES</li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/InscripcionEdicionServlet" class="text-dark text-decoration-none d-block p-2">Inscribirme a Edición</a>
                                    </li>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/InscripcionProgramaServlet" class="text-dark text-decoration-none d-block p-2">Inscribirme a Programa</a>
                                    </li>
                                    <%-- NUEVO: enlace a los resultados de inscripciones del estudiante --%>
                                    <li class="list-group-item text-dark fw-semibold p-0">
                                        <a href="<%= request.getContextPath() %>/ListarResultadosInscripcionesServlet" class="text-dark text-decoration-none d-block p-2">Ver Resultados</a>
                                    </li>
                            <%
                                }
                            } else {
                            %>
                                <%-- SECCIÓN USUARIOS INVITADOS --%>
                                <li class="list-group-item text-secondary fw-bold text-center fs-5">USUARIOS</li>
                                <li class="list-group-item text-dark fw-semibold p-0">
                                    <a href="<%= request.getContextPath() %>/ConsultaUsuarioServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Usuarios</a>
                                </li>

                                <%-- SECCIÓN NAVEGACIÓN DE CURSOS --%>
                                <li class="list-group-item text-secondary fw-bold border-top border-dark fs-5 text-center">CURSOS</li>
                                <li class="list-group-item text-dark fw-semibold p-0">
                                    <a href="<%= request.getContextPath() %>/ConsultaCursoServlet" class="text-dark text-decoration-none d-block p-2">Consulta de Curso</a>
                                </li>
                                <li class="list-group-item text-dark fw-semibold p-0">
                                    <a href="<%= request.getContextPath() %>/ConsultaEdicionServlet" class="text-dark text-decoration-none d-block p-2">Consulta Edición</a>
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
                                            <a href="${pageContext.request.contextPath}/ListaCurEdiProgServlet?dato=<%= auxDt.getNombre() %>&tipoDeBusqueda=Instituto" class="text-decoration-none">
                                                <li class="list-group-item text-dark fw-semibold"><%= auxDt.getNombre() %></li>
                                            </a>            
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
                                            <a href="${pageContext.request.contextPath}/ListaCurEdiProgServlet?dato=<%= auxDt.getNombre() %>&tipoDeBusqueda=Categoria" class="text-decoration-none">
                                                <li class="list-group-item text-dark fw-semibold"><%= auxDt.getNombre() %></li>
                                            </a> 
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
