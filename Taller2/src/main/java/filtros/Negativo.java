package filtros;

import java.awt.image.BufferedImage;

public class Negativo {
	public static BufferedImage negativo(BufferedImage img) {
		int w = img.getWidth();
	    int h = img.getHeight();
	    BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    for (int y = 0; y < h; y++) {
	        for (int x = 0; x < w; x++) {
	            int pixel = img.getRGB(x, y);
	            int a = (pixel >> 24) & 0xff;
	            int r = 255 - ((pixel >> 16) & 0xff);
	            int g = 255 - ((pixel >> 8) & 0xff);
	            int b = 255 - (pixel & 0xff);
	            out.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
	        }
	    }
	    return out;
	}
}
