package service;

import filtros.Degradados;
import filtros.Retro;
import filtros.Colores;
import filtros.HSV;
import filtros.Negativo;
import filtros.ReducirBits;
import filtros.EscalaGrisesN;
import filtros.BlancoNegro;
import filtros.VidrioEsmerilado;
import java.awt.Color;
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
	public BufferedImage aplicarRetro(int N, String canales) {
		if (imagenOriginal == null)
			return null;
		this.imagenActual = Retro.aplicarRetro(imagenOriginal, N, canales);
		return imagenActual;
	}

	// Método dedicado para la opción de "Guardar" de la interfaz
	public void guardarImagen(File archivoDestino, String formato) throws Exception {
		if (imagenActual != null) {
			ImageIO.write(imagenActual, formato, archivoDestino);
		}
	}

	public BufferedImage aplicarDegradadoDerIzq() {
		if (imagenActual == null)
			return null;
		imagenActual = Degradados.derIzq(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoIzqDer() {
		if (imagenActual == null)
			return null;
		imagenActual = Degradados.izqDer(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoArriAbajo() {
		if (imagenActual == null)
			return null;
		imagenActual = Degradados.arribaAbajo(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoAbajoArriba() {
		if (imagenActual == null)
			return null;
		imagenActual = Degradados.abajoArriba(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoRadial() {
		if (imagenActual == null)
			return null;
		imagenActual = Degradados.radial(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoRadialInverso() {
		if (imagenActual == null)
			return null;
		imagenActual = Degradados.radialInverso(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarTinte(Color colorSeleccionado) {
		if (imagenActual == null)
			return null;
		this.imagenActual = Colores.color(imagenActual, colorSeleccionado);
		return imagenActual;
	}

	public BufferedImage aplicarHSV(float fTran, float fSatu, float fBri) {
		if (imagenActual == null)
			return null;

		this.imagenActual = HSV.aplicarHSV(imagenActual, fTran, fSatu, fBri);

		return imagenActual;
	}

	public BufferedImage reducirBit(int bits) {
		if (imagenOriginal == null)
			return null;
		this.imagenActual = ReducirBits.reducirBits(imagenActual, bits);
		return imagenActual;
	}

	public BufferedImage aplicarNegativo() {
		if (imagenActual == null)
			return null;

		this.imagenActual = Negativo.negativo(imagenOriginal);

		return imagenActual;
	}

	public BufferedImage aplicarBlancoNegro() {
		if (imagenOriginal == null)
			return null;

		this.imagenActual = BlancoNegro.bNegro(imagenOriginal);

		return imagenActual;
	}

	public BufferedImage aplicarVidrioEsmerilado() {
		if (imagenActual == null)
			return null;

		this.imagenActual = VidrioEsmerilado.vidrioE(imagenActual);

		return imagenActual;
	}

	public BufferedImage aplicarEscalaGrises(int N) {
		if (imagenActual == null)
			return null;

		this.imagenActual = EscalaGrisesN.escalaGN(imagenActual, N);

		return imagenActual;
	}

	public BufferedImage aplicarEfectoConvolucion(float[] matriz, int iteraciones) {
		if (this.imagenOriginal == null) {
			return null;
		}
		this.imagenActual = filtros.Convoluciones.aplicarFiltro(this.imagenActual, matriz, iteraciones);
		return this.imagenActual;
	}

	public BufferedImage aplicarKernelPersonalizado(float valorCentral) {
		if (this.imagenOriginal == null)
			return null;

		float[] kernelVariable = { 0, 0, 0, 0, valorCentral, 0, 0, 0, 0 };

		// Es vital asignar el resultado a imagenActual
		this.imagenActual = filtros.Convoluciones.aplicarFiltro(this.imagenOriginal, kernelVariable, 1);
		return this.imagenActual;
	}

	public BufferedImage getImagenActual() {
		return imagenActual;
	}
}
