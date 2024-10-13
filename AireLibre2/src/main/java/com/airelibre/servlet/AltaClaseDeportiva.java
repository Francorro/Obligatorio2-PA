package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import Clases.Fabrica;
import Clases.IControladorActividad;
import Clases.IControladorClase;

public class AltaClaseDeportiva extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IControladorClase icontroladorClase;
    private IControladorActividad icontroladorActividad;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorClase = fabrica.getIControladorClase();
        icontroladorActividad = fabrica.getIControladorActividad();
    }
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Boolean isEntrenador = (Boolean) session.getAttribute("isEntrenador");

        if (isEntrenador != null && isEntrenador) {
            request.getRequestDispatcher("/WEB-INF/views/AltaClaseDeportiva.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Boolean isEntrenador = (Boolean) session.getAttribute("isEntrenador");

        if (isEntrenador != null && isEntrenador) {
            // Obtener los parámetros del formulario
            String actividad = request.getParameter("actividad");
            String fechaStr = request.getParameter("fecha");
            String horaStr = request.getParameter("hora");
            String lugar = request.getParameter("lugar");
            String cuposStr = request.getParameter("cupos");
            String fechaAltaStr = request.getParameter("fechaAlta");
            
            System.out.println("actividad: " + request.getParameter("actividad"));
            System.out.println("fecha: " + request.getParameter("fecha"));
            System.out.println("hora: " + request.getParameter("hora"));
            System.out.println("lugar: " + request.getParameter("lugar"));
            System.out.println("cupos: " + request.getParameter("cupos"));
            System.out.println("fechaAlta: " + request.getParameter("fechaAlta"));

            // Validar que los campos no sean nulos o vacíos
            if (actividad == null || fechaStr == null || horaStr == null || lugar == null || cuposStr == null || fechaAltaStr == null
                || actividad.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || lugar.isEmpty() || cuposStr.isEmpty() || fechaAltaStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: Todos los campos son obligatorios");
                return;
            }

            try {
                // Parsear los datos
                LocalDate fecha = LocalDate.parse(fechaStr);
                LocalTime hora = LocalTime.parse(horaStr);
                int cupos = Integer.parseInt(cuposStr);
                LocalDate fechaAlta = LocalDate.parse(fechaAltaStr);

                // Llamar al controlador para guardar la clase
                icontroladorClase.guardarClase(icontroladorActividad.getDataFromActividad(actividad), fecha, hora, lugar, cupos, fechaAlta);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Clase deportiva creada con éxito!");
            } catch (DateTimeParseException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: Formato de fecha u hora inválido");
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Error: Formato de cupos inválido");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error al crear la clase: " + e.getMessage());
            }
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Acceso denegado: El usuario no es un entrenador");
        }
    }
}
