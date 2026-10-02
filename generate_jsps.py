import os

base_dir = "PDA_WebServer/src/main/webapp/InterfacesJSP"
os.makedirs(base_dir, exist_ok=True)

jsps = {
    "IniciarSesion.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión</title>
    <style>body { font-family: Arial; padding: 20px; }</style>
</head>
<body>
    <h2>Iniciar Sesión</h2>
    <form action="../IniciarSesionServlet" method="POST">
        <label>Nickname o Correo:</label><br>
        <input type="text" name="id" required><br><br>
        <label>Contraseña:</label><br>
        <input type="password" name="password" required><br><br>
        <button type="submit">Ingresar</button>
        <button type="button" onclick="window.history.back();">Cancelar</button>
    </form>
</body>
</html>""",

    "AltaUsuario.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Alta de Usuario</title>
    <style>body { font-family: Arial; padding: 20px; }</style>
</head>
<body>
    <h2>Alta de Usuario</h2>
    <form action="../AltaUsuarioServlet" method="POST" enctype="multipart/form-data">
        <label>Nickname:</label><br>
        <input type="text" name="nickname" required><br>
        <label>Nombre:</label><br>
        <input type="text" name="nombre" required><br>
        <label>Apellido:</label><br>
        <input type="text" name="apellido" required><br>
        <label>Correo Electrónico:</label><br>
        <input type="email" name="correo" required><br>
        <label>Fecha de Nacimiento:</label><br>
        <input type="date" name="fechaNacimiento" required><br>
        <label>Contraseña:</label><br>
        <input type="password" name="password" required><br>
        <label>Confirmar Contraseña:</label><br>
        <input type="password" name="passwordConfirm" required><br>
        <label>Imagen de Perfil:</label><br>
        <input type="file" name="imagen" accept="image/png, image/jpeg"><br>
        <br>
        <label>Tipo de Usuario:</label><br>
        <select name="tipoUsuario" id="tipoUsuario" onchange="toggleDocente()">
            <option value="estudiante">Estudiante</option>
            <option value="docente">Docente</option>
        </select><br><br>
        <div id="divInstituto" style="display:none;">
            <label>Instituto (Solo para docentes):</label><br>
            <select name="instituto">
                <!-- Llenar dinámicamente con JSTL -->
                <option value="">Seleccione un instituto...</option>
            </select><br><br>
        </div>
        <button type="submit">Registrar</button>
    </form>
    <script>
        function toggleDocente() {
            var tipo = document.getElementById("tipoUsuario").value;
            document.getElementById("divInstituto").style.display = (tipo === "docente") ? "block" : "none";
        }
    </script>
</body>
</html>""",

    "ConsultaUsuario.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Usuario</title>
</head>
<body>
    <h2>Consulta de Usuario</h2>
    <form action="../ConsultaUsuarioServlet" method="GET">
        <label>Seleccionar Usuario:</label>
        <select name="nickname">
            <!-- Iterar usuarios con JSTL -->
        </select>
        <button type="submit">Ver Detalles</button>
    </form>
    <hr>
    <!-- Aquí se mostrarán los datos si hay un usuario seleccionado en el request -->
    <div id="detallesUsuario">
        <h3>Detalles del Perfil</h3>
        <!-- Mostrar imagen, datos y pestañas (Estudiante/Docente) -->
    </div>
</body>
</html>""",

    "ModificarDatosUsuario.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Modificar Datos</title>
</head>
<body>
    <h2>Modificar Datos de Usuario</h2>
    <form action="../ModificarUsuarioServlet" method="POST">
        <label>Nickname (No editable):</label><br>
        <input type="text" name="nickname" value="${usuario.nickname}" readonly><br>
        <label>Correo (No editable):</label><br>
        <input type="email" name="correo" value="${usuario.correo}" readonly><br>
        <label>Nombre:</label><br>
        <input type="text" name="nombre" value="${usuario.nombre}" required><br>
        <label>Apellido:</label><br>
        <input type="text" name="apellido" value="${usuario.apellido}" required><br>
        <label>Fecha de Nacimiento:</label><br>
        <input type="date" name="fechaNacimiento" value="${usuario.fechaNacimiento}" required><br><br>
        <button type="submit">Guardar Cambios</button>
    </form>
</body>
</html>""",

    "AltaCurso.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Alta de Curso</title>
</head>
<body>
    <h2>Alta de Curso</h2>
    <form action="../AltaCursoServlet" method="POST">
        <label>Instituto:</label><br>
        <select name="instituto" required>
            <!-- Llenar con institutos -->
        </select><br>
        <label>Nombre del Curso:</label><br>
        <input type="text" name="nombre" required><br>
        <label>Descripción:</label><br>
        <textarea name="descripcion" required></textarea><br>
        <label>Duración (meses):</label><br>
        <input type="number" name="duracion" required><br>
        <label>Horas:</label><br>
        <input type="number" name="horas" required><br>
        <label>Créditos:</label><br>
        <input type="number" name="creditos" required><br>
        <label>URL:</label><br>
        <input type="url" name="url" required><br>
        <label>Categorías:</label><br>
        <select name="categorias" multiple required>
            <!-- Llenar con categorías -->
        </select><br>
        <label>Previas (Opcional):</label><br>
        <select name="previas" multiple>
            <!-- Llenar con cursos -->
        </select><br><br>
        <button type="submit">Registrar Curso</button>
    </form>
</body>
</html>""",

    "ConsultaCurso.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Curso</title>
</head>
<body>
    <h2>Consulta de Curso</h2>
    <form action="" method="GET">
        <label>Filtrar por Instituto o Categoría:</label>
        <select name="filtro">
             <!-- Opciones -->
        </select>
        <button type="submit">Buscar Cursos</button>
    </form>
    <hr>
    <form action="../ConsultaCursoServlet" method="GET">
        <label>Seleccionar Curso:</label>
        <select name="curso">
             <!-- Opciones de cursos filtrados -->
        </select>
        <button type="submit">Ver Información</button>
    </form>
</body>
</html>""",

    "AltaEdicionCurso.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Alta Edición de Curso</title>
</head>
<body>
    <h2>Alta Edición de Curso</h2>
    <form action="../AltaEdicionServlet" method="POST">
        <label>Curso:</label><br>
        <select name="curso" required>
            <!-- Cursos asociados al instituto del docente logueado -->
        </select><br>
        <label>Nombre de la Edición:</label><br>
        <input type="text" name="nombre" required><br>
        <label>Fecha de Inicio:</label><br>
        <input type="date" name="fechaInicio" required><br>
        <label>Fecha de Fin:</label><br>
        <input type="date" name="fechaFin" required><br>
        <label>Cupo (Opcional):</label><br>
        <input type="number" name="cupo"><br>
        <label>Docentes Participantes:</label><br>
        <select name="docentes" multiple required>
            <!-- Lista de docentes -->
        </select><br><br>
        <button type="submit">Crear Edición</button>
    </form>
</body>
</html>""",

    "ConsultaEdicionCurso.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta Edición de Curso</title>
</head>
<body>
    <h2>Consulta de Edición de Curso</h2>
    <form action="" method="GET">
        <label>Seleccionar Curso:</label>
        <select name="curso">
             <!-- Opciones -->
        </select>
        <button type="submit">Ver Ediciones</button>
    </form>
    <form action="../ConsultaEdicionServlet" method="GET">
        <label>Seleccionar Edición:</label>
        <select name="edicion">
             <!-- Opciones de ediciones -->
        </select>
        <button type="submit">Ver Detalles</button>
    </form>
</body>
</html>""",

    "InscripcionEdicionCurso.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscripción a Edición de Curso</title>
</head>
<body>
    <h2>Inscripción a Edición de Curso</h2>
    <form action="../InscripcionEdicionServlet" method="POST">
        <label>Seleccionar Curso:</label><br>
        <select name="curso" required>
            <!-- Cursos -->
        </select><br>
        <label>Edición Vigente:</label><br>
        <input type="text" name="edicion" value="${edicionVigente.nombre}" readonly required><br><br>
        <button type="submit">Inscribirme</button>
    </form>
</body>
</html>""",

    "CrearProgramaFormacion.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Crear Programa de Formación</title>
</head>
<body>
    <h2>Crear Programa de Formación</h2>
    <form action="../CrearProgramaServlet" method="POST">
        <label>Nombre:</label><br>
        <input type="text" name="nombre" required><br>
        <label>Descripción:</label><br>
        <textarea name="descripcion" required></textarea><br>
        <label>Fecha de Inicio:</label><br>
        <input type="date" name="fechaInicio" required><br>
        <label>Fecha de Fin:</label><br>
        <input type="date" name="fechaFin" required><br><br>
        <button type="submit">Crear Programa</button>
    </form>
</body>
</html>""",

    "AgregarCursoProgramaFormacion.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar Curso a Programa</title>
</head>
<body>
    <h2>Agregar Curso a Programa de Formación</h2>
    <form action="../AgregarCursoProgramaServlet" method="POST">
        <label>Programa de Formación:</label><br>
        <select name="programa" required>
            <!-- Programas -->
        </select><br>
        <label>Curso a agregar:</label><br>
        <select name="curso" required>
            <!-- Cursos -->
        </select><br><br>
        <button type="submit">Agregar Curso</button>
    </form>
</body>
</html>""",

    "ConsultaProgramaFormacion.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Consulta de Programa de Formación</title>
</head>
<body>
    <h2>Consulta de Programa de Formación</h2>
    <form action="../ConsultaProgramaServlet" method="GET">
        <label>Seleccionar Programa:</label><br>
        <select name="programa" required>
            <!-- Programas -->
        </select><br><br>
        <button type="submit">Ver Detalles</button>
    </form>
</body>
</html>""",

    "InscripcionProgramaFormacion.jsp": """<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Inscripción a Programa de Formación</title>
</head>
<body>
    <h2>Inscripción a Programa de Formación</h2>
    <form action="../InscripcionProgramaServlet" method="POST">
        <label>Seleccionar Programa:</label><br>
        <select name="programa" required>
            <!-- Programas existentes -->
        </select><br><br>
        <button type="submit">Inscribirme</button>
    </form>
</body>
</html>"""
}

for name, content in jsps.items():
    with open(os.path.join(base_dir, name), "w") as f:
        f.write(content)

print(f"Created {len(jsps)} JSP files.")
