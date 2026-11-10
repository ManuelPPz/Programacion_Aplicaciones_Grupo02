<%@page import="Logica.DTsClasses.DTCategoria"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTEdi_Usu"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTInstituto"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.util.ImagenUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!
    // Escapa texto para mostrarlo dentro del HTML
    private static String html(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    private static String url(String s) {
        return s == null ? "" : URLEncoder.encode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static String fecha(java.util.Date d) {
        return d == null ? "-" : new SimpleDateFormat("dd/MM/yyyy").format(d);
    }

    private static String estado(Object e) {
        if (e == null) {
            return "Inscripto";
        }
        String s = e.toString().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
%>
<%
    String ctx = request.getContextPath();
    boolean soloEstudiantes = Boolean.TRUE.equals(request.getAttribute("soloEstudiantes"));
    String filtro = (String) request.getAttribute("filtro");
    if (filtro == null) {
        filtro = "instituto";
    }
    String valor = (String) request.getAttribute("valor");
    String cursoSel = (String) request.getAttribute("cursoSeleccionado");
    List<DTMaster> institutos = (List<DTMaster>) request.getAttribute("institutos");
    List<DTMaster> categorias = (List<DTMaster>) request.getAttribute("categorias");
    List<DTMaster> cursos = (List<DTMaster>) request.getAttribute("cursos");
    DTEdicionCurso vigente = (DTEdicionCurso) request.getAttribute("edicionVigente");
    DTEdi_Usu inscripcion = (DTEdi_Usu) request.getAttribute("inscripcion");
    String urlVideoIngresada = (String) request.getAttribute("urlVideoIngresada");
    String baseFiltro = ctx + "/InscripcionEdicionServlet?filtro=" + url(filtro) + "&valor=" + url(valor);
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Inscripción a Edición de Curso</title>
    </head>
    <body>
        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>
        <div class="col-md-9 col-lg-10 ps-md-4 d-flex flex-column align-items-center mb-5">
            <div class="card border-dark shadow p-3 p-md-4 w-100" style="max-width: 850px;">
                <h2 class="card-title text-center mb-4 fw-bold fs-3">Inscripción a Edición de Curso</h2>

                <% if (request.getAttribute("error") != null) { %>
                    <div class="alert alert-danger text-center"><%= html(request.getAttribute("error")) %></div>
                <% } %>
                <% if (request.getAttribute("exito") != null) { %>
                    <div class="alert alert-success text-center"><%= html(request.getAttribute("exito")) %></div>
                <% } %>

                <% if (soloEstudiantes) { %>
                    <div class="alert alert-warning text-center mb-0">
                        Solo los estudiantes pueden inscribirse a una edición de curso.
                    </div>
                <% } else { %>

                <%-- Paso 1: instituto o categoría --%>
                <form action="<%= ctx %>/InscripcionEdicionServlet" method="GET" class="mb-4" id="formFiltro">
                    <label class="form-label fw-bold">1. Buscar cursos por</label>
                    <div class="btn-group w-100 mb-2" role="group">
                        <input type="radio" class="btn-check" name="filtro" id="filtroInstituto" value="instituto"
                               <%= "instituto".equals(filtro) ? "checked" : "" %> onchange="cambiarFiltro()">
                        <label class="btn btn-outline-dark" for="filtroInstituto">Instituto</label>
                        <input type="radio" class="btn-check" name="filtro" id="filtroCategoria" value="categoria"
                               <%= "categoria".equals(filtro) ? "checked" : "" %> onchange="cambiarFiltro()">
                        <label class="btn btn-outline-dark" for="filtroCategoria">Categoría</label>
                    </div>

                    <select name="valor" id="selInstituto" class="form-select border-dark"
                            onchange="this.form.submit()" <%= "instituto".equals(filtro) ? "" : "disabled hidden" %>>
                        <option value="" <%= valor == null ? "selected" : "" %> disabled>-- Seleccione un instituto --</option>
                        <% if (institutos != null) {
                               for (DTMaster dt : institutos) {
                                   if (dt instanceof DTInstituto) { DTInstituto i = (DTInstituto) dt; %>
                            <option value="<%= html(i.getNombre()) %>"
                                    <%= "instituto".equals(filtro) && i.getNombre().equals(valor) ? "selected" : "" %>><%= html(i.getNombre()) %></option>
                        <%         }
                               }
                           } %>
                    </select>

                    <select name="valor" id="selCategoria" class="form-select border-dark"
                            onchange="this.form.submit()" <%= "categoria".equals(filtro) ? "" : "disabled hidden" %>>
                        <option value="" <%= valor == null ? "selected" : "" %> disabled>-- Seleccione una categoría --</option>
                        <% if (categorias != null) {
                               for (DTMaster dt : categorias) {
                                   if (dt instanceof DTCategoria) { DTCategoria c = (DTCategoria) dt; %>
                            <option value="<%= html(c.getNombre()) %>"
                                    <%= "categoria".equals(filtro) && c.getNombre().equals(valor) ? "selected" : "" %>><%= html(c.getNombre()) %></option>
                        <%         }
                               }
                           } %>
                    </select>
                    <noscript><button type="submit" class="btn btn-dark mt-2">Buscar cursos</button></noscript>
                </form>

                <%-- Paso 2: cursos asociados --%>
                <% if (valor != null) { %>
                    <label class="form-label fw-bold">2. Elegí un curso</label>
                    <% if (cursos == null || cursos.isEmpty()) { %>
                        <div class="alert alert-secondary text-center">
                            No hay cursos para <%= "categoria".equals(filtro) ? "la categoría" : "el instituto" %> '<%= html(valor) %>'.
                        </div>
                    <% } else { %>
                        <div class="list-group mb-4">
                            <% for (DTMaster dt : cursos) {
                                   if (dt instanceof DTCurso) { DTCurso c = (DTCurso) dt;
                                       boolean activo = c.getNombre().equals(cursoSel); %>
                                <a href="<%= baseFiltro %>&curso=<%= url(c.getNombre()) %>"
                                   class="list-group-item list-group-item-action d-flex justify-content-between align-items-center <%= activo ? "active" : "" %>">
                                    <span class="fw-semibold text-break"><%= html(c.getNombre()) %></span>
                                    <small class="<%= activo ? "" : "text-muted" %> ms-2 text-nowrap"><%= html(c.getInstituto()) %></small>
                                </a>
                            <%     }
                               } %>
                        </div>
                    <% } %>
                <% } %>

                <%-- Paso 3: edición vigente del curso --%>
                <% if (cursoSel != null) { %>
                    <label class="form-label fw-bold">3. Edición vigente de "<%= html(cursoSel) %>"</label>
                    <% if (vigente == null) { %>
                        <div class="alert alert-secondary text-center mb-0">
                            El curso no tiene una edición vigente en este momento.
                        </div>
                    <% } else {
                           String img = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_EDICIONCURSOS, vigente.getImage(), ctx);
                           int cupo = vigente.getCupo();
                           List<String> docentes = vigente.getDocentes();
                    %>
                        <div class="card border-secondary mb-3">
                            <div class="row g-0">
                                <div class="col-sm-4 d-flex align-items-center justify-content-center p-2">
                                    <img src="<%= img %>" alt="Imagen de la edición" class="img-fluid rounded" style="max-height: 160px; object-fit: cover;">
                                </div>
                                <div class="col-sm-8">
                                    <div class="card-body">
                                        <h5 class="card-title fw-bold text-break">
                                            <a href="<%= ctx %>/ConsultaEdicionServlet?edicion=<%= url(vigente.getNombre()) %>" class="text-dark"><%= html(vigente.getNombre()) %></a>
                                        </h5>
                                        <ul class="list-unstyled mb-0 small">
                                            <li><strong>Instituto:</strong> <%= html(vigente.getInstituto()) %></li>
                                            <li><strong>Inicio:</strong> <%= fecha(vigente.getFInicio()) %> &middot; <strong>Fin:</strong> <%= fecha(vigente.getFFin()) %></li>
                                            <li><strong>Cupo:</strong> <%= cupo > 0 ? String.valueOf(cupo) : "Sin límite" %></li>
                                            <li><strong>Docentes:</strong> <%= (docentes == null || docentes.isEmpty()) ? "-" : html(String.join(", ", docentes)) %></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <form action="<%= ctx %>/InscripcionEdicionServlet" method="POST">
                            <input type="hidden" name="filtro" value="<%= html(filtro) %>">
                            <input type="hidden" name="valor" value="<%= html(valor) %>">
                            <input type="hidden" name="curso" value="<%= html(cursoSel) %>">
                            <input type="hidden" name="edicion" value="<%= html(vigente.getNombre()) %>">

                            <% if (inscripcion != null) {
                                   String videoActual = urlVideoIngresada != null ? urlVideoIngresada : inscripcion.getUrlVideo(); %>
                                <div class="alert alert-info">
                                    Ya estás inscripto a esta edición desde el <%= fecha(inscripcion.getfInscripcion()) %>
                                    (estado: <strong><%= estado(inscripcion.getEstadoIns()) %></strong>).
                                    Podés modificar los datos de tu inscripción o cancelar.
                                    <% if (inscripcion.getUrlVideo() != null) { %>
                                        <div class="mt-1 text-break">
                                            Video actual: <a href="<%= html(inscripcion.getUrlVideo()) %>" target="_blank" rel="noopener noreferrer"><%= html(inscripcion.getUrlVideo()) %></a>
                                        </div>
                                    <% } %>
                                </div>
                                <input type="hidden" name="accion" value="modificar">
                                <div class="mb-3">
                                    <label for="urlVideo" class="form-label fw-bold">Video de motivación (opcional)</label>
                                    <input type="url" class="form-control border-dark" id="urlVideo" name="urlVideo" maxlength="500"
                                           placeholder="https://www.youtube.com/watch?v=..." value="<%= html(videoActual) %>">
                                    <div class="form-text">Dejalo vacío para quitar el video.</div>
                                </div>
                                <div class="d-flex flex-column flex-sm-row gap-2">
                                    <button type="submit" class="btn btn-primary fw-bold flex-fill">Guardar cambios</button>
                                    <a href="<%= ctx %>/MainMenu" class="btn btn-outline-secondary flex-fill">Cancelar</a>
                                </div>
                            <% } else { %>
                                <input type="hidden" name="accion" value="inscribir">
                                <div class="mb-3">
                                    <label for="urlVideo" class="form-label fw-bold">Video de motivación (opcional)</label>
                                    <input type="url" class="form-control border-dark" id="urlVideo" name="urlVideo" maxlength="500"
                                           placeholder="https://www.youtube.com/watch?v=..." value="<%= html(urlVideoIngresada) %>">
                                    <div class="form-text">URL de un video donde cuentes por qué querés hacer el curso.</div>
                                </div>
                                <div class="d-flex flex-column flex-sm-row gap-2">
                                    <button type="submit" class="btn btn-primary fw-bold flex-fill">Inscribirme</button>
                                    <a href="<%= ctx %>/MainMenu" class="btn btn-outline-secondary flex-fill">Cancelar</a>
                                </div>
                            <% } %>
                        </form>
                    <% } %>
                <% } %>
                <% } %>
            </div>
        </div>

        <script>
            // Muestra el selector del filtro elegido (instituto o categoría) y oculta el otro.
            function cambiarFiltro() {
                var porCategoria = document.getElementById("filtroCategoria").checked;
                var selI = document.getElementById("selInstituto");
                var selC = document.getElementById("selCategoria");
                selI.hidden = selI.disabled = porCategoria;
                selC.hidden = selC.disabled = !porCategoria;
                (porCategoria ? selC : selI).selectedIndex = 0;
            }
        </script>
    </body>
</html>
