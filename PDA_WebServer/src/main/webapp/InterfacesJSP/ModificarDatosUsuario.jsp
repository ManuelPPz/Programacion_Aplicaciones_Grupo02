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
        <title>Modificar Datos</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body>
        <%
            DTMaster dtm = (DTMaster)session.getAttribute("usuarioLogueado");
        %>
        <div class="container-fluid px-4 mt-3">
            <%-- BARRA DE NAVEGACIÓN --%>
            <nav class="navbar bg-body-tertiary px-3 rounded border">
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
                    <form class="navbar-search-form my-0" role="search">
                        <input class="form-control" type="search" placeholder="Cursos, Programas" aria-label="Search"/>
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
            <hr class="border border-dark border-1 opacity-100 mb-4">
            
            <div class="row">

                <div class="col-md-3 col-lg-2">
                    <div class="card border-dark w-100">
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
                                    <li class="list-group-item">
                                        <a href="${pageContext.request.contextPath}/AltaCursoServlet" class="text-dark fw-semibold text-decoration-none d-block">
                                            Alta Curso
                                        </a>
                                    </li>
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
                                    <li class="list-group-item">
                                        <a href="${pageContext.request.contextPath}/InscripcionEdicionServlet" class="text-dark fw-semibold text-decoration-none d-block">
                                            Inscribirme
                                        </a>
                                    </li>
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
                                    DTUsuarioBase dtu = (DTUsuarioBase) dtm;
                                    %>
                                    <li class="list-group-item text-dark fw-bold text-center fs-5">
                                        
                                        <a href="#" class="text-dark text-decoration-none d-block">
                                            <img src="<%= request.getContextPath() %>/Images/Exit_Simbol.png" 
                                                alt="Perfil" 
                                                width="22" 
                                                height="22" 
                                                class="border border-0">
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

                        
                <!---Columna despues de user interface default de la pagina-->
                <div class="col-md-9 col-lg-10 ps-md-4">
                    <h2>Modificar Datos de Usuario</h2>
        
                <% if(request.getAttribute("error") != null) { %>
                    <div class="alert-error"><%= request.getAttribute("error") %></div>
                <% } %>
                <% if(request.getAttribute("exito") != null) { %>
                    <div class="alert-success"><%= request.getAttribute("exito") %></div>
                <% } %>

                <form action="${pageContext.request.contextPath}/ModificarUsuarioServlet" method="POST" enctype="multipart/form-data">

                            <% 
                                DTUsuarioBase u = (DTUsuarioBase) request.getAttribute("usuario");
                                boolean esDocente = false;
                                String fechaFormateada = "";

                                if (u != null) {
                                    // Determinar si es docente mediante el nombre de su clase concreta
                                    esDocente = u.getClass().getSimpleName().toLowerCase().contains("docente");

                                    if (u.getFNac() != null) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        fechaFormateada = sdf.format(u.getFNac());
                                    }
                                }
                            %>

                            <%-- Pasa el tipo de usuario al Servlet para procesar el rol correctamente --%>
                            <input type="hidden" name="tipoUsuario" value="<%= esDocente ? "docente" : "estudiante" %>">

                            <label>Nickname (No editable):</label><br>
                            <input type="text" name="nickname" value="${usuario.nickname}" readonly><br>

                            <label>Correo (No editable):</label><br>
                            <input type="email" name="correo" value="${usuario.correo}" readonly><br>

                            <label>Nombre:</label><br>
                            <input type="text" name="nombre" value="${usuario.nombre}" required><br>

                            <label>Apellido:</label><br>
                            <input type="text" name="apellido" value="${usuario.apellido}" required><br>

                            <label>Fecha de Nacimiento:</label><br>
                            <input type="date" name="fechaNacimiento" value="<%= fechaFormateada %>" required><br><br>

                            <label>Nueva Contraseña (opcional):</label><br>
                            <input type="password" name="password"><br>
                            <label>Confirmar Nueva Contraseña:</label><br>
                            <input type="password" name="passwordConfirm"><br>

                            <!-- Selector de Instituto (se despliega solo si el usuario es Docente) -->
                            <% 
                                List<DTMaster> institutos = (List<DTMaster>) request.getAttribute("institutos");
                                if (esDocente && institutos != null && !institutos.isEmpty()) { 
                            %>
                                <label>Instituto:</label><br>
                                <select name="instituto">
                                    <option value="">-- Seleccionar Instituto --</option>
                                    <% 
                                        for (DTMaster elem : institutos) { 
                                            DTInstituto inst = (DTInstituto) elem;
                                    %>
                                        <option value="<%= inst.getNombre() %>"><%= inst.getNombre() %></option>
                                    <% } %>
                                </select><br><br>
                            <% } %>


                            <label>Cambiar Imagen de Perfil:</label><br>
                            <input type="file" name="imagen" accept="image/png, image/jpeg"><br><br>

                            <button type="submit">Guardar Cambios</button>
                        </form>
                </div>

            </div>
        </div>
        
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>