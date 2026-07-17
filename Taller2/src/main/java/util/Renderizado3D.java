package util;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderizado3D {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private long window;
	private int shaderProgram;

	private int vaoColor, vboColor;
	private int vaoTextura, vboTextura;

	private int texturaId;
	private boolean zBufferEnabled = true;

	private float angulo = 0.0f;

	public void iniciar(BufferedImage imagenTextura) {

		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("No se pudo inicializar GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(
		        WIDTH,
		        HEIGHT,
		        "Renderizado 3D | Presione Z para activar/desactivar el Z-Buffer",
		        NULL,
		        NULL);
	
		if (window == NULL) {
			throw new RuntimeException("No se pudo crear la ventana");
		}

		// Configurar callbacks
		glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_Z && action == GLFW_PRESS) {
				zBufferEnabled = !zBufferEnabled;
				if (zBufferEnabled) {
					glEnable(GL_DEPTH_TEST);
					glfwSetWindowTitle(window, "Renderizado 3D - Z-Buffer ACTIVADO");
				} else {
					glDisable(GL_DEPTH_TEST);
					glfwSetWindowTitle(window, "Renderizado 3D - Z-Buffer DESACTIVADO");
				}
			}
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
		});

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1); // VSync

		GL.createCapabilities();

		glEnable(GL_DEPTH_TEST);
		glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

		initShaders();
		initTextura(imagenTextura);
		initBuffers();

		glfwShowWindow(window);

		loop();

		cleanup();
	}

	private void initShaders() {
		// Shader de vértices
		String vertexShaderSource = "#version 330 core\n" + "layout (location = 0) in vec3 aPos;\n"
				+ "layout (location = 1) in vec3 aColor;\n" + "layout (location = 2) in vec2 aTexCoord;\n"
				+ "out vec3 ourColor;\n" + "out vec2 TexCoord;\n" + "uniform mat4 model;\n"
				+ "uniform mat4 projection;\n" + "void main() {\n"
				+ "    gl_Position = projection * model * vec4(aPos, 1.0);\n" + "    ourColor = aColor;\n"
				+ "    TexCoord = aTexCoord;\n" + "}\n";

		// Shader de fragmentos
		String fragmentShaderSource = "#version 330 core\n" + "in vec3 ourColor;\n" + "in vec2 TexCoord;\n"
				+ "out vec4 FragColor;\n" + "uniform sampler2D ourTexture;\n" + "uniform bool useTexture;\n"
				+ "void main() {\n" + "    if (useTexture) {\n" + "        FragColor = texture(ourTexture, TexCoord);\n"
				+ "    } else {\n" + "        FragColor = vec4(ourColor, 1.0);\n" + "    }\n" + "}\n";

		// Compilar shaders
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		checkShaderCompilation(vertexShader, "VERTEX");

		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		checkShaderCompilation(fragmentShader, "FRAGMENT");

		// Crear programa
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);
		checkProgramLink(shaderProgram);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}

	private void checkShaderCompilation(int shader, String tipo) {
		IntBuffer success = MemoryUtil.memAllocInt(1);
		glGetShaderiv(shader, GL_COMPILE_STATUS, success);
		if (success.get(0) == GL_FALSE) {
			String log = glGetShaderInfoLog(shader);
			throw new RuntimeException("Error compilando shader " + tipo + ":\n" + log);
		}
	}

	private void checkProgramLink(int program) {
		IntBuffer success = MemoryUtil.memAllocInt(1);
		glGetProgramiv(program, GL_LINK_STATUS, success);
		if (success.get(0) == GL_FALSE) {
			String log = glGetProgramInfoLog(program);
			throw new RuntimeException("Error linkeando programa:\n" + log);
		}
	}

	private void initTextura(BufferedImage imagen) {

		if (imagen == null) {
			ByteBuffer buffer = ByteBuffer.allocateDirect(256 * 256 * 3);
			for (int y = 0; y < 256; y++) {
				for (int x = 0; x < 256; x++) {
					boolean blanco = ((x / 32) + (y / 32)) % 2 == 0;
					byte color = blanco ? (byte) 255 : (byte) 0;
					buffer.put(color);
					buffer.put(color);
					buffer.put(color);
				}
			}
			buffer.flip();

			texturaId = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texturaId);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 256, 256, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			return;
		}

		// Convertir BufferedImage a ByteBuffer (formato RGB)
		int ancho = imagen.getWidth();
		int alto = imagen.getHeight();
		ByteBuffer buffer = ByteBuffer.allocateDirect(ancho * alto * 3);
		for (int y = alto - 1; y >= 0; y--) { // STBImage espera Y invertido
			for (int x = 0; x < ancho; x++) {
				int rgb = imagen.getRGB(x, y);
				buffer.put((byte) ((rgb >> 16) & 0xFF));
				buffer.put((byte) ((rgb >> 8) & 0xFF));
				buffer.put((byte) (rgb & 0xFF));
			}
		}
		buffer.flip();

		texturaId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texturaId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, ancho, alto, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}

	private void initBuffers() {
		// Triángulo 1: Color interpolado
		float[] verticesColor = { -0.8f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.0f, 1.0f,
				0.0f, 0.0f, 0.0f, -0.8f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f };

		vaoColor = glGenVertexArrays();
		glBindVertexArray(vaoColor);

		vboColor = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColor);
		FloatBuffer fbColor = memAllocFloat(verticesColor.length);
		fbColor.put(verticesColor).flip();
		glBufferData(GL_ARRAY_BUFFER, fbColor, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		memFree(fbColor);

		// Triángulo 2: Con textura
		float[] verticesTextura = { 0.2f, -0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.8f, -0.5f, 0.5f, 1.0f, 1.0f,
				1.0f, 1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.5f, 1.0f };

		vaoTextura = glGenVertexArrays();
		glBindVertexArray(vaoTextura);

		vboTextura = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboTextura);
		FloatBuffer fbTextura = memAllocFloat(verticesTextura.length);
		fbTextura.put(verticesTextura).flip();
		glBufferData(GL_ARRAY_BUFFER, fbTextura, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
		glEnableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		memFree(fbTextura);
	}

	private void loop() {
		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			angulo += 0.01f;
			if (angulo > 360.0f)
				angulo -= 360.0f;

			glUseProgram(shaderProgram);

			float[] proj = new float[16];
			proj[0] = 1.0f;
			proj[5] = 1.0f;
			proj[10] = 1.0f;
			proj[15] = 1.0f;
			int projLoc = glGetUniformLocation(shaderProgram, "projection");
			glUniformMatrix4fv(projLoc, false, proj);

			float[] modelColor = crearMatrizRotacion(angulo);
			modelColor[12] = -0.3f; 
			modelColor[13] = 0.0f;
			modelColor[14] = -0.5f; 
			int modelLoc = glGetUniformLocation(shaderProgram, "model");
			glUniformMatrix4fv(modelLoc, false, modelColor);

			glUniform1i(glGetUniformLocation(shaderProgram, "useTexture"), 0);

			glBindVertexArray(vaoColor);
			glDrawArrays(GL_TRIANGLES, 0, 3);
			glBindVertexArray(0);

			float[] modelTextura = crearMatrizRotacion(angulo);
			modelTextura[12] = 0.3f; 
			modelTextura[13] = 0.0f;
			modelTextura[14] = 0.5f; 
			glUniformMatrix4fv(modelLoc, false, modelTextura);

			glUniform1i(glGetUniformLocation(shaderProgram, "useTexture"), 1);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, texturaId);
			glUniform1i(glGetUniformLocation(shaderProgram, "ourTexture"), 0);

			glBindVertexArray(vaoTextura);
			glDrawArrays(GL_TRIANGLES, 0, 3);
			glBindVertexArray(0);

			// Swap buffers (doble buffering)
			glfwSwapBuffers(window);

			glfwPollEvents();
		}
	}

	// Rotación sobre Y
	private float[] crearMatrizRotacion(float angulo) {
		float cos = (float) Math.cos(angulo);
		float sin = (float) Math.sin(angulo);
		float[] m = new float[16];
		m[0] = 1;
		m[5] = 1;
		m[10] = 1;
		m[15] = 1;
		m[0] = cos;
		m[2] = sin;
		m[8] = -sin;
		m[10] = cos;
		return m;
	}

	private void cleanup() {
		glDeleteProgram(shaderProgram);
		glDeleteBuffers(vboColor);
		glDeleteBuffers(vboTextura);
		glDeleteVertexArrays(vaoColor);
		glDeleteVertexArrays(vaoTextura);
		glDeleteTextures(texturaId);

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}