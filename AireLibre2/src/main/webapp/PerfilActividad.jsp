<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de Actividad Deportiva</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/PerfilActividad.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
</head>
<body class="activity-page perfil-actividad-page">
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<%@ include file="/WEB-INF/views/LoginRegister.jsp" %>
    <main class="profile-container">
        <section class="activity-header">
            <div class="profile-image-container">
                <img src="" alt="Imagen de la actividad deportiva" id="activityImage" class="profile-image">
            </div>
            <div class="activity-info">
                <h1 id="activityName"></h1>
                <p id="activityDescription"></p>
                <p>Entrenador: <span id="datosEntrenador"></span></p>
                <p>Duración: <span id="activityDuration"></span></p>
                <p>Costo: <span id="activityCost"></span></p>
            </div>
        </section>
        <section class="activity-classes">
            <h2>Inscripciones</h2>
            <h3> Seleccione una clase:</h3>
            <table class="classes-table">
                <thead>
                    <tr>
                        <th>Fecha</th>
                        <th>Hora</th>
                        <th>Lugar</th>
                        <th>Cupo</th>
                    </tr>
                </thead>
                <tbody id="classesTableBody">
                    <!-- Las filas de la tabla se llenarán dinámicamente con JavaScript -->
                </tbody>
            </table>
        </section>
    </main>
    <script>
    var actividadData = ${actividadData};
    </script>
    <script src="${pageContext.request.contextPath}/js/PerfilActividad.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/CheckEntrenador.js"></script>
    <script src="${pageContext.request.contextPath}/js/ExistenciaNickname.js"></script>
    <script src="${pageContext.request.contextPath}/js/Login.js"></script>
    
        <footer class="bg-dark text-white text-center py-3 fixed-bottom">
		<%@ include file="/WEB-INF/views/footer.jsp" %>
    </footer>
</body>
</html>