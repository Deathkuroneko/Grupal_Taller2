package service;

import filtros.Degradados;
import filtros.Retro;
import filtros.VidrioEsmerilado;
import filtros.BlancoNegro;
import filtros.EscalaGrisesN;
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
    public BufferedImage aplicarRetro(int niveles, String canales) {
        if (imagenOriginal == null) return null;
        System.out.println("N = " + niveles + " Canales = " + canales);
        this.imagenActual = Retro.aplicarRetro(imagenOriginal, niveles, canales);
        return imagenActual;
    }

    public BufferedImage aplicarVidrioEsmerilado() {
        if (imagenActual == null) return null;
        this.imagenActual = VidrioEsmerilado.aplicar(imagenActual);
        return imagenActual;
    }

    public BufferedImage aplicarBlancoNegro() {
        if (imagenActual == null) return null;
        this.imagenActual = BlancoNegro.aplicar(imagenActual);
        return imagenActual;
    }

    public BufferedImage aplicarEscalaGrisesN(int n) {
        if (imagenActual == null) return null;
        this.imagenActual = EscalaGrisesN.aplicar(imagenOriginal, n);
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


