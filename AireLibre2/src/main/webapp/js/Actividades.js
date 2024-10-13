document.addEventListener('DOMContentLoaded', function() {
    fetch(contextPath + '/ActividadesData')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const activityContainer = document.getElementById('activityContainer');
            if (data && data.actividades && Array.isArray(data.actividades)) {
                if (data.actividades.length === 0) {
                    activityContainer.innerHTML = '<p class="text-center">No hay actividades disponibles.</p>';
                } else {
                    data.actividades.forEach((activity, index) => {
                        const card = createActivityCard(activity, index);
                        activityContainer.appendChild(card);
                    });
                }
            } else {
                console.error('Unexpected data structure:', data);
                activityContainer.innerHTML = '<p class="text-center">Error en la estructura de datos de las actividades.</p>';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            const activityContainer = document.getElementById('activityContainer');
            activityContainer.innerHTML = `<p class="text-center">Error al cargar las actividades: ${error.message}</p>`;
        });
});

function createActivityCard(activity, index) {
    const col = document.createElement('div');
    col.className = 'col';
    
    let imageHtml = '';
    if (activity.imagen) {
        imageHtml = `<img src="data:image/jpeg;base64,${activity.imagen}" class="card-img-top actividades-card-img" alt="${activity.nombre}">`;
    } else {
        imageHtml = `<div class="card-img-top actividades-card-img bg-secondary d-flex align-items-center justify-content-center">
                        <span class="text-white">No tiene imagen</span>
                     </div>`;
    }
    
    col.innerHTML = `
        <div class="card actividades-card h-100">
            ${imageHtml}
            <div class="card-body">
                <h5 class="card-title">${activity.nombre}</h5>
                <p class="card-text">${activity.descripcion}</p>
                <p class="card-text">Duración: ${activity.duracion}</p>
                <p class="mt-2">Costo: $${activity.costo}</p>
                <a href="${contextPath}/PerfilActividad?nombre=${activity.nombre}" class="btn btn-success">
                    Inscripciones
                </a>
                
                
            </div>
        </div>
    `;
    return col;
}



function showActivityDetails(index) {
    fetch(contextPath + '/ActividadesData')
        .then(response => response.json())
        .then(data => {
            if (data && data.actividades && Array.isArray(data.actividades) && data.actividades[index]) {
                const activity = data.actividades[index];
                const modalBody = document.querySelector('#activityModal .modal-body');
                
                let classListHtml = '<p>No hay clases programadas.</p>';
                if (activity.clases && activity.clases.length > 0) {
                    classListHtml = '<ul>';
                    activity.clases.forEach(clase => {
                        classListHtml += `
                            <li>${clase.FechaClase} a las ${clase.Hora} - ${clase.Lugar} (Cupo: ${clase.Cupo}) Datos Entrenador: ${clase.DatosEntrenador}</li>
                        `;
                    });
                    classListHtml += '</ul>';
                }

                modalBody.innerHTML = `
                    <h4>${activity.nombre}</h4>
                    <p>Duración: ${activity.duracion}</p>
                    <p>${activity.descripcion}</p>
                    <p>Costo: $${activity.costo}</p>
                    
                    <h5>Clases:</h5>
                    ${classListHtml}
                `;
            } else {
                console.error('Activity not found or unexpected data structure');
            }
        })
        .catch(error => console.error('Error:', error));
}

function signUpForActivity(activityName) {
    alert(`Te has anotado para la actividad: ${activityName}`);
}