package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import Clases.Fabrica;
import Clases.IControladorActividad;

public class ExisteActividad extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorActividad icontroladorActividad;
       
    public ExisteActividad() {
        super();
    }

    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorActividad = fabrica.getIControladorActividad();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String nombre = request.getParameter("nombre");
            boolean existe = icontroladorActividad.existeActividad(nombre);
            
            response.getWriter().write("{\"exists\": " + existe + "}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}