<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta de Actividad Deportiva</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/AltaActividadDeportiva.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    
</head>
<body>
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<%@ include file="/WEB-INF/views/LoginRegister.jsp" %>
    <div class="activity-form-container mt-5">
        <h1>Alta de Actividad Deportiva</h1>
        <% if (request.getParameter("success") != null && request.getParameter("success").equals("true")) { %>
            <div class="alert alert-success">
                La actividad deportiva ha sido creada exitosamente.
            </div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        <form id="activityForm" action="${pageContext.request.contextPath}/AltaActividadDeportiva" method="POST" enctype="multipart/form-data">
            <div class="form-group">
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" required>
            </div>
            <div class="form-group">
                <label for="descripcion">Descripción:</label>
                <textarea id="descripcion" name="descripcion" required maxlength="255"></textarea>
            </div>
            <div class="form-group">
                <label for="duracion">Duración:</label>
                <div class="duracion-container">
                    <div class="duracion-input">
                        <label for="duracionHoras">Horas:</label>
                        <input type="number" id="duracionHoras" name="duracionHoras" min="0" value="0" required>
                    </div>
                    <div class="duracion-input">
                        <label for="duracionMinutos">Minutos:</label>
                        <input type="number" id="duracionMinutos" name="duracionMinutos" min="0" max="59" value="0" required>
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label for="costo">Costo:</label>
                <input type="number" id="costo" name="costo" min="0" step="0.01" required>
            </div>
            <div class="form-group">
                <label for="foto">Foto de la actividad:</label>
                <div class="file-input-wrapper">
                    <button class="btn-file-input" type="button">Seleccionar archivo</button>
                    <input type="file" id="foto" name="foto" accept="image/*">
                </div>
                <img id="imagePreview" src="#" alt="Vista previa de la imagen" style="display: none;">
            </div>
            <input type="hidden" id="entrenadornickname" name="entrenadornickname">
            <button type="submit" id="submitButton">Crear Actividad Deportiva</button>
        </form>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/altaActividadDeportiva.js"></script>
        <script src="${pageContext.request.contextPath}/js/CheckEntrenador.js"></script>
   <script src="${pageContext.request.contextPath}/js/ExistenciaNickname.js"></script>
   <script src="${pageContext.request.contextPath}/js/Login.js"></script>
   
       <footer class="bg-dark text-white text-center py-3">
		<%@ include file="/WEB-INF/views/footer.jsp" %>
    </footer>
</body>
</html>