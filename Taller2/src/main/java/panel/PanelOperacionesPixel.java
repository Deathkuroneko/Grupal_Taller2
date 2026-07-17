package panel;

import service.ImagenService;
import frame.VisorImagenPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalSliderUI;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class PanelOperacionesPixel extends JPanel {

    private final ImagenService service;
    private final VisorImagenPanel visor;

    private JComboBox<String> comboOperacion;
    private JSlider sliderAlpha;
    private JSlider sliderUmbral;
    private JLabel lblAlpha;
    private JLabel lblUmbral;
    private JLabel lblValorAlpha;   
    private JLabel lblValorUmbral;  
    private JButton btnAplicar, btnGenerarStencil, btnCargarSegunda;

    private BufferedImage segundaImagen = null;
    private BufferedImage imagenOriginalStencil = null;

    public PanelOperacionesPixel(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 12));
        setBackground(new Color(30, 30, 30));

        // --- Combo ---
        JLabel lblOper = new JLabel("Operación:");
        lblOper.setForeground(Color.WHITE);
        add(lblOper);
        comboOperacion = new JComboBox<>(new String[]{"Stencil", "OR", "AND", "XOR", "NOT", "Blending"});
        comboOperacion.setPreferredSize(new Dimension(130, 28));
        add(comboOperacion);

        // --- Slider Alpha (Blending) ---
        lblAlpha = new JLabel("Alpha:");
        lblAlpha.setForeground(Color.WHITE);
        add(lblAlpha);

        sliderAlpha = new JSlider(0, 100, 50);
        sliderAlpha.setPreferredSize(new Dimension(200, 50));
        sliderAlpha.setBackground(new Color(30, 30, 30));
        sliderAlpha.setForeground(new Color(0, 200, 255));
        sliderAlpha.setMajorTickSpacing(25);
        sliderAlpha.setMinorTickSpacing(5);
        sliderAlpha.setPaintTicks(true);
        sliderAlpha.setPaintLabels(true);
      
        sliderAlpha.setLabelTable(sliderAlpha.createStandardLabels(25));
       
        lblValorAlpha = new JLabel("0.50");
        lblValorAlpha.setForeground(new Color(0, 255, 120));
        lblValorAlpha.setFont(new Font("Consolas", Font.BOLD, 14));
        add(lblValorAlpha);


        sliderAlpha.addChangeListener(e -> {
            float alpha = sliderAlpha.getValue() / 100f;
            lblValorAlpha.setText(String.format("%.2f", alpha));
        });

        add(sliderAlpha);

        // --- Slider Umbral (Stencil) ---
        lblUmbral = new JLabel("Umbral:");
        lblUmbral.setForeground(Color.WHITE);
        add(lblUmbral);

        sliderUmbral = new JSlider(0, 255, 128);
        sliderUmbral.setPreferredSize(new Dimension(200, 50));
        sliderUmbral.setBackground(new Color(30, 30, 30));
        sliderUmbral.setForeground(new Color(255, 200, 0));
        sliderUmbral.setMajorTickSpacing(64);
        sliderUmbral.setMinorTickSpacing(16);
        sliderUmbral.setPaintTicks(true);
        sliderUmbral.setPaintLabels(true);
        // Etiquetas personalizadas
        java.util.Hashtable<Integer, JLabel> tablaUmbral = new java.util.Hashtable<>();
        tablaUmbral.put(0, new JLabel("0"));
        tablaUmbral.put(64, new JLabel("64"));
        tablaUmbral.put(128, new JLabel("128"));
        tablaUmbral.put(192, new JLabel("192"));
        tablaUmbral.put(255, new JLabel("255"));
        for (JLabel l : tablaUmbral.values()) {
            l.setForeground(Color.WHITE);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        }
        sliderUmbral.setLabelTable(tablaUmbral);

        lblValorUmbral = new JLabel("128");
        lblValorUmbral.setForeground(new Color(255, 200, 0));
        lblValorUmbral.setFont(new Font("Consolas", Font.BOLD, 14));
        add(lblValorUmbral);

        sliderUmbral.addChangeListener(e -> {
            lblValorUmbral.setText(String.valueOf(sliderUmbral.getValue()));
        });

        add(sliderUmbral);

        // --- Botones ---
        btnGenerarStencil = new JButton("Generar Stencil");
        btnGenerarStencil.setBackground(new Color(70, 70, 70));
        btnGenerarStencil.setForeground(Color.WHITE);
        btnGenerarStencil.setFocusPainted(false);
        btnGenerarStencil.addActionListener(e -> generarStencil());
        add(btnGenerarStencil);

        btnCargarSegunda = new JButton("Cargar 2ª Imagen");
        btnCargarSegunda.setBackground(new Color(70, 70, 70));
        btnCargarSegunda.setForeground(Color.WHITE);
        btnCargarSegunda.setFocusPainted(false);
        btnCargarSegunda.addActionListener(e -> cargarSegundaImagen());
        add(btnCargarSegunda);

        btnAplicar = new JButton("Aplicar");
        btnAplicar.setBackground(new Color(50, 120, 220));
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.setFocusPainted(false);
        btnAplicar.addActionListener(e -> aplicarOperacion());
        add(btnAplicar);

        // --- Visibilidad inicial ---
        comboOperacion.addActionListener(e -> actualizarVisibilidad());
        actualizarVisibilidad();
    }

    private void actualizarVisibilidad() {
        String op = (String) comboOperacion.getSelectedItem();
        boolean esBlending = "Blending".equals(op);
        boolean esStencil = "Stencil".equals(op);

        // Alpha
        lblAlpha.setVisible(esBlending);
        sliderAlpha.setVisible(esBlending);
        lblValorAlpha.setVisible(esBlending);

        // Umbral
        lblUmbral.setVisible(esStencil);
        sliderUmbral.setVisible(esStencil);
        lblValorUmbral.setVisible(esStencil);

        btnGenerarStencil.setVisible(esStencil);
        btnCargarSegunda.setVisible(!esStencil && !"NOT".equals(op));
    }

    private void generarStencil() {
        imagenOriginalStencil = visor.getImagenActual();
        if (imagenOriginalStencil == null) {
            JOptionPane.showMessageDialog(this, "Cargue una imagen primero.");
            return;
        }
        BufferedImage stencil = service.generarStencil();
        if (stencil != null) {
            visor.setImagen(stencil);
        }
    }

    private void cargarSegundaImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                segundaImagen = ImageIO.read(chooser.getSelectedFile());

                service.cargarImagenSecundaria(segundaImagen);

                visor.setBlending(segundaImagen);

                JOptionPane.showMessageDialog(this, "Segunda imagen cargada.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage());
            }
        }
    }

    private void aplicarOperacion() {
        String op = (String) comboOperacion.getSelectedItem();
        BufferedImage resultado = null;

        try {
            if ("Stencil".equals(op)) {
                if (imagenOriginalStencil == null) {
                    JOptionPane.showMessageDialog(this, "Primero genere un stencil.");
                    return;
                }
                BufferedImage mask = visor.getImagenActual();
                if (mask == null) {
                    JOptionPane.showMessageDialog(this, "No hay stencil.");
                    return;
                }
                int umbral = sliderUmbral.getValue();
                resultado = service.aplicarStencil(imagenOriginalStencil, mask, umbral);
            } else if ("Blending".equals(op)) {
                if (segundaImagen == null) {
                    JOptionPane.showMessageDialog(this, "Cargue una segunda imagen.");
                    return;
                }
                float alpha = sliderAlpha.getValue() / 100f;
                resultado = service.blending(segundaImagen, alpha);
            } else if ("NOT".equals(op)) {
                resultado = service.operacionNOT();
            } else if (segundaImagen != null) {
                resultado = service.operacionLogica(segundaImagen, op);
            } else {
                JOptionPane.showMessageDialog(this, "Cargue una segunda imagen.");
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            return;
        }

        if (resultado != null) {
            visor.setImagen(resultado);
        }
    }
}