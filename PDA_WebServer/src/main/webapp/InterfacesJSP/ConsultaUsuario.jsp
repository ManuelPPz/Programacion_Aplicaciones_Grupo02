<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTDocente"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="Logica.util.ImagenUtil"%>
<%@page import="java.util.List"%>

<%
    request.setAttribute("titlePage", "Consulta de Usuario - Seleccionar");
    List<DTMaster> listaUsuarios = (List<DTMaster>) request.getAttribute("usuarios");
%>

<%-- Cargamos la plantilla base global (Barra superior y menú lateral) --%>
<%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

<div class="w-100 px-3">
    <h2 class="mb-4 fw-bold">Consulta de Usuario</h2>

    <div class="card border-dark shadow-sm p-4 bg-white">
        <h4 class="mb-3 text-secondary">Seleccione un usuario para consultar su perfil:</h4>
        
        <% if (listaUsuarios != null && !listaUsuarios.isEmpty()) { %>
            <div class="list-group">
                <% for (DTMaster m : listaUsuarios) { 
                    if (m instanceof DTUsuarioBase) {
                        DTUsuarioBase u = (DTUsuarioBase) m;
                        String imgPerfil = (u.getImg() != null && !u.getImg().trim().isEmpty()) 
                            ? ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PERFILES, u.getImg(), request.getContextPath()) 
                            : request.getContextPath() + "/Images/ImagenUserDefault.png";
                %>
                        <%-- Al hacer clic, redirige al servlet individual pasando el nickname --%>
                        <a href="${pageContext.request.contextPath}/ConsultaUsuarioIndividualServlet?nickname=<%= u.getNickname() %>" 
                           class="list-group-item list-group-item-action d-flex align-items-center gap-3 py-3">
                            <img src="<%= imgPerfil %>" alt="Avatar" width="50" height="50" class="rounded-circle border" style="object-fit: cover;">
                            <div>
                                <h5 class="mb-1 fw-bold text-dark"><%= u.getNombre() %> <%= u.getApellido() %></h5>
                                <p class="mb-0 text-muted">
                                    @<%= u.getNickname() %> - 
                                    <span class="badge bg-secondary"><%= u instanceof DTDocente ? "Docente" : "Estudiante" %></span>
                                </p>
                            </div>
                        </a>
                <% 
                    } 
                } 
                %>
            </div>
        <% } else { %>
            <div class="alert alert-info mb-0">No hay usuarios registrados en el sistema.</div>
        <% } %>
    </div>
</div>

<%-- Cierre de etiquetas abiertas en InterfazBase.jsp --%>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>