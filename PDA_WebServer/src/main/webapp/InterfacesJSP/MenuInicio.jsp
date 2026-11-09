<%@page import="Logica.DTsClasses.DTCurso"%>
<%@page import="Logica.DTsClasses.DTEdicionCurso"%>
<%@page import="Logica.DTsClasses.DTMaster"%>
<%@page import="Logica.DTsClasses.DTUsuarioBase"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="Logica.util.ImagenUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!
    // Método auxiliar compartido para extraer la fecha de alta de cualquier tipo de objeto de forma dinámica
    // (usado por la vista MÓVIL, sin cambios)
    private Date obtenerFechaAlta(Object obj) {
        if (obj == null) return null;
        String[] metodosFecha = {"getFechaAlta", "getF Alta", "getAlta", "getFecha", "getFInicio"};
        for (String mName : metodosFecha) {
            try {
                java.lang.reflect.Method m = obj.getClass().getMethod(mName);
                Object res = m.invoke(obj);
                if (res instanceof Date) {
                    return (Date) res;
                }
            } catch (Exception e) {}
        }
        return null;
    }
%>

<%!
    // ===== MÉTODOS AUXILIARES EXCLUSIVOS DE LA VISTA DE ESCRITORIO =====

    // Consulta en tiempo real si el usuario logueado sigue al usuario objetivo
    private boolean usuarioSigueA(String nickLogueado, String nickTarget) {
        if (nickLogueado == null || nickTarget == null || nickLogueado.isBlank() || nickTarget.isBlank()) return false;
        try {
            String[] clasesManejador = {
                "Logica.Manejadores.ManejadorUsuario",
                "Logica.Manejadores.ManejadorUsuarios"
            };
            for (String nomClase : clasesManejador) {
                try {
                    Class<?> clazz = Class.forName(nomClase);
                    Object inst = clazz.getMethod("GetInstance").invoke(null);
                    Object userObj = null;
                    for (String mName : new String[]{"BuscarUsuario", "buscarUsuario", "obtenerUsuario", "getUsuario"}) {
                        try {
                            userObj = clazz.getMethod(mName, String.class).invoke(inst, nickLogueado);
                            if (userObj != null) break;
                        } catch (Exception ex) {}
                    }
                    if (userObj != null) {
                        for (String mSeguidos : new String[]{"getSeguidos", "getListaSeguidos", "getNicksSeguidos"}) {
                            try {
                                Object listObj = userObj.getClass().getMethod(mSeguidos).invoke(userObj);
                                if (listObj instanceof List<?>) {
                                    for (Object item : (List<?>) listObj) {
                                        if (item instanceof String && ((String) item).equalsIgnoreCase(nickTarget)) return true;
                                        if (item != null) {
                                            try {
                                                Object n = item.getClass().getMethod("getNickname").invoke(item);
                                                if (n != null && n.toString().equalsIgnoreCase(nickTarget)) return true;
                                            } catch (Exception ex) {}
                                        }
                                    }
                                }
                            } catch (Exception ex) {}
                        }
                    }
                } catch (Exception e) {}
            }
        } catch (Exception e) {}
        return false;
    }

    // Obtiene la fecha de alta de Cursos, Ediciones y Programas (versión de escritorio)
    private Date obtenerFechaAltaEscritorio(Object item) {
        if (item == null) return null;
        try {
            if (item instanceof DTCurso) {
                return ((DTCurso) item).getFechaAlta();
            }
            if (item instanceof DTEdicionCurso) {
                return ((DTEdicionCurso) item).getFechaAlta();
            }

            for (String mFecha : new String[]{"getFechaAlta", "getFechaIngresado", "getFecha", "getFechaCreacion", "getFechaInicio"}) {
                try {
                    Object f = item.getClass().getMethod(mFecha).invoke(item);
                    if (f instanceof Date) return (Date) f;
                } catch (Exception ex) {}
            }
        } catch (Exception e) {}
        return null;
    }
%>

<%
    // Detección de dispositivo móvil por User-Agent
    String ua = request.getHeader("User-Agent");
    boolean esMovil = ua != null && ua.matches("(?i).*(android|iphone|ipad|ipod|mobile|blackberry|opera mini).*");

    if (esMovil) {
        // ===================================================================
        // VISTA MÓVIL PURA (Inicio con Novedades integradas y diseño unificado)
        // ===================================================================
        DTMaster dtmUserLogueado = (DTMaster) session.getAttribute("usuarioLogueado");
        boolean esVisitante = (dtmUserLogueado == null);
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Inicio - Móvil</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
        <style>
            .mobile-header {
                background-color: #f8f9fa;
                border-bottom: 1px solid #e9ecef;
            }
            .mobile-menu-link {
                color: #6c757d;
                text-decoration: none;
                font-weight: 500;
                display: block;
                padding: 8px 0;
            }
            .mobile-menu-link:hover {
                color: #212529;
            }
            .btn-hamburger-clean {
                color: #212529 !important;
                border: none !important;
                background: transparent !important;
            }
        </style>
    </head>
    <body class="bg-light">

        <!-- Header Superior Móvil -->
        <div class="mobile-header p-3 mb-3 shadow-sm">
            <div class="d-flex align-items-center justify-content-between">
                <button class="btn btn-hamburger-clean p-1 shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#mobileMenu" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="bi bi-list fs-2"></i>
                </button>
                <img src="<%= request.getContextPath() %>/Images/LogoEdext.png" alt="edEXT Logo" style="height: 35px; width: auto;" onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
            </div>
            
            <!-- Menú Desplegable -->
            <div class="collapse pt-3 ps-1 border-top mt-2" id="mobileMenu">
                <ul class="list-unstyled mb-0">
                    <li><a href="<%= request.getContextPath() %>/MainMenu" class="mobile-menu-link text-dark fw-semibold"><i class="bi bi-house-door me-2"></i>Inicio</a></li>
                    <li><a href="<%= request.getContextPath() %>/ConsultaCursoServlet" class="mobile-menu-link text-dark fw-semibold"><i class="bi bi-book me-2"></i>Ver Cursos</a></li>
                    <li><a href="<%= request.getContextPath() %>/ConsultaEdicionServlet" class="mobile-menu-link text-dark fw-semibold"><i class="bi bi-calendar-event me-2"></i>Ver Ediciones</a></li>
                    
                    <li class="border-top mt-2 pt-2">
                        <% if (esVisitante) { %>
                            <a href="<%= request.getContextPath() %>/LoginServlet" class="mobile-menu-link text-success fw-semibold"><i class="bi bi-box-arrow-in-right me-2"></i>Iniciar Sesión</a>
                        <% } else { %>
                            <a href="<%= request.getContextPath() %>/CerrarSesionServlet" class="mobile-menu-link text-danger fw-semibold"><i class="bi bi-box-arrow-right me-2"></i>Salir</a>
                        <% } %>
                    </li>
                </ul>
            </div>
        </div>

        <!-- Contenido Móvil: Novedades de la Comunidad -->
        <div class="container px-3 pb-5">
            <div class="card border-dark shadow-sm bg-white rounded-3 p-3">
                
                <div class="text-center mb-3">
                    <h4 class="fw-bold text-dark mb-1">📰 Novedades de la Comunidad</h4>
                    <p class="text-muted small mb-2">Explora contenido, cursos, ediciones y programas publicados recientemente.</p>
                    <hr class="w-50 mx-auto border-dark mt-2 mb-3">
                </div>

                <div class="d-flex flex-column gap-3">
                    <%
                        List<Object> novedades = new ArrayList<>();
                        List<?> novedadesTemp = (List<?>) request.getAttribute("novedadesRecientes");
                        if (novedadesTemp != null) {
                            novedades.addAll(novedadesTemp);
                        }

                        boolean tieneProgramas = false;
                        for (Object obj : novedades) {
                            if (obj != null && (obj.getClass().getSimpleName().toLowerCase().contains("programa") || obj.getClass().getName().toLowerCase().contains("programa"))) {
                                tieneProgramas = true;
                                break;
                            }
                        }

                        if (!tieneProgramas) {
                            try {
                                List<?> progsList = null;
                                Object manejadorInst = null;
                                Class<?> manejadorClass = null;

                                String[] clasesManejadorProg = {
                                    "Logica.Manejadores.ManejadorPrograma",
                                    "Logica.Manejadores.ManejadorProgramaFormacion",
                                    "Logica.Manejadores.ManejadorProgramas"
                                };

                                for (String nomClase : clasesManejadorProg) {
                                    try {
                                        manejadorClass = Class.forName(nomClase);
                                        for (String mInst : new String[]{"GetInstance", "getInstance", "getInstancia"}) {
                                            try {
                                                manejadorInst = manejadorClass.getMethod(mInst).invoke(null);
                                                if (manejadorInst != null) break;
                                            } catch (Exception ex) {}
                                        }
                                        if (manejadorInst != null) {
                                            for (String mList : new String[]{"getDTList", "getProgramas", "getProgramasFormacion", "getListaProgramas", "obtenerProgramas", "getColeccionProgramas", "listarProgramas"}) {
                                                try {
                                                    Object res = manejadorClass.getMethod(mList).invoke(manejadorInst);
                                                    if (res instanceof List<?>) {
                                                        progsList = (List<?>) res;
                                                        if (progsList != null && !progsList.isEmpty()) break;
                                                    }
                                                } catch (Exception ex) {}
                                            }
                                        }
                                        if (progsList != null && !progsList.isEmpty()) break;
                                    } catch (Exception e) {}
                                }

                                if (progsList != null && !progsList.isEmpty()) {
                                    for (Object pObj : progsList) {
                                        if (pObj == null) continue;
                                        if (pObj instanceof String) {
                                            String progNombre = (String) pObj;
                                            Object dtProg = null;
                                            if (manejadorInst != null && manejadorClass != null) {
                                                for (String mObt : new String[]{"buscarPrograma", "obtenerPrograma", "getPrograma", "obtenerDTPrograma", "getDTPrograma", "buscarProgramaFormacion"}) {
                                                    try {
                                                        dtProg = manejadorClass.getMethod(mObt, String.class).invoke(manejadorInst, progNombre);
                                                        if (dtProg != null) break;
                                                    } catch (Exception ex) {}
                                                }
                                            }
                                            if (dtProg != null) {
                                                novedades.add(dtProg);
                                            }
                                        } else {
                                            novedades.add(pObj);
                                        }
                                    }
                                }
                            } catch (Exception e) {}
                        }

                        if (!novedades.isEmpty()) {
                            Collections.sort(novedades, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    Date f1 = obtenerFechaAlta(o1);
                                    Date f2 = obtenerFechaAlta(o2);
                                    if (f1 == null && f2 == null) return 0;
                                    if (f1 == null) return 1;
                                    if (f2 == null) return -1;
                                    return f2.compareTo(f1); 
                                }
                            });

                            SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
                            for (Object item : novedades) {
                                if (item == null) continue;

                                String tipo = "Publicación";
                                String badgeColor = "bg-secondary";
                                String nombre = "";
                                String descripcion = "";
                                String fechaAltaStr = "";
                                String linkConsulta = "#";
                                String imagenUrl = request.getContextPath() + "/Images/ImagenUserDefault.png";
                                String instituto = "";

                                if (item instanceof DTCurso) {
                                    DTCurso curso = (DTCurso) item;
                                    tipo = "Nuevo Curso";
                                    badgeColor = "bg-primary text-white";
                                    nombre = curso.getNombre();
                                    descripcion = curso.getDescripcion();
                                    instituto = curso.getInstituto();
                                    if (curso.getFechaAlta() != null) fechaAltaStr = sdf.format(curso.getFechaAlta());
                                    linkConsulta = request.getContextPath() + "/ConsultaCursoServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                    if (curso.getImage() != null && !curso.getImage().isBlank()) {
                                        imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, curso.getImage(), request.getContextPath());
                                    }
                                } else if (item instanceof DTEdicionCurso) {
                                    DTEdicionCurso edicion = (DTEdicionCurso) item;
                                    tipo = "Nueva Edición";
                                    badgeColor = "bg-success text-white";
                                    nombre = edicion.getNombre();
                                    instituto = edicion.getInstituto();
                                    descripcion = "Edición correspondiente al curso. Inicio: " + (edicion.getFInicio() != null ? new SimpleDateFormat("dd/MM/yyyy").format(edicion.getFInicio()) : "A confirmar");
                                    if (edicion.getFechaAlta() != null) fechaAltaStr = sdf.format(edicion.getFechaAlta());
                                    linkConsulta = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                    if (edicion.getImage() != null && !edicion.getImage().isBlank()) {
                                        imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_EDICIONCURSOS, edicion.getImage(), request.getContextPath());
                                    }
                                } else if (item.getClass().getSimpleName().toLowerCase().contains("programa") || item.getClass().getName().toLowerCase().contains("programa")) {
                                    tipo = "Programa de Formación";
                                    badgeColor = "bg-info text-dark";
                                    instituto = "Programa Formativo";
                                    
                                    for (String mNom : new String[]{"getNombre", "getNombrePrograma", "getNomPrograma", "getNom"}) {
                                        try {
                                            Object nomObj = item.getClass().getMethod(mNom).invoke(item);
                                            if (nomObj != null && !nomObj.toString().isBlank()) {
                                                nombre = nomObj.toString();
                                                break;
                                            }
                                        } catch (Exception ex) {}
                                    }

                                    for (String mDesc : new String[]{"getDescripcion", "getDesc", "getResumen"}) {
                                        try {
                                            Object descObj = item.getClass().getMethod(mDesc).invoke(item);
                                            if (descObj != null && !descObj.toString().isBlank()) {
                                                descripcion = descObj.toString();
                                                break;
                                            }
                                        } catch (Exception ex) {}
                                    }

                                    Date fAltaProg = obtenerFechaAlta(item);
                                    if (fAltaProg != null) fechaAltaStr = sdf.format(fAltaProg);

                                    if (nombre != null && !nombre.isBlank()) {
                                        linkConsulta = request.getContextPath() + "/ConsultaProgramaServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                    } else {
                                        linkConsulta = request.getContextPath() + "/ConsultaProgramaServlet";
                                    }

                                    String imgProg = "";
                                    for (String mImg : new String[]{"getImage", "getImg", "getImagen", "getFoto", "getNomImagen"}) {
                                        try {
                                            Object imgObj = item.getClass().getMethod(mImg).invoke(item);
                                            if (imgObj != null && !imgObj.toString().isBlank()) {
                                                imgProg = imgObj.toString();
                                                break;
                                            }
                                        } catch (Exception ex) {}
                                    }

                                    if (!imgProg.isBlank()) {
                                        try {
                                            imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PROGRAMAS, imgProg, request.getContextPath());
                                        } catch (Exception ex) {
                                            imagenUrl = request.getContextPath() + "/Images/" + imgProg;
                                        }
                                    }
                                }
                    %>
                                <div class="card border border-dark rounded-2 p-2 bg-light shadow-sm">
                                    <div class="d-flex align-items-center gap-2 mb-2">
                                        <img src="<%= imagenUrl %>" alt="Imagen" width="40" height="40" class="rounded border border-dark flex-shrink-0" style="object-fit: cover;" onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
                                        <div class="text-truncate">
                                            <span class="badge <%= badgeColor %>" style="font-size: 0.65rem;"><%= tipo %></span>
                                            <h6 class="fw-bold text-dark mb-0 text-truncate small"><%= nombre %></h6>
                                        </div>
                                    </div>
                                    <% if (!instituto.isBlank()) { %>
                                        <p class="text-muted mb-1" style="font-size: 0.7rem;"><strong>Instituto/Tipo:</strong> <%= instituto %></p>
                                    <% } %>
                                    <p class="text-secondary small mb-2 text-break" style="font-size: 0.75rem; line-height: 1.2; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                                        <%= descripcion != null && !descripcion.isBlank() ? descripcion : "Sin descripción disponible." %>
                                    </p>
                                    <div class="d-flex justify-content-between align-items-center pt-1 border-top">
                                        <small class="text-muted" style="font-size: 0.65rem;"><%= fechaAltaStr %></small>
                                        <a href="<%= linkConsulta %>" class="btn btn-outline-dark btn-sm py-0 px-2 fw-bold" style="font-size: 0.7rem;">Ver Detalle</a>
                                    </div>
                                </div>
                    <%
                            }
                        } else {
                    %>
                        <div class="alert alert-secondary text-center small py-3 mb-0" role="alert">
                            No hay novedades recientes registradas en la comunidad.
                        </div>
                    <% } %>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

<%
    } else {
        // ===================================================================
        // VISTA DE ESCRITORIO (NUEVO DISEÑO: FEED DE NOVEDADES + A QUIÉN SEGUIR)
        // ===================================================================
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inicio - edEXT</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Styles/StylesContainers.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="${pageContext.request.contextPath}/js/main.js"></script>

        <style>
            .blog-feed-container {
                max-width: 100%;
                background-color: transparent !important;
                border: none !important;
                box-shadow: none !important;
            }
            .blog-card {
                transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
            }
            .blog-card:hover {
                transform: translateY(-3px);
                box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
            }
            .sidebar-sticky {
                position: -webkit-sticky;
                position: sticky;
                top: 20px;
            }
        </style>

        <script>
            // Función interactiva AJAX para cambiar estado de seguimiento sin recargar
            function toggleSeguir(btn, nickname, contextPath) {
                btn.disabled = true;
                const isFollowing = btn.getAttribute('data-following') === 'true';
                const servletUrl = isFollowing ? (contextPath + '/DejarDeSeguirServlet') : (contextPath + '/SeguirUserServlet');

                const params = new URLSearchParams();
                params.append('nickname', nickname);

                fetch(servletUrl, {
                    method: 'POST',
                    credentials: 'same-origin',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: params
                })
                .then(response => {
                    if (isFollowing) {
                        btn.setAttribute('data-following', 'false');
                        btn.innerText = 'Seguir';
                        btn.style.backgroundColor = 'transparent';
                        btn.style.color = '#0d6efd';
                        btn.style.borderColor = '#0d6efd';
                    } else {
                        btn.setAttribute('data-following', 'true');
                        btn.innerText = 'Siguiendo';
                        btn.style.backgroundColor = '#6c757d';
                        btn.style.color = '#ffffff';
                        btn.style.borderColor = '#6c757d';
                    }
                    btn.disabled = false;
                })
                .catch(err => {
                    console.error('Error al procesar la acción:', err);
                    btn.disabled = false;
                });
            }
        </script>
    </head>
    <body class="bg-light">

        <%@ include file="/InterfacesJSP/InterfazBase.jsp" %>

<!-- CONTENEDOR PRINCIPAL CON 2 COLUMNAS INDEPENDIENTES EN EL FONDO GRIS -->
<div class="container my-4 blog-feed-container">
    <div class="row g-4">
        
        <!-- COLUMNA IZQUIERDA: BLOQUE BLANCO UNIFICADO DE NOVEDADES -->
        <div class="col-lg-8">
            <div class="card border-dark shadow-sm bg-white rounded-3 p-4">
                
                <!-- Cabecera de Novedades -->
                <div class="text-center mb-4">
                    <h2 class="fw-bold text-dark display-6 mb-2">📰 Novedades de la Comunidad</h2>
                    <p class="text-muted">Explora el contenido, cursos, ediciones y programas de formación publicados recientemente.</p>
                    <hr class="w-25 mx-auto border-dark mt-3 mb-4">
                </div>

                <!-- Feed de Publicaciones dentro del Bloque Blanco -->
                <div class="d-flex flex-column gap-4">
                    <%
                        List<Object> novedades = new ArrayList<>();
                        List<?> novedadesTemp = (List<?>) request.getAttribute("novedadesRecientes");
                        if (novedadesTemp != null) {
                            novedades.addAll(novedadesTemp);
                        }

                        // Verificación: Si no hay programas en la lista recibida por la request, los buscamos automáticamente
                        boolean tieneProgramas = false;
                        for (Object obj : novedades) {
                            if (obj != null && (obj.getClass().getSimpleName().toLowerCase().contains("programa") || obj.getClass().getName().toLowerCase().contains("programa"))) {
                                tieneProgramas = true;
                                break;
                            }
                        }

                        // Si el Servlet no envió programas de formación, los recuperamos dinámicamente desde el Backend
                        if (!tieneProgramas) {
                            try {
                                List<?> progsList = null;
                                Object manejadorInst = null;
                                Class<?> manejadorClass = null;

                                String[] clasesManejadorProg = {
                                    "Logica.Manejadores.ManejadorPrograma",
                                    "Logica.Manejadores.ManejadorProgramaFormacion",
                                    "Logica.Manejadores.ManejadorProgramas"
                                };

                                for (String nomClase : clasesManejadorProg) {
                                    try {
                                        manejadorClass = Class.forName(nomClase);
                                        for (String mInst : new String[]{"GetInstance", "getInstance", "getInstancia"}) {
                                            try {
                                                manejadorInst = manejadorClass.getMethod(mInst).invoke(null);
                                                if (manejadorInst != null) break;
                                            } catch (Exception ex) {}
                                        }
                                        if (manejadorInst != null) {
                                            for (String mList : new String[]{"getDTList", "getProgramas", "getProgramasFormacion", "getListaProgramas", "obtenerProgramas", "getColeccionProgramas", "listarProgramas"}) {
                                                try {
                                                    Object res = manejadorClass.getMethod(mList).invoke(manejadorInst);
                                                    if (res instanceof List<?>) {
                                                        progsList = (List<?>) res;
                                                        if (progsList != null && !progsList.isEmpty()) break;
                                                    }
                                                } catch (Exception ex) {}
                                            }
                                        }
                                        if (progsList != null && !progsList.isEmpty()) break;
                                    } catch (Exception e) {}
                                }

                                if (progsList != null && !progsList.isEmpty()) {
                                    for (Object pObj : progsList) {
                                        if (pObj == null) continue;
                                        
                                        if (pObj instanceof String) {
                                            String progNombre = (String) pObj;
                                            Object dtProg = null;
                                            if (manejadorInst != null && manejadorClass != null) {
                                                for (String mObt : new String[]{"buscarPrograma", "obtenerPrograma", "getPrograma", "obtenerDTPrograma", "getDTPrograma", "buscarProgramaFormacion"}) {
                                                    try {
                                                        dtProg = manejadorClass.getMethod(mObt, String.class).invoke(manejadorInst, progNombre);
                                                        if (dtProg != null) break;
                                                    } catch (Exception ex) {}
                                                }
                                            }
                                            if (dtProg != null) {
                                                novedades.add(dtProg);
                                            }
                                        } else {
                                            novedades.add(pObj);
                                        }
                                    }
                                }
                            } catch (Exception e) {}
                        }

                        if (!novedades.isEmpty()) {
                            
                            // ORDENAMIENTO CRONOLÓGICO (De más reciente a más antiguo)
                            Collections.sort(novedades, new Comparator<Object>() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    Date f1 = obtenerFechaAltaEscritorio(o1);
                                    Date f2 = obtenerFechaAltaEscritorio(o2);

                                    if (f1 == null && f2 == null) return 0;
                                    if (f1 == null) return 1;
                                    if (f2 == null) return -1;
                                    
                                    return f2.compareTo(f1); 
                                }
                            });

                            SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
                            for (Object item : novedades) {
                                if (item == null) continue;

                                String tipo = "Publicación";
                                String badgeColor = "bg-secondary";
                                String nombre = "";
                                String descripcion = "";
                                String fechaAltaStr = "";
                                String linkConsulta = "#";
                                String imagenUrl = request.getContextPath() + "/Images/ImagenUserDefault.png";
                                String instituto = "";

                                if (item instanceof DTCurso) {
                                    DTCurso curso = (DTCurso) item;
                                    tipo = "Nuevo Curso";
                                    badgeColor = "bg-primary text-white";
                                    nombre = curso.getNombre();
                                    descripcion = curso.getDescripcion();
                                    instituto = curso.getInstituto();
                                    if (curso.getFechaAlta() != null) fechaAltaStr = sdf.format(curso.getFechaAlta());
                                    linkConsulta = request.getContextPath() + "/ConsultaCursoServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                    if (curso.getImage() != null && !curso.getImage().isBlank()) {
                                        imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_CURSOS, curso.getImage(), request.getContextPath());
                                    }
                                } else if (item instanceof DTEdicionCurso) {
                                    DTEdicionCurso edicion = (DTEdicionCurso) item;
                                    tipo = "Nueva Edición";
                                    badgeColor = "bg-success text-white";
                                    nombre = edicion.getNombre();
                                    instituto = edicion.getInstituto();
                                    descripcion = "Edición correspondiente al curso principal. Inicio previsto: " + (edicion.getFInicio() != null ? new SimpleDateFormat("dd/MM/yyyy").format(edicion.getFInicio()) : "A confirmar");
                                    if (edicion.getFechaAlta() != null) fechaAltaStr = sdf.format(edicion.getFechaAlta());
                                    linkConsulta = request.getContextPath() + "/ConsultaEdicionServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                    if (edicion.getImage() != null && !edicion.getImage().isBlank()) {
                                        imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_EDICIONCURSOS, edicion.getImage(), request.getContextPath());
                                    }
                                } else if (item.getClass().getSimpleName().toLowerCase().contains("programa") || item.getClass().getName().toLowerCase().contains("programa")) {
                                    tipo = "Programa de Formación";
                                    badgeColor = "bg-info text-dark";
                                    instituto = "Programa Formativo";
                                    
                                    // Obtención flexible del nombre del programa por reflexión
                                    for (String mNom : new String[]{"getNombre", "getNombrePrograma", "getNomPrograma", "getNom"}) {
                                        try {
                                            Object nomObj = item.getClass().getMethod(mNom).invoke(item);
                                            if (nomObj != null && !nomObj.toString().isBlank()) {
                                                nombre = nomObj.toString();
                                                break;
                                            }
                                        } catch (Exception ex) {}
                                    }

                                    // Obtención flexible de la descripción
                                    for (String mDesc : new String[]{"getDescripcion", "getDesc", "getResumen"}) {
                                        try {
                                            Object descObj = item.getClass().getMethod(mDesc).invoke(item);
                                            if (descObj != null && !descObj.toString().isBlank()) {
                                                descripcion = descObj.toString();
                                                break;
                                            }
                                        } catch (Exception ex) {}
                                    }

                                    Date fAltaProg = obtenerFechaAltaEscritorio(item);
                                    if (fAltaProg != null) fechaAltaStr = sdf.format(fAltaProg);

                                    // Redirección al servlet mapeado: /ConsultaProgramaServlet
                                    if (nombre != null && !nombre.isBlank()) {
                                        linkConsulta = request.getContextPath() + "/ConsultaProgramaServlet?nombre=" + java.net.URLEncoder.encode(nombre, "UTF-8");
                                    } else {
                                        linkConsulta = request.getContextPath() + "/ConsultaProgramaServlet";
                                    }

                                    String imgProg = "";
                                    for (String mImg : new String[]{"getImage", "getImg", "getImagen", "getFoto", "getNomImagen"}) {
                                        try {
                                            Object imgObj = item.getClass().getMethod(mImg).invoke(item);
                                            if (imgObj != null && !imgObj.toString().isBlank()) {
                                                imgProg = imgObj.toString();
                                                break;
                                            }
                                        } catch (Exception ex) {}
                                    }

                                    if (!imgProg.isBlank()) {
                                        try {
                                            imagenUrl = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PROGRAMAS, imgProg, request.getContextPath());
                                        } catch (Exception ex) {
                                            imagenUrl = request.getContextPath() + "/Images/" + imgProg;
                                        }
                                    }
                                }
                    %>

                    <!-- ARTÍCULO / POST DEL BLOG -->
                    <div class="card blog-card border-dark shadow-sm bg-white rounded-3 overflow-hidden">
                        
                        <div class="position-relative">
                            <img src="<%= imagenUrl %>" class="w-100" alt="<%= nombre %>" style="height: 280px; object-fit: cover;" onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
                            <span class="position-absolute top-0 start-0 m-3 badge <%= badgeColor %> fs-6 px-3 py-2 shadow"><%= tipo %></span>
                        </div>

                        <div class="card-body p-4">
                            <div class="d-flex justify-content-between align-items-center text-muted small mb-2">
                                <span>🏛️ <%= instituto != null && !instituto.isBlank() ? instituto : "Institución educativa" %></span>
                                <span>📅 Publicado <%= fechaAltaStr.isBlank() ? "" : "el " + fechaAltaStr %></span>
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
                    <div class="text-center py-5">
                        <h4 class="fw-bold text-muted mb-2">No hay publicaciones recientes</h4>
                        <p class="text-muted mb-0">Vuelve más tarde para enterarte de los nuevos cursos, ediciones y programas lanzados este mes.</p>
                    </div>
                    <% } %>
                </div>

            </div>
        </div>

        <!-- COLUMNA DERECHA: BLOQUE BLANCO INDEPENDIENTE DE "A QUIÉN SEGUIR" -->
        <div class="col-lg-4">
            <div class="sidebar-sticky">
                
                <div class="card border-dark shadow-sm rounded-3 bg-white p-3">
                    <div class="d-flex align-items-center justify-content-between mb-3 border-bottom pb-2">
                        <h6 class="fw-bold text-dark mb-0">👥 A quién seguir</h6>
                        <span class="badge bg-primary rounded-pill" style="font-size: 0.7rem;">Sugerencias</span>
                    </div>

                    <div style="display: flex; flex-direction: column; gap: 4px;">
                        <%
                            DTMaster dtmUserLogueado = (DTMaster) session.getAttribute("usuarioLogueado");
                            String nickLogueadoStr = "";
                            if (dtmUserLogueado instanceof DTUsuarioBase) {
                                nickLogueadoStr = ((DTUsuarioBase) dtmUserLogueado).getNickname();
                            }

                            List<DTUsuarioBase> sugerenciasLista = new ArrayList<>();
                            List<?> usuariosRequest = (List<?>) request.getAttribute("sugerenciasUsuarios");
                            if (usuariosRequest == null) {
                                usuariosRequest = (List<?>) request.getAttribute("usuarios");
                            }

                            if (usuariosRequest == null) {
                                String[] clasesManejador = {
                                    "Logica.Manejadores.ManejadorUsuario",
                                    "Logica.Manejadores.ManejadorUsuarios"
                                };
                                for (String nomClase : clasesManejador) {
                                    try {
                                        Class<?> clazz = Class.forName(nomClase);
                                        Object inst = clazz.getMethod("GetInstance").invoke(null);
                                        usuariosRequest = (List<?>) clazz.getMethod("getDTList").invoke(inst);
                                        if (usuariosRequest != null) break;
                                    } catch (Exception e) {}
                                }
                            }

                            if (usuariosRequest != null) {
                                for (Object obj : usuariosRequest) {
                                    if (obj instanceof DTUsuarioBase) {
                                        DTUsuarioBase u = (DTUsuarioBase) obj;
                                        if (!u.getNickname().equals(nickLogueadoStr)) {
                                            sugerenciasLista.add(u);
                                        }
                                    }
                                }
                                Collections.shuffle(sugerenciasLista);
                                if (sugerenciasLista.size() > 5) {
                                    sugerenciasLista = sugerenciasLista.subList(0, 5);
                                }
                            }

                            if (sugerenciasLista != null && !sugerenciasLista.isEmpty()) {
                                for (DTUsuarioBase uSug : sugerenciasLista) {
                                    String imgAvatar = request.getContextPath() + "/Images/ImagenUserDefault.png";
                                    if (uSug.getImg() != null && !uSug.getImg().isBlank()) {
                                        imgAvatar = ImagenUtil.versionedUrl(ImagenUtil.CARPETA_PERFILES, uSug.getImg(), request.getContextPath());
                                    }
                                    String perfilUrl = request.getContextPath() + "/ConsultaUsuarioIndividualServlet?nickname=" + java.net.URLEncoder.encode(uSug.getNickname(), "UTF-8");
                                    
                                    String nick = (uSug.getNickname() != null) ? uSug.getNickname() : "";
                                    String nom = (uSug.getNombre() != null) ? uSug.getNombre() : "";
                                    String ape = (uSug.getApellido() != null) ? uSug.getApellido() : "";
                                    String nombreCompleto = (nom + " " + ape).trim();
                                    if (nombreCompleto.isEmpty()) {
                                        nombreCompleto = nick;
                                    }

                                    boolean yaLoSigue = usuarioSigueA(nickLogueadoStr, nick);
                                    String btnTexto = yaLoSigue ? "Siguiendo" : "Seguir";
                                    String dataFollowing = yaLoSigue ? "true" : "false";
                                    String initialBg = yaLoSigue ? "#6c757d" : "transparent";
                                    String initialColor = yaLoSigue ? "#ffffff" : "#0d6efd";
                                    String initialBorder = yaLoSigue ? "#6c757d" : "#0d6efd";
                        %>
                        
                        <!-- FILA DE USUARIO -->
                        <div style="display: grid; grid-template-columns: 36px minmax(0, 1fr) auto; column-gap: 10px; align-items: center; padding: 8px 0; border-bottom: 1px solid #f0f0f0;">
                            
                            <!-- COLUMNA 1: FOTO DE PERFIL (36px) -->
                            <img src="<%= imgAvatar %>" 
                                 alt="<%= nick %>" 
                                 style="width: 36px; height: 36px; border-radius: 50%; object-fit: cover; border: 1px solid #dee2e6; display: block;" 
                                 onerror="this.onerror=null; this.src='<%= request.getContextPath() %>/Images/ImagenUserDefault.png';">
                            
                            <!-- COLUMNA 2: NOMBRE Y NICKNAME -->
                            <div style="overflow: hidden; line-height: 1.2;">
                                <a href="<%= perfilUrl %>" class="fw-bold text-dark text-decoration-none" style="font-size: 0.85rem; display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                    <%= nombreCompleto %>
                                </a>
                                <small class="text-muted" style="font-size: 0.725rem; display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                    @<%= nick %>
                                </small>
                            </div>
                            
                            <!-- COLUMNA 3: BOTÓN DE SEGUIR INTERACTIVO -->
                            <div style="width: auto !important;">
                                <% if (dtmUserLogueado != null) { %>
                                    <button type="button" 
                                            data-following="<%= dataFollowing %>"
                                            onclick="toggleSeguir(this, '<%= nick %>', '<%= request.getContextPath() %>')"
                                            style="font-size: 0.75rem; padding: 3px 12px; border-radius: 20px; font-weight: bold; white-space: nowrap; cursor: pointer; background-color: <%= initialBg %>; color: <%= initialColor %>; border: 1px solid <%= initialBorder %>; display: inline-block !important; width: auto !important; min-width: 0 !important;">
                                        <%= btnTexto %>
                                    </button>
                                <% } else { %>
                                    <a href="<%= perfilUrl %>" class="btn btn-outline-secondary fw-bold" style="font-size: 0.75rem; padding: 3px 12px; border-radius: 20px; white-space: nowrap; width: auto !important; display: inline-block !important;">
                                        Ver
                                    </a>
                                <% } %>
                            </div>

                        </div>

                        <% 
                                }
                            } else { 
                        %>
                        <p class="text-muted small text-center my-2">No hay sugerencias por el momento.</p>
                        <% } %>
                    </div>
                </div>

            </div>
        </div>

    </div>
</div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
<% } %>
