<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head></head>
<body>
<!-- Login Modal -->
<div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="loginModalLabel">Iniciar Sesión</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="mb-3">
                        <label for="loginNickname" class="form-label">Nickname</label>
                        <input type="text" class="form-control" id="loginNickname" required>
                    </div>
                    <div class="mb-3">
                        <label for="loginPassword" class="form-label">Contraseña</label>
                        <input type="password" class="form-control" id="loginPassword" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Iniciar Sesión</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Register Modal -->
<div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="registerModalLabel">Registrarse</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
			<form action="/AireLibre2/register" method="POST" enctype="multipart/form-data">
			    <div class="mb-3">
			        <label for="registerName" class="form-label">Nombre</label>
			        <input type="text" class="form-control" id="registerName" name="registerName" required>
			    </div>
			    <div class="mb-3">
			        <label for="registerLastName" class="form-label">Apellido</label>
			        <input type="text" class="form-control" id="registerLastName" name="registerLastName" required>
			    </div>
					<div class="mb-3">
                        <label for="registerNickname" class="form-label">Nickname</label>
                        <input type="text" class="form-control" id="registerNickname" name="registerNickname" required>
                        <div id="nicknameError" class="text-danger" style="display: none;">Este nickname ya está en uso.</div>
                    </div>
			    <div class="mb-3">
			        <label for="registerPassword" class="form-label">Contraseña</label>
			        <input type="password" class="form-control" id="registerPassword" name="registerPassword" required>
			    </div>
			    <div class="mb-3">
			        <label for="registerConfirmPassword" class="form-label">Confirmar Contraseña</label>
			        <input type="password" class="form-control" id="registerConfirmPassword" required>
			    </div>
			    <div id="passwordError" class="text-danger" style="display: none;">Las contraseñas no coinciden</div>
			    <div class="mb-3">
			        <label for="registerEmail" class="form-label">Correo Electrónico</label>
			        <input type="email" class="form-control" id="registerEmail" name="registerEmail" required>
			    </div>
			    <div class="mb-3">
			        <label for="registerBirthdate" class="form-label">Fecha de Nacimiento</label>
			        <input type="date" class="form-control" id="registerBirthdate" name="registerBirthdate" required>
			    </div>
			    <div class="mb-3">
                            <label for="profileImage" class="form-label">Imagen de Perfil</label>
                            <input type="file" class="form-control" id="profileImage" name="profileImage" accept="image/*">
                        </div>
					    <div class="mb-3">
					        <label class="form-label">Tipo de Deportista</label>
					        <div class="form-check">
					            <input class="form-check-input" type="radio" name="athleteType" id="professionalAthlete" value="Si">
					            <label class="form-check-label" for="professionalAthlete">
					                Deportista Profesional
					            </label>
					        </div>
					        <div class="form-check">
					            <input class="form-check-input" type="radio" name="athleteType" id="amateurAthlete" value="No">
					            <label class="form-check-label" for="amateurAthlete">
					                Deportista No Profesional
					            </label>
					        </div>
					    </div>
					    <button type="submit" class="btn btn-primary" id="registerButton">Registrarse</button>
					</form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
