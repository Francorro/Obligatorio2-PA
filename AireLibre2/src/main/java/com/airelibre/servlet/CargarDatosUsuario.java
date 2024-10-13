package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import Clases.DataEntrenador;
import Clases.DataDeportista;
import Clases.Fabrica;
import Clases.IControladorUsuario;
import Clases.DataActividad;
import Clases.IControladorActividad;
import Clases.IControladorInscripcion;
import Clases.DataInscripcion;
import Clases.DataUsuario;
import Clases.IControladorClase;
import Clases.DataClase;

public class CargarDatosUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorUsuario icontroladorUsuario;
    private IControladorActividad icontroladorActividad;
    private IControladorInscripcion icontroladorInscripcion;
    private IControladorClase icontroladorClase;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorUsuario = fabrica.getIControladorUsuario();
        icontroladorActividad = fabrica.getIControladorActividad();
        icontroladorInscripcion = fabrica.getIControladorInscripcion();
        icontroladorClase = fabrica.getIControladorClase();
    }
    
    public CargarDatosUsuario() {
        super();
    }

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
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        if (nickname == null || nickname.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nickname is required");
            return;
        }

        DataEntrenador entrenador = icontroladorUsuario.getEntrenador(nickname);
        DataDeportista deportista = null;
        
        if (entrenador == null) {
            deportista = icontroladorUsuario.getDeportista(nickname);
            if (deportista == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                return;
            }
        }

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

        JsonObject jsonResponse = new JsonObject();
        
        if (entrenador != null) {
            populateJsonWithEntrenador(request, jsonResponse, entrenador);
        } else {
            populateJsonWithDeportista(request, jsonResponse, deportista);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    private void populateJsonWithEntrenador(HttpServletRequest request, JsonObject jsonResponse, DataEntrenador entrenador) {
        jsonResponse.addProperty("nombre", entrenador.getNombre());
        jsonResponse.addProperty("apellido", entrenador.getApellido());
        jsonResponse.addProperty("nickname", entrenador.getNickname());
        jsonResponse.addProperty("email", entrenador.getCorreoElectronico());
        jsonResponse.addProperty("fechaNacimiento", entrenador.getFechaNacimiento().format(DateTimeFormatter.ISO_LOCAL_DATE));
        jsonResponse.addProperty("tipo", "Entrenador");
        jsonResponse.addProperty("web", entrenador.getSitioWeb());
        jsonResponse.addProperty("disciplina", entrenador.getDisciplina());
        jsonResponse.addProperty("color", entrenador.getCoverColor());
        jsonResponse.addProperty("pass", entrenador.getPass());
        
        if (entrenador.getImagen() != null) {
            String imagenBase64 = Base64.getEncoder().encodeToString(entrenador.getImagen());
            jsonResponse.addProperty("imagenBase64", imagenBase64);
        }
        
        jsonResponse.addProperty("esProfesional", "Si");

        JsonArray actividadesArray = new JsonArray();
        
        boolean EsDueñoPerfil = isProfileOwner(request, entrenador.getNickname());
        
        List<DataActividad> actividades;
        if (EsDueñoPerfil) {
            actividades = icontroladorActividad.transformarActividadesEntrenador(entrenador,"");
        } else {
            actividades = icontroladorActividad.transformarActividadesEntrenador(entrenador, "Aceptada");
        }
        
        for (DataActividad actividad : actividades) {
            JsonObject actividadJson = new JsonObject();
            actividadJson.addProperty("nombre", actividad.getNombre());
            
            Duration duracion = actividad.getDuracion();
            long horas = duracion.toHours();
            long minutos = duracion.toMinutes() % 60;
            String duracionFormato = String.format("%02d:%02d", horas, minutos);
            
            actividadJson.addProperty("duracion", duracionFormato);
            actividadJson.addProperty("costo", actividad.getCosto());
            actividadJson.addProperty("estado", actividad.getEstado());
            
            JsonArray clasesArray = new JsonArray();
            List<DataClase> clasesord = icontroladorActividad.transformarClasesActividad(actividad);
            
            Collections.sort(clasesord, new Comparator<DataClase>() {
                @Override
                public int compare(DataClase c1, DataClase c2) {
                    LocalDateTime dateTime1 = LocalDateTime.of(c1.getFecha(), c1.getHoraEntrenamiento());
                    LocalDateTime dateTime2 = LocalDateTime.of(c2.getFecha(), c2.getHoraEntrenamiento());
                    return dateTime1.compareTo(dateTime2);
                }
            }); 
            
            for (DataClase clase : clasesord) {
                JsonObject claseJson = new JsonObject();
                claseJson.addProperty("Cupo", clase.getCupo());  
                claseJson.addProperty("Hora", clase.getFormattedHoraEntrenamiento()); 
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String fechaFormato = clase.getFecha().format(formatter);
                claseJson.addProperty("FechaClase", fechaFormato);
                claseJson.addProperty("Lugar", clase.getLugar());
                String fechaFormato2 = clase.getFechaAlta().format(formatter);
                claseJson.addProperty("FechaAlta", fechaFormato2);
                
                clasesArray.add(claseJson);
            }
            actividadJson.add("clases", clasesArray);
            
            actividadesArray.add(actividadJson);
        }
        jsonResponse.add("actividades", actividadesArray);
    }

    private void populateJsonWithDeportista(HttpServletRequest request, JsonObject jsonResponse, DataDeportista deportista) {
        jsonResponse.addProperty("nombre", deportista.getNombre());
        jsonResponse.addProperty("apellido", deportista.getApellido());
        jsonResponse.addProperty("nickname", deportista.getNickname());
        jsonResponse.addProperty("email", deportista.getCorreoElectronico());
        jsonResponse.addProperty("fechaNacimiento", deportista.getFechaNacimiento().format(DateTimeFormatter.ISO_LOCAL_DATE));
        jsonResponse.addProperty("tipo", "Deportista");
        jsonResponse.addProperty("color", deportista.getCoverColor());
        jsonResponse.addProperty("pass", deportista.getPass());

        if (deportista.getImagen() != null) {
            String imagenBase64 = Base64.getEncoder().encodeToString(deportista.getImagen());
            jsonResponse.addProperty("imagenBase64", imagenBase64);
        }

        jsonResponse.addProperty("esProfesional", deportista.getEsProfesional());

        List<Pair<DataClase, DataInscripcion>> clasesList = new ArrayList<>();
        for (DataInscripcion inscripcion : icontroladorInscripcion.obtenerInscripcionDepor(deportista)) {
            DataInscripcion ins = icontroladorClase.getDataFromInscripcion(inscripcion.getId());
            DataClase clase = ins.getClase();
            clasesList.add(new Pair<>(clase, ins));
        }

        Collections.sort(clasesList, new Comparator<Pair<DataClase, DataInscripcion>>() {
            @Override
            public int compare(Pair<DataClase, DataInscripcion> p1, Pair<DataClase, DataInscripcion> p2) {
                LocalDateTime dateTime1 = LocalDateTime.of(p1.getFirst().getFecha(), p1.getFirst().getHoraEntrenamiento());
                LocalDateTime dateTime2 = LocalDateTime.of(p2.getFirst().getFecha(), p2.getFirst().getHoraEntrenamiento());
                return dateTime1.compareTo(dateTime2);
            }
        });

        JsonArray clasesArray = new JsonArray();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Pair<DataClase, DataInscripcion> pair : clasesList) {
            DataClase clase = pair.getFirst();
            DataInscripcion ins = pair.getSecond();

            JsonObject claseJson = new JsonObject();
            claseJson.addProperty("Cupo", clase.getCupo());
            claseJson.addProperty("Hora", clase.getFormattedHoraEntrenamiento());
            String fechaFormato = clase.getFecha().format(formatter);
            claseJson.addProperty("FechaClase", fechaFormato);
            claseJson.addProperty("Lugar", clase.getLugar());
            claseJson.addProperty("NombreActividad", clase.getActividad().getNombre());

            Duration duracion = clase.getActividad().getDuracion();
            long horas = duracion.toHours();
            long minutos = duracion.toMinutes() % 60;
            String duracionFormato = String.format("%02d:%02d", horas, minutos);
            claseJson.addProperty("DuracionActividad", duracionFormato);
            claseJson.addProperty("CostoActividad", clase.getActividad().getCosto());
            claseJson.addProperty("CantidadDeportistasInscriptos", ins.getCantidadDeportistas());

            String fechaInscripcionFormato = ins.getFechaInsc().format(formatter);
            claseJson.addProperty("FechaInscripcion", fechaInscripcionFormato);
            claseJson.addProperty("NombreEntrenador", clase.getActividad().getEntrenador().getNombre() + " " + clase.getActividad().getEntrenador().getApellido() + " " + "(" + clase.getActividad().getEntrenador().getNickname() + ")");

            clasesArray.add(claseJson);
        }

        jsonResponse.add("clases", clasesArray);
    }

    private boolean isProfileOwner(HttpServletRequest request, String profileNickname) {
        HttpSession session = request.getSession(false);
        DataUsuario loggedInUser = (session != null) ? (DataUsuario) session.getAttribute("usuario") : null;
        String requestedNickname = request.getParameter("nickname");
        
        // Debug logging
        System.out.println("Profile Nickname: " + profileNickname);
        System.out.println("Requested Nickname: " + requestedNickname);
        System.out.println("Logged-in User: " + (loggedInUser != null ? loggedInUser.getNickname() : "null"));
        
        boolean isOwner = false;
        if (loggedInUser != null && requestedNickname != null) {
            isOwner = loggedInUser.getNickname().equals(requestedNickname);
        }
        
        System.out.println("Is Profile Owner: " + isOwner);
        
        return isOwner;
    }
    
    class Pair<K, V> {
        private K first;
        private V second;

        public Pair(K first, V second) {
            this.first = first;
            this.second = second;
        }

        public K getFirst() {
            return first;
        }

        public V getSecond() {
            return second;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}