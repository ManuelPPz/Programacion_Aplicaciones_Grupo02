<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Alta de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h2>Alta de Usuario</h2>
        <% if(request.getAttribute("error") != null) { %><div class="alert-error"><%= request.getAttribute("error") %></div><% } %>
        <% if(request.getAttribute("exito") != null) { %><div class="alert-success"><%= request.getAttribute("exito") %></div><% } %>
        
        <form action="${pageContext.request.contextPath}/AltaUsuarioServlet" method="POST" enctype="multipart/form-data">
            <label>Nickname:</label><br>
            <input type="text" name="nickname" required><br>
            <label>Nombre:</label><br>
            <input type="text" name="nombre" required><br>
            <label>Apellido:</label><br>
            <input type="text" name="apellido" required><br>
            <label>Correo Electrónico:</label><br>
            <input type="email" name="correo" required><br>
            
            <!-- CORREGIDO: name="fechaNac" para que coincida con el Servlet -->
            <label>Fecha de Nacimiento:</label><br>
            <input type="date" name="fechaNac" required><br>
            
            <label>Contraseña:</label><br>
            <input type="password" name="password" required><br>
            <label>Confirmar Contraseña:</label><br>
            <input type="password" name="passwordConfirm" required><br>
            <label>Imagen de Perfil:</label><br>
            <input type="file" name="imagen" accept="image/png, image/jpeg"><br><br>
            
            <label>Tipo de Usuario:</label><br>
            <select name="tipoUsuario" id="tipoUsuario" onchange="toggleDocente()">
                <option value="estudiante">Estudiante</option>
                <option value="docente">Docente</option>
            </select><br><br>
            
            <div id="divInstituto" style="display:none;">
                <label>Instituto (Solo para docentes):</label><br>
                <select name="instituto">
                    <option value="">Seleccione un instituto...</option>
                    <% 
                        List<DTMaster> institutos = (List<DTMaster>) request.getAttribute("institutos");
                        if (institutos != null) {
                            for (DTMaster inst : institutos ) {
                                // Ajusta .getNombre() según el método real de tu DTInstituto
                                DTInstituto i = (DTInstituto)inst;
                    %>
                                <option value="<%= i.getNombre() %>"><%= i.getNombre() %></option>
                    <% 
                            }
                        } 
                    %>
                </select><br><br>
            </div>
            <button type="submit">Registrar</button>
        </form>
    </div>
    
    <script>
        function toggleDocente() {
            var tipo = document.getElementById("tipoUsuario").value;
            document.getElementById("divInstituto").style.display = (tipo === "docente") ? "block" : "none";
        }
    </script>
</body>
</html>