<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/EditarPerfil.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
</head>
<body>
<%@ include file="/WEB-INF/views/navbar.jsp" %>
<%@ include file="/WEB-INF/views/LoginRegister.jsp" %>
    <div class="edit-profile-container mt-5">
        <h1>Editar Perfil</h1>
<form id="editProfileForm" action="${pageContext.request.contextPath}/ActualizarPerfil" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label for="nombre">Nombre:</label>
        <input type="text" id="nombre" name="nombre" required>
    </div>
    <div class="form-group">
        <label for="apellido">Apellido:</label>
        <input type="text" id="apellido" name="apellido" required>
    </div>
				<div class="form-group">
                <label for="actualPassword" class="form-label">Contraseña actual</label>
                <input type="password" class="form-control" id="actualPassword" name="actualPassword"  placeholder="Ingrese su contraseña actual" required>
            </div>
		<div class="form-group">
		    <label for="EditregistredPassword" class="form-label">Nueva Contraseña</label>
		    <input type="password" class="form-control" id="EditregistredPassword" name="EditregistredPassword" placeholder="Si no se ingresan datos, se mantiene la contraseña">
		</div>
		<div class="form-group">
		    <label for="EditregisterConfirmPassword" class="form-label">Confirmar Nueva Contraseña</label>
		    <input type="password" class="form-control" id="EditregisterConfirmPassword" name="EditregisterConfirmPassword" placeholder="Si no se ingresan datos, se mantiene la contraseña">
		</div>
		<div id="EditPasswordError" class="text-danger" style="display: none;">Las contraseñas no coinciden</div>
    <div class="form-group">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
    </div>
    <div class="form-group">
        <label for="fechaNacimiento">Fecha de Nacimiento:</label>
        <input type="date" id="fechaNacimiento" name="fechaNacimiento" required>
    </div>
		<div class="form-group" id="websiteGroup" style="display: none;">
		    <label for="web">Sitio Web:</label>
		    <input type="text" id="web" name="web" onblur="validateWebsite()">
		</div>
    <div class="form-group" id="disciplineGroup" style="display: none;">
        <label for="disciplina">Disciplina:</label>
        <input type="text" id="disciplina" name="disciplina">
    </div>
    <div class="form-group">
        <label for="profileImage">Imagen de Perfil:</label>
        <input type="file" id="profileImage" name="profileImage" accept="image/*">
    </div>
    <div class="form-group">
        <label for="coverColor">Color de Portada:</label>
        <input type="color" id="coverColor" name="coverColor">
    </div>

    <!-- Campo oculto para el tipo de usuario -->
    <input type="hidden" id="tipo" name="tipo" value="">

    <!-- Campo oculto para esProfesional -->
    <input type="hidden" id="esProfesional" name="esProfesional" value="">
  

    <button type="submit" id="guardarCambios">Guardar Cambios</button>
</form>

    </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/EditarPerfil.js"></script>
            <script src="js/CheckEntrenador.js"></script>
   <script src="js/ExistenciaNickname.js"></script>
   <script src="js/Login.js"></script>
   
       <footer class="bg-dark text-white text-center py-3">
		<%@ include file="/WEB-INF/views/footer.jsp" %>
    </footer>
</body>
</html>
