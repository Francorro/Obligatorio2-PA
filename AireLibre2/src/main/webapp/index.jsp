<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aire Libre - Actividades al aire libre</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/Actividades.css">
    <style>
        #inicio {
            background-image: url('Images/imagen_fondo.avif');
            background-size: cover;
            background-position: center;
            height: 100vh;
            display: flex;
            align-items: center;
            color: white;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.5);
        }
        .activity-card .card-img-top {
            height: 200px;
            object-fit: cover;
        }
    </style>
</head>
<body>
     <script src="js/IniciarBase.js"></script>
    <%@ include file="/WEB-INF/views/navbar.jsp" %>
    
    <!-- Inicio -->
    <section id="inicio" class="d-flex align-items-center">
        <div class="container text-center">
            <h1 class="display-1 fw-bold mb-3">AIRE LIBRE</h1>
            <p class="lead fs-2">Descubre la aventura en cada paso</p>
            <a href="${pageContext.request.contextPath}/Actividades" class="btn btn-light btn-lg mt-3">Explora nuestras actividades</a>
        </div>
    </section>

    <!-- Sobre nosotros -->
    <section id="sobre-nosotros" class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-5">Sobre Nosotros</h2>
            <p class="lead text-center">En AireLibre, nos dedicamos a promover un estilo de vida activo a través de actividades deportivas al aire libre. Ofrecemos una variedad de opciones para todas las edades y niveles, siempre con el objetivo de fomentar la salud, el bienestar y la conexión con la naturaleza. Nuestro equipo de profesionales está comprometido en brindarte experiencias deportivas únicas, en un entorno inclusivo y motivador.

            <br />¡Únete a AireLibre y descubre todo lo que el deporte puede hacer por ti!</p>
        </div>
    </section>

    <!-- Actividades -->
    <section id="actividades" class="py-5">
        <div class="container">
            <h2 class="text-center mb-5">ALGUNAS DE NUESTRAS ACTIVIDADES</h2>
            <div id="activityContainer" class="row row-cols-1 row-cols-md-3 g-4">
                <!-- Las actividades se cargarán aquí dinámicamente -->
            </div>
        </div>
    </section>

    <!-- Contacto -->
    <section id="contacto" class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-5">Contacto</h2>
            <div class="row">
                <div class="col-md-6 mb-4 mb-md-0">
                    <h3 class="mb-4">Envíanos un mensaje</h3>
                    <form>
                        <div class="mb-3">
                            <input type="text" class="form-control" placeholder="Nombre" required>
                        </div>
                        <div class="mb-3">
                            <input type="email" class="form-control" placeholder="Email" required>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" placeholder="Asunto" required>
                        </div>
                        <div class="mb-3">
                            <textarea class="form-control" rows="4" placeholder="Mensaje" required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Enviar mensaje</button>
                    </form>
                </div>
                <div class="col-md-6">
                    <h3 class="mb-4">Información de contacto</h3>
                    <ul class="list-unstyled">
                        <li class="mb-3"><i class="bi bi-geo-alt me-2"></i> Calle Ejemplo 123, Ciudad, País</li>
                        <li class="mb-3"><i class="bi bi-telephone me-2"></i> +1 234 567 890</li>
                        <li class="mb-3"><i class="bi bi-envelope me-2"></i> info@airelibre.com</li>
                        <li class="mb-3"><i class="bi bi-clock me-2"></i> Lunes a Viernes, 9:00 AM - 6:00 PM</li>
                    </ul>
                    <h4 class="mt-4 mb-3">Síguenos en redes sociales</h4>
                    <div class="d-flex">
                        <a href="#" class="btn btn-outline-primary me-2"><i class="bi bi-facebook"></i></a>
                        <a href="#" class="btn btn-outline-info me-2"><i class="bi bi-twitter"></i></a>
                        <a href="#" class="btn btn-outline-danger"><i class="bi bi-instagram"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-white text-center py-3">
        <%@ include file="/WEB-INF/views/footer.jsp" %>
    </footer>

    <%@ include file="/WEB-INF/views/LoginRegister.jsp" %>

    <!-- Modal para los detalles de las actividades -->
    <div class="modal fade" id="activityModal" tabindex="-1" aria-labelledby="activityModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header actividades-modal-header">
                    <h5 class="modal-title" id="activityModalLabel">Detalles de la Actividad</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <!-- El contenido del modal se llenará dinámicamente -->
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="js/CheckEntrenador.js"></script>
    <script src="js/ExistenciaNickname.js"></script>
    <script src="js/Login.js"></script>
    <script src="js/CargarPrimerasActividades.js"></script>
    
    

</body>
</html>
