 // Parametros URL
function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
}

// Consigo nickname de usuario logeado
function getLoggedInUserNickname() {
    return sessionStorage.getItem('userNickname') || null;
}

// Cargo los datos del usuario seleccionado
function fetchUserData(nickname) {
    console.log('Fetching data for nickname:', nickname);
    fetch(`/AireLibre2/CargarDatosUsuario?nickname=${nickname}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los datos del usuario');
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos del usuario recibidos:', data);
            populateUserProfile(data);
            setupEditProfileButton(data.nickname);
        })
        .catch(error => {
            console.error('Error fetching user data:', error);
            showError('Error al cargar los datos del usuario.');
        });
}

// Lleno el perfil con los datos
function populateUserProfile(userData) {
    if (!userData) {
        console.error('No se recibieron datos de usuario');
        showError('No se recibieron datos de usuario.');
        return;
    }

    console.log('Populating user profile with data:', userData);
    
        // Asignar el color de portada dinámicamente
    const coverPhotoElement = document.getElementById('coverPhoto');
    if (coverPhotoElement && userData.color) {
        coverPhotoElement.style.backgroundColor = userData.color;
    }

  
    document.getElementById('fullName').textContent = `${userData.nombre || ''} ${userData.apellido || ''}`.trim();
    document.getElementById('nickname').textContent = userData.nickname || '';
    document.getElementById('email').textContent = userData.email || '';
    document.getElementById('birthDate').textContent = userData.fechaNacimiento || '';
    
	const professionalStatus = document.getElementById('professionalStatus');
	if (professionalStatus) {
	    professionalStatus.textContent = userData.esProfesional === 'Si' ? 'Profesional' : 'Aficionado';
	    professionalStatus.className = userData.esProfesional === 'Si' ? 'professional-status professional' : 'professional-status amateur';
	}
  

    
    const userType = userData.tipo === 'Entrenador' ? 'Entrenador' : 'Deportista';
    const userTypeElement = document.getElementById('userType');
    userTypeElement.textContent = userType;
    userTypeElement.className = `user-type ${userType.toLowerCase()}`;
    document.getElementById('userTypeInfo').textContent = `Información del ${userType}`;


    if (userData.tipo === 'Entrenador') {

	
        const websiteContainer = document.getElementById('websiteContainer');
        const disciplineContainer = document.getElementById('disciplineContainer');
        
        if (websiteContainer && disciplineContainer) {
            websiteContainer.style.display = 'flex';
            disciplineContainer.style.display = 'flex';
            
            document.getElementById('website').textContent = userData.web || 'No especificado';
            document.getElementById('website').setAttribute("href", `https://${userData.web}`);
            document.getElementById('discipline').textContent = userData.disciplina || 'No especificada';
            
        }
    }
    
    // Seteo imagen
    const profileImg = document.getElementById('profileImg');
    if (profileImg) {
        if (userData.imagenBase64) {
            profileImg.src = `data:image/jpeg;base64,${userData.imagenBase64}`;
        } else {
            profileImg.src = 'https://i.imgur.com/rLk1DJC.jpeg';
        }
        profileImg.alt = `Foto de perfil de ${userData.nombre || 'usuario'}`;
    }
    

    
    // Cargo los datos de las actividades
    const tableBody = document.getElementById('activitiesTableBody');
    const nombreSesion = sessionStorage.getItem('userNickname');
    const deberiaMostrarEstado = userData.nickname === nombreSesion && sessionStorage.getItem('isEntrenador') === 'true';
    

	if (tableBody) {
	    tableBody.innerHTML = ''; // Limpio el contenido si ya esta cargada
	
	    // Creo los headers
	    let tableHeaders = `
	        <th>Nombre</th>
	        <th>Duración</th>
	        <th>Costo</th>`;
	    
	    if (deberiaMostrarEstado) {
	        tableHeaders += `<th>Estado</th>`;
	    }
	
	    // Agrego columnas
	    const headerRow = document.createElement('tr');
	    headerRow.innerHTML = tableHeaders;
	    tableBody.appendChild(headerRow);
        if (Array.isArray(userData.actividades) && userData.actividades.length > 0) {
            userData.actividades.forEach(activity => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${activity.nombre || ''}</td>
                    <td>${activity.duracion || ''}</td>
                    <td>${activity.costo || 'Gratuito'}</td>
                    ${deberiaMostrarEstado ? `<td>${activity.estado || ''}</td>` : ''}
                `
                             row.addEventListener('click', () => {
                    currentActivityName = activity.nombre; 
                    showClassDetails(activity.clases, userData.tipo === 'Entrenador');
                });
                tableBody.appendChild(row);
            });
        } else if (Array.isArray(userData.clases) && userData.clases.length > 0) {
            // Agrupo clases por normbe de actividad
            const classesByActivity = userData.clases.reduce((acc, clase) => {
                if (!acc[clase.NombreActividad]) {
                    acc[clase.NombreActividad] = [];
                }
                acc[clase.NombreActividad].push(clase);
                return acc;
            }, {});

            // Creo una fila para cada actividad
            Object.entries(classesByActivity).forEach(([activityName, classes]) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${activityName}</td>
                    <td>${classes[0].DuracionActividad || ''}</td>
                    <td>${classes[0].CostoActividad || 'Gratuito'}</td>
                    ${deberiaMostrarEstado ? `<td>${classes[0].EstadoActividad || ''}</td>` : ''}
                `;
                row.addEventListener('click', () => showClassDetails(classes, userData.tipo === 'Entrenador'));
                tableBody.appendChild(row);
            });
        } else {
            const row = document.createElement('tr');
            row.innerHTML = `<td colspan="${deberiaMostrarEstado ? '4' : '3'}">No hay actividades registradas</td>`;
            tableBody.appendChild(row);
        }
    }
}

// Funcion para setear el boton de EditarPerfil
function setupEditProfileButton(userNickname) {
    const editProfileButton = document.getElementById('editProfileButton');
    if (editProfileButton) {
        const loggedInUserNickname = getLoggedInUserNickname();
        console.log('Logged in user:', loggedInUserNickname);
        console.log('Profile user:', userNickname);
        if (loggedInUserNickname === userNickname) {
            editProfileButton.style.display = 'block';
            editProfileButton.addEventListener('click', () => {
                console.log('Edit profile button clicked');
                window.location.href = `/AireLibre2/EditarPerfil?nickname=${userNickname}`;
            });
        } else {
            editProfileButton.style.display = 'none';
        }
    }
}

function showClassDetails(classes, isTrainer) {
  const contextPath = window.contextPath || '';
  const popup = document.createElement('div');
  popup.className = 'popup';
  
  const loggedInUserNickname = getLoggedInUserNickname();
  const isOwnProfile = loggedInUserNickname === getUrlParameter('nickname');

  let tableHeaders, classesHtml;

  if (isTrainer) {
    tableHeaders = `
      <th>Fecha</th>
      <th>Hora</th>
      <th>Lugar</th>
      <th>Cupo Restantes</th>
       ${isOwnProfile ? `
            <th>Fecha Alta</th>
            ` : ''}
    `;
    classesHtml = classes.map(classData => `
      	<tr onclick="window.location.href='${contextPath}/PerfilActividad?nombre=${encodeURIComponent(currentActivityName)}'">
        <td>${classData.FechaClase || ''}</td>
        <td>${classData.Hora || ''}</td>
        <td>${classData.Lugar || ''}</td>
        <td>${classData.Cupo !== undefined ? classData.Cupo : '0'}</td>
                ${isOwnProfile ? `
        <td>${classData.FechaAlta || ''}</td>
        ` : ''}
      </tr>
    `).join('');
  } else {
        tableHeaders = `
            <th>Fecha</th>
            <th>Hora</th>
            <th>Lugar</th>
            <th>Cupos Restantes</th>
            <th>Entrenador</th>
            ${isOwnProfile ? `
            <th>Cantidad Inscriptos</th>
            <th>Fecha Inscripción</th>
            <th>Costo total</th>
            ` : ''}
        `;
        classesHtml = classes.map(classData => {
            const costoTotal = isOwnProfile ? (classData.CantidadDeportistasInscriptos * classData.CostoActividad).toFixed(2) : '';
            return `
    	<tr onclick="window.location.href='${contextPath}/PerfilActividad?nombre=${encodeURIComponent(classData.NombreActividad)}'">
        <td>${classData.FechaClase || ''}</td>
        <td>${classData.Hora || ''}</td>
        <td>${classData.Lugar || ''}</td>
        <td>${classData.Cupo !== undefined ? classData.Cupo : '0'}</td>
        <td>${classData.NombreEntrenador || ''}</td>
        ${isOwnProfile ? `
        <td>${classData.CantidadDeportistasInscriptos || ''}</td>
        <td>${classData.FechaInscripcion || ''}</td>
        <td>$${costoTotal || 'Sin Costo'}</td>
        ` : ''}
    </tr>
        `}).join('');
    }
if(isTrainer){
    popup.innerHTML = `
        <div class="popup-content">
            <h2>Clases dictadas</h2>
            <table>
                <thead>
                    <tr>
                        ${tableHeaders}
                    </tr>
                </thead>
                <tbody>
                    ${classesHtml}
                </tbody>
            </table>
            <button onclick="closePopup()">Cerrar</button>
        </div>
    `;
       }else{
		       popup.innerHTML = `
        <div class="popup-content">
            <h2>Clases inscripto</h2>
            <table>
                <thead>
                    <tr>
                        ${tableHeaders}
                    </tr>
                </thead>
                <tbody>
                    ${classesHtml}
                </tbody>
            </table>
            <button onclick="closePopup()">Cerrar</button>
        </div>
    `;
		   
	}
    document.body.appendChild(popup);

  
}

// Funcion para cerrar popups
function closePopup() {
    const popup = document.querySelector('.popup');
    if (popup) {
        popup.remove();
    }
}

// Funcion para mostrar mensajes de error
function showError(message) {
    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;
    document.body.insertBefore(errorElement, document.body.firstChild);
}

// Main
document.addEventListener('DOMContentLoaded', () => {
    const nickname = getUrlParameter('nickname');
    if (nickname) {
        fetchUserData(nickname);
    } else {
        console.error('No nickname provided in URL');
        showError('No se proporcionó un nickname en la URL.');
    }
    
});
    