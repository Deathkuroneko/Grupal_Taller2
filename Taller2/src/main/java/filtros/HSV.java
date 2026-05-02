package filtros;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class HSV {
    public static BufferedImage aplicarHSV(BufferedImage img, float factorTran, float factorSatu, float factorBrillo) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        float[] hsb = new float[3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                
                // 1. Extraer canales
                int a = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                // 2. Modificar transparencia (Alfa)
                a = (int) Math.min(255, a * factorTran);

                // 3. Convertir a HSB
                Color.RGBtoHSB(r, g, b, hsb);
                
                float hue = hsb[0]; 
  
                float saturation = Math.min(1.0f, hsb[1] * factorSatu);
                float brightness = Math.min(1.0f, hsb[2] * factorBrillo);

                // 5. Convertir de nuevo a RGB (esto devuelve un entero con los canales R, G y B ya empaquetados)
                int rgbProcesado = Color.HSBtoRGB(hue, saturation, brightness);
                
                // 6. Re-empaquetar con la nueva transparencia
                // Limpiamos el canal alfa que devuelve HSBtoRGB (que suele ser FF) y ponemos el nuestro
                int nuevoPixel = (a << 24) | (rgbProcesado & 0x00FFFFFF);
                
                out.setRGB(x, y, nuevoPixel);
            }
        }
        return out;
    }
}