// Función para crear y añadir filas a la tabla
function populateTable(people) {
    const tableBody = document.querySelector("#personTable tbody");
    
    // Limpiar la tabla antes de rellenar
    tableBody.innerHTML = "";

    people.forEach(person => {
        const row = document.createElement("tr");
        row.style.cursor = "pointer";
        row.dataset.nickname = person.nickname;

        // Agregar las celdas correspondientes
        const photoCell = document.createElement("td");
        const img = document.createElement("img");

        // Verificar si la imagen en Base64 está disponible
        img.src = person.imagenBase64 
            ? `data:image/jpeg;base64,${person.imagenBase64}`
            : 'https://i.imgur.com/rLk1DJC.jpeg';  // Ruta de la imagen por defecto
        img.alt = `${person.nombre} ${person.apellido}`;
        img.className = "avatar";

        photoCell.appendChild(img);

        // Celda del nombre
        const nombreCell = document.createElement("td");
        nombreCell.textContent = person.nombre;
        
        // Celda del apellido
        const apellidoCell = document.createElement("td");
        apellidoCell.textContent = person.apellido;
        
        // Celda del nickname
        const nicknameCell = document.createElement("td");
        nicknameCell.textContent = person.nickname;
        
        // Añadir celdas a la fila
        row.appendChild(photoCell);
        row.appendChild(nombreCell);
        row.appendChild(apellidoCell);
        row.appendChild(nicknameCell);

        // Agregar evento de clic a la fila
        row.addEventListener('click', () => {
            window.location.href = `/AireLibre2/Usuario?nickname=${person.nickname}`;
        });

        // Agregar la fila al cuerpo de la tabla
        tableBody.appendChild(row);
    });
}

// Función para obtener y poblar los usuarios
function fetchUsuarios() {
    console.log('Iniciando fetch de usuarios');
    fetch('CargarDatosListaUsuarios')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los datos');
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos del servidor:', data);  // Debug
            populateTable(data);  // Llenar la tabla con los datos
        })
        .catch(error => console.error('Error fetching data:', error));
}

// Llamar a la función cuando el DOM esté completamente cargado
document.addEventListener("DOMContentLoaded", fetchUsuarios);