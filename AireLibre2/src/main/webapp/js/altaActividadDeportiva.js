document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('activityForm');
    const nombreInput = document.getElementById('nombre');
    const submitButton = document.getElementById('submitButton');
    const entrenadorNicknameInput = document.getElementById('entrenadornickname');
    const errorMessage = document.createElement('div');
    errorMessage.style.color = 'red';
    errorMessage.style.marginTop = '5px';
    nombreInput.parentNode.appendChild(errorMessage);

    const fileInput = document.getElementById('foto');
    const imagePreview = document.getElementById('imagePreview');
    const fileInputButton = document.querySelector('.btn-file-input');

    // Seteo el nombre del entrenador del usuario logeado
    entrenadorNicknameInput.value = sessionStorage.getItem('userNickname');

    nombreInput.addEventListener('input', checkActivityName);
    form.addEventListener('submit', validateForm);
    fileInput.addEventListener('change', handleFileSelect);
    fileInputButton.addEventListener('click', () => fileInput.click());

    function checkActivityName() {
        const nombre = nombreInput.value;
        fetch(`${form.action.split('/AltaActividadDeportiva')[0]}/ExisteActividad?nombre=${encodeURIComponent(nombre)}`)
            .then(response => response.json())
            .then(data => {
                if (data.exists) {
                    submitButton.disabled = true;
                    nombreInput.setCustomValidity('Este nombre de actividad ya existe');
                    errorMessage.textContent = 'Este nombre de actividad ya existe';
                } else {
                    submitButton.disabled = false;
                    nombreInput.setCustomValidity('');
                    errorMessage.textContent = '';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                errorMessage.textContent = 'Error al verificar el nombre de la actividad';
            });
    }

    function validateForm(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            Array.from(form.elements).forEach((input) => {
                if (input.checkValidity()) {
                    input.classList.remove('invalid');
                } else {
                    input.classList.add('invalid');
                }
            });
        }
    }

    function handleFileSelect(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                imagePreview.src = e.target.result;
                imagePreview.style.display = 'block';
            }
            reader.readAsDataURL(file);
        }
    }
});