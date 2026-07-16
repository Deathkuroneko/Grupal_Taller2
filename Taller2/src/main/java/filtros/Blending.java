package filtros;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Blending {

	public static BufferedImage aplicarBlending(BufferedImage imagen1, BufferedImage imagen2, float alpha,String tipo) {

		int ancho = imagen1.getWidth();
		int alto = imagen1.getHeight();

		// Ajustar segunda imagen al tamaño de la primera

		Image imgTemp = imagen2.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

		BufferedImage img2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = img2.createGraphics();

		g.drawImage(imgTemp, 0, 0, null);

		g.dispose();

		BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < alto; y++) {

			for (int x = 0; x < ancho; x++) {

				int pixel1 = imagen1.getRGB(x, y);

				int pixel2 = img2.getRGB(x, y);

				int r1 = (pixel1 >> 16) & 255;
				int g1 = (pixel1 >> 8) & 255;
				int b1 = pixel1 & 255;

				int r2 = (pixel2 >> 16) & 255;
				int g2 = (pixel2 >> 8) & 255;
				int b2 = pixel2 & 255;

				int r, green, b;

				switch (tipo) {

				case "SUMA":
					r = Math.min(255, r1 + r2);
					green = Math.min(255, g1 + g2);
					b = Math.min(255, b1 + b2);

					break;
				case "MULTIPLICAR":
					r = (r1 * r2) / 255;
					green= (g1 * g2) / 255;
					b = (b1 * b2) / 255;
					break;
				case "NORMAL":
				default:

					r = (int) ((1 - alpha) * r1 + alpha * r2);
					green = (int) ((1 - alpha) * g1 + alpha * g2);
					b = (int) ((1 - alpha) * b1 + alpha * b2);

					break;
				}

				int pixel = (r << 16) | (green << 8) | b;

				resultado.setRGB(x, y, pixel);
			}
		}

		return resultado;
	}
}