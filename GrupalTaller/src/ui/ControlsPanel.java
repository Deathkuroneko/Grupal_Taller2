package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Main.MainView;
import service.ImagenService;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ControlsPanel extends JPanel {
    
    private ImagenService imageService;
    private MainView mainView;

    public ControlsPanel(ImagenService service, MainView view) {
        this.imageService = service;
        this.mainView = view;

        // Configuración visual
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setPreferredSize(new Dimension(220, 0));
        this.setBackground(new Color(240, 240, 240));

        // --- TÍTULO ---
        JLabel titulo = new JLabel("EFECTO RETRO");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(titulo);

        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SLIDER ---
        JLabel lblNiveles = new JLabel("Niveles de posterización:");
        lblNiveles.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(lblNiveles);
        
        JSlider sliderN = new JSlider(2, 10, 5);
        sliderN.setMajorTickSpacing(1);
        sliderN.setPaintTicks(true);
        sliderN.setPaintLabels(true);
        sliderN.setBackground(new Color(240, 240, 240));
        this.add(sliderN);

        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- BOTÓN APLICAR ---
        JButton btnAplicarRetro = new JButton("Aplicar Retro");
        btnAplicarRetro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAplicarRetro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnAplicarRetro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(btnAplicarRetro);

        // =========================
        // EVENTOS
        // =========================

        btnAplicarRetro.addActionListener(e -> {
            int n = sliderN.getValue();
            // Aplicamos sobre la imagen que esté actualmente en el lienzo
            BufferedImage resultado = imageService.aplicarRetro(n);

            if (resultado != null) {
                mainView.getImagePanel().setImagen(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "Carga una imagen primero.");
            }
        });
    }
}