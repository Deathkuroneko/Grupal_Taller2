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

        // 1. Configuración Visual
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Contenedor del Filtro
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        // --- COMBOBOX ---
        JLabel lblNiveles = new JLabel("Niveles de posterización:");
        lblNiveles.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNiveles.setFont(new Font("SansSerif", Font.PLAIN, 11));
        content.add(lblNiveles);
        
        content.add(Box.createVerticalStrut(5));

        Integer[] nivelesOpciones = {2, 4, 8, 64, 128, 255};
        JComboBox<Integer> comboN = new JComboBox<>(nivelesOpciones);
        comboN.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        content.add(comboN);

        content.add(Box.createVerticalStrut(10));

        // --- CHECKBOXES ---
        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JCheckBox chkR = new JCheckBox("R", true);
        JCheckBox chkG = new JCheckBox("G", true);
        JCheckBox chkB = new JCheckBox("B", true);
        checkPanel.add(chkR);
        checkPanel.add(chkG);
        checkPanel.add(chkB);
        content.add(checkPanel);

        content.add(Box.createVerticalStrut(15));

        // --- BOTÓN APLICAR ---
        JButton btnAplicarRetro = new JButton("Aplicar Filtro");
        btnAplicarRetro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAplicarRetro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnAplicarRetro.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        this.add(btnHeader);
        this.add(content);

        // =========================
        // EVENTOS
        // =========================

        btnHeader.addActionListener(e -> {
            boolean visible = content.isVisible();
            content.setVisible(!visible);
            btnHeader.setText(visible ? "► RETRO & VINTAGE" : "▼ RETRO & VINTAGE");
            this.revalidate();
        });

        btnAplicarRetro.addActionListener(e -> {
            int n = (Integer) comboN.getSelectedItem();
            String canales = "";
            if (chkR.isSelected()) canales += "R";
            if (chkG.isSelected()) canales += "G";
            if (chkB.isSelected()) canales += "B";

            BufferedImage resultado = imageService.aplicarRetro(n, canales);

            if (resultado != null) {
                mainView.getImagePanel().setImagen(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "Carga una imagen primero.");
            }
        });
    }
}