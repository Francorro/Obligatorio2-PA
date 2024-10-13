<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alta Clase Deportiva</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/AltaClaseDeportiva.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body data-context-path="${pageContext.request.contextPath}">
    <%@ include file="/WEB-INF/views/navbar.jsp" %>
    <%@ include file="/WEB-INF/views/LoginRegister.jsp" %>
    <div class="activity-form-container mt-5">
        <h1>Alta Clase Deportiva</h1>
        <form id="activityForm" action="${pageContext.request.contextPath}/AltaClaseDeportiva" method="POST">
            <div class="form-group">
                <label for="actividad">Actividad:</label>
                <select id="actividad" name="actividad" required>
                    <option value="">Seleccionar actividad...</option>
                </select>
            </div>
            <div class="form-group">
                <label for="fecha">Fecha:</label>
                <input type="date" id="fecha" name="fecha" required>
            </div>
            <div class="form-group">
                <label for="hora">Hora:</label>
                <input type="time" id="hora" name="hora" required>
            </div>
            <div class="form-group">
                <label for="lugar">Lugar:</label>
                <input type="text" id="lugar" name="lugar" required>
            </div>
            <div class="form-group">
                <label for="cupos">Cupos:</label>
                <input type="number" id="cupos" name="cupos" min="1" required>
            </div>
            <div class="form-group">
                <label for="fechaAlta">Fecha de Alta:</label>
                <input type="date" id="fechaAlta" name="fechaAlta" required>
            </div>
            <button type="submit">Crear Clase Deportiva</button>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/AltaClaseDeportiva.js"></script>
    <script src="${pageContext.request.contextPath}/js/CheckEntrenador.js"></script>
    <script src="${pageContext.request.contextPath}/js/ExistenciaNickname.js"></script>
    <script src="${pageContext.request.contextPath}/js/Login.js"></script>
    
        <footer class="bg-dark text-white text-center py-3">
		<%@ include file="/WEB-INF/views/footer.jsp" %>
    </footer>
</body>
</html>
