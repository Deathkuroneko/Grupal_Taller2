package filtros;

import java.awt.image.BufferedImage;

public class BlancoNegro {
    public static BufferedImage aplicar(BufferedImage img) {
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

                int promedio = (r + g + b) / 3;
                int valor = (promedio > 127) ? 255 : 0;

                int pixeln = (a << 24) | (valor << 16) | (valor << 8) | valor;
                out.setRGB(x, y, pixeln);
            }
        }
        return out;
    }
}
