package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

// Importar las clases desde Logica.jar
import Clases.Fabrica;
import Clases.IControladorUsuario;

/**
 * Servlet implementation class Servlet
 */
public class RegistroUsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    // Instancias de los controladores
    private IControladorUsuario icontroladorUsuario;


    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistroUsuarioServlet() {
        super();
    }

    /**
     * Método init() que se ejecuta al inicializar el servlet
     */
    @Override
    public void init() throws ServletException {
        // Obtener la instancia de Fabrica y los controladores
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorUsuario = fabrica.getIControladorUsuario();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener los parámetros del formulario
        String nombre = request.getParameter("registerName");
        String apellido = request.getParameter("registerLastName");
        String nickname = request.getParameter("registerNickname");
        String correoElectronico = request.getParameter("registerEmail");
        String pass = request.getParameter("registerPassword");
        
        // Comprobar si el usuario es profesional o no
        String athleteType = request.getParameter("athleteType");
        boolean esProfesional = "Si".equals(athleteType);

        // Convertir la fecha de nacimiento a LocalDate
        String fechaNacimientoStr = request.getParameter("registerBirthdate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);

     // Procesar la imagen subida
        Part filePart = request.getPart("profileImage");
        byte[] imageBytes = null; // This will hold the byte array for the image

        if (filePart != null && filePart.getSize() > 0) {
            // Leer la imagen desde el InputStream
            InputStream inputStream = filePart.getInputStream();
            BufferedImage originalImage = ImageIO.read(inputStream);

            // Redimensionar la imagen a 168x168 píxeles
            BufferedImage resizedImage = new BufferedImage(168, 168, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 168, 168, null);
            g.dispose();

            // Convertir la imagen redimensionada en un array de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            baos.flush();
            imageBytes = baos.toByteArray();  // Obtener el arreglo de bytes de la imagen
            baos.close();
        }

     // Manejar el resto de los datos como lo hacías anteriormente
        try {
            // Aquí es donde puedes guardar los datos en la base de datos
            icontroladorUsuario.crearUsuario(nombre, apellido, nickname, correoElectronico, fechaNacimiento, pass, true, false, esProfesional, null, null,imageBytes);

            // Imprimir el resultado exitoso en la consola
            System.out.println("Usuario creado exitosamente: " + nickname);
            response.sendRedirect(request.getContextPath() + "");
        } catch (Exception e) {
            // Imprimir el error en la consola
            e.printStackTrace();
            System.out.println("Error al crear el usuario: " + e.getMessage());
        }
    }
}
