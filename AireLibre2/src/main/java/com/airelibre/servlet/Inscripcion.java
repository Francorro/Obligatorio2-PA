package com.airelibre.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.gson.JsonObject;

import Clases.Fabrica;
import Clases.IControladorActividad;
import Clases.IControladorClase;
import Clases.IControladorInscripcion;
import Clases.IControladorUsuario;

public class Inscripcion extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private IControladorInscripcion icontroladorInscripcion;
    private IControladorUsuario icontroladorUsuario;
    private IControladorClase icontroladorClase;
    private IControladorActividad icontroladorActividad;
    
    @Override
    public void init() throws ServletException {
        Fabrica fabrica = Fabrica.getInstance();
        icontroladorInscripcion = fabrica.getIControladorInscripcion();
        icontroladorUsuario = fabrica.getIControladorUsuario();
        icontroladorClase = fabrica.getIControladorClase();
        icontroladorActividad = fabrica.getIControladorActividad();
    }
       
    public Inscripcion() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String deportistaNickname = request.getParameter("deportistaNickname");
            long claseId = Long.parseLong(request.getParameter("claseId"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            String actividadId = request.getParameter("actividadId");
            
            System.out.println("Received parameters:");
            System.out.println("deportistaNickname: " + deportistaNickname);
            System.out.println("claseId: " + claseId);
            System.out.println("cantidad: " + cantidad);
            System.out.println("actividadId: " + actividadId);
            
            boolean result = icontroladorInscripcion.guardarInscripcion(
                icontroladorUsuario.getDeportista(deportistaNickname),
                icontroladorClase.getDataFromClases(claseId),
                cantidad,
                icontroladorActividad.getDataFromActividad(actividadId)
            );

            JsonObject jsonResponse = new JsonObject();
            if (result) {
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Inscripción guardada exitosamente");
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("message", "Error al guardar la inscripción");
            }
            
            out.print(jsonResponse.toString());
            
        } catch (Exception e) {
            e.printStackTrace(); 
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", "Error: " + e.getMessage());
            out.print(errorResponse.toString());
        } finally {
            out.flush();
        }
    }
}