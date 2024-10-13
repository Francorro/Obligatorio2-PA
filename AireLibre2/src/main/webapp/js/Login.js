document.addEventListener('DOMContentLoaded', function() {
    const authButtons = document.getElementById('authButtons');
    const userProfile = document.getElementById('userProfile');
    const profileImage = document.getElementById('profileImage');
    const profileLink = document.getElementById('profileLink');
    const logoutButton = document.getElementById('logoutButton');
    const loginForm = document.querySelector('#loginModal form');
    const loginModal = document.getElementById('loginModal');

    function checkLoginState() {
        const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';
        const userNickname = sessionStorage.getItem('userNickname');
        const userImageBytes = sessionStorage.getItem('userImageBytes');
        const isEntrenador = sessionStorage.getItem('isEntrenador') === 'true';

        if (isLoggedIn && userNickname) {
            authButtons.classList.add('d-none');
            userProfile.classList.remove('d-none');
            if (userImageBytes) {
                profileImage.src = `data:image/jpeg;base64,${userImageBytes}`;
            } else {
                profileImage.src = 'https://i.imgur.com/rLk1DJC.jpeg';
            }
            profileLink.href = `/AireLibre2/Usuario?nickname=${userNickname}`;
            
            if (isEntrenador) {
                console.log('User is an Entrenador');
            } else {
                console.log('User is a Deportista');
            }
        } else {
            authButtons.classList.remove('d-none');
            userProfile.classList.add('d-none');
        }
    }

    // Checkeo el estado de login cuando se carga la pagina
    checkLoginState();

loginForm.addEventListener('submit', function(e) {
    e.preventDefault();
    const nickname = document.getElementById('loginNickname').value;
    const password = document.getElementById('loginPassword').value;

    fetch('/AireLibre2/Login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `nickname=${encodeURIComponent(nickname)}&pass=${encodeURIComponent(password)}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            sessionStorage.setItem('isLoggedIn', 'true');
            sessionStorage.setItem('userNickname', nickname);
            sessionStorage.setItem('userImageBytes', data.imageBytes);
            sessionStorage.setItem('isEntrenador', data.isEntrenador);
            
            // Seteo es entrenador del lado del servidor en la sesion
            fetch('/AireLibre2/SetEntrenadorStatus', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `isEntrenador=${data.isEntrenador}`
            });
            
            checkLoginState();
            const modalInstance = bootstrap.Modal.getInstance(loginModal);
            modalInstance.hide();
            
          
            document.dispatchEvent(new Event('loginStateChanged'));
        } else {
            throw new Error(data.message || 'Login failed');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error durante el inicio de sesión: ' + error.message);
    });
});

logoutButton.addEventListener('click', function(e) {
        e.preventDefault();
        fetch('/AireLibre2/Logout', {
            method: 'POST',
        })
        .then(response => response.json())
        .then(data => {
			if (data.success) {
			    sessionStorage.removeItem('isLoggedIn');
			    sessionStorage.removeItem('userNickname');
			    sessionStorage.removeItem('userImageBytes');
			    sessionStorage.removeItem('isEntrenador');
			    checkLoginState();
			    
			   
			    document.dispatchEvent(new Event('loginStateChanged'));
			    window.location.href = '/AireLibre2';

			} else {
			    throw new Error(data.message || 'Logout failed');
			}
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error durante el cierre de sesión: ' + error.message);
            // Forzar cierre de sesión en el cliente en caso de error
            sessionStorage.removeItem('isLoggedIn');
            sessionStorage.removeItem('userNickname');
            sessionStorage.removeItem('userImageBytes');
            sessionStorage.removeItem('isEntrenador');
            checkLoginState();
            
            window.location.href = '/AireLibre2';
            // Triggereo evento storage para checkTrainerStatus.js
            window.dispatchEvent(new Event('storage'));
        });
    });
});