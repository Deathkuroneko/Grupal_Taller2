package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import Main.MainView;
import service.ImagenService;

public class EscalaGrisesNPanel extends JPanel {
    private ImagenService imageService;
    private MainView mainView;

    public EscalaGrisesNPanel(ImagenService service, MainView view) {
        this.imageService = service;
        this.mainView = view;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        JLabel lblNiveles = new JLabel("Niveles de grises:");
        lblNiveles.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNiveles.setFont(new Font("SansSerif", Font.PLAIN, 11));
        content.add(lblNiveles);
        
        content.add(Box.createVerticalStrut(5));

        Integer[] nivelesOpciones = {2, 4, 8, 16, 32, 64, 128, 255};
        JComboBox<Integer> comboN = new JComboBox<>(nivelesOpciones);
        comboN.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        content.add(comboN);

        content.add(Box.createVerticalStrut(10));

        JButton btnAplicar = new JButton("Aplicar Grises N");
        btnAplicar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAplicar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicar.putClientProperty("JButton.buttonType", "roundRect");
        content.add(btnAplicar);

        JButton btnHeader = new JButton("▼ ESCALA DE GRISES");
        btnHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnHeader.setBorderPainted(false);
        btnHeader.setFocusPainted(false);
        btnHeader.setContentAreaFilled(false);
        btnHeader.setFont(new Font("SansSerif", Font.BOLD, 12));

        this.add(btnHeader);
        this.add(content);

        btnHeader.addActionListener(e -> {
            boolean visible = content.isVisible();
            content.setVisible(!visible);
            btnHeader.setText(visible ? "► ESCALA DE GRISES" : "▼ ESCALA DE GRISES");
            this.revalidate();
        });

        btnAplicar.addActionListener(e -> {
            int n = (Integer) comboN.getSelectedItem();
            BufferedImage resultado = imageService.aplicarEscalaGrisesN(n);
            if (resultado != null) {
                mainView.getImagePanel().setImagen(resultado);
            } else {
                JOptionPane.showMessageDialog(this, "Carga una imagen primero.");
            }
        });
    }
}
