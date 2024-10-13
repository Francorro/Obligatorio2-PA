document.addEventListener('DOMContentLoaded', function() {
    var form = document.getElementById('activityForm');
    var actividadSelect = document.getElementById('actividad');

    function cargarActividades() {
        const nickname = sessionStorage.getItem('userNickname');
        const contextPath = document.body.dataset.contextPath || '';

        fetch(`${contextPath}/ActividadesEntrenador?nickname=${encodeURIComponent(nickname)}`)
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Error al cargar las actividades: ${text}`);
                    });
                }
                return response.json();
            })
            .then(actividades => {
                actividadSelect.innerHTML = '<option value="">Seleccionar actividad...</option>';
                actividades.forEach(function(actividad) {
                    var option = document.createElement('option');
                    option.value = actividad;
                    option.textContent = actividad;
                    actividadSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Error:', error);
                alert('No se pudieron cargar las actividades. Por favor, intente de nuevo más tarde.');
            });
    }

    cargarActividades();

    function handleSubmit(event) {
       event.preventDefault();

        const formData = new FormData(form);
        const data = {};
        for (let [key, value] of formData.entries()) {
            data[key] = value;
        }

        // Convertir las cadenas de fecha en objetos Date
        const today = new Date();
        today.setHours(0, 0, 0, 0);  // Ignorar la hora en la comparación

        const fecha = new Date(data.fecha);
        fecha.setHours(0, 0, 0, 0);  // Ignorar la hora

        const fechaAlta = new Date(data.fechaAlta);
        fechaAlta.setHours(0, 0, 0, 0);  // Ignorar la hora

        // Validar que la fecha no sea anterior a la fecha actual
        if (fecha < today) {
            alert("La fecha tiene que ser un dia mayor a la fecha actual.");
            return;
        }

        // Validar que la fecha de alta sea mayor a la fecha actual y menor que la fecha de la clase
        if (fechaAlta < today) {
            alert("La fecha de alta tiene que ser 1 dia mayor a la fecha actual.");
            return;
        }

        if (fechaAlta > fecha) {
            alert("La fecha de alta no puede ser posterior a la fecha de la clase.");
            return;
        }

        // Check for scheduling conflicts
        const contextPath = document.body.dataset.contextPath || '';
        fetch(`${contextPath}/VerificarConflictoHorario`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                actividad: data.actividad,
                fecha: data.fecha,
                hora: data.hora
            }).toString()
        })
        .then(response => response.json())
        .then(conflictoExiste => {
            if (conflictoExiste) {
                alert("Usted ya tiene una clase registrada para esta fecha en este rango horario.");
            } else {
                // If no conflict, proceed with form submission
                submitForm(data);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al verificar conflictos de horario: ' + error.message);
        });
    }

    function submitForm(data) {
        const contextPath = document.body.dataset.contextPath || '';

        fetch(`${contextPath}/AltaClaseDeportiva`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams(data).toString()
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.text();
        })
        .then(result => {
            alert(result);
            form.reset();
            cargarActividades();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al crear la clase deportiva: ' + error.message);
        });
    }

    form.addEventListener('submit', handleSubmit);
});