package com.airelibre.servlet;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import Clases.Fabrica;
import Clases.IControladorUsuario;
import Clases.DataUsuario;

public class CargarDatosListaUsuarios extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IControladorUsuario icontroladorUsuario;
    
    @Override
    public void init() throws ServletException {
        // Obtener la instancia de Fabrica y los controladores
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorUsuario = fabrica.getIControladorUsuario();
    }
    
    // Adaptador personalizado para LocalDate
    public class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
            jsonWriter.value(localDate.format(formatter));
        }

        @Override
        public LocalDate read(JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString(), formatter);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Desactivar la caché del navegador
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        List<DataUsuario> usuarios = icontroladorUsuario.listaUsuarios();

        // Crear un StringBuilder para construir el JSON manualmente
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("[");

        for (int i = 0; i < usuarios.size(); i++) {
            DataUsuario usuario = usuarios.get(i);
            byte[] imagenBytes = usuario.getImagen(); // Suponiendo que la imagen es un arreglo de bytes
            String imagenBase64 = "";

            if (imagenBytes != null) {
                imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes); // Codificar a Base64
            }

            // Construir el JSON para cada usuario
            jsonResponse.append("{")
                        .append("\"nickname\":\"").append(usuario.getNickname()).append("\",")
                        .append("\"nombre\":\"").append(usuario.getNombre()).append("\",")
                        .append("\"apellido\":\"").append(usuario.getApellido()).append("\",")
                        .append("\"imagenBase64\":\"").append(imagenBase64).append("\"")
                        .append("}");

            if (i < usuarios.size() - 1) {
                jsonResponse.append(",");  // Separador de objetos JSON
            }
        }

        jsonResponse.append("]");

        // Configurar la respuesta HTTP para devolver JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Escribir la respuesta
        response.getWriter().write(jsonResponse.toString());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
