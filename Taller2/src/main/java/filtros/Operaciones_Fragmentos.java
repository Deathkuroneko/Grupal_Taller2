package filtros;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Operaciones_Fragmentos {

    public static BufferedImage aplicarAlphaTest(BufferedImage origen, int alphaRef) {
        int ancho = origen.getWidth();
        int alto = origen.getHeight();
        BufferedImage destino = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = origen.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                int brillo = (r + g + b) / 3;
                int a = brillo; 

                if (a > alphaRef) {
                    a = 255; 
                } else {
                    a = 0;   
                }

                destino.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return destino;
    }

    public static BufferedImage aplicarAlphaToCoverage(BufferedImage origen, int threshold) {
        int ancho = origen.getWidth();
        int alto = origen.getHeight();
        BufferedImage destino = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int centroX = ancho / 2;
        int centroY = alto / 2;
        double distMaxima = Math.sqrt(centroX * centroX + centroY * centroY);
        int[] patronDither = { 0, 128, 192, 64 };

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = origen.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                double distancia = Math.sqrt((x - centroX) * (x - centroX) + (y - centroY) * (y - centroY));
                int a = (int) ((distMaxima - distancia) * 255 / distMaxima);

                int indice = (y % 2) * 2 + (x % 2);
                int patron = patronDither[indice];
                // Desplazamos el patrón según el threshold (centrado en 128)
                int patronAjustado = Math.min(255, Math.max(0, patron + (threshold - 128)));
                a = (a > patronAjustado) ? 255 : 0;

                destino.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return destino;
    }

    public static BufferedImage aplicarZTest(BufferedImage origen, int depthRef) {
        int ancho = origen.getWidth();
        int alto = origen.getHeight();
        BufferedImage destino = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int centroX = ancho / 2;
        int centroY = alto / 2;
        double distMaxima = Math.sqrt(centroX * centroX + centroY * centroY);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = origen.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                double distancia = Math.sqrt((x - centroX) * (x - centroX) + (y - centroY) * (y - centroY));
                int depthIncoming = (int) ((distancia * 255) / distMaxima);

                int a;
                if (depthIncoming < depthRef) {

                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    a = 255;
                } else {
                    a = 0;
                }
                destino.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return destino;
    }
    //filtros de acumulacion y color 
    public static BufferedImage aplicarEfectoEstela(BufferedImage imagen, int muestras, int desplazamiento) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);


        if (muestras <= 1) {
            Graphics2D g = resultado.createGraphics();
            g.drawImage(imagen, 0, 0, null);
            g.dispose();
            return resultado;
        }

        double[] bufferR = new double[ancho * alto];
        double[] bufferG = new double[ancho * alto];
        double[] bufferB = new double[ancho * alto];
        double[] bufferPeso = new double[ancho * alto]; 

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int index = y * ancho + x;
                int pixel = imagen.getRGB(x, y);
                bufferR[index] = (pixel >> 16) & 0xFF;
                bufferG[index] = (pixel >> 8) & 0xFF;
                bufferB[index] = pixel & 0xFF;
                bufferPeso[index] = 1.0;
            }
        }

        for (int i = 1; i < muestras; i++) {
            int offset = i * desplazamiento;
            double peso = Math.pow(0.85, i); 

            if (offset >= ancho) break; 

            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    int origenX = x - offset;
                    if (origenX < 0) continue; 
                    int pixel = imagen.getRGB(origenX, y);
                    int r = (pixel >> 16) & 0xFF;
                    int g = (pixel >> 8) & 0xFF;
                    int b = pixel & 0xFF;

                    int index = y * ancho + x;
                    bufferR[index] += r * peso;
                    bufferG[index] += g * peso;
                    bufferB[index] += b * peso;
                    bufferPeso[index] += peso;
                }
            }
        }

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int index = y * ancho + x;
                double peso = bufferPeso[index];
                if (peso == 0) peso = 1.0; 

                int r = (int) Math.min(255, Math.max(0, bufferR[index] / peso));
                int g = (int) Math.min(255, Math.max(0, bufferG[index] / peso));
                int b = (int) Math.min(255, Math.max(0, bufferB[index] / peso));

                resultado.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }

        return resultado;
    }
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