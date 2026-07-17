package rasterizador;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class UtilRasterizador {

	public static final int MODO_TEXTURA = 1;
	public static final int MODO_COLOR = 2;
	public static final int MODO_PROFUNDIDAD = 3;
	public static final int MODO_WBUFFER = 4;
	public static final int MODO_COMPLETO = 5;

	public static BufferedImage renderizar(int modo, int width, int height, BufferedImage textura) {
		if (textura == null)
			textura = crearTexturaAjedrez(256, 256);

		BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		float[][] wBuffer = new float[width][height];
		limpiarWBuffer(wBuffer);

		switch (modo) {
		case MODO_TEXTURA -> renderTextura(canvas, width, height, textura, wBuffer);
		case MODO_COLOR -> renderColor(canvas, width, height, textura, wBuffer);
		case MODO_PROFUNDIDAD -> renderProfundidad(canvas, width, height, textura, wBuffer);
		case MODO_WBUFFER -> renderWBuffer(canvas, width, height, textura, wBuffer);
		default -> renderCompleto(canvas, width, height, textura, wBuffer);
		}
		return canvas;
	}

	private static void renderTextura(BufferedImage canvas, int width, int height, BufferedImage textura,
			float[][] wBuffer) {
		int cx = width / 2;
		int cy = height / 2;
		// Triángulo con perspectiva extrema
		Vertex a = new Vertex(cx - 300, cy - 200, 1.0f, 1, 1, 1, 0, 0);
		Vertex b = new Vertex(cx + 300, cy - 200, 0.1f, 1, 1, 1, 1, 0);
		Vertex c = new Vertex(cx + 300, cy + 200, 0.1f, 1, 1, 1, 1, 1);
		rasterizarTriangulo(canvas, a, b, c, width, height, textura, wBuffer, true, false, true, false);
	}

	private static void renderColor(BufferedImage canvas, int width, int height, BufferedImage textura,
			float[][] wBuffer) {
		int cx = width / 2;
		int cy = height / 2;
		// Colores saturados para notar la interpolación
		Vertex a = new Vertex(cx - 300, cy - 200, 1.0f, 1, 0, 0, 0, 0);
		Vertex b = new Vertex(cx + 300, cy - 200, 1.0f, 0, 1, 0, 1, 0);
		Vertex c = new Vertex(cx, cy + 200, 1.0f, 0, 0, 1, 0.5f, 1);
		rasterizarTriangulo(canvas, a, b, c, width, height, textura, wBuffer, false, true, true, false);
	}

	private static void renderProfundidad(BufferedImage canvas, int width, int height, BufferedImage textura,
			float[][] wBuffer) {
		int cx = width / 2;
		int cy = height / 2;
		// Izquierda: Afín (sin corrección) | Derecha: Perspectiva correcta
		Vertex a1 = new Vertex(cx - 350, cy - 150, 1.0f, 1, 1, 1, 0, 0);
		Vertex b1 = new Vertex(cx - 100, cy - 150, 0.1f, 1, 1, 1, 1, 0);
		Vertex c1 = new Vertex(cx - 100, cy + 150, 0.1f, 1, 1, 1, 1, 1);
		Vertex d1 = new Vertex(cx - 350, cy + 150, 1.0f, 1, 1, 1, 0, 1);
		rasterizarTriangulo(canvas, a1, b1, c1, width, height, textura, wBuffer, true, false, false, false);
		rasterizarTriangulo(canvas, a1, c1, d1, width, height, textura, wBuffer, true, false, false, false);

		Vertex a2 = new Vertex(cx + 100, cy - 150, 1.0f, 1, 1, 1, 0, 0);
		Vertex b2 = new Vertex(cx + 350, cy - 150, 0.1f, 1, 1, 1, 1, 0);
		Vertex c2 = new Vertex(cx + 350, cy + 150, 0.1f, 1, 1, 1, 1, 1);
		Vertex d2 = new Vertex(cx + 100, cy + 150, 1.0f, 1, 1, 1, 0, 1);
		rasterizarTriangulo(canvas, a2, b2, c2, width, height, textura, wBuffer, true, false, true, false);
		rasterizarTriangulo(canvas, a2, c2, d2, width, height, textura, wBuffer, true, false, true, false);
	}

	private static void renderWBuffer(BufferedImage canvas, int width, int height, BufferedImage textura,
			float[][] wBuffer) {
		int cx = width / 2;
		// Triángulo rojo LEJANO (W=0.2)
		Vertex a1 = new Vertex(cx - 200, 100, 0.2f, 1, 0, 0, 0, 0);
		Vertex b1 = new Vertex(cx + 200, 100, 0.2f, 1, 0, 0, 1, 0);
		Vertex c1 = new Vertex(cx, 400, 0.2f, 1, 0, 0, 0.5f, 1);
		// Triángulo azul CERCANO (W=1.0) superpuesto
		Vertex a2 = new Vertex(cx - 200, 200, 1.0f, 0, 0, 1, 0, 0);
		Vertex b2 = new Vertex(cx + 200, 200, 1.0f, 0, 0, 1, 1, 0);
		Vertex c2 = new Vertex(cx, 500, 1.0f, 0, 0, 1, 0.5f, 1);

		rasterizarTriangulo(canvas, a1, b1, c1, width, height, textura, wBuffer, false, true, true, true);
		rasterizarTriangulo(canvas, a2, b2, c2, width, height, textura, wBuffer, false, true, true, true);
	}

	private static void renderCompleto(BufferedImage canvas, int width, int height, BufferedImage textura,
			float[][] wBuffer) {
		int cx = width / 2;
		int cy = height / 2;

		Vertex v1 = new Vertex(cx - 250, cy - 250, 0.2f, 1, 1, 1, 0, 0);
		Vertex v2 = new Vertex(cx + 250, cy - 250, 0.2f, 1, 1, 1, 1, 0);
		Vertex v3 = new Vertex(cx + 250, cy + 250, 0.2f, 1, 1, 1, 1, 1);
		Vertex v4 = new Vertex(cx - 250, cy + 250, 0.2f, 1, 1, 1, 0, 1);

		rasterizarTriangulo(canvas, v1, v2, v3, width, height, textura, wBuffer, true, false, true, true);
		rasterizarTriangulo(canvas, v1, v3, v4, width, height, textura, wBuffer, true, false, true, true);

		Vertex p1 = new Vertex(cx - 300, cy + 100, 1.0f, 1, 0, 0, 0, 0);
		Vertex p2 = new Vertex(cx + 300, cy + 100, 1.0f, 0, 1, 0, 0, 0);
		Vertex p3 = new Vertex(cx, cy - 300, 1.0f, 0, 0, 1, 0, 0);

		rasterizarTriangulo(canvas, p1, p2, p3, width, height, textura, wBuffer, false, true, true, true);
	}

	private static void rasterizarTriangulo(BufferedImage canvas, Vertex v0, Vertex v1, Vertex v2, int width,
			int height, BufferedImage textura, float[][] wBuffer, boolean usarTextura, boolean usarColor,
			boolean perspectivaCorrecta, boolean usarWBuffer) {
		int minX = (int) Math.max(0, Math.min(v0.x, Math.min(v1.x, v2.x)));
		int maxX = (int) Math.min(width - 1, Math.max(v0.x, Math.max(v1.x, v2.x)));
		int minY = (int) Math.max(0, Math.min(v0.y, Math.min(v1.y, v2.y)));
		int maxY = (int) Math.min(height - 1, Math.max(v0.y, Math.max(v1.y, v2.y)));

		float area = edge(v0, v1, v2);
		if (Math.abs(area) < 1e-6f)
			return;

		int texW = textura.getWidth() - 1;
		int texH = textura.getHeight() - 1;

		for (int y = minY; y <= maxY; y++) {
			for (int x = minX; x <= maxX; x++) {
				float w0 = edge(v1, v2, x, y) / area;
				float w1 = edge(v2, v0, x, y) / area;
				float w2 = edge(v0, v1, x, y) / area;

				if (w0 < 0 || w1 < 0 || w2 < 0)
					continue;

				float invW = (w0 / v0.w) + (w1 / v1.w) + (w2 / v2.w);

				if (usarWBuffer) {
					if (invW <= wBuffer[x][y])
						continue;
					wBuffer[x][y] = invW;
				}

				float r, g, b, u, v;
				if (perspectivaCorrecta) {
					r = (w0 * v0.r / v0.w + w1 * v1.r / v1.w + w2 * v2.r / v2.w) / invW;
					g = (w0 * v0.g / v0.w + w1 * v1.g / v1.w + w2 * v2.g / v2.w) / invW;
					b = (w0 * v0.b / v0.w + w1 * v1.b / v1.w + w2 * v2.b / v2.w) / invW;
					u = (w0 * v0.u / v0.w + w1 * v1.u / v1.w + w2 * v2.u / v2.w) / invW;
					v = (w0 * v0.v / v0.w + w1 * v1.v / v1.w + w2 * v2.v / v2.w) / invW;
				} else {
					r = w0 * v0.r + w1 * v1.r + w2 * v2.r;
					g = w0 * v0.g + w1 * v1.g + w2 * v2.g;
					b = w0 * v0.b + w1 * v1.b + w2 * v2.b;
					u = w0 * v0.u + w1 * v1.u + w2 * v2.u;
					v = w0 * v0.v + w1 * v1.v + w2 * v2.v;
				}

				int finalRGB = 0xFFFFFF;
				if (usarTextura) {
					int texRGB = textura.getRGB(clamp((int) (u * texW), 0, texW), clamp((int) (v * texH), 0, texH));
					if (usarColor) {
						finalRGB = (clamp((int) (((texRGB >> 16) & 0xFF) * r), 0, 255) << 16)
								| (clamp((int) (((texRGB >> 8) & 0xFF) * g), 0, 255) << 8)
								| (clamp((int) ((texRGB & 0xFF) * b), 0, 255));
					} else {
						finalRGB = texRGB;
					}
				} else if (usarColor) {
					finalRGB = (clamp((int) (r * 255), 0, 255) << 16) | (clamp((int) (g * 255), 0, 255) << 8)
							| clamp((int) (b * 255), 0, 255);
				}
				canvas.setRGB(x, y, finalRGB);
			}
		}
	}

	private static float edge(Vertex a, Vertex b, float px, float py) {
		return (px - a.x) * (b.y - a.y) - (py - a.y) * (b.x - a.x);
	}

	private static float edge(Vertex a, Vertex b, Vertex c) {
		return edge(a, b, c.x, c.y);
	}

	private static int clamp(int v, int min, int max) {
		return Math.max(min, Math.min(max, v));
	}

	private static void limpiarWBuffer(float[][] wBuffer) {
		for (float[] row : wBuffer)
			Arrays.fill(row, Float.NEGATIVE_INFINITY);
	}

	private static BufferedImage crearTexturaAjedrez(int w, int h) {
		BufferedImage tex = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
				tex.setRGB(x, y, (((x / 32) + (y / 32)) % 2 == 0) ? 0xFFFFFF : 0x000000);
		return tex;
	}

	static class Vertex {
		float x, y, w, r, g, b, u, v;

		Vertex(float x, float y, float w, float r, float g, float b, float u, float v) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.r = r;
			this.g = g;
			this.b = b;
			this.u = u;
			this.v = v;
		}
	}
}