package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import Clases.Fabrica;
import Clases.IControladorActividad;
import Clases.DataActividad;
import Clases.DataClase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CargarPrimerasActividades extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorActividad icontroladorActividad;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorActividad = fabrica.getIControladorActividad();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonResponse = new JsonObject();
        JsonArray actividadesArray = new JsonArray();

        List<DataActividad> todasLasActividades = icontroladorActividad.transformarActividades("Aceptada");
        List<DataActividad> primeras6Actividades = todasLasActividades.subList(0, Math.min(6, todasLasActividades.size()));

        for (DataActividad actividad : primeras6Actividades) {
            JsonObject actividadJson = new JsonObject();
            actividadJson.addProperty("nombre", actividad.getNombre());
            if (actividad.getImagen() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(actividad.getImagen());
                actividadJson.addProperty("imagen", imagenBase64);                
            }
            actividadJson.addProperty("descripcion", actividad.getDescripcion());
            
            Duration duracion = actividad.getDuracion();
            long horas = duracion.toHours();
            long minutos = duracion.toMinutes() % 60;
            String duracionFormato = String.format("%02d:%02d", horas, minutos);
            
            actividadJson.addProperty("duracion", duracionFormato);
            actividadJson.addProperty("costo", actividad.getCosto());
            
            JsonArray clasesArray = new JsonArray();
            
            for (DataClase clase : icontroladorActividad.transformarClasesActividad(actividad)) {
                JsonObject claseJson = new JsonObject();
                claseJson.addProperty("Cupo", clase.getCupo());
                claseJson.addProperty("Hora", clase.getFormattedHoraEntrenamiento());
                claseJson.addProperty("Id", clase.getId());
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String fechaFormato = clase.getFecha().format(formatter);
                claseJson.addProperty("FechaClase", fechaFormato);
                claseJson.addProperty("Lugar", clase.getLugar());
                claseJson.addProperty("DatosEntrenador", clase.getActividad().getEntrenador().getNombre() + " " + clase.getActividad().getEntrenador().getApellido() + " " + "(" + clase.getActividad().getEntrenador().getNickname()+ ")");
                
                clasesArray.add(claseJson);
            }
            actividadJson.add("clases", clasesArray);
            
            actividadesArray.add(actividadJson);
        }
        jsonResponse.add("actividades", actividadesArray);

        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonResponse);
        response.getWriter().write(jsonString);
    }
}