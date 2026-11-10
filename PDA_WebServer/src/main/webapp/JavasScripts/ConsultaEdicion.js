document.addEventListener("DOMContentLoaded", function() {
    document.addEventListener("click", function(event) {
        // Clic en Inscribir
        const btnInscribir = event.target.closest("#btnInscribir");
        if (btnInscribir) {
            const curso = btnInscribir.dataset.curso;
            const edicion = btnInscribir.dataset.edicion;
            const contextPath = btnInscribir.dataset.contextpath || '';
            
            inscribirDirectamente(curso, edicion, contextPath);
        }

        // Clic en Cancelar
        const btnCancelar = event.target.closest("#btnCancelar");
        if (btnCancelar) {
            const curso = btnCancelar.dataset.curso;
            const edicion = btnCancelar.dataset.edicion;
            const contextPath = btnCancelar.dataset.contextpath || '';
            
            cancelarInscripcion(curso, edicion, contextPath);
        }
    });
});

function inscribirDirectamente(curso, edicion, contextPath) {
    const btn = document.getElementById("btnInscribir");
    if (!btn) return;

    btn.disabled = true;
    btn.innerText = "Inscribiendo...";

    const url = (contextPath || '') + '/ConsultaEdicionServlet';
    const params = new URLSearchParams({
        action: 'inscribirDirecto',
        curso: curso,
        edicion: edicion
    });

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        },
        body: params.toString()
    })
    .then(async response => {
        if (response.ok) {
            const contenedor = document.getElementById("contenedorInscripcion");
            if (contenedor) {
                // Reemplazamos dinámicamente por el botón Cancelar
                contenedor.innerHTML = `
                    <button type="button" 
                            id="btnCancelar" 
                            data-curso="${curso}" 
                            data-edicion="${edicion}"
                            data-contextpath="${contextPath}"
                            class="btn btn-danger fw-bold w-100 py-2 shadow-sm">
                        Cancelar
                    </button>
                `;
            }
        } else {
            const msg = await response.text();
            alert(msg && msg.trim() !== "" ? msg : "Ocurrió un error al procesar la inscripción.");
            btn.disabled = false;
            btn.innerText = "Solicitar Inscripción a esta Edición";
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Error de conexión al intentar inscribir.");
        btn.disabled = false;
        btn.innerText = "Solicitar Inscripción a esta Edición";
    });
}

function cancelarInscripcion(curso, edicion, contextPath) {
    const btn = document.getElementById("btnCancelar");
    if (!btn) return;

    

    btn.disabled = true;
    btn.innerText = "Cancelando...";

    const url = (contextPath || '') + '/ConsultaEdicionServlet';
    const params = new URLSearchParams({
        action: 'desistirInscripcion',
        curso: curso,
        edicion: edicion
    });

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        },
        body: params.toString()
    })
    .then(async response => {
        if (response.ok) {
            const contenedor = document.getElementById("contenedorInscripcion");
            if (contenedor) {
                // Reemplazamos dinámicamente por el botón verde de Inscribir
                contenedor.innerHTML = `
                    <button type="button" 
                            id="btnInscribir" 
                            data-curso="${curso}" 
                            data-edicion="${edicion}"
                            data-contextpath="${contextPath}"
                            class="btn fw-bold w-100 py-2 shadow-sm text-white" 
                            style="background-color: #198754 !important; border-color: #198754 !important;">
                        Solicitar Inscripción a esta Edición
                    </button>
                `;
            }
        } else {
            const msg = await response.text();
            alert(msg && msg.trim() !== "" ? msg : "Ocurrió un error al cancelar la inscripción.");
            btn.disabled = false;
            btn.innerText = "Cancelar";
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Error de conexión al intentar cancelar.");
        btn.disabled = false;
        btn.innerText = "Cancelar";
    });
}