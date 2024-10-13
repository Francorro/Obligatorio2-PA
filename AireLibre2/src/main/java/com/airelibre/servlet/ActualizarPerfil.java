package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import Clases.DataUsuario;
import Clases.Fabrica;
import Clases.IControladorUsuario;

@MultipartConfig // Required for handling file uploads
public class ActualizarPerfil extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IControladorUsuario icontroladorUsuario;

    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorUsuario = fabrica.getIControladorUsuario();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DataUsuario loggedInUser = (session != null) ? (DataUsuario) session.getAttribute("usuario") : null;
        String oldNickname = loggedInUser.getNickname();

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String email = request.getParameter("email");
        String fechaNacimientoStr = request.getParameter("fechaNacimiento");
        String esEntrenadorStr = request.getParameter("tipo");
        String web = request.getParameter("web");
        String disciplina = request.getParameter("disciplina");
        String esProfesionalStr = request.getParameter("esProfesional");
        String coverColor = request.getParameter("coverColor");
        String pass= request.getParameter("pass");

        Part filePart = request.getPart("profileImage");
        byte[] imageBytes = null;

        if (filePart != null && filePart.getSize() > 0) {
            InputStream inputStream = filePart.getInputStream();
            BufferedImage originalImage = ImageIO.read(inputStream);

            // Cambio el tamaño de la imagen
            BufferedImage resizedImage = new BufferedImage(168, 168, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 168, 168, null);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            baos.flush();
            imageBytes = baos.toByteArray();  // Get the byte array of the image
            baos.close();
        }

        // Convierto datastring a localdate
        LocalDate fechaNacimiento = null;
        if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
            try {
                fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                response.setContentType("application/json");
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid date format.\"}");
                return;
            }
        }

      
        boolean esEntrenador = "Entrenador".equals(esEntrenadorStr);
        boolean esProfesional = "true".equals(esProfesionalStr);

        
        boolean success = icontroladorUsuario.actualizarUsuario(oldNickname, nombre, apellido, email, fechaNacimiento, esEntrenador, web, disciplina, esProfesional , pass,imageBytes ,coverColor);

        response.setContentType("application/json");
        if (success) {
            response.getWriter().write("{\"success\": true, \"message\": \"Profile updated successfully.\"}");
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"Error updating profile.\"}");
        }
    }
}
