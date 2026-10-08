<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modificar Datos</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</head>
<body>
    <div class="container">
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
</body>
</html>