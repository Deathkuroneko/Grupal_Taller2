package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;

@SuppressWarnings("serial")
public class VisorImagenPanel extends JPanel {

    private BufferedImage imagen;
    private BufferedImage marco;

    public VisorImagenPanel() {
        setBackground(Color.BLACK);
        setDoubleBuffered(true); // mejora renderizado
        cargarMarco();
    }

    private void cargarMarco() {
        try (InputStream is = getClass().getResourceAsStream("/marco_visor.png")) {
            if (is != null) {
                marco = ImageIO.read(is);
            } else {
                System.err.println("No se encontró /marco_visor.png");
            }
        } catch (Exception e) {
            System.err.println("Error cargando marco");
        }
    }

    public void setImagen(BufferedImage img) {
        this.imagen = img;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen == null && marco == null) return;

        Graphics2D g2 = (Graphics2D) g.create();

        
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (imagen != null) {
            dibujarImagenEscalada(g2);
        }

        if (marco != null) {
            g2.drawImage(marco, 0, 0, getWidth(), getHeight(), null);
        }

        g2.dispose();
    }

    private void dibujarImagenEscalada(Graphics2D g2) {
        int panelW = getWidth();
        int panelH = getHeight();

        int imgW = imagen.getWidth();
        int imgH = imagen.getHeight();

        double escala = Math.min((double) panelW / imgW, (double) panelH / imgH);

        int w = (int) (imgW * escala);
        int h = (int) (imgH * escala);

        int x = (panelW - w) / 2;
        int y = (panelH - h) / 2;

        g2.drawImage(imagen, x, y, w, h, null);
    }
}