package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;

import Clases.Fabrica;
import Clases.IControladorUsuario;



/**
 * Servlet implementation class IniciarBase
 */
public class IniciarBase extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private IControladorUsuario icontroladorUsuario;
	
    @Override
    public void init() throws ServletException {
        // Obtener la instancia de Fabrica y los controladores
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorUsuario = fabrica.getIControladorUsuario();
    }
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IniciarBase() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        icontroladorUsuario.iniciarConexionBaseDeDatos();
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
