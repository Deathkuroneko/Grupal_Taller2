package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

//Clase VisorImagenInteligente integrada o en el mismo archivo
@SuppressWarnings("serial")
public class VisorImagenPanel extends JPanel {

    private BufferedImage imagenOriginal;
    private BufferedImage imagenMarco;

    public VisorImagenPanel() {
        setBackground(Color.BLACK);
        cargarMarco();
    }

    private void cargarMarco() {
        try {
            // cargar el marco 
            InputStream is = getClass().getResourceAsStream("/marco_visor.png");
            if (is != null) {
                imagenMarco = ImageIO.read(is);
            }
        } catch (IOException e) {
            System.err.println("No se pudo cargar el marco decorativo");
        }
    }

    public void setImagen(BufferedImage img) {
        this.imagenOriginal = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Suavizado de imagen (Bilinear) para que no se vea pixelada al escalar
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (imagenOriginal != null) {
            // --- LÓGICA DE ESCALADO PROPORCIONAL ---
            double escala = Math.min((double) getWidth() / imagenOriginal.getWidth(),
                                     (double) getHeight() / imagenOriginal.getHeight());
            
            int anchoEscalado = (int) (imagenOriginal.getWidth() * escala);
            int altoEscalado = (int) (imagenOriginal.getHeight() * escala);

            // Centrar la imagen en el panel
            int x = (getWidth() - anchoEscalado) / 2;
            int y = (getHeight() - altoEscalado) / 2;

            g2d.drawImage(imagenOriginal, x, y, anchoEscalado, altoEscalado, null);
        }

        // --- DIBUJAR EL MARCO ENCIMA ---
        if (imagenMarco != null) {
            g2d.drawImage(imagenMarco, 0, 0, getWidth(), getHeight(), null);
        }
    }
}