package filtros;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;



public class Convoluciones {
	public static BufferedImage aplicarFiltro(BufferedImage img, float[] matriz, int iteraciones) {
        if (img == null) return null;

        int size = (int) Math.sqrt(matriz.length);
        Kernel kernel = new Kernel(size, size, matriz);

        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        BufferedImage imagenActual = img;
        
        for (int i = 0; i < iteraciones; i++) {
        	
            BufferedImage destino = new BufferedImage(
                imagenActual.getWidth(), 
                imagenActual.getHeight(), 
                imagenActual.getType()
            );
            op.filter(imagenActual, destino);
            imagenActual = destino; 
        }
        
        return imagenActual;
    }
}
