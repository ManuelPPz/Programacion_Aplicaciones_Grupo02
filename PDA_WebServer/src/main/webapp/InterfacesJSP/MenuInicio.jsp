<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Date"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 
    Aquí importas la estructura base estáticamente. 
    Esto hace que todo el HTML de la InterfazBase se pegue aquí al compilarse.
--%>
<%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

<%-- 
    Aquí se escribe ÚNICAMENTE el contenido específico y exclusivo 
    que va en el centro de tu Menú Principal 
--%>
<!-- SECCIÓN CENTRAL: NOVEDADES RECIENTES (ÚLTIMOS 30 DÍAS) -->
<!-- ESTILO PERSONALIZADO PARA EL FEED TIPO BLOG -->
<style>
    .blog-feed-container {
        max-width: 750px; /* Ancho cómodo de lectura tipo blog */
        margin: 0 auto;
    }
    .blog-card {
        transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
    }
    .blog-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
    }
</style>

<!-- CONTENEDOR PRINCIPAL DEL BLOG / FEED -->
<div class="container my-5 blog-feed-container">
    
    <!-- Cabecera del Blog -->
    <div class="text-center mb-5">
        <h2 class="fw-bold text-dark display-6">📰 Novedades de la Comunidad</h2>
        <p class="text-muted">Explora el contenido, cursos y ediciones publicados recientemente.</p>
        <hr class="w-25 mx-auto border-dark">
    </div>

    <!-- Lista de Publicaciones en formato Feed Vertical -->
    <div class="d-flex flex-column gap-4">
        <%
            List<DTMaster> novedades = (List<DTMaster>) request.getAttribute("novedadesRecientes");
            
            if (novedades != null && !novedades.isEmpty()) {
                
                // ORDENAMIENTO CRONOLÓGICO (De más reciente a más antiguo)
                Collections.sort(novedades, new Comparator<DTMaster>() {
                    @Override
                    public int compare(DTMaster o1, DTMaster o2) {
                        Date f1 = null;
                        Date f2 = null;

                        if (o1 instanceof DTCurso c1) f1 = c1.getFechaAlta();
                        else if (o1 instanceof DTEdicionCurso e1) f1 = e1.getFechaAlta();

                        if (o2 instanceof DTCurso c2) f2 = c2.getFechaAlta();
                        else if (o2 instanceof DTEdicionCurso e2) f2 = e2.getFechaAlta();

                        if (f1 == null && f2 == null) return 0;
                        if (f1 == null) return 1; // Los nulos van al final
                        if (f2 == null) return -1;
                        
                        // Para ordenar de más antiguo a más reciente, invierte f2 y f1 (f1.compareTo(f2))
                        return f2.compareTo(f1); 
                    }
                });

                SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
                for (DTMaster item : novedades) {
                    String tipo = "Publicación";
                    String badgeColor = "bg-secondary";
                    String nombre = "";
                    String descripcion = "";
                    String fechaAltaStr = "";
                    String linkConsulta = "#";
                    String imagenUrl = request.getContextPath() + "/Images/ImagenUserDefault.png";
                    String instituto = "";

                    if (item instanceof DTCurso curso) {
                        tipo = "Nuevo Curso";
                        badgeColor = "bg-primary";
                        nombre = curso.getNombre();
                        descripcion = curso.getDescripcion();
                        instituto = curso.getInstituto();
                        if (curso.getFechaAlta() != null) fechaAltaStr = sdf.format(curso.getFechaAlta());
                        linkConsulta = request.getContextPath() + "/ConsultaCursoServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                        if (curso.getImage() != null && !curso.getImage().isBlank()) {
                            imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, curso.getImage(), request.getContextPath());
                        }
                    } else if (item instanceof DTEdicionCurso edicion) {
                        tipo = "Nueva Edición";
                        badgeColor = "bg-success";
                        nombre = edicion.getNombre();
                        instituto = edicion.getInstituto();
                        descripcion = "Edición correspondiente al curso principal. Inicio previsto: " + (edicion.getFInicio() != null ? new SimpleDateFormat("dd/MM/yyyy").format(edicion.getFInicio()) : "A confirmar");
                        if (edicion.getFechaAlta() != null) fechaAltaStr = sdf.format(edicion.getFechaAlta());
                        linkConsulta = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                        if (edicion.getImage() != null && !edicion.getImage().isBlank()) {
                            imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_EDICIONCURSOS, edicion.getImage(), request.getContextPath());
                        }
                    }
        %>

        <!-- ARTÍCULO / POST DEL BLOG -->
        <div class="card blog-card border-dark shadow-sm bg-white rounded-3 overflow-hidden">
            
            <!-- Imagen de cabecera del post -->
            <div class="position-relative">
                <img src="<%= imagenUrl %>" class="w-100" alt="<%= nombre %>" style="height: 280px; object-fit: cover;">
                <span class="position-absolute top-0 start-0 m-3 badge <%= badgeColor %> fs-6 px-3 py-2 shadow"><%= tipo %></span>
            </div>

            <!-- Cuerpo del Post -->
            <div class="card-body p-4">
                <div class="d-flex justify-content-between align-items-center text-muted small mb-2">
                    <span>🏛️ <%= instituto != null ? instituto : "Institución educativa" %></span>
                    <span>📅 Publicado el <%= fechaAltaStr %></span>
                </div>

                <h3 class="card-title fw-bold text-dark mb-3"><%= nombre %></h3>
                
                <p class="card-text text-secondary mb-4" style="display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden;">
                    <%= descripcion != null && !descripcion.isBlank() ? descripcion : "Sin descripción detallada disponible para esta publicación." %>
                </p>

                <div class="text-end">
                    <a href="<%= linkConsulta %>" class="btn btn-dark px-4 fw-bold shadow-sm">
                        Leer más &rarr;
                    </a>
                </div>
            </div>

        </div>
        <% 
                }
            } else { 
        %>
        <!-- Estado Vacío -->
        <div class="card border-dark shadow-sm bg-white text-center py-5 rounded-3">
            <div class="card-body">
                <h4 class="fw-bold text-muted mb-2">No hay publicaciones recientes</h4>
                <p class="text-muted mb-0">Vuelve más tarde para enterarte de los nuevos cursos y ediciones lanzados este mes.</p>
            </div>
        </div>
        <% } %>
    </div>
</div>