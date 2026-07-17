package panel;

import service.ImagenService;
import frame.VisorImagenPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class PanelRenderizado3D extends JPanel {

    private final ImagenService service;
    private final VisorImagenPanel visor;
    private JButton btnAbrir;
    private JButton btnAbrir1;
    public PanelRenderizado3D(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        setBackground(new Color(30, 30, 30));

        JLabel lbl = new JLabel("Renderizado 3D con LWJGL");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(lbl);

        btnAbrir = new JButton("Abrir Ventana 3D Triangulo");
        btnAbrir.setBackground(new Color(50, 120, 220));
        btnAbrir.setForeground(Color.WHITE);
        btnAbrir.setFocusPainted(false);
        btnAbrir.addActionListener(e -> abrirVentana3DTriangulo());
        add(btnAbrir);
        btnAbrir1 = new JButton("Abrir Ventana 3D Cubo");
        btnAbrir1.setBackground(new Color(50, 120, 220));
        btnAbrir1.setForeground(Color.WHITE);
        btnAbrir1.setFocusPainted(false);
        btnAbrir1.addActionListener(e -> abrirVentana3DCubo());
        add(btnAbrir1);
    }

    private void abrirVentana3DCubo() {
        BufferedImage img = visor.getImagenActual();

        new Thread(() -> {
            try {
                util.Renderizar render = new util.Renderizar();
                render.iniciar(img);
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                        this,
                        "Error al iniciar renderizado 3D:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    )
                );
            }
        }).start();
    }
    private void abrirVentana3DTriangulo() {
        BufferedImage img = visor.getImagenActual();

        new Thread(() -> {
            try {
                util.Renderizado3D render = new util.Renderizado3D();
                render.iniciar(img);
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                        this,
                        "Error al iniciar renderizado 3D:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    )
                );
            }
        }).start();
    }
}