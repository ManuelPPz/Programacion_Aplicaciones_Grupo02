document.addEventListener('submit', function (event) {
    if (event.target && event.target.id === 'formLogin') {
        const emailInput = document.getElementById('floatingInputGroup2');
        const passwordInput = document.getElementById('inputPassword');
        
        const errorMessageEmail = document.getElementById("mensajeErrorEmail");
        const errorMessagePassword = document.getElementById("mensajeErrorPassword");
        
        const email = emailInput ? emailInput.value.trim() : '';
        const password = passwordInput ? passwordInput.value.trim() : '';

        let hayError = false;

        // Validar Email
        if (email === '') {
            if (emailInput) emailInput.classList.add('is-invalid');
            if (errorMessageEmail) {
                errorMessageEmail.classList.remove('d-none');
                errorMessageEmail.classList.add('d-block');
            }
            hayError = true;
        } else {
            if (emailInput) emailInput.classList.remove('is-invalid');
            if (errorMessageEmail) {
                errorMessageEmail.classList.add('d-none');
                errorMessageEmail.classList.remove('d-block');
            }
        }

        // Validar Contraseña
        if (password === '') {
            if (passwordInput) passwordInput.classList.add('is-invalid');
            if (errorMessagePassword) {
                errorMessagePassword.classList.remove('d-none');
                errorMessagePassword.classList.add('d-block');
            }
            hayError = true;
        } else {
            if (passwordInput) passwordInput.classList.remove('is-invalid');
            if (errorMessagePassword) {
                errorMessagePassword.classList.add('d-none');
                errorMessagePassword.classList.remove('d-block');
            }
        }

        // Frenar el envío si hay errores
        if (hayError) {
            event.preventDefault();
            event.stopPropagation();
            return false;
        }
    }
});