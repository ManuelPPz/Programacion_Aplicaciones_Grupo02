document.addEventListener("DOMContentLoaded", function () {
    const togglePassword = document.getElementById("btnTogglePassword");
    const passwordInput = document.getElementById("inputPassword");
    const iconoOjo = document.getElementById("iconoOjo");

    if (togglePassword && passwordInput && iconoOjo) {
        togglePassword.addEventListener("click", function () {
            const esPassword = passwordInput.getAttribute("type") === "password";
            
            passwordInput.setAttribute("type", esPassword ? "text" : "password");

            iconoOjo.classList.toggle("bi-eye-slash", !esPassword);
            iconoOjo.classList.toggle("bi-eye", esPassword);
        });
    }
});


