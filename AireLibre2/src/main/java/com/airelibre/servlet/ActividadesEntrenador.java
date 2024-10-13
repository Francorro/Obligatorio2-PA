package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import Clases.Fabrica;
import Clases.IControladorActividad;
import Clases.IControladorUsuario;
import Clases.DataActividad;
import Clases.DataEntrenador;
import Clases.DataUsuario;

public class ActividadesEntrenador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorActividad icontroladorActividad;
    private IControladorUsuario icontroladorUsuario;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorActividad = fabrica.getIControladorActividad();
        icontroladorUsuario = fabrica.getIControladorUsuario();
    }   

    public ActividadesEntrenador() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String requestedNickname = request.getParameter("nickname");

            if (requestedNickname == null || requestedNickname.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Nickname no proporcionado\"}");
                return;
            }

            DataEntrenador entrenador = icontroladorUsuario.getEntrenador(requestedNickname);
            if (entrenador == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\": \"Entrenador no encontrado\"}");
                return;
            }

            List<DataActividad> actividades = icontroladorActividad.transformarActividadesEntrenador(entrenador, "Aceptada");
            List<String> nombresActividades = actividades.stream()
                .map(DataActividad::getNombre)
                .collect(Collectors.toList());

            Gson gson = new Gson();
            String jsonActividades = gson.toJson(nombresActividades);

            out.print(jsonActividades);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}