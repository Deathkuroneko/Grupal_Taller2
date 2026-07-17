package util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class OperacionesPixel {

    public static BufferedImage generarStencil(int ancho, int alto) {
        BufferedImage stencil = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = stencil.createGraphics();
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, ancho, alto);
        g.setColor(java.awt.Color.WHITE);
        int radio = Math.min(ancho, alto) / 3;
        int cx = ancho / 2;
        int cy = alto / 2;
        g.fillOval(cx - radio, cy - radio, 2 * radio, 2 * radio);
        g.dispose();
        return stencil;
    }

    public static BufferedImage aplicarStencil(BufferedImage imagen, BufferedImage stencil, int umbral) {
        int ancho = Math.min(imagen.getWidth(), stencil.getWidth());
        int alto = Math.min(imagen.getHeight(), stencil.getHeight());
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixelImg = imagen.getRGB(x, y);
                int pixelMask = stencil.getRGB(x, y);

                int r = (pixelMask >> 16) & 0xFF;
                int g = (pixelMask >> 8) & 0xFF;
                int b = pixelMask & 0xFF;
                int gris = (r + g + b) / 3;

                if (gris > umbral) {
                    resultado.setRGB(x, y, pixelImg);
                } else {
                    resultado.setRGB(x, y, 0xFF000000); 
                }
            }
        }
        return resultado;
    }

    public static BufferedImage operacionLogica(BufferedImage img1, BufferedImage img2, String operacion) {
        int ancho = img1.getWidth();
        int alto = img1.getHeight();
        if (img2 != null) {
            ancho = Math.min(ancho, img2.getWidth());
            alto = Math.min(alto, img2.getHeight());
        }
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p1 = img1.getRGB(x, y);
                int r1 = (p1 >> 16) & 0xFF, g1 = (p1 >> 8) & 0xFF, b1 = p1 & 0xFF;
                int r2 = 0, g2 = 0, b2 = 0;
                if (img2 != null) {
                    int p2 = img2.getRGB(x, y);
                    r2 = (p2 >> 16) & 0xFF;
                    g2 = (p2 >> 8) & 0xFF;
                    b2 = p2 & 0xFF;
                }
                int rFinal, gFinal, bFinal;
                switch (operacion.toUpperCase()) {
                    case "OR":
                        rFinal = r1 | r2;
                        gFinal = g1 | g2;
                        bFinal = b1 | b2;
                        break;
                    case "AND":
                        rFinal = r1 & r2;
                        gFinal = g1 & g2;
                        bFinal = b1 & b2;
                        break;
                    case "XOR":
                        rFinal = r1 ^ r2;
                        gFinal = g1 ^ g2;
                        bFinal = b1 ^ b2;
                        break;
                    case "NOT":
                        rFinal = (~r1) & 0xFF;
                        gFinal = (~g1) & 0xFF;
                        bFinal = (~b1) & 0xFF;
                        break;
                    default:
                        rFinal = r1;
                        gFinal = g1;
                        bFinal = b1;
                }
                resultado.setRGB(x, y, (rFinal << 16) | (gFinal << 8) | bFinal);
            }
        }
        return resultado;
    }

    public static BufferedImage blending(BufferedImage img1, BufferedImage img2, float alpha) {
        int ancho = Math.min(img1.getWidth(), img2.getWidth());
        int alto = Math.min(img1.getHeight(), img2.getHeight());
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int r1 = (p1 >> 16) & 0xFF, g1 = (p1 >> 8) & 0xFF, b1 = p1 & 0xFF;
                int r2 = (p2 >> 16) & 0xFF, g2 = (p2 >> 8) & 0xFF, b2 = p2 & 0xFF;
                int rFinal = (int)(r2 * alpha + r1 * (1 - alpha));
                int gFinal = (int)(g2 * alpha + g1 * (1 - alpha));
                int bFinal = (int)(b2 * alpha + b1 * (1 - alpha));
                resultado.setRGB(x, y, (rFinal << 16) | (gFinal << 8) | bFinal);
            }
        }
        return resultado;
    }
}