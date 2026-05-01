package service;

import filtros.Degradados;
import filtros.Retro;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImagenService {
    private BufferedImage imagenOriginal;
    private BufferedImage imagenActual;

    public void cargarImagen(BufferedImage img) {
        this.imagenOriginal = img;
        this.imagenActual = img;
    }
    
 // Agrega esto a tu clase ImagenService
    public BufferedImage getImagenOriginal() {
        return imagenOriginal;
    }

    public void restablecer() {
        // La imagen actual vuelve a ser la original
        this.imagenActual = this.imagenOriginal;
    }

    // Aplica el filtro y actualiza el estado
    public BufferedImage aplicarRetro(int niveles) {
        if (imagenOriginal == null) return null;
        System.out.println("N = " + niveles);
        this.imagenActual = Retro.aplicarRetro(imagenOriginal, niveles);
        return imagenActual;
    }

    // Método dedicado para la opción de "Guardar" de la interfaz
    public void guardarImagen(File archivoDestino) throws Exception {
        if (imagenActual != null) {
            ImageIO.write(imagenActual, "png", archivoDestino);
        }
    }
    
    public BufferedImage aplicarDegradadoDerIzq() {
        if (imagenActual == null) return null;
        imagenActual = Degradados.derIzq(imagenActual);
        return imagenActual;
    }
    
    public BufferedImage aplicarDegradadoRadial() {
        if (imagenActual == null) return null;
        imagenActual = Degradados.radial(imagenActual);
        return imagenActual;
    }

    public BufferedImage getImagenActual() {
        return imagenActual;
    }
}


