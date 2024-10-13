let userData; // Variable para guardar datos usuario

// Funcion para URL
function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

// Funcion para conseguir el nickname del usuario logeado
function getLoggedInUserNickname() {
    return sessionStorage.getItem('userNickname') || null;
}

// Funcion para traer los datos del usuario
function fetchUserData(nickname) {
    const loggedInUserNickname = getLoggedInUserNickname();
    if (loggedInUserNickname !== nickname) {
        window.location.href = `/AireLibre2/Usuario?nickname=${nickname}`;
        return;
    }

    fetch(`/AireLibre2/CargarDatosUsuario?nickname=${nickname}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los datos del usuario');
            }
            return response.json();
        })
        .then(data => {
            userData = data; 
            populateForm(data);
            validatePasswords(); // Validacion contraseña
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            showError('Error al cargar los datos del usuario.');
        });
}

function isValidURL(url) {
  // Permite URLs con o sin protocolo, con o sin www
  const pattern = /^(https?:\/\/)?(www\.)?[a-zA-Z0-9-]+(\.[a-zA-Z]{2,})+(\S*)?$/;
  return pattern.test(url);
}

function validateWebsite() {
    const webInput = document.getElementById('web');
    const url = webInput.value.trim();
    
    if (url && !isValidURL(url)) {
        alert('Por favor, ingrese una URL válida. Ejemplos:\nejemplo.com\nwww.ejemplo.com\nhttp://ejemplo.com\nhttps://www.ejemplo.com');
        webInput.value = '';
    }
}

function populateForm(userData) {
    document.getElementById('nombre').value = userData.nombre || '';
    document.getElementById('apellido').value = userData.apellido || '';
    document.getElementById('email').value = userData.email || '';
    document.getElementById('fechaNacimiento').value = userData.fechaNacimiento || '';
    document.getElementById('coverColor').value = userData.color || '#96bcd9'

    // Seteando el tipo de usuario en el campo oculto
    document.getElementById('tipo').value = userData.tipo || '';

    // Transformar esProfesional de String ("Sí" o "No") a booleano
    const esProfesionalBoolean = userData.esProfesional === 'Sí';
    document.getElementById('esProfesional').value = esProfesionalBoolean ? 'true' : 'false';

    if (userData.tipo === 'Entrenador') {
        document.getElementById('websiteGroup').style.display = 'block';
        document.getElementById('disciplineGroup').style.display = 'block';
        document.getElementById('web').value = userData.web || '';
        document.getElementById('disciplina').value = userData.disciplina || '';
    } else {
        document.getElementById('websiteGroup').style.display = 'none';
        document.getElementById('disciplineGroup').style.display = 'none';
    }
}

// Funcion para manejar el submit
function handleSubmit(event) {
    event.preventDefault();
    const form = event.target;
    const formData = new FormData(form);
    const nickname = getUrlParameter('nickname');
    
    if (form.tipo.value === 'Entrenador') {
        const webValue = form.web.value.trim();
        if (webValue && !isValidURL(webValue)) {
            showError('Por favor, ingrese una URL válida para el sitio web.');
            return;
        }
        formData.append('web', webValue);
        formData.append('disciplina', form.disciplina.value);
    }

    // Verifico que el usuario tenga permisos de modificar ese perfil
    if (getLoggedInUserNickname() !== nickname) {
        showError('You do not have permission to modify this profile.');
        return;
    }

    // Verifico que la contraseña actual y la contraseña ingresada coinciden y las nuevas contraseñas si hay tambien coinciden
    if (form.EditregistredPassword.value !== form.EditregisterConfirmPassword.value || !isCurrentPasswordCorrect(form.actualPassword.value)) {
        showError('Passwords do not match or current password is incorrect.');
        return;
    }

    // Me aseguro que todos los campos son enviados
    formData.append('nickname', nickname);
    formData.append('nombre', form.nombre.value);
    formData.append('apellido', form.apellido.value);
    formData.append('email', form.email.value);
    formData.append('fechaNacimiento', form.fechaNacimiento.value);
    formData.append('coverColor', form.coverColor.value);
    formData.append('tipo', form.tipo.value); // Campo oculto para tipo de usuario
    formData.append('esProfesional', form.esProfesional.value); // Campo oculto para profesional
	if (form.EditregistredPassword.value) {
		formData.append('pass', form.EditregistredPassword.value);
	} else {
	        // Si no se coloco nueva contraseña uso la vieja
	   	formData.append('pass', userData.pass);
	}
	


    
    
    // Checkeo si se subio nueva imagen
    const profileImageInput = form.profileImage;
    if (profileImageInput.files && profileImageInput.files.length > 0) {
        formData.append('profileImage', profileImageInput.files[0]); // Add the file
    }

    // Solo agregar web y disciplina si es entrenador
    if (form.tipo.value === 'Entrenador') {
        formData.append('web', form.web.value);
        formData.append('disciplina', form.disciplina.value);
    }

    fetch('/AireLibre2/ActualizarPerfil', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al actualizar el perfil');
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert('Perfil actualizado con éxito');
            
            // Si el usuario cambio su foto actualiuzo el user storage con la nueva imagen
            if (profileImageInput.files && profileImageInput.files.length > 0) {
                const reader = new FileReader();
                reader.onloadend = function() {
                    const base64String = reader.result.replace('data:', '').replace(/^.+,/, ''); // Convierto a base 64
                    sessionStorage.setItem('userImageBytes', base64String); // Actualizo la sesion
                    updateProfileImage(base64String); 
                };
                reader.readAsDataURL(profileImageInput.files[0]); 
            }
            
            window.location.href = `/AireLibre2/Usuario?nickname=${nickname}`;
        } else {
            showError('Error al actualizar el perfil: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error updating profile:', error);
        showError('Error al actualizar el perfil.');
    });
}

// Funcion para mostrar mensajes de error
function showError(message) {
    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    document.body.insertBefore(errorElement, document.body.firstChild);
}

function validatePasswords() {
    const passwordInput = document.getElementById('EditregistredPassword');
    const confirmPasswordInput = document.getElementById('EditregisterConfirmPassword');
    const currentPasswordInput = document.getElementById('actualPassword');
    const EditPasswordError = document.getElementById('EditPasswordError');
    const submitButton = document.getElementById('guardarCambios');

    const passwordsMatch = passwordInput.value === confirmPasswordInput.value;
    const currentPasswordCorrect = isCurrentPasswordCorrect(currentPasswordInput.value);
    const newPasswordsEmpty = !passwordInput.value && !confirmPasswordInput.value;
    const allFieldsFilled = passwordInput.value && confirmPasswordInput.value && currentPasswordInput.value;

    if ((passwordsMatch && currentPasswordCorrect && allFieldsFilled) || 
        (newPasswordsEmpty && currentPasswordCorrect)) {
        EditPasswordError.style.display = 'none';
        submitButton.disabled = false;
        console.log('Password conditions met');
    } else {
        EditPasswordError.style.display = 'block';
        submitButton.disabled = true;
        console.log('Password conditions not met');
        
        if (!passwordsMatch && !newPasswordsEmpty) {
            EditPasswordError.textContent = 'Las contraseñas no coinciden';
        } else if (!currentPasswordCorrect) {
            EditPasswordError.textContent = 'La contraseña actual es incorrecta';
        } else if (!newPasswordsEmpty && !allFieldsFilled) {
            EditPasswordError.textContent = 'Por favor, complete todos los campos de contraseña';
        } else {
            EditPasswordError.textContent = '';
        }
    }
}

// Funcion para checkear si la contraseña actual coincide con la ingresada
function isCurrentPasswordCorrect(currentPassword) {
    return userData && currentPassword === userData.pass;
}

// Main
document.addEventListener('DOMContentLoaded', () => {
    const submitButton = document.getElementById('guardarCambios');
    submitButton.disabled = true;
    
    const webInput = document.getElementById('web');
    if (webInput) {
        webInput.addEventListener('blur', validateWebsite);
    }

    const nickname = getUrlParameter('nickname');
    if (nickname) {
        fetchUserData(nickname);
    } else {
        console.error('No se proporcionó un nickname en la URL');
        showError('No se proporcionó un nickname en la URL.');
        return;
    }

    const form = document.getElementById('editProfileForm');
    form.addEventListener('submit', handleSubmit);

    // Event listener para validacion de contraseña
    const passwordInput = document.getElementById('EditregistredPassword');
    const confirmPasswordInput = document.getElementById('EditregisterConfirmPassword');
    const currentPasswordInput = document.getElementById('actualPassword');
    
    if (passwordInput && confirmPasswordInput && currentPasswordInput) {
        passwordInput.addEventListener('input', validatePasswords);
        confirmPasswordInput.addEventListener('input', validatePasswords);
        currentPasswordInput.addEventListener('input', validatePasswords);
        console.log('Event listeners added to password fields');
    } else {
        console.error('Password input fields not found');
    }

    // Initial validation
    validatePasswords();
});