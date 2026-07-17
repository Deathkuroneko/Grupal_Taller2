package filtros;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class EcualizadorHistograma {

	public static BufferedImage ecualizar(BufferedImage imagen, float factor) {
		if (imagen == null)
			return null;
		if (factor < 0)
			factor = 0;
		if (factor > 1)
			factor = 1;

		int ancho = imagen.getWidth();
		int alto = imagen.getHeight();
		int tipo = imagen.getType();

		boolean tieneAlfa = (tipo == BufferedImage.TYPE_INT_ARGB || tipo == BufferedImage.TYPE_4BYTE_ABGR
				|| tipo == BufferedImage.TYPE_4BYTE_ABGR_PRE);

		BufferedImage resultado = new BufferedImage(ancho, alto, tipo);

		int[] histoBrillo = new int[256];
		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				int pixel = imagen.getRGB(x, y);
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;
				int brillo = (int) (0.299 * r + 0.587 * g + 0.114 * b);
				histoBrillo[brillo]++;
			}
		}

		int[] cdf = new int[256];
		int totalPixeles = ancho * alto;
		cdf[0] = histoBrillo[0];
		for (int i = 1; i < 256; i++) {
			cdf[i] = cdf[i - 1] + histoBrillo[i];
		}

		int min = 0;
		while (min < 256 && cdf[min] == 0)
			min++;
		int max = 255;
		while (max >= 0 && cdf[max] == totalPixeles)
			max--;

		int[] mapa = new int[256];
		if (min >= max) {

			for (int i = 0; i < 256; i++)
				mapa[i] = i;
		} else {
			for (int i = 0; i < 256; i++) {
				if (cdf[i] == 0) {
					mapa[i] = 0;
				} else {
					mapa[i] = (int) (((cdf[i] - cdf[min]) * 255.0) / (totalPixeles - cdf[min]));
				}
			}
		}

		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				int pixel = imagen.getRGB(x, y);
				int a = tieneAlfa ? ((pixel >> 24) & 0xFF) : 255;
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;

				int brilloOrig = (int) (0.299 * r + 0.587 * g + 0.114 * b);
				int brilloEcualizado = mapa[brilloOrig];

				int nuevoBrillo = (int) (brilloOrig * (1 - factor) + brilloEcualizado * factor);
				if (brilloOrig == 0) {
					r = g = b = 0;
				} else {
					float escala = (float) nuevoBrillo / brilloOrig;
					r = Math.min(255, (int) (r * escala));
					g = Math.min(255, (int) (g * escala));
					b = Math.min(255, (int) (b * escala));
				}

				int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
				resultado.setRGB(x, y, pixelNuevo);
			}
		}

		return resultado;
	}
}