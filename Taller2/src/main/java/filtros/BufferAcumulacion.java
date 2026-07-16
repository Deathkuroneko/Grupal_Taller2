package filtros;

import java.awt.image.BufferedImage;

public class BufferAcumulacion {

	public static BufferedImage aplicar(BufferedImage imagen, int muestras, int desplazamiento) {

		int ancho = imagen.getWidth();
		int alto = imagen.getHeight();

		float[] bufferR = new float[ancho * alto];
		float[] bufferG = new float[ancho * alto];
		float[] bufferB = new float[ancho * alto];

		BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				int index = y * ancho + x;
				int pixel = imagen.getRGB(x, y);
				bufferR[index] = (pixel >> 16) & 255;
				bufferG[index] = (pixel >> 8) & 255;
				bufferB[index] = pixel & 255;
			}
		}

		for (int i = 1; i < muestras; i++) {

			int offset = i * desplazamiento;

			float peso = (float) Math.pow(0.85, i);

			for (int y = 0; y < alto; y++) {
				for (int x = 0; x < ancho; x++) {

					int origenX = x - offset;

					if (origenX >= 0 && origenX < ancho) {

						int index = y * ancho + x;
						int pixel = imagen.getRGB(origenX, y);
						int r = (pixel >> 16) & 255;

						int g = (pixel >> 8) & 255;

						int b = pixel & 255;

						bufferR[index] += r * peso;
						bufferG[index] += g * peso;
						bufferB[index] += b * peso;

					}
				}
			}
		}

		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				int index = y * ancho + x;

				int r = Math.min(255, (int) bufferR[index]);
				int g = Math.min(255, (int) bufferG[index]);
				int b = Math.min(255, (int) bufferB[index]);

				int pixelNuevo = (r << 16) | (g << 8) | b;

				resultado.setRGB(x, y, pixelNuevo);
			}
		}

		return resultado;
	}

	// Día → Noche 
	public static BufferedImage diaANoche(BufferedImage imagen, float factor) {

		int ancho = imagen.getWidth();
		int alto = imagen.getHeight();

		BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {

				int pixel = imagen.getRGB(x, y);

				int r = (int) (((pixel >> 16) & 255) * factor);
				int g = (int) (((pixel >> 8) & 255) * factor);
				int b = (int) ((pixel & 255) * factor);

				r = Math.min(255, r);
				g = Math.min(255, g);
				b = Math.min(255, b);

				int nuevo = (r << 16) | (g << 8) | b;

				resultado.setRGB(x, y, nuevo);

			}
		}

		return resultado;
	}

}