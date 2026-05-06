package filtros;

import java.awt.image.BufferedImage;

public class VidrioEsmerilado {
	public static BufferedImage vidrioE(BufferedImage img) {
	    int w = img.getWidth();
	    int h = img.getHeight();
	    
	    BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    java.util.Random rnd = new java.util.Random();
	    
	    int r_distorsion = 4;

	    for (int y = 0; y < h; y++) {
	        for (int x = 0; x < w; x++) {
	            
	        	// distorsion de vidrio
	            int nx = x + rnd.nextInt(r_distorsion * 2 + 1) - r_distorsion;
	            int ny = y + rnd.nextInt(r_distorsion * 2 + 1) - r_distorsion;

	            
	            nx = Math.max(0, Math.min(w - 1, nx));
	            ny = Math.max(0, Math.min(h - 1, ny));

	            int pixel = img.getRGB(nx, ny);
	            int r = (pixel >> 16) & 0xFF;
	            int g = (pixel >> 8) & 0xFF;
	            int b = pixel & 0xFF;

	            
	            int brillo = (int)(0.299 * r + 0.587 * g + 0.114 * b);
	            int a = 150 + (int) ((brillo / 255.0) * 105);

	            int pixeln = (a << 24) | (r << 16) | (g << 8) | b;
	            out.setRGB(x, y, pixeln);
	        }
	    }
	    return out;
	}
}
