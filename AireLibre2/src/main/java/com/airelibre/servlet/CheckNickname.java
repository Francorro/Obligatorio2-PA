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
 * Servlet implementation class CheckNickname
 */
public class CheckNickname extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private IControladorUsuario icontroladorUsuario;

    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorUsuario = fabrica.getIControladorUsuario();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        boolean exists = icontroladorUsuario.CheckNickname(nickname);  // Método en tu controlador que valida si el nickname existe

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"exists\": " + exists + "}");
    }
}
