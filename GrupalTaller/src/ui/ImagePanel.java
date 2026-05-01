package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Dimension;

public class ImagePanel extends JPanel {
    private BufferedImage imagen;

    public void setImagen(BufferedImage nuevaImagen) {
        this.imagen = nuevaImagen;
        // Si hay imagen, ajustamos el tamaño del panel para el Scroll
        if (imagen != null) {
            setPreferredSize(new Dimension(imagen.getWidth(), imagen.getHeight()));
        }
        revalidate(); // Avisa al Layout que el tamaño cambió
        repaint();    // Llama a paintComponent para redibujar
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            // Dibuja la imagen en la posición 0,0
            g.drawImage(imagen, 0, 0, null);
        }
    }
}