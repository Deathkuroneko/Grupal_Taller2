package filtros;

import java.awt.image.BufferedImage;

public class Retro {
    // Cambiamos a que retorne BufferedImage y sea estático
    public static BufferedImage aplicarRetro(BufferedImage img, int N, String canales) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = img.getRGB(x, y);

                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Aplicamos la cuantización (recomiendo usar N-1 > 0 para evitar /0)
                int factor = (N > 1) ? N - 1 : 1;
                
                r = canales.contains("R") ? (int)(Math.round(r / 255.0 * factor) * (255.0 / factor)) : 0;
                g = canales.contains("G") ? (int)(Math.round(g / 255.0 * factor) * (255.0 / factor)) : 0;
                b = canales.contains("B") ? (int)(Math.round(b / 255.0 * factor) * (255.0 / factor)) : 0;

                int pixeln = (a << 24) | (r << 16) | (g << 8) | b;
                out.setRGB(x, y, pixeln);
            }
        }
        return out; // Devolvemos la imagen para que el Service la gestione
    }
}