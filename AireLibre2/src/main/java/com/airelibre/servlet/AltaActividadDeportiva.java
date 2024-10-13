package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import Clases.Fabrica;
import Clases.IControladorActividad;

@MultipartConfig
public class AltaActividadDeportiva extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IControladorActividad icontroladorActividad;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorActividad = fabrica.getIControladorActividad();
    }
       
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Boolean isEntrenador = (Boolean) session.getAttribute("isEntrenador");

        if (isEntrenador != null && isEntrenador) {
            request.getRequestDispatcher("/WEB-INF/views/AltaActividadDeportiva.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Boolean isEntrenador = (Boolean) session.getAttribute("isEntrenador");

        if (isEntrenador != null && isEntrenador) {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            int duracionHoras = Integer.parseInt(request.getParameter("duracionHoras"));
            int duracionMinutos = Integer.parseInt(request.getParameter("duracionMinutos"));
            Duration duracion = Duration.ofMinutes(duracionHoras * 60 + duracionMinutos);
            double costo = Double.parseDouble(request.getParameter("costo"));
            String entrenadorNickname = request.getParameter("entrenadornickname");
        
            try {
                Part filePart = request.getPart("foto");
                byte[] fotoBytes = null;
                if (filePart != null && filePart.getSize() > 0) {
                    InputStream fileContent = filePart.getInputStream();
                    fotoBytes = fileContent.readAllBytes();
                }

                icontroladorActividad.crearActividad(nombre, descripcion, duracion, costo, entrenadorNickname, fotoBytes);
                response.sendRedirect(request.getContextPath() + "/AltaActividadDeportiva?success=true");
            } catch (Exception e) {
                request.setAttribute("error", "Error al crear la actividad: " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/AltaActividadDeportiva.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + "");
        }
    }
}