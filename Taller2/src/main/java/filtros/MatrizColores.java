package filtros;

import java.awt.image.BufferedImage;

public class MatrizColores {
    public static BufferedImage aplicarMatriz(BufferedImage img, float[][] matriz) {
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

                int r1 = Math.min(255, Math.round(matriz[0][0] * r + matriz[0][1] * g + matriz[0][2] * b + matriz[0][3] * a));
                int g1 = Math.min(255, Math.round(matriz[1][0] * r + matriz[1][1] * g + matriz[1][2] * b + matriz[1][3] * a));
                int b1 = Math.min(255, Math.round(matriz[2][0] * r + matriz[2][1] * g + matriz[2][2] * b + matriz[2][3] * a));
                int a1 = Math.min(255, Math.round(matriz[3][0] * r + matriz[3][1] * g + matriz[3][2] * b + matriz[3][3] * a));

                int pixeln = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                out.setRGB(x, y, pixeln);
            }
        }
        return out;
    }
}
