package service;

import java.awt.Color;
import filtros.BufferAcumulacion;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import filtros.BlancoNegro;
import filtros.Colores;
import filtros.Convoluciones;
import filtros.Degradados;
import filtros.EscalaGrisesN;
import filtros.HSV;
import filtros.MatrizColores;
import filtros.Negativo;
import filtros.ReducirBits;
import filtros.Retro;
import filtros.VidrioEsmerilado;

public class ImagenService {

	private BufferedImage imagenOriginal;
	private BufferedImage imagenActual;
	private BufferedImage imagenSecundaria;

	// ===================== CARGA =====================
	public void cargarImagen(BufferedImage img) {
		this.imagenOriginal = deepCopy(img);
		this.imagenActual = deepCopy(img);
	}

	public BufferedImage getImagenOriginal() {
		return imagenOriginal;
	}

	public BufferedImage getImagenActual() {
		return imagenActual;
	}

	public void cargarImagenSecundaria(BufferedImage img) {
		imagenSecundaria = deepCopy(img);
	}

	public BufferedImage getImagenSecundaria() {
		return imagenSecundaria;
	}

	public boolean hayImagen() {
		return imagenActual != null;
	}

	// ===================== RESET =====================
	public void restablecer() {
		if (imagenOriginal != null) {
			imagenActual = deepCopy(imagenOriginal);
		}
	}

	// ===================== GUARDAR =====================
	public void guardarImagen(File archivoDestino, String formato) throws Exception {
		if (imagenActual != null) {
			ImageIO.write(imagenActual, formato, archivoDestino);
		}
	}

	// ===================== FILTROS =====================

	public BufferedImage aplicarRetro(int N, String canales) {
		if (!hayImagen())
			return null;

		imagenActual = Retro.aplicarRetro(imagenActual, N, canales);
		return imagenActual;
	}

	public BufferedImage aplicarNegativo() {
		if (!hayImagen())
			return null;

		imagenActual = Negativo.negativo(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarBlancoNegro() {
		if (!hayImagen())
			return null;

		imagenActual = BlancoNegro.bNegro(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarVidrioEsmerilado() {
		if (!hayImagen())
			return null;

		imagenActual = VidrioEsmerilado.vidrioE(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarEscalaGrises(int niveles) {
		if (!hayImagen())
			return null;

		imagenActual = EscalaGrisesN.escalaGN(imagenActual, niveles);
		return imagenActual;
	}

	public BufferedImage reducirBit(int bits) {
		if (!hayImagen())
			return null;

		imagenActual = ReducirBits.reducirBits(imagenActual, bits);
		return imagenActual;
	}

	public BufferedImage aplicarTinte(Color color) {
		if (!hayImagen())
			return null;

		imagenActual = Colores.color(imagenActual, color);
		return imagenActual;
	}

	public BufferedImage aplicarHSV(float opacidad, float saturacion, float brillo) {
		if (!hayImagen())
			return null;

		imagenActual = HSV.aplicarHSV(imagenActual, opacidad, saturacion, brillo);
		return imagenActual;
	}

	// ===================== DEGRADADOS =====================

	public BufferedImage aplicarDegradadoIzqDer() {
		if (!hayImagen())
			return null;

		imagenActual = Degradados.izqDer(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoDerIzq() {
		if (!hayImagen())
			return null;

		imagenActual = Degradados.derIzq(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoArribaAbajo() {
		if (!hayImagen())
			return null;

		imagenActual = Degradados.arribaAbajo(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoAbajoArriba() {
		if (!hayImagen())
			return null;

		imagenActual = Degradados.abajoArriba(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoRadial() {
		if (!hayImagen())
			return null;

		imagenActual = Degradados.radial(imagenActual);
		return imagenActual;
	}

	public BufferedImage aplicarDegradadoRadialInverso() {
		if (!hayImagen())
			return null;

		imagenActual = Degradados.radialInverso(imagenActual);
		return imagenActual;
	}

	// ===================== CONVOLUCIONES =====================

	public BufferedImage aplicarEfectoConvolucion(float[] kernel, int iteraciones) {
		if (!hayImagen())
			return null;

		imagenActual = Convoluciones.aplicarFiltro(imagenActual, kernel, iteraciones);
		return imagenActual;
	}

	public BufferedImage aplicarKernelPersonalizado(float valorCentral) {
		if (!hayImagen())
			return null;

		float[] kernel = { 0, 0, 0, 0, valorCentral, 0, 0, 0, 0 };

		imagenActual = Convoluciones.aplicarFiltro(imagenActual, kernel, 1);
		return imagenActual;
	}

	// ===================== MATRIZ COLOR =====================
	public BufferedImage aplicarEfectoMatrizColor(float[][] matriz) {
		if (!hayImagen())
			return null;

		imagenActual = MatrizColores.aplicarMatriz(imagenActual, matriz);
		return imagenActual;
	}

	// ===================== HISTOGRAMA =====================
	public BufferedImage generarHistograma(boolean red, boolean green, boolean blue) {
		if (!hayImagen())
			return null;
		return filtros.Histograma.generarHistograma(imagenActual, red, green, blue);
	}

	// ===================== BLENDING =====================
	public BufferedImage aplicarBlending(float alpha, String tipo) {
		if (!hayImagen() || imagenSecundaria == null)
			return null;

		imagenActual = filtros.Blending.aplicarBlending(imagenActual, imagenSecundaria, alpha, tipo);

		return imagenActual;
	}

	// =====================OPERACIONES CON FRAGMENTOS=====================
	public BufferedImage aplicarBufferAcumulacion(int muestras, int desplazamiento) {
		if (!hayImagen())
			return null;
		imagenActual = BufferAcumulacion.aplicar(imagenActual, muestras, desplazamiento);
		return imagenActual;
	}

	public BufferedImage aplicarDiaNoche(float factor) {
		if (!hayImagen())
			return null;
		imagenActual = BufferAcumulacion.diaANoche(imagenOriginal, factor);
		return imagenActual;
	}
	// ===================== UTIL =====================

	private BufferedImage deepCopy(BufferedImage original) {
		BufferedImage copia = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		copia.setData(original.getData());
		return copia;
	}
}