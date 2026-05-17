package filtros;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Histograma {

    public static BufferedImage generarHistograma(BufferedImage imagen, boolean red, boolean green, boolean blue) {

        int anchoHisto = 800;
        int altoHisto = 600;

        int[] histoRed = new int[256];
        int[] histoGreen = new int[256];
        int[] histoBlue = new int[256];

        BufferedImage histogramaImg = new BufferedImage(
                anchoHisto,
                altoHisto,
                BufferedImage.TYPE_INT_RGB);

        Graphics2D gr = histogramaImg.createGraphics();

    
        gr.setColor(Color.BLACK);
        gr.setStroke(new BasicStroke(2));
        gr.fillRect(0, 0, anchoHisto, altoHisto);

        // CALCULAR HISTOGRAMAS
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();

        for (int y = 0; y < alto; y++) {

            for (int x = 0; x < ancho; x++) {

                int pixel = imagen.getRGB(x, y);

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                histoRed[r]++;
                histoGreen[g]++;
                histoBlue[b]++;
            }
        }

        // MAXIMO GLOBAL
        int maxR = maximo(histoRed);
        int maxG = maximo(histoGreen);
        int maxB = maximo(histoBlue);

        int maxGlobal = Math.max(maxR,
                Math.max(maxG, maxB));


        // ESCALAS
        float escalaX = anchoHisto / 256.0f;
        float escalaY = altoHisto * (1.0f / maxGlobal);

   
        // DIBUJAR HISTOGRAMAS
        if (red) {

            dibujarHistograma(
                    gr,
                    histoRed,
                    new Color(255, 0, 0, 180),
                    escalaX,
                    escalaY,
                    altoHisto);
        }

        if (green) {

            dibujarHistograma(
                    gr,
                    histoGreen,
                    new Color(0, 255, 0, 180),
                    escalaX,
                    escalaY,
                    altoHisto);
        }

        if (blue) {

            dibujarHistograma(
                    gr,
                    histoBlue,
                    new Color(0, 0, 255, 180),
                    escalaX,
                    escalaY,
                    altoHisto);
        }

        gr.dispose();

        return histogramaImg;
    }

    private static int maximo(int[] h) {

        int max = h[0];
        for (int i = 1; i < h.length; i++) {
            if (h[i] > max) {
                max = h[i];
            }
        }

        return max;
    }

    private static void dibujarHistograma(
            Graphics2D g,
            int[] histo,
            Color color,
            float escalaX,
            float escalaY,
            int alto) {

        g.setColor(color);
        g.setStroke(new BasicStroke(2));

        for (int i = 1; i < histo.length; i++) {
            int x1 = (int) (escalaX * (i - 1));
            int y1 = alto - (int) (escalaY * histo[i - 1]);
            int x2 = (int) (escalaX * i);
            int y2 = alto - (int) (escalaY * histo[i]);
            g.drawLine(x1, y1, x2, y2);
        }
    }
}