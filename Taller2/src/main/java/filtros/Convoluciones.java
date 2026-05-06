package filtros;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Convoluciones {

    public static BufferedImage aplicarFiltro(BufferedImage img, float[] matriz, int iteraciones) {
        if (img == null) return null;

        img = asegurarRGB(img);

        int size = (int) Math.sqrt(matriz.length);
        Kernel kernel = new Kernel(size, size, matriz);

        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

        BufferedImage imagenActual = img;

        for (int i = 0; i < iteraciones; i++) {

            BufferedImage destino = new BufferedImage(
                imagenActual.getWidth(),
                imagenActual.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );

            op.filter(imagenActual, destino);
            imagenActual = destino;
        }

        return imagenActual;
    }

    private static BufferedImage asegurarRGB(BufferedImage img) {
        if (img.getType() == BufferedImage.TYPE_INT_RGB) return img;

        BufferedImage nueva = new BufferedImage(
            img.getWidth(),
            img.getHeight(),
            BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = nueva.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return nueva;
    }
}