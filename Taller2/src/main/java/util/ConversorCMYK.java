package util;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class ConversorCMYK {


    public static BufferedImage rgbToCmykSimulado(BufferedImage imagen) {
        if (imagen == null) return null;

        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        boolean tieneAlfa = imagen.getType() == BufferedImage.TYPE_INT_ARGB ||
                            imagen.getType() == BufferedImage.TYPE_4BYTE_ABGR;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = imagen.getRGB(x, y);
                int a = tieneAlfa ? ((pixel >> 24) & 0xFF) : 255;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Convertir RGB a CMYK (valores 0-1)
                float rf = r / 255f;
                float gf = g / 255f;
                float bf = b / 255f;

                float k = 1 - Math.max(rf, Math.max(gf, bf));
                float c, m, yc;
                if (k == 1) {
                    c = m = yc = 0;
                } else {
                    c = (1 - rf - k) / (1 - k);
                    m = (1 - gf - k) / (1 - k);
                    yc = (1 - bf - k) / (1 - k);
                }

                // Convertir CMYK de vuelta a RGB para visualización
                int rCmyk = (int) (255 * (1 - c) * (1 - k));
                int gCmyk = (int) (255 * (1 - m) * (1 - k));
                int bCmyk = (int) (255 * (1 - yc) * (1 - k));

                rCmyk = Math.max(0, Math.min(255, rCmyk));
                gCmyk = Math.max(0, Math.min(255, gCmyk));
                bCmyk = Math.max(0, Math.min(255, bCmyk));

                int pixelNuevo = (a << 24) | (rCmyk << 16) | (gCmyk << 8) | bCmyk;
                resultado.setRGB(x, y, pixelNuevo);
            }
        }
        return resultado;
    }
    
    public static BufferedImage convertir(BufferedImage imagen, boolean aCmyk) {
        if (imagen == null) return null;
        return rgbToCmykSimulado(imagen);
    }
}