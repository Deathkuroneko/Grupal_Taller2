package filtros;

import java.awt.image.BufferedImage;

public class EscalaGrisesN {
    public static BufferedImage escalaGN(BufferedImage img, int N) {
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

                int luma = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
                
                int factor = (N > 1) ? N - 1 : 1;
                int valor = (int) (Math.round(luma / 255.0 * factor) * (255.0 / factor));

                int pixeln = (a << 24) | (valor << 16) | (valor << 8) | valor;
                out.setRGB(x, y, pixeln);
            }
        }
        return out;
    }
}
