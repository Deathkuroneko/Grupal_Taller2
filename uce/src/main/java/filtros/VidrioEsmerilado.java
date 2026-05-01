package filtros;

import java.awt.image.BufferedImage;

public class VidrioEsmerilado {
    public static BufferedImage aplicar(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = img.getRGB(x, y);

                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                int brillo = (r + g + b) / 3;
                int a = 50 + (int) ((brillo / 255.0) * 205);

                int pixeln = (a << 24) | (r << 16) | (g << 8) | b;
                out.setRGB(x, y, pixeln);
            }
        }
        return out;
    }
}
