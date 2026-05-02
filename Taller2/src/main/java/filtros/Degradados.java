package filtros;

import java.awt.image.BufferedImage;

public class Degradados {

	// IZQ → DER (oscurece hacia la derecha)
	public static BufferedImage izqDer(BufferedImage img) {
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

				float factor = (float) (w - x) / w;

				r = (int) (r + (255 - r) * factor);
				g = (int) (g + (255 - g) * factor);
				b = (int) (b + (255 - b) * factor);

				int nuevo = (a << 24) | (r << 16) | (g << 8) | b;
				out.setRGB(x, y, nuevo);
			}
		}
		return out;
	}

	// DER → IZQ (oscurece hacia la derecha)
	public static BufferedImage derIzq(BufferedImage img) {
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

				float factor = (float) x / w;

				r = (int) (r + (255 - r) * factor);
				g = (int) (g + (255 - g) * factor);
				b = (int) (b + (255 - b) * factor);

				int nuevo = (a << 24) | (r << 16) | (g << 8) | b;
				out.setRGB(x, y, nuevo);
			}
		}
		return out;
	}

	// ARRIBA → ABAJO
	public static BufferedImage arribaAbajo(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < h; y++) {
			// El factor depende de 'y'
			float factor = (float) (h - y) / h;

			for (int x = 0; x < w; x++) {
				int pixel = img.getRGB(x, y);
				int a = (pixel >> 24) & 0xFF;
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;

				// Aplicar degradado (hacia blanco en este caso)
				r = (int) (r + (255 - r) * factor);
				g = (int) (g + (255 - g) * factor);
				b = (int) (b + (255 - b) * factor);

				out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
			}
		}
		return out;
	}

	// ABAJO → ARRIBA
	public static BufferedImage abajoArriba(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < h; y++) {
			// Invertimos la lógica del factor: y / h
			float factor = (float) y / h;

			for (int x = 0; x < w; x++) {
				int pixel = img.getRGB(x, y);
				int a = (pixel >> 24) & 0xFF;
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;

				r = (int) (r + (255 - r) * factor);
				g = (int) (g + (255 - g) * factor);
				b = (int) (b + (255 - b) * factor);

				out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
			}
		}
		return out;
	}

	// CENTRO → OSCURO
	public static BufferedImage radial(BufferedImage img) {

		int w = img.getWidth();
		int h = img.getHeight();

		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		int cx = w / 2;
		int cy = h / 2;

		double maxDist = Math.sqrt(cx * cx + cy * cy);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {

				int pixel = img.getRGB(x, y);

				int a = (pixel >> 24) & 0xFF;
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = pixel & 0xFF;

				double dist = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
				float factor = (float) (dist / maxDist);

				r = (int) (r + (255 - r) * (1 - factor));
				g = (int) (g + (255 - g) * (1 - factor));
				b = (int) (b + (255 - b) * (1 - factor));

				int pixeln = (a << 24) | (r << 16) | (g << 8) | b;
				out.setRGB(x, y, pixeln);
			}
		}

		return out;
	}

	public static BufferedImage radialInverso(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		int cx = w / 2;
		int cy = h / 2;
		// La distancia máxima real a una esquina es maxDist
		double maxDist = Math.sqrt(cx * cx + cy * cy);

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int pixel = img.getRGB(x, y);

				int a = (pixel >> 24) & 0xFF;
				int r = (pixel >> 16) & 0xFF;
				int g = (pixel >> 8) & 0xFF;
				int b = (pixel >> 0) & 0xFF;

				double dist = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));

				// Factor va de 0 (centro) a 1 (esquinas)
				float factor = (float) (dist / maxDist);
				r = (int) (r + (255 - r) * factor);
				g = (int) (g + (255 - g) * factor);
				b = (int) (b + (255 - b) * factor);

				// IMPORTANTE: Asegurar que no exceda 255 (Clamping)
				r = Math.min(255, Math.max(0, r));
				g = Math.min(255, Math.max(0, g));
				b = Math.min(255, Math.max(0, b));

				int pixeln = (a << 24) | (r << 16) | (g << 8) | b;
				out.setRGB(x, y, pixeln);
			}
		}
		return out;
	}
}