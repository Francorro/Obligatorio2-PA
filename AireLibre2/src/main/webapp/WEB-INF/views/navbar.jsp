<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>

</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light fixed-top">
    <div class="container">
        <a class="navbar-brand fw-bold" href="/AireLibre2">Aire Libre</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/#sobre-nosotros">Sobre Nosotros</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" id="actividadesLink">Actividades</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ListadoUsuarios" id="usuariosLink">Usuarios</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/#contacto">Contacto</a>
                </li>
                <li class="nav-item" id="altaActividadLink" style="display: none;">
                    <a class="nav-link" href="${pageContext.request.contextPath}/AltaActividadDeportiva">Alta Actividad Deportiva</a>
                </li>
                <li class="nav-item" id="altaClaseLink" style="display: none;">
                    <a class="nav-link" href="${pageContext.request.contextPath}/AltaClaseDeportiva">Alta Clase Deportiva</a>
                </li>
            </ul>
            <div id="authButtons">
                <button type="button" class="btn btn-outline-primary me-2" data-bs-toggle="modal" data-bs-target="#loginModal">Login</button>
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#registerModal">Registrar</button>
            </div>
            <div id="userProfile" class="d-none">
                <div class="d-flex align-items-center">
                    <a href="#" id="profileLink" class="text-decoration-none me-3">
                        <img src="" alt="Profile" id="profileImage" class="rounded-circle" width="40" height="40">
                    </a>
                    <div class="dropdown">
                        <button class="btn btn-link dropdown-toggle" type="button" id="profileDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="bi bi-chevron-down"></i>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
                            <li><a class="dropdown-item text-danger" href="#" id="logoutButton">Cerrar sesión</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</nav>


<script>
document.addEventListener('DOMContentLoaded', function() {
    const actividadesLink = document.getElementById('actividadesLink');
    
    actividadesLink.addEventListener('click', function(e) {
        window.location.href = '${pageContext.request.contextPath}/Actividades';
    });
});


</script>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const navbarHeight = document.querySelector('.navbar').offsetHeight;
    
    // Function to adjust scroll with navbar offset
    function adjustScroll() {
        const hash = window.location.hash;
        if (hash) {
            const target = document.querySelector(hash);
            if (target) {
                const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - navbarHeight;
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        }
    }
    
    // Adjust scroll when navigating through anchor links
    window.addEventListener('hashchange', adjustScroll);
    
    // Adjust scroll if page loads with a hash in the URL
    if (window.location.hash) {
        setTimeout(adjustScroll, 400); // Ensure the page has fully loaded before adjusting scroll
    }
    
    // Override default anchor link behavior to smoothly scroll with offset
    document.querySelectorAll('a.nav-link[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            const target = document.querySelector(targetId);
            if (target) {
                const targetPosition = target.getBoundingClientRect().top + window.pageYOffset - navbarHeight;
                window.history.pushState(null, null, targetId); // Update URL with hash
                window.scrollTo({
                    top: targetPosition,
                    behavior: 'smooth'
                });
            }
        });
    });
});

</script>

</body>
</html>