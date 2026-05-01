package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import Main.MainView;
import service.ImagenService;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TopPanel extends JPanel {

    public TopPanel(ImagenService service, MainView view) {

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(230,230,230));

        JButton btnAbrir = new JButton("Abrir");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnReset = new JButton("Reset");

        add(btnAbrir);
        add(btnGuardar);
        add(btnReset);

        // ABRIR
        btnAbrir.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            selector.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg","png","jpeg"));

            if (selector.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedImage img = ImageIO.read(selector.getSelectedFile());
                    service.cargarImagen(img);
                    view.getImagePanel().setImagen(img);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // GUARDAR
        btnGuardar.addActionListener(e -> {
            if (service.getImagenActual() == null) return;

            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File f = chooser.getSelectedFile();
                    if (!f.getName().endsWith(".png")) {
                        f = new File(f.getAbsolutePath()+".png");
                    }
                    service.guardarImagen(f);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // RESET REAL (corregido)
     // Busca el evento de btnReset en TopPanel y cámbialo por esto:
        btnReset.addActionListener(e -> {
            BufferedImage original = service.getImagenOriginal();
            if (original != null) {
                // 1. Le decimos al servicio que descarte los cambios
                service.restablecer(); 
                // 2. Le decimos al visor que muestre la original
                view.getImagePanel().setImagen(original);
            } else {
                JOptionPane.showMessageDialog(this, "No hay una imagen original cargada.");
            }
        });
    }
}