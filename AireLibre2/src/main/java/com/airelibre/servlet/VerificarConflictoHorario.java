package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import Clases.DataActividad;
import Clases.Fabrica;
import Clases.IControladorActividad;
import Clases.IControladorClase;

/**
 * Servlet implementation class VerificarConflictoHorario
 */
public class VerificarConflictoHorario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IControladorClase icontroladorClase;
	private IControladorActividad icontroladorActividad;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorClase = fabrica.getIControladorClase();
        icontroladorActividad = fabrica.getIControladorActividad();
    }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerificarConflictoHorario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String act = request.getParameter("actividad");
        LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
        LocalTime hora = LocalTime.parse(request.getParameter("hora"));
        DataActividad actividad = icontroladorActividad.getDataFromActividad(act);
        boolean conflictoExiste = icontroladorClase.existeConflictoHorario(actividad, fecha, hora);

        response.setContentType("application/json");
        response.getWriter().write(String.valueOf(conflictoExiste));
    }
}
