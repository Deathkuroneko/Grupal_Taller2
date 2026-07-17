package panel;

import service.ImagenService;
import frame.VisorImagenPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class PanelFiltroSeparable extends JPanel {

    private final ImagenService service;
    private final VisorImagenPanel visor;

    private JSlider sliderIteraciones;
    private JLabel lblValor;
    private JButton btnAplicar;

    public PanelFiltroSeparable(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        setBackground(new Color(30, 30, 30));

        JLabel lbl = new JLabel("Filtro Gaussiano (iteraciones):");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(lbl);

        // Slider para iteraciones (1 a 10)
        sliderIteraciones = new JSlider(1, 10, 3);
        sliderIteraciones.setPreferredSize(new Dimension(250, 50));
        sliderIteraciones.setBackground(new Color(30, 30, 30));
        sliderIteraciones.setForeground(new Color(0, 200, 255));
        sliderIteraciones.setMajorTickSpacing(2);
        sliderIteraciones.setMinorTickSpacing(1);
        sliderIteraciones.setPaintTicks(true);
        sliderIteraciones.setPaintLabels(true);

        Hashtable<Integer, JLabel> tabla = new Hashtable<>();
        tabla.put(1, new JLabel("1"));
        tabla.put(3, new JLabel("3"));
        tabla.put(5, new JLabel("5"));
        tabla.put(7, new JLabel("7"));
        tabla.put(10, new JLabel("10"));
        for (JLabel l : tabla.values()) {
            l.setForeground(Color.WHITE);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        }
        sliderIteraciones.setLabelTable(tabla);

        add(sliderIteraciones);

        // Etiqueta con el valor actual
        lblValor = new JLabel("3");
        lblValor.setForeground(new Color(0, 255, 120));
        lblValor.setFont(new Font("Consolas", Font.BOLD, 16));
        sliderIteraciones.addChangeListener(e -> {
            lblValor.setText(String.valueOf(sliderIteraciones.getValue()));
        });
        add(lblValor);

        // Botón aplicar
        btnAplicar = new JButton("Aplicar Filtro");
        btnAplicar.setBackground(new Color(50, 120, 220));
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.setFocusPainted(false);
        btnAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicar.addActionListener(e -> aplicarFiltro());
        add(btnAplicar);
    }

    private void aplicarFiltro() {
        BufferedImage img = visor.getImagenActual();
        if (img == null) {
            JOptionPane.showMessageDialog(this,
                    "Cargue una imagen primero.",
                    "Sin imagen",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int iteraciones = sliderIteraciones.getValue();
        BufferedImage resultado = service.aplicarFiltroSeparable(iteraciones);
        if (resultado != null) {
            visor.setImagen(resultado);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al aplicar el filtro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}