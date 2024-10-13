package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Clases.Fabrica;
import Clases.IControladorActividad;
import Clases.DataActividad;
import Clases.DataClase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PerfilActividad extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorActividad icontroladorActividad;

    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorActividad = fabrica.getIControladorActividad();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String activityId = request.getParameter("nombre");

        if (activityId != null && !activityId.isEmpty()) {
            DataActividad actividad = icontroladorActividad.getDataFromActividad(activityId);

            if (actividad != null) {
                JsonObject actividadJsonObject = new JsonObject();
                actividadJsonObject.addProperty("nombre", actividad.getNombre());

                if (actividad.getImagen() != null) {
                    String imagenBase64 = Base64.getEncoder().encodeToString(actividad.getImagen());
                    actividadJsonObject.addProperty("imagen", imagenBase64);
                }

                actividadJsonObject.addProperty("descripcion", actividad.getDescripcion());

                Duration duracion = actividad.getDuracion();
                long horas = duracion.toHours();
                long minutos = duracion.toMinutes() % 60;
                String duracionFormato = String.format("%02d:%02d", horas, minutos);

                actividadJsonObject.addProperty("duracion", duracionFormato);
                actividadJsonObject.addProperty("costo", actividad.getCosto());
                actividadJsonObject.addProperty("DatosEntrenador", actividad.getEntrenador().getNombre() + " " + actividad.getEntrenador().getApellido() + " (" + actividad.getEntrenador().getNickname() + ")");

                JsonArray clasesArray = new JsonArray();
                
                List<DataClase> clases = icontroladorActividad.transformarClasesActivasActividad(actividad);
                
                //ordeno las clases
                Collections.sort(clases, new Comparator<DataClase>() {
                    @Override
                    public int compare(DataClase c1, DataClase c2) {
                        // Combinar fecha y hora en LocalDateTime para una comparación precisa
                        LocalDateTime dateTime1 = LocalDateTime.of(c1.getFecha(), c1.getHoraEntrenamiento());
                        LocalDateTime dateTime2 = LocalDateTime.of(c2.getFecha(), c2.getHoraEntrenamiento());
                        return dateTime1.compareTo(dateTime2);
                    }
                }); 
                
   
                
                for (DataClase clase : clases) {
                    JsonObject claseJson = new JsonObject();
                    claseJson.addProperty("Cupo", clase.getCupo());
                    claseJson.addProperty("Hora", clase.getFormattedHoraEntrenamiento());
                    
                    claseJson.addProperty("Id", clase.getId());
                    
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String fechaFormato = clase.getFecha().format(formatter);
                    claseJson.addProperty("FechaClase", fechaFormato);
                    claseJson.addProperty("Lugar", clase.getLugar());
                  

                    clasesArray.add(claseJson);
                }

                actividadJsonObject.add("clases", clasesArray);

                Gson gson = new Gson();
                String actividadJsonString = gson.toJson(actividadJsonObject);
                request.setAttribute("actividadData", actividadJsonString);
                request.getRequestDispatcher("/PerfilActividad.jsp").forward(request, response);

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Actividad no encontrada");
            }

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de actividad no proporcionado");
        }
    }
}
