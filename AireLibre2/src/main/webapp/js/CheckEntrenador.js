document.addEventListener('DOMContentLoaded', function() {
    function checkTrainerStatus() {
        const isEntrenador = sessionStorage.getItem('isEntrenador') === 'true';
        const altaActividadLink = document.getElementById('altaActividadLink');
        const altaClaseLink = document.getElementById('altaClaseLink');
        
        if (isEntrenador) {
            altaActividadLink.style.display = 'block';
            altaClaseLink.style.display= 'block';
        } else {
            altaActividadLink.style.display = 'none';
            altaClaseLink.style.display = 'none';
        }
    }

    // Checkeo si el entrenador logeado es entrenador
    checkTrainerStatus();

    // agrego listener cuando login/logout
    window.addEventListener('storage', function(e) {
        if (e.key === 'isEntrenador') {
            checkTrainerStatus();
        }
    });

    // Agrego event listener para los cambios de estado de login
    document.addEventListener('loginStateChanged', checkTrainerStatus);
});