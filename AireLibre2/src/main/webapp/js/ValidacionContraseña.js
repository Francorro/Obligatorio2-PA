
document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');
    const passwordInput = document.getElementById('registerPassword');
    const confirmPasswordInput = document.getElementById('registerConfirmPassword');
    const passwordError = document.getElementById('passwordError');
    const registerButton = document.getElementById('registerButton');

    function validatePasswords() {
        if (passwordInput.value !== confirmPasswordInput.value) {
            passwordError.style.display = 'block';
            registerButton.disabled = true;
        } else {
            passwordError.style.display = 'none';
            registerButton.disabled = false;
        }
    }

    passwordInput.addEventListener('input', validatePasswords);
    confirmPasswordInput.addEventListener('input', validatePasswords);

    registerForm.addEventListener('submit', function(e) {
        e.preventDefault();
        if (passwordInput.value !== confirmPasswordInput.value) {
            passwordError.style.display = 'block';
            return;
        }

        const formData = new FormData(registerForm);

        fetch(registerForm.action, {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            alert(data); // Muestro al respuesta del servidor
            if (data.includes('exitosamente')) {
                // Cierro el modal si el registro fue exitoso
                var registerModal = bootstrap.Modal.getInstance(document.getElementById('registerModal'));
                registerModal.hide();
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Hubo un error al procesar su solicitud. Por favor, inténtelo de nuevo.');
        });
    });
});