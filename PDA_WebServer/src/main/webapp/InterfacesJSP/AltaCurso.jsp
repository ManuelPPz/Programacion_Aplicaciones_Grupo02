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
            String instSel = (String) request.getAttribute("institutoSeleccionado");
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

                <%-- FORMULARIO PRINCIPAL DE ALTA DE CURSO --%>
                <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">

                    <div class="card border-dark shadow p-4 w-100" style="max-width: 850px;">
                        <h2 class="card-title text-center mb-4 fw-bold">Alta de Curso</h2>
                        
                        <% if(request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
                        <% } %>
                        <% if(request.getAttribute("exito") != null) { %>
                            <div class="alert alert-success"><%= request.getAttribute("exito") %></div>
                        <% } %>

                        <form action="${pageContext.request.contextPath}/AltaCursoServlet" method="POST">
                            <div class="row g-3">
                                
                                <!-- Instituto -->
                                <div class="col-md-6">
                                    <label class="form-label fw-bold">Instituto:</label>
                                    <select name="instituto" class="form-select" required onchange="cargarCursosPorInstituto(this.value)">
                                        <option value="" disabled <%= (instSel == null) ? "selected" : "" %>>Seleccione un instituto...</option>
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
                                                    <option value="<%= dti.getNombre() %>" <%= selected ? "selected" : "" %>>
                                                        <%= dti.getNombre() %>
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
                                    <label class="form-label fw-bold">Duración (meses):</label>
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
                                                    <option value="<%= dtc.getNombre() %>"><%= dtc.getNombre() %></option>
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
                                        <%
                                        List<DTMaster> auxListPre = (List<DTMaster>) request.getAttribute("cursos");
                                        if (auxListPre != null) {
                                            for (int i = 0; i < auxListPre.size(); i++) {
                                                if (auxListPre.get(i) instanceof DTCurso) {
                                                    DTCurso dtp = (DTCurso) auxListPre.get(i);
                                        %>
                                                    <option value="<%= dtp.getNombre() %>"><%= dtp.getNombre() %></option>
                                        <%
                                                }
                                            }
                                        }
                                        %>
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
                </div>

            </div>
        </div>

        <script>
            function cargarCursosPorInstituto(nombreInstituto) {
                if (nombreInstituto) {
                    window.location.href = "${pageContext.request.contextPath}/AltaCursoServlet?instituto=" + encodeURIComponent(nombreInstituto);
                }
            }

            function cargarEdiciones(nombreCurso) {
                if (nombreCurso) {
                    window.location.href = "${pageContext.request.contextPath}/InscripcionEdicionServlet?curso=" + encodeURIComponent(nombreCurso);
                }
            }
        </script>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>