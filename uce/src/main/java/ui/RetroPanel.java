package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import Main.MainView;
import service.ImagenService;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RetroPanel extends JPanel {
    
    private ImagenService imageService;
    private MainView mainView;

    public RetroPanel(ImagenService service, MainView view) {
        this.imageService = service;
        this.mainView = view;

        // 1. Configuración Visual (Eliminamos el fondo fijo para que use el tema oscuro)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Contenedor del Filtro (Usamos un panel interno para poder ocultarlo/mostrarlo)
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        // --- SLIDER ---
        JLabel lblNiveles = new JLabel("Niveles de posterización:");
        lblNiveles.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNiveles.setFont(new Font("SansSerif", Font.PLAIN, 11));
        content.add(lblNiveles);
        
        content.add(Box.createVerticalStrut(10));

        JSlider sliderN = new JSlider(2, 10, 5);
        sliderN.setMajorTickSpacing(1);
        sliderN.setPaintTicks(true);
        sliderN.setPaintLabels(true);
        // Quitamos setBackground fijo para que sea transparente al tema
        content.add(sliderN);

        content.add(Box.createVerticalStrut(15));

        // --- BOTÓN APLICAR ---
        JButton btnAplicarRetro = new JButton("Aplicar Filtro");
        btnAplicarRetro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAplicarRetro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnAplicarRetro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Estilo FlatLaf para botones de acción
        btnAplicarRetro.putClientProperty("JButton.buttonType", "roundRect");
        content.add(btnAplicarRetro);

        // 3. Botón de Cabecera (El "Acordeón")
        JButton btnHeader = new JButton("▼ RETRO & VINTAGE");
        btnHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnHeader.setBorderPainted(false);
        btnHeader.setFocusPainted(false);
        btnHeader.setContentAreaFilled(false);
        btnHeader.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Agregar al panel principal
        this.add(btnHeader);
        this.add(content);

        // =========================
        // EVENTOS
        // =========================

        // Lógica de colapsar
        btnHeader.addActionListener(e -> {
            boolean visible = content.isVisible();
            content.setVisible(!visible);
            btnHeader.setText(visible ? "► RETRO & VINTAGE" : "▼ RETRO & VINTAGE");
            this.revalidate();
        });

        // Aplicar Filtro
        btnAplicarRetro.addActionListener(e -> {
            int n = sliderN.getValue();
            BufferedImage resultado = imageService.aplicarRetro(n);

            if (resultado != null) {
                mainView.getImagePanel().setImagen(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "Carga una imagen primero.");
            }
        });
    }
}