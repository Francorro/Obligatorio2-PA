package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

import Clases.Fabrica;
import Clases.IControladorUsuario;
import Clases.DataUsuario;

public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorUsuario icontroladorUsuario;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorUsuario = fabrica.getIControladorUsuario();
    }
       
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String nickname = request.getParameter("nickname");
            String password = request.getParameter("pass");
            
            DataUsuario usuario = icontroladorUsuario.login(nickname, password);
            
            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                
                // Convertir la imagen a Base64
                String imageBase64 = "";
                if (usuario.getImagen() != null) {
                    imageBase64 = Base64.getEncoder().encodeToString(usuario.getImagen());
                }
                
                // Determinar si es Entrenador o Deportista
                boolean isEntrenador = icontroladorUsuario.getDeportista(nickname) == null;
                
                // Usar StringBuilder para construir el JSON
                StringBuilder jsonResponse = new StringBuilder();
                jsonResponse.append("{");
                jsonResponse.append("\"success\": true,");
                jsonResponse.append("\"message\": \"Login successful\",");
                jsonResponse.append("\"imageBytes\": \"").append(imageBase64).append("\",");
                jsonResponse.append("\"isEntrenador\": ").append(isEntrenador);
                jsonResponse.append("}");
                
                out.print(jsonResponse.toString());
            } else {
                out.print("{\"success\": false, \"message\": \"Credenciales inválidas\"}");
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"message\": \"Error en el servidor: " + e.getMessage().replace("\"", "'") + "\"}");
        } finally {
            out.flush();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}