document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('registerForm');
    const nicknameInput = document.getElementById('registerNickname');
    const nicknameError = document.getElementById('nicknameError');
    const passwordInput = document.getElementById('registerPassword');
    const confirmPasswordInput = document.getElementById('registerConfirmPassword');
    const passwordError = document.getElementById('passwordError');
    const registerButton = document.getElementById('registerButton');
    let nicknameTimeout;
    let isNicknameValid = false;
    let arePasswordsValid = false;

    function checkNickname(nickname) {
        return fetch(`/AireLibre2/CheckNickname?nickname=${encodeURIComponent(nickname)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error en la respuesta del servidor');
                }
                return response.json();
            })
            .then(data => {
                console.log('Server response:', data);
                return data.exists;
            })
            .catch(error => {
                console.error('Error en la solicitud de verificación:', error);
                throw error;
            });
    }

    function updateButtonState() {
        registerButton.disabled = !(isNicknameValid && arePasswordsValid);
    }

    function validatePasswords() {
        arePasswordsValid = passwordInput.value === confirmPasswordInput.value && passwordInput.value !== '';
        if (!arePasswordsValid) {
            passwordError.style.display = 'block';
        } else {
            passwordError.style.display = 'none';
        }
        updateButtonState();
    }

    nicknameInput.addEventListener('input', function() {
        clearTimeout(nicknameTimeout);
        const nickname = nicknameInput.value;
        
        if (nickname) {
            nicknameTimeout = setTimeout(() => {
                checkNickname(nickname).then(exists => {
                    if (exists) {
                        nicknameError.textContent = 'Este nickname ya está en uso.';
                        nicknameError.style.display = 'block';
                        isNicknameValid = false;
                    } else {
                        nicknameError.style.display = 'none';
                        isNicknameValid = true;
                    }
                    updateButtonState();
                }).catch(error => {
                    console.error('Error al verificar el nickname:', error);
                });
            });
        } else {
            nicknameError.style.display = 'none';
            isNicknameValid = false;
            updateButtonState();
        }
    });

    passwordInput.addEventListener('input', validatePasswords);
    confirmPasswordInput.addEventListener('input', validatePasswords);

    registerForm.addEventListener('submit', function(e) {
        e.preventDefault();

        if (!isNicknameValid) {
            nicknameError.textContent = 'Por favor, elige un nickname válido.';
            nicknameError.style.display = 'block';
            nicknameInput.focus();
            return;
        }

        if (!arePasswordsValid) {
            passwordError.textContent = 'Las contraseñas no coinciden o están vacías.';
            passwordError.style.display = 'block';
            passwordInput.focus();
            return;
        }

        const formData = new FormData(registerForm);

        fetch(registerForm.action, {
            method: 'POST',
            body: formData
        })
        .then(response => response.text())
        .then(data => {
            alert(data); //Muestra la respuesta del servidor
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

    // Estado inicial del boton
    updateButtonState();
});