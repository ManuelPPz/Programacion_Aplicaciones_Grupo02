<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    public String versionedUrl(ServletContext app, String relativePath) {
        String realPath = app.getRealPath(relativePath);
        if (realPath != null) {
            java.io.File file = new java.io.File(realPath);
            if (file.exists()) {
                return relativePath + "?v=" + file.lastModified();
            }
        }
        return relativePath;
    }
%>
<!doctype html>
<html lang="es">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>edEXT_Lab</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    
    <!-- CSS personalizado (Instanciado/Vinculado) -->
    <link rel="stylesheet" href="<%= request.getContextPath() %><%= versionedUrl(application, "/Styles/StylesContainers.css") %>">
  </head>
  <body>
    <div class="container-fluid vh-100 d-flex justify-content-center align-items-center bg-light">
        
        <div class="col-11 col-sm-8 col-md-5 col-lg-4 p-4 bg-white rounded shadow-sm border">
            
            <h3 class="text-center mb-4">Iniciar Sesión</h3>

            <%
                String errorLogin = (String) request.getAttribute("errorLogin");
                if (errorLogin != null) {
            %>
                <div class="alert alert-danger text-center py-2 mb-3" role="alert">
                    <%= errorLogin %>
                </div>
            <%
                }
            %>

            <form id="formLogin" action="MainMenu" method="POST">
                
                <!-- Correo electronico -->
                <div class="mb-3">
                    <div class="input-group">
                        <span class="input-group-text">@︎︎</span>
                        <div class="form-floating">
                          <input type="text" name="email" class="form-control" id="floatingInputGroup2" placeholder="Username">
                          <label for="floatingInputGroup2">Nickname o Correo Electrónico</label>
                        </div>
                    </div>
                    <div id="mensajeErrorEmail" class="invalid-feedback d-none">
                      El Nickname/correo electrónico no es válido
                    </div>
                </div>

                <!-- Contraseña -->
                <div class="mb-3">
                    <div class="input-group">
                        <span class="input-group-text"><i class="bi bi-lock-fill"></i></span>

                        <div class="form-floating flex-grow-1 position-relative">
                            <input type="password" name="password" class="form-control pe-5" id="inputPassword" placeholder="Contraseña">
                            <label for="inputPassword">Contraseña</label>

                            <!-- Botón del ojo -->
                            <button class="btn btn-link text-secondary position-absolute top-50 end-0 translate-middle-y me-2 p-0 border-0 shadow-none text-decoration-none" 
                                    type="button" 
                                    id="btnTogglePassword" 
                                    style="z-index: 10;">
                                <i class="bi bi-eye-slash" id="iconoOjo"></i>
                            </button>
                        </div>
                    </div>
                    <!-- El mensaje de error va fuera de la caja flex del input-group -->
                    <div id="mensajeErrorPassword" class="invalid-feedback d-none">
                        La contraseña es inválida
                    </div>
                </div>

                <!-- Boton iniciar sesion -->
                <div class="d-grid mt-4">
                    <button type="submit" class="btn btn-primary">Iniciar Sesión</button>
                </div>
                <br>
                <div class="row">
                    <div class="col-6">
                        <div class="mb-3 text-center">
                            <p class="mb-2"><a class="link-offset-2 link-underline link-underline-opacity-0" href="MainMenu">Ingresar como visitante</a></p>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="mb-3 text-center">
                            <p class="mb-2"><a class="link-offset-2 link-underline link-underline-opacity-0" href="#">Crear una cuenta</a></p>
                        </div>
                    </div>
                </div>
            </form>

        </div>
    </div>
    
    <!-- JavaScripts -->
    <script src="<%= request.getContextPath() %><%= versionedUrl(application, "/JavasScripts/ValidarIndex.js") %>"></script>
    <script src="<%= request.getContextPath() %><%= versionedUrl(application, "/JavasScripts/ShowPassword.js") %>"></script>
  </body>
</html>