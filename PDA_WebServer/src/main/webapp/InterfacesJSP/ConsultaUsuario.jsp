<%@page import="java.util.List"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="Logica.DTsClasses.DTDocente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
        <h2>Consulta de Usuario</h2>
        
        <%-- Mensaje de Error --%>
        <% if(request.getAttribute("error") != null) { %>
            <div class="alert-error"><%= request.getAttribute("error") %></div>
        <% } %>
        
        <%-- Formulario de Selección --%>
        <form action="${pageContext.request.contextPath}/ConsultaUsuarioServlet" method="GET">
            <label for="nickname">Seleccionar Usuario:</label>
            <select name="nickname" id="nickname" onchange="this.form.submit()">
                <option value="">-- Seleccione un usuario --</option>
                <% 
                    List<DTMaster> listaUsuarios = (List<DTMaster>) request.getAttribute("usuarios");
                    String nickSeleccionado = request.getParameter("nickname");
                    
                    if (listaUsuarios != null) {
                        for (DTMaster u : listaUsuarios) {
                            DTUsuarioBase usr = (DTUsuarioBase) u;
                            boolean selected = (nickSeleccionado != null && nickSeleccionado.equals(usr.getNickname()));
                %>
                            <option value="<%= usr.getNickname() %>" <%= selected ? "selected" : "" %>>
                                <%= usr.getNickname() %> - <%= usr.getNombre() %> <%= usr.getApellido() %>
                            </option>
                <% 
                        }
                    } 
                %>
            </select>
            <button type="submit">Ver Detalles</button>
        </form>
        <hr>
        
        <%-- Detalles del Perfil --%>
        <% 
            DTUsuarioBase usuarioConsultado = (DTUsuarioBase) request.getAttribute("usuarioConsultado");
            if (usuarioConsultado != null) { 
        %>
            <div id="detallesUsuario">
                <h3>Detalles del Perfil de <%= usuarioConsultado.getNickname() %></h3>
                
                <div class="tab-header">
                    <button class="tab-button active" onclick="openTab(event, 'tabInfo')">Información General</button>
                    <button class="tab-button" onclick="openTab(event, 'tabActividad')">Actividad (Cursos/Programas)</button>
                </div>
                
                <div id="tabInfo" class="tab-content active" style="display: block;">
                    <p><strong>Nickname:</strong> <%= usuarioConsultado.getNickname() %></p>
                    <p><strong>Nombre:</strong> <%= usuarioConsultado.getNombre() %></p>
                    <p><strong>Apellido:</strong> <%= usuarioConsultado.getApellido() %></p>
                    <p><strong>Email:</strong> <%= usuarioConsultado.getCorreo() %></p>
                    <p><strong>Fecha Nacimiento:</strong> <%= usuarioConsultado.getFNac()%></p>
                    
                    <%-- Datos específicos si es docente --%>
                    <% if (usuarioConsultado instanceof DTDocente) { 
                        DTDocente docente = (DTDocente) usuarioConsultado;
                    %>
                        <p><strong>Tipo:</strong> Docente</p>
                        <p><strong>Instituto:</strong> <%= docente.getInstituto() != null && !docente.getInstituto().isEmpty() ? docente.getInstituto() : "N/A" %></p>
                    <% } else { %>
                        <p><strong>Tipo:</strong> Estudiante</p>
                    <% } %>
                </div>
                
                <div id="tabActividad" class="tab-content" style="display: none;">
                    <% if (usuarioConsultado instanceof DTDocente) { %>
                        <h4>Ediciones de Cursos impartidas:</h4>
                        <!-- Iterar las ediciones que vienen dentro de DTDocente -->
                    <% } else { %>
                        <h4>Inscripciones a Ediciones:</h4>
                        <!-- Iterar inscripciones que vienen dentro de DTEstudiante -->
                    <% } %>
                </div>
            </div>
        <% } %>
    </div>
</body>
</html>