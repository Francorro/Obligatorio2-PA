<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Actividades</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/Actividades.css">
</head>
<body class="actividades-page">
    <%@ include file="/WEB-INF/views/navbar.jsp" %>
    <%@ include file="/WEB-INF/views/LoginRegister.jsp" %>
    
    <div class="container py-5 mt-5 actividades-container">
        <h1 class="text-center mb-5 actividades-title">Todas Nuestras Actividades</h1>
        <div id="activityContainer" class="row row-cols-1 row-cols-md-3 g-4">
            <!-- Inserto las actividades dinamicamente -->
        </div>
    </div>

    <!-- Modal template -->
    <div class="modal fade" id="activityModal" tabindex="-1" aria-labelledby="activityModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header actividades-modal-header">
                    <h5 class="modal-title" id="activityModalLabel">Detalles de la Actividad</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <!-- Inserto el contenido del modal dinamicamente -->
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        var contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/js/Actividades.js"></script>
    <script src="js/CheckEntrenador.js"></script>
    <script src="js/ExistenciaNickname.js"></script>
    <script src="js/Login.js"></script>
    
        <footer class="bg-dark text-white text-center py-3">
		<%@ include file="/WEB-INF/views/footer.jsp" %>
    </footer>
</body>
</html>