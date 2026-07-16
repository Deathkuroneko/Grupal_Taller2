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
    private BufferedImage miniatura;
    private BufferedImage blending;

    public VisorImagenPanel() {
        setBackground(Color.BLACK);
        setDoubleBuffered(true); // mejora renderizado
        cargarMarco();
    }

    public void setMiniatura(BufferedImage img){
        miniatura = img;
        repaint();

    }
    public void setBlending(BufferedImage img){
        blending = img;
        repaint();
    }
    public void limpiarBlending(){
        blending = null;
        repaint();
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

     // Dibujar miniatura ORIGINAL
        int x = getWidth() - 150;
        int y = getHeight() - 130;
        int ancho = 140;
        int alto = 110;

        // Fondo oscuro
        g2.setColor(new Color(30, 30, 30));
        g2.fillRect(x, y, ancho, alto);

        // Borde
        g2.setColor(Color.GRAY);
        g2.drawRect(x, y, ancho, alto);

     // IZQUIERDA

     if(blending != null){
         int bx = 10;
         int by = getHeight() - 130;

         int bw = 140;
         int bh = 110;
         // fondo
         g2.setColor(new Color(30,30,30));

         g2.fillRect( bx, by, bw, bh);
         // borde
         g2.setColor(Color.CYAN);

         g2.drawRect( bx, by, bw,bh);
         // imagen secundaria
         g2.drawImage(blending, bx, by, bw, bh, null);

         g2.setColor(Color.WHITE);

         g2.drawString(   "BLENDING", bx+35,by-5);

     }
        // Si existe una imagen, dibujarla encima
        if (miniatura != null) {
            g2.drawImage(miniatura, x, y, ancho, alto, null);
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