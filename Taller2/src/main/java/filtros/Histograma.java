package filtros;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Histograma {

	public static BufferedImage generarHistograma(BufferedImage imagen, boolean red, boolean green, boolean blue) {

		int anchoHisto = 900;
		int altoHisto = 600;

		int[] histoRed = new int[256];
		int[] histoGreen = new int[256];
		int[] histoBlue = new int[256];

		BufferedImage histogramaImg = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = histogramaImg.createGraphics();

		// ANTIALIASING
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// ================= FONDO =================

		GradientPaint fondo = new GradientPaint(0, 0, new Color(30, 30, 35), 0, altoHisto, new Color(5, 5, 10));

		g.setPaint(fondo);
		g.fillRect(0, 0, anchoHisto, altoHisto);

		// ================= CALCULAR =================

		for (int y = 0; y < imagen.getHeight(); y++) {

			for (int x = 0; x < imagen.getWidth(); x++) {

				int pixel = imagen.getRGB(x, y);

				int r = (pixel >> 16) & 255;
				int gr = (pixel >> 8) & 255;
				int b = pixel & 255;

				histoRed[r]++;
				histoGreen[gr]++;
				histoBlue[b]++;
			}
		}

		int max = Math.max(maximo(histoRed), Math.max(maximo(histoGreen), maximo(histoBlue)));

		// ================= TITULO =================

		g.setFont(new Font("Segoe UI", Font.BOLD, 22));
		g.setColor(Color.WHITE);

		g.drawString("Histograma RGB", 30, 40);

		// ================= CUADRICULA =================

		g.setStroke(new BasicStroke(1));

		g.setColor(new Color(255, 255, 255, 40));

		for (int i = 0; i <= 10; i++) {

			int y = 80 + i * 45;

			g.drawLine(60, y, 850, y);
		}

		// ================= EJES =================

		g.setColor(Color.WHITE);

		g.drawLine(60, 530, 850, 530);

		g.drawLine(60, 80, 60, 530);

		float escalaX = 790f / 256f;
		float escalaY = 430f / max;
		// ================= DIBUJAR =================

		if (red) {
			dibujarBarras(g, histoRed, new Color(255, 70, 70, 180), escalaX, escalaY);
		}

		if (green) {
			dibujarBarras(g, histoGreen, new Color(50, 255, 120, 180), escalaX, escalaY);
		}

		if (blue) {
			dibujarBarras(g, histoBlue, new Color(70, 120, 255, 180), escalaX, escalaY);
		}
	
		g.dispose();
		return histogramaImg;

	}

	private static int maximo(int[] h) {
		int max = 0;
		for (int n : h) {
			if (n > max)
				max = n;
		}
		return max;
	}

	private static void dibujarBarras(Graphics2D g, int[] histo, Color color, float escalaX, float escalaY) {
		g.setColor(color);
		for (int i = 0; i < 256; i++) {
			int altura = (int) (histo[i] * escalaY);
			int x = 60 + (int) (i * escalaX);
			int y = 530 - altura;
			g.fillRoundRect(x, y, 3, altura, 3, 3);

		}

	}
}