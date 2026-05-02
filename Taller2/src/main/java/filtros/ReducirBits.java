package filtros;

import java.awt.image.BufferedImage;

public class ReducirBits {
	public static BufferedImage reducirBits(BufferedImage img, int bitsDeseados) {
	    if (bitsDeseados < 1) bitsDeseados = 1;
	    if (bitsDeseados > 8) bitsDeseados = 8;

	    int w = img.getWidth();
	    int h = img.getHeight();
	    BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    // 1. Calculamos el desplazamiento
	    int desplazamiento = 8 - bitsDeseados;
	    
	    // 2. CREAMOS LA MÁSCARA DE RECORTE
	    // Si bits=2, la máscara debe ser 11000000 en binario (0xC0)
	    // Esto apaga los bits menos significativos antes de procesar
	    int mascaraCorte = (0xFF << desplazamiento) & 0xFF;

	    for (int y = 0; y < h; y++) {
	        for (int x = 0; x < w; x++) {
	            int pixel = img.getRGB(x, y);

	            int a = (pixel >> 24) & 0xFF;
	            int r = (pixel >> 16) & 0xFF;
	            int g = (pixel >> 8) & 0xFF;
	            int b = pixel & 0xFF;

	            // Paso A: "Limpiamos" los bits que no queremos
	            r = r & mascaraCorte;
	            g = g & mascaraCorte;
	            b = b & mascaraCorte;

	            // Paso B: Normalización opcional
	            // Para evitar que la imagen se oscurezca al apagar bits, 
	            // llenamos los bits vacíos con una copia de los bits significativos
	            r = r | (r >> bitsDeseados);
	            g = g | (g >> bitsDeseados);
	            b = b | (b >> bitsDeseados);

	            int nuevo = (a << 24) | (r << 16) | (g << 8) | b;
	            out.setRGB(x, y, nuevo);
	        }
	    }
	    return out;
	}
}
