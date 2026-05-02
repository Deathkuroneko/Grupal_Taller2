package filtros;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Colores {
	public static BufferedImage color(BufferedImage img, Color tinte) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int pixel = img.getRGB(x, y);
				int a = (pixel >> 24) & 0xff;
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;

				// Convertimos el píxel a escala de grises para conservar la luminosidad
				int luminosidad = (int) (0.299 * r + 0.587 * g + 0.114 * b);

				// Aplicamos el tinte proporcionalmente a la luminosidad
				int nr = (luminosidad * tinte.getRed()) / 255;
				int ng = (luminosidad * tinte.getGreen()) / 255;
				int nb = (luminosidad * tinte.getBlue()) / 255;

				out.setRGB(x, y, (a << 24) | (nr << 16) | (ng << 8) | nb);
			}
		}
		return out;
	}

}
