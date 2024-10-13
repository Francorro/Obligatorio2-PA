
// Función para cargar los datos de la actividad
function loadActivityData() {
    if (actividadData.imagen) {
        document.getElementById('activityImage').src = `data:image/jpeg;base64,${actividadData.imagen}`;
    } else {
        document.getElementById('activityImage').src = '/placeholder.svg?height=300&width=400';
    }
    document.getElementById('activityName').textContent = actividadData.nombre;
    document.getElementById('activityDescription').textContent = actividadData.descripcion;
    document.getElementById('activityDuration').textContent = actividadData.duracion;
    document.getElementById('activityCost').textContent = `$${actividadData.costo.toFixed(2)}`;
    document.getElementById('datosEntrenador').textContent = actividadData.DatosEntrenador;

    const tableBody = document.getElementById('classesTableBody');
    actividadData.clases.forEach(clase => {
        const row = tableBody.insertRow();
        row.insertCell(0).textContent = clase.FechaClase;
        row.insertCell(1).textContent = clase.Hora;
        row.insertCell(2).textContent = clase.Lugar;
        row.insertCell(3).textContent = clase.Cupo;
        row.addEventListener('click', () => handleClassClick(clase));
    });
}

// Función para manejar el clic en una clase
function handleClassClick(clase) {
    const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';
    const isEntrenador = sessionStorage.getItem('isEntrenador') === 'true';

    if (isLoggedIn && !isEntrenador) {
        showInscriptionPopup(clase);
    } else if (!isLoggedIn) {
        alert('Debes iniciar sesión como deportista para inscribirte en una clase.');
    } else {
        alert('Los entrenadores no pueden inscribirse en clases.');
    }
}

// Función para mostrar el popup de inscripción
function showInscriptionPopup(clase) {
    // Crear el popup
    const popup = document.createElement('div');
    popup.className = 'inscription-popup';
    popup.innerHTML = `
        <div class="inscription-content">
            <h2>Inscripción a la Clase</h2>
            <p>Fecha: ${clase.FechaClase}</p>
            <p>Hora: ${clase.Hora}</p>
            <p>Lugar: ${clase.Lugar}</p>
            <p>Cupos Disponibles: ${clase.Cupo}</p>
            <div class="form-group">
                <label for="cantidadDeportistas">Cantidad de Deportistas:</label>
                <input type="number" id="cantidadDeportistas" min="1" value="1">
            </div>
            <p>Costo Total: $<span id="costoTotal">${actividadData.costo.toFixed(2)}</span></p>
            <div class="button-group">
                <button id="confirmarInscripcion">Confirmar</button>
                <button id="cancelarInscripcion">Cancelar</button>
            </div>
        </div>
    `;

    // Agregar el popup al body
    document.body.appendChild(popup);

    // Manejar la actualización del costo total
    const cantidadInput = document.getElementById('cantidadDeportistas');
    const costoTotalSpan = document.getElementById('costoTotal');
    cantidadInput.addEventListener('input', () => {
        const cantidad = parseInt(cantidadInput.value) || 0;
        const costoTotal = (cantidad * actividadData.costo).toFixed(2);
        costoTotalSpan.textContent = costoTotal;
    });

    // Manejar el botón de confirmar
    document.getElementById('confirmarInscripcion').addEventListener('click', () => {
        const cantidad = parseInt(cantidadInput.value) || 0;
        if (cantidad > 0) {
            // Verificar si hay cupos suficientes
            if (clase.Cupo - cantidad < 0) {
                alert('No hay cupos suficientes para esta inscripción.');
                return;
            }

            // Obtener el nickname del deportista de la sesión
            const deportistaNickname = sessionStorage.getItem('userNickname');
            
            // Crear los datos para enviar al servidor
            const data = new URLSearchParams();
            data.append('deportistaNickname', deportistaNickname);
            data.append('claseId', clase.Id.toString());
            data.append('cantidad', cantidad.toString());
            data.append('actividadId', actividadData.nombre);

            console.log('Sending data:', {
                deportistaNickname,
                claseId: clase.Id,
                cantidad,
                actividadId: actividadData.nombre
            });

            // Enviar la solicitud al servidor
            fetch('Inscripcion', {
                method: 'POST',
                body: data,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text();
            })
            .then(text => {
                try {
                    return JSON.parse(text);
                } catch (e) {
                    console.error('Server response:', text);
                    throw new Error('The server returned an invalid JSON response');
                }
            })
            .then(result => {
                if (result.success) {
                    alert(result.message);
                    document.body.removeChild(popup);
                    // Aquí puedes actualizar la UI si es necesario, por ejemplo, actualizando el cupo de la clase
                    updateClassCapacity(clase.Id, cantidad);
                } else {
                    alert('Error: ' + result.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al procesar la inscripción: ' + error.message);
            });
        } else {
            alert('Por favor, ingrese una cantidad válida de deportistas.');
        }
    });

    // Manejar el botón de cancelar
    document.getElementById('cancelarInscripcion').addEventListener('click', () => {
        document.body.removeChild(popup);
    });
}

// Función para actualizar el cupo de la clase en la UI
function updateClassCapacity(claseId, inscriptionCount) {
    const tableRows = document.getElementById('classesTableBody').rows;
    for (let i = 0; i < tableRows.length; i++) {
        if (actividadData.clases[i].Id === claseId) {
            const newCapacity = actividadData.clases[i].Cupo - inscriptionCount;
            tableRows[i].cells[3].textContent = newCapacity;
            actividadData.clases[i].Cupo = newCapacity;
            break;
        }
    }
}

// Cargar los datos cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', loadActivityData);