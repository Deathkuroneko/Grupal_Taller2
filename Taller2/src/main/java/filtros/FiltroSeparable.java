package filtros;

import java.awt.image.BufferedImage;

public class FiltroSeparable {


    public static BufferedImage aplicarFiltroSeparable(BufferedImage imagen, int iteraciones) {
        if (imagen == null || iteraciones <= 0) {
            return imagen;
        }


        double[] kernel = { 0.25, 0.5, 0.25 };

        BufferedImage resultado = imagen;
        for (int i = 0; i < iteraciones; i++) {
            resultado = aplicarPaso(resultado, kernel);
        }
        return resultado;
    }


    private static BufferedImage aplicarPaso(BufferedImage imagen, double[] kernel) {
        BufferedImage horizontal = convolucionHorizontal(imagen, kernel);
        return convolucionVertical(horizontal, kernel);
    }


    private static BufferedImage convolucionHorizontal(BufferedImage imagen, double[] kernel) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        int radio = kernel.length / 2;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                double sumaRojo = 0, sumaVerde = 0, sumaAzul = 0, sumaAlfa = 0;

                for (int k = -radio; k <= radio; k++) {
                    int vecinoX = clamp(x + k, 0, ancho - 1);
                    int pixel = imagen.getRGB(vecinoX, y);

                    int alfa = (pixel >> 24) & 0xFF;
                    int rojo = (pixel >> 16) & 0xFF;
                    int verde = (pixel >> 8) & 0xFF;
                    int azul = pixel & 0xFF;

                    double peso = kernel[k + radio];
                    sumaAlfa  += alfa * peso;
                    sumaRojo  += rojo * peso;
                    sumaVerde += verde * peso;
                    sumaAzul  += azul * peso;
                }

                int a = clamp((int) Math.round(sumaAlfa));
                int r = clamp((int) Math.round(sumaRojo));
                int g = clamp((int) Math.round(sumaVerde));
                int b = clamp((int) Math.round(sumaAzul));

                salida.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return salida;
    }

 
    private static BufferedImage convolucionVertical(BufferedImage imagen, double[] kernel) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        int radio = kernel.length / 2;

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                double sumaRojo = 0, sumaVerde = 0, sumaAzul = 0, sumaAlfa = 0;

                for (int k = -radio; k <= radio; k++) {
                    int vecinoY = clamp(y + k, 0, alto - 1);
                    int pixel = imagen.getRGB(x, vecinoY);

                    int alfa = (pixel >> 24) & 0xFF;
                    int rojo = (pixel >> 16) & 0xFF;
                    int verde = (pixel >> 8) & 0xFF;
                    int azul = pixel & 0xFF;

                    double peso = kernel[k + radio];
                    sumaAlfa  += alfa * peso;
                    sumaRojo  += rojo * peso;
                    sumaVerde += verde * peso;
                    sumaAzul  += azul * peso;
                }

                int a = clamp((int) Math.round(sumaAlfa));
                int r = clamp((int) Math.round(sumaRojo));
                int g = clamp((int) Math.round(sumaVerde));
                int b = clamp((int) Math.round(sumaAzul));

                salida.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
            }
        }
        return salida;
    }

    private static int clamp(int valor, int min, int max) {
        return Math.max(min, Math.min(max, valor));
    }

    private static int clamp(int valor) {
        return clamp(valor, 0, 255);
    }
}