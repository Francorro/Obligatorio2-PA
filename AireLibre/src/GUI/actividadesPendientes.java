package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.time.Duration;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Clases.DataActividad;
import Clases.DataEntrenador;
import Clases.IControladorActividad;
import Clases.IControladorUsuario;
import java.awt.Component;

public class actividadesPendientes extends JInternalFrame {
    private static final long serialVersionUID = 1L;
    private JTable TablaActividades;
    private JTable TablaActividades2;
    private JTextField txtNombre_Actividad;
    private JTextArea txtDescripcion;
    private JSpinner spinnerDuracionHoras;
    private JSpinner spinnerDuracionMinutos;
    private JTextField txtCosto;
    private JComboBox<String> comboBoxEntrenadores;
    private DataActividad actividadSeleccionada; // Actividad actualmente seleccionada
    private IControladorActividad icontroladorActividad;
    private IControladorUsuario icontroladorUsuario;
    DataEntrenador antiguo;
    private JLabel lblImagenActividad;

    

    public actividadesPendientes(IControladorActividad icontroladorActiv, IControladorUsuario icontroladorUsu) {
    	icontroladorActividad = icontroladorActiv;
    	icontroladorUsuario = icontroladorUsu;
        // Configuración de JInternalFrame
        setClosable(true);  
        setIconifiable(true); 
        setResizable(true);
        setMaximizable(true); 
        getContentPane().setBackground(Color.LIGHT_GRAY);
        getContentPane().setLayout(null);

        // Título del marco
        JLabel lblNewLabel = new JLabel("Actividades Pendientes");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(226, 11, 325, 25);
        getContentPane().add(lblNewLabel);

        // Tabla de actividades
        TablaActividades = new JTable();
        JScrollPane scrollPane = new JScrollPane(TablaActividades);
        scrollPane.setBounds(10, 118, 563, 187);
        getContentPane().add(scrollPane);

        TablaActividades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = TablaActividades.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // Deseleccionar la fila seleccionada en la segunda tabla
                    TablaActividades2.clearSelection();
                    
                    // Obtener la actividad seleccionada en la primera tabla
                    String nombreActividad = (String) TablaActividades.getValueAt(filaSeleccionada, 0);
                    actividadSeleccionada = icontroladorActividad.getDataFromActividad(nombreActividad); // Obtener la actividad seleccionada
                    if (actividadSeleccionada != null) {
                        cargarDatosActividad();
                    }
                }
            }
        });
        
        // Tabla de actividades
        TablaActividades2 = new JTable();
        JScrollPane scrollPane_1 = new JScrollPane(TablaActividades2);
        scrollPane_1.setBounds(10, 358, 563, 187);
        getContentPane().add(scrollPane_1);

        TablaActividades2.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = TablaActividades2.getSelectedRow();
                if (filaSeleccionada != -1) {
                    // Deseleccionar la fila seleccionada en la primera tabla
                    TablaActividades.clearSelection();

                    // Obtener la actividad seleccionada en la segunda tabla
                    String nombreActividad = (String) TablaActividades2.getValueAt(filaSeleccionada, 0);
                    actividadSeleccionada = icontroladorActividad.getDataFromActividad(nombreActividad); // Obtener la actividad seleccionada
                    if (actividadSeleccionada != null) {
                        cargarDatosActividad();
                    }
                }
            }
        });

        JLabel lblNewLabel_1 = new JLabel("Entrenador de la actividad:");
        lblNewLabel_1.setBounds(497, 56, 206, 25);
        getContentPane().add(lblNewLabel_1);

        comboBoxEntrenadores = new JComboBox<>();
        comboBoxEntrenadores.setBounds(660, 58, 247, 20);
        getContentPane().add(comboBoxEntrenadores);
        comboBoxEntrenadores.setEnabled(false);

        JSeparator separator = new JSeparator();
        separator.setBounds(590, 108, 284, 0);
        getContentPane().add(separator);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(590, 93, 338, 4);
        getContentPane().add(separator_1);

        JLabel lblNewLabel_1_1 = new JLabel("Nombre:");
        lblNewLabel_1_1.setBounds(590, 134, 85, 25);
        getContentPane().add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_2 = new JLabel("Datos de Actividad");
        lblNewLabel_1_2.setBounds(660, 100, 183, 25);
        getContentPane().add(lblNewLabel_1_2);

        txtDescripcion = new JTextArea();
        txtDescripcion.setBounds(610, 196, 297, 76);
        txtDescripcion.setLineWrap(true); // Habilita el ajuste de línea
        txtDescripcion.setWrapStyleWord(true); // Ajusta las palabras completas

        getContentPane().add(txtDescripcion);
        txtDescripcion.setEditable(false);

        txtNombre_Actividad = new JTextField();
        txtNombre_Actividad.setEditable(false);
        txtNombre_Actividad.setBounds(653, 137, 223, 20);
        getContentPane().add(txtNombre_Actividad);
        txtNombre_Actividad.setColumns(10);

        JLabel lblNewLabel_1_1_2 = new JLabel("Duracion (horas):");
        lblNewLabel_1_1_2.setBounds(590, 283, 115, 25);
        getContentPane().add(lblNewLabel_1_1_2);

        spinnerDuracionHoras = new JSpinner();
        spinnerDuracionHoras.setBounds(715, 285, 60, 20);
        getContentPane().add(spinnerDuracionHoras);
        spinnerDuracionHoras.setEnabled(false);

        spinnerDuracionMinutos = new JSpinner();
        spinnerDuracionMinutos.setBounds(785, 285, 58, 20);
        getContentPane().add(spinnerDuracionMinutos);
        spinnerDuracionMinutos.setEnabled(false);

        JLabel lblNewLabel_1_1_2_1 = new JLabel("Costo:");
        lblNewLabel_1_1_2_1.setBounds(590, 310, 50, 25);
        getContentPane().add(lblNewLabel_1_1_2_1);

        txtCosto = new JTextField();
        txtCosto.setBounds(653, 313, 142, 20);
        getContentPane().add(txtCosto);
        txtCosto.setColumns(10);
        txtCosto.setEditable(false);
        
        // En el constructor de `actividadesPendientes` agrega el JLabel:
        lblImagenActividad = new JLabel();
        lblImagenActividad.setBounds(641, 344, 233, 134); // Ajusta el tamaño y la posición
        getContentPane().add(lblImagenActividad);
        
        

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAceptar.setBounds(656, 488, 104, 23);
        getContentPane().add(btnAceptar);
        btnAceptar.addActionListener(e -> aceptarActividad());
        
        JButton btnRechazar = new JButton("Rechazar");
        btnRechazar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRechazar.setBounds(770, 488, 104, 23);
        getContentPane().add(btnRechazar);
        btnRechazar.addActionListener(e -> rechazarActividad());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setBounds(703, 522, 117, 23);
        getContentPane().add(btnCancelar);
        btnCancelar.addActionListener(e -> cancelar());

        JLabel lblActividadesExistentes = new JLabel("Seleccione actividad:");
        lblActividadesExistentes.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblActividadesExistentes.setBounds(10, 23, 416, 46);
        getContentPane().add(lblActividadesExistentes);
        
        JLabel lblNewLabel_1_1_1 = new JLabel("Descripcion:");
        lblNewLabel_1_1_1.setBounds(590, 170, 85, 25);
        getContentPane().add(lblNewLabel_1_1_1);
        
        JLabel lblNewLabel_1_1_2_1_1 = new JLabel("Imagen:");
        lblNewLabel_1_1_2_1_1.setBounds(590, 352, 50, 25);
        getContentPane().add(lblNewLabel_1_1_2_1_1);
                
        JLabel lblActividadesPendientes = new JLabel("Actividades Pendientes:");
        lblActividadesPendientes.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblActividadesPendientes.setBounds(10, 79, 416, 46);
        getContentPane().add(lblActividadesPendientes);
        
        JLabel lblActividadesRechazadas = new JLabel("Actividades Rechazadas:");
        lblActividadesRechazadas.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblActividadesRechazadas.setBounds(10, 318, 416, 46);
        getContentPane().add(lblActividadesRechazadas);

        setTitle("Actividades Pendientes");
        setBounds(100, 100, 950, 600);

        // Cargar entrenadores en el JComboBox
        cargarEntrenadores();

        // Cargar actividades en la tabla
        cargarActividadesPendientes();
        cargarActividadesRechazadas();
    }

    private void cargarEntrenadores() {
        // Obtener la lista de entrenadores desde la base de datos
        List<DataEntrenador> entrenadores = icontroladorUsuario.listaEntrenadores();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        for (DataEntrenador entrenador : entrenadores) {
            model.addElement(entrenador.getNickname() + " - " + entrenador.getNombre() + " " + entrenador.getApellido() + " (" + entrenador.getDisciplina() + ")");
        }

        comboBoxEntrenadores.setModel(model);
        comboBoxEntrenadores.setSelectedIndex(-1);
    }

    private void cargarActividadesPendientes() {
        // Obtener la lista de actividades desde la base de datos
        List<DataActividad> actividades = icontroladorActividad.transformarActividades("Pendiente");

        // Definir las columnas de la tabla
        String[] columnNames = { "Nombre", "Duración", "Costo" };

        // Crear el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer todas las celdas no editables
            }
        };

        // Llenar la tabla con los datos de las actividades
        for (DataActividad actividad : actividades) {
            Object[] rowData = {
                actividad.getNombre(),
                actividad.getDuracion().toHours() + "h " + (actividad.getDuracion().toMinutes() % 60) + "m", // Formato de duración
                actividad.getCosto()
            };
            model.addRow(rowData);
        }

        TablaActividades.setModel(model);
    }
    
    private void cargarActividadesRechazadas() {
        // Obtener la lista de actividades desde la base de datos
        List<DataActividad> actividades = icontroladorActividad.transformarActividades("Rechazada");

        // Definir las columnas de la tabla
        String[] columnNames = { "Nombre", "Duración", "Costo" };

        // Crear el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer todas las celdas no editables
            }
        };

        // Llenar la tabla con los datos de las actividades
        for (DataActividad actividad : actividades) {
            Object[] rowData = {
                actividad.getNombre(),
                actividad.getDuracion().toHours() + "h " + (actividad.getDuracion().toMinutes() % 60) + "m", // Formato de duración
                actividad.getCosto()
            };
            model.addRow(rowData);
        }

        TablaActividades2.setModel(model);
    }

    private void cargarDatosActividad() {
        if (actividadSeleccionada != null) {
            txtNombre_Actividad.setText(actividadSeleccionada.getNombre());
            txtDescripcion.setText(actividadSeleccionada.getDescripcion());
            Duration duracion = actividadSeleccionada.getDuracion();
            long horas = duracion.toHours(); // Devuelve long
            long minutosTotales = duracion.toMinutes(); // Devuelve long
            int minutos = (int) (minutosTotales % 60); // Convertir minutos a int

            // Establecer los valores en los spinners, asegurándose de usar Integer
            spinnerDuracionHoras.setValue((int) horas); // Convertir long a int
            spinnerDuracionMinutos.setValue(minutos); 
            txtCosto.setText(String.valueOf(actividadSeleccionada.getCosto()));
            
            // Cargar la imagen en el JLabel
            ImageIcon imagenIcon = convertirImagen(actividadSeleccionada.getImagen());
            if (imagenIcon != null) {
                lblImagenActividad.setIcon(imagenIcon);
            } else {
                lblImagenActividad.setIcon(null); // Si no hay imagen, limpiar el JLabel
            }

            // Seleccionar el entrenador en el JComboBox
            DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBoxEntrenadores.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                String item = model.getElementAt(i);
                String idEntrenador = item.split(" - ")[0];
                if (icontroladorActividad.getEntrenadorByActId(actividadSeleccionada.getNombre()).getNickname().equals(idEntrenador)) {
                    comboBoxEntrenadores.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    
    private ImageIcon convertirImagen(byte[] imagenBytes) {
        if (imagenBytes != null && imagenBytes.length > 0) {
            // Convertir los bytes a una imagen
            ImageIcon icon = new ImageIcon(imagenBytes);
            Image img = icon.getImage(); // Obtener la imagen original

            // Escalar la imagen a 233x134
            Image imgEscalada = img.getScaledInstance(233, 134, Image.SCALE_SMOOTH);

            // Retornar la imagen escalada como ImageIcon
            return new ImageIcon(imgEscalada);
        } else {
            return null; // Si no hay imagen, retorna null
        }
    }


    private void aceptarActividad() {
        if (actividadSeleccionada != null) {
            // Actualiza la actividad en la base de datos
            if (icontroladorActividad.cambiarEstado(actividadSeleccionada,"Aceptada")) {
                JOptionPane.showMessageDialog(this, "Actividad aceptada exitosamente.");
                cargarActividadesPendientes(); // Recargar la lista de actividades
                cargarActividadesRechazadas();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al aceptar actividad.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            
        }
    }
    
    private void rechazarActividad() {
        if (actividadSeleccionada != null) {
            // Actualiza la actividad en la base de datos
            if (icontroladorActividad.cambiarEstado(actividadSeleccionada,"Rechazada")) {
                JOptionPane.showMessageDialog(this, "Actividad rechazada exitosamente.");
                cargarActividadesRechazadas(); // Recargar la lista de actividades
                cargarActividadesPendientes();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al rechazar actividad.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            
        }
    }
    
    private void limpiarCampos() {
        txtNombre_Actividad.setText("");
        txtDescripcion.setText("");
        spinnerDuracionHoras.setValue(0);
        spinnerDuracionMinutos.setValue(0);
        txtCosto.setText("");
        comboBoxEntrenadores.setSelectedIndex(-1); // Deselecciona cualquier entrenador
        lblImagenActividad.setIcon(null); // Limpia la imagen
        actividadSeleccionada = null; // Limpiar la actividad seleccionada
    }

    

    

    // Método para cerrar la ventana al cancelar
    private void cancelar() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cancelar?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            dispose(); // Cierra la ventana actual
        }
    }
}
