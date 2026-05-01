package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import Main.MainView;
import service.ImagenService;

public class DegradadosPanel extends JPanel {

    public DegradadosPanel(ImagenService service, MainView view) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(220, 0));
        setBackground(new Color(240,240,240));

        add(new JLabel("DEGRADADOS"));
        add(Box.createVerticalStrut(10));

        JButton btnIzqDer = new JButton("Izquierda → Derecha");
        JButton btnDerIzq = new JButton("Derecha → Izquierda");
        JButton btnRadial = new JButton("Radial");

        add(btnIzqDer);
        add(Box.createVerticalStrut(10));
        add(btnDerIzq);
        add(Box.createVerticalStrut(10));
        add(btnRadial);

        // EVENTOS (preview en tiempo real)

        btnIzqDer.addActionListener(e -> {
            BufferedImage img = service.aplicarDegradadoDerIzq();
            if (img != null) {
                view.getImagePanel().setImagen(img);
            }
        });

        btnDerIzq.addActionListener(e -> {
            BufferedImage img = service.aplicarDegradadoDerIzq();
            if (img != null) {
                view.getImagePanel().setImagen(img);
            }
        });

        btnRadial.addActionListener(e -> {
            BufferedImage img = service.aplicarDegradadoRadial();
            if (img != null) {
                view.getImagePanel().setImagen(img);
            }
        });
    }
}