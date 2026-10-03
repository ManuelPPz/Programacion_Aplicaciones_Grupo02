// Lógica para cambiar de Pestañas (Tabs)
function openTab(evt, tabId) {
    // Ocultar todos los contenidos de las pestañas
    var tabContents = document.getElementsByClassName("tab-content");
    for (var i = 0; i < tabContents.length; i++) {
        tabContents[i].classList.remove("active");
    }
    
    // Quitar la clase 'active' de todos los botones de pestaña
    var tabButtons = document.getElementsByClassName("tab-button");
    for (var i = 0; i < tabButtons.length; i++) {
        tabButtons[i].classList.remove("active");
    }
    
    // Mostrar la pestaña actual y añadir 'active' al botón clicado
    document.getElementById(tabId).classList.add("active");
    evt.currentTarget.classList.add("active");
}

// Validaciones en el cliente al cargar el documento
document.addEventListener("DOMContentLoaded", function() {
    // 1. Validación de contraseñas iguales en Alta de Usuario
    var formAlta = document.querySelector("form[action='../AltaUsuarioServlet']");
    if (formAlta) {
        formAlta.addEventListener("submit", function(e) {
            var pass = document.querySelector("input[name='password']").value;
            var confirm = document.querySelector("input[name='passwordConfirm']").value;
            if (pass !== confirm) {
                e.preventDefault(); // Evita que se envíe el formulario
                alert("Las contraseñas no coinciden. Por favor, verifícalas.");
            }
        });
    }

    // 2. Dar formato a los mensajes de error/éxito si existen en el JSP
    // (Asegúrate de imprimir variables ${error} o ${exito} dentro de divs con estas clases)
});