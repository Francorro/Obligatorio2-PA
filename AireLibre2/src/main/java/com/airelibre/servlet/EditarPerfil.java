package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import Clases.DataUsuario;

import java.io.IOException;


public class EditarPerfil extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    public EditarPerfil() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuario loggedInUser = (session != null) ? (DataUsuario) session.getAttribute("usuario") : null;
        String requestedNickname = request.getParameter("nickname");

        if (loggedInUser == null || !loggedInUser.getNickname().equals(requestedNickname)) {
            if (loggedInUser == null) {
                response.sendRedirect(request.getContextPath() + "");
            } else {
                response.sendRedirect(request.getContextPath() + "/Usuario?nickname=" + loggedInUser.getNickname());
            }
            return; 
        }

        request.setAttribute("userData", loggedInUser);
        request.getRequestDispatcher("/WEB-INF/views/EditarPerfil.jsp").forward(request, response);
    }
}