<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Alta de Usuario</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5 mb-5" style="max-width: 650px;">
        <div class="card shadow p-4 bg-white border-dark">
            <h2 class="text-center fw-bold mb-4">Alta de Usuario</h2>
            
            <%-- Mensajes generales enviados desde el Servlet --%>
            <% if(request.getAttribute("error") != null) { %>
                <div class="alert alert-danger shadow-sm"><%= request.getAttribute("error") %></div>
            <% } %>
            <% if(request.getAttribute("exito") != null) { %>
                <div class="alert alert-success shadow-sm"><%= request.getAttribute("exito") %></div>
            <% } %>
            
            <%
                // Recuperamos los valores previos para evitar que se borren al fallar
                String nickname = request.getParameter("nickname") != null ? request.getParameter("nickname") : "";
                String nombre = request.getParameter("nombre") != null ? request.getParameter("nombre") : "";
                String apellido = request.getParameter("apellido") != null ? request.getParameter("apellido") : "";
                String correo = request.getParameter("correo") != null ? request.getParameter("correo") : "";
                String fechaNac = request.getParameter("fechaNac") != null ? request.getParameter("fechaNac") : "";
                String tipoUsuario = request.getParameter("tipoUsuario") != null ? request.getParameter("tipoUsuario") : "estudiante";
                String institutoSeleccionado = request.getParameter("instituto") != null ? request.getParameter("instituto") : "";
                
                // Verificamos si el servidor envió errores específicos de duplicados
                String errorNickServidor = (String) request.getAttribute("errorNicknameDuplicado");
                boolean tieneErrorNick = (errorNickServidor != null && !errorNickServidor.isBlank());

                String errorCorreoServidor = (String) request.getAttribute("errorCorreoDuplicado");
                boolean tieneErrorCorreo = (errorCorreoServidor != null && !errorCorreoServidor.isBlank());
            %>
            
            <form action="${pageContext.request.contextPath}/AltaUsuarioServlet" method="POST" enctype="multipart/form-data" onsubmit="return validarFormulario();" novalidate>
                
                <%-- Nickname (Con validación de espacios y duplicados) --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Nickname:</label>
                    <input type="text" id="inputNickname" name="nickname" class="form-control <%= tieneErrorNick ? "is-invalid" : "" %>" value="<%= nickname %>" required>
                    <div id="errorNicknameMsg" class="invalid-feedback">
                        <%= tieneErrorNick ? errorNickServidor : "El nickname es obligatorio y no debe contener espacios." %>
                    </div>
                </div>

                <%-- Nombre --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Nombre:</label>
                    <input type="text" id="inputNombre" name="nombre" class="form-control" value="<%= nombre %>" required>
                    <div class="invalid-feedback">El nombre es obligatorio.</div>
                </div>

                <%-- Apellido --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Apellido:</label>
                    <input type="text" id="inputApellido" name="apellido" class="form-control" value="<%= apellido %>" required>
                    <div class="invalid-feedback">El apellido es obligatorio.</div>
                </div>

                <%-- Correo Electrónico (Con validación de @ y duplicados del servidor) --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Correo Electrónico:</label>
                    <input type="text" id="inputCorreo" name="correo" class="form-control <%= tieneErrorCorreo ? "is-invalid" : "" %>" value="<%= correo %>" required>
                    <div id="errorCorreoMsg" class="invalid-feedback">
                        <%= tieneErrorCorreo ? errorCorreoServidor : "Ingrese un correo electrónico válido que contenga el símbolo '@'." %>
                    </div>
                </div>
                
                <%-- Fecha de Nacimiento --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Fecha de Nacimiento:</label>
                    <input type="date" id="inputFechaNac" name="fechaNac" class="form-control" value="<%= fechaNac %>" required>
                    <div class="invalid-feedback">Debe seleccionar su fecha de nacimiento.</div>
                </div>
                
                <%-- Contraseñas --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Contraseña:</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                    <div class="invalid-feedback">La contraseña es obligatoria.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">Confirmar Contraseña:</label>
                    <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-control" required>
                    <div class="invalid-feedback">Las contraseñas no coinciden.</div>
                </div>

                <%-- Imagen --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Imagen de Perfil:</label>
                    <input type="file" name="imagen" class="form-control" accept="image/png, image/jpeg">
                </div>
                
                <%-- Tipo de Usuario --%>
                <div class="mb-3">
                    <label class="form-label fw-bold">Tipo de Usuario:</label>
                    <select name="tipoUsuario" id="tipoUsuario" class="form-select" onchange="toggleDocente()">
                        <option value="estudiante" <%= "estudiante".equals(tipoUsuario) ? "selected" : "" %>>Estudiante</option>
                        <option value="docente" <%= "docente".equals(tipoUsuario) ? "selected" : "" %>>Docente</option>
                    </select>
                </div>
                
                <%-- Instituto (Condicional para docentes) --%>
                <div id="divInstituto" class="mb-3" style="display: <%= "docente".equals(tipoUsuario) ? "block" : "none" %>;">
                    <label class="form-label fw-bold text-danger">Instituto (Obligatorio para docentes):</label>
                    <select name="instituto" id="selectInstituto" class="form-select">
                        <option value="">Seleccione un instituto...</option>
                        <% 
                            List<DTMaster> institutos = (List<DTMaster>) request.getAttribute("institutos");
                            if (institutos != null) {
                                for (DTMaster inst : institutos ) {
                                    DTInstituto i = (DTInstituto)inst;
                                    String selected = i.getNombre().equals(institutoSeleccionado) ? "selected" : "";
                        %>
                                    <option value="<%= i.getNombre() %>" <%= selected %>><%= i.getNombre() %></option>
                        <% 
                                }
                            } 
                        %>
                    </select>
                    <div class="invalid-feedback">Debe seleccionar un instituto.</div>
                </div>

                <button type="submit" class="btn btn-dark w-100 fw-bold py-2 shadow-sm">Registrar</button>
            </form>
        </div>
    </div>
    
    <script>
        function toggleDocente() {
            var tipo = document.getElementById("tipoUsuario").value;
            var divInst = document.getElementById("divInstituto");
            divInst.style.display = (tipo === "docente") ? "block" : "none";
        }

        function validarFormulario() {
            let esValido = true;

            // Limpiamos clases de error previas
            document.querySelectorAll('.form-control, .form-select').forEach(el => el.classList.remove('is-invalid'));

            // 1. Validar campo Nickname (Obligatorio y sin espacios)
            let inputNickname = document.getElementById("inputNickname");
            let valorNick = inputNickname.value.trim();
            let errorNicknameMsg = document.getElementById("errorNicknameMsg");

            if (!valorNick) {
                errorNicknameMsg.innerText = "El nickname es obligatorio.";
                inputNickname.classList.add('is-invalid');
                esValido = false;
            } else if (/\s/.test(valorNick)) {
                errorNicknameMsg.innerText = "El nickname no puede contener espacios en blanco.";
                inputNickname.classList.add('is-invalid');
                esValido = false;
            }

            // 2. Validar campos de texto vacíos restantes
            const campos = ['inputNombre', 'inputApellido', 'inputFechaNac', 'password'];
            campos.forEach(id => {
                let elem = document.getElementById(id);
                if (!elem.value.trim()) {
                    elem.classList.add('is-invalid');
                    esValido = false;
                }
            });

            // 3. Validación de Correo (obligatorio y con '@')
            let inputCorreo = document.getElementById("inputCorreo");
            let valorCorreo = inputCorreo.value.trim();
            let errorCorreoMsg = document.getElementById("errorCorreoMsg");

            if (!valorCorreo) {
                errorCorreoMsg.innerText = "El correo electrónico es obligatorio.";
                inputCorreo.classList.add('is-invalid');
                esValido = false;
            } else if (!valorCorreo.includes('@') || valorCorreo.indexOf('@') === 0 || valorCorreo.indexOf('@') === valorCorreo.length - 1) {
                errorCorreoMsg.innerText = "El correo electrónico debe contener un símbolo '@' válido.";
                inputCorreo.classList.add('is-invalid');
                esValido = false;
            }

            // 4. Validación de Coincidencia de Contraseñas
            let pass = document.getElementById("password");
            let passConfirm = document.getElementById("passwordConfirm");
            if (pass.value !== passConfirm.value || !passConfirm.value.trim()) {
                passConfirm.classList.add('is-invalid');
                esValido = false;
            }

            // 5. Validación de Instituto para Docentes
            let tipo = document.getElementById("tipoUsuario").value;
            let instituto = document.getElementById("selectInstituto");
            if (tipo === "docente" && instituto.value === "") {
                instituto.classList.add('is-invalid');
                esValido = false;
            }

            return esValido;
        }
    </script>
</body>
</html>