package util;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderizar {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;

	private long window;
	private int shaderProgram;

	private int vaoCube, vboCube;
	private int vaoFloor, vboFloor;
	private int textureId;

	private boolean showLinearDepth = false;
	private boolean depthTestEnabled = true;

	private float angle = 0.0f;

	public void iniciar(BufferedImage texturaImg) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("No se pudo inicializar GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

		window = glfwCreateWindow(WIDTH, HEIGHT, "Renderizado 3D - Z-Buffer ACTIVO", MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL) {
			throw new RuntimeException("No se pudo crear la ventana");
		}

		// Callbacks
		glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
		    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
		        glfwSetWindowShouldClose(win, true);
		    }
		    if (key == GLFW_KEY_L && action == GLFW_PRESS) {
		        showLinearDepth = !showLinearDepth;
		        actualizarTitulo(win); // Actualiza el mensaje
		    }
		    if (key == GLFW_KEY_F && action == GLFW_PRESS) {
		        depthTestEnabled = !depthTestEnabled;
		        if (depthTestEnabled) {
		            glEnable(GL_DEPTH_TEST);
		        } else {
		            glDisable(GL_DEPTH_TEST);
		        }
		        actualizarTitulo(win); // Actualiza el mensaje
		    }
		});

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		GL.createCapabilities();

		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LESS);
		glClearColor(0.1f, 0.1f, 0.15f, 1.0f);


		initShaders();
		actualizarTitulo(window);
		initTextura(texturaImg);
		initBuffers(); 
		glfwShowWindow(window);

		loop();
		cleanup();
	}
	private void actualizarTitulo(long win) {
	    String modoProfundidad = depthTestEnabled ? "ACTIVO" : "DESACTIVADO";
	    String modoBuffer = showLinearDepth ? "W-Buffer (Lineal)" : "Z-Buffer (Estándar)";
	    
	    String titulo = String.format("Render: %s | Z-Test: %s | [L] Cambiar Buffer | [F] Z-Test ON/OFF | [ESC] Salir", 
	                                  modoBuffer, modoProfundidad);
	    glfwSetWindowTitle(win, titulo);
	}

	// ---------- SHADERS ----------
	private void initShaders() {
		String vertexSrc = "#version 330 core\n" + "layout (location = 0) in vec3 aPos;\n"
				+ "layout (location = 1) in vec3 aColor;\n" + "layout (location = 2) in vec2 aTexCoord;\n"
				+ "out vec3 vColor;\n" + "out vec2 vTexCoord;\n" + "uniform mat4 model;\n" + "uniform mat4 view;\n"
				+ "uniform mat4 projection;\n" + "void main() {\n"
				+ "    gl_Position = projection * view * model * vec4(aPos, 1.0);\n" + "    vColor = aColor;\n"
				+ "    vTexCoord = aTexCoord;\n" + "}\n";

		String fragmentSrc = "#version 330 core\n" + "out vec4 FragColor;\n" + "in vec3 vColor;\n"
				+ "in vec2 vTexCoord;\n" + "uniform sampler2D uTexture;\n" + "uniform int useTexture;\n"
				+ "uniform int linearDepth;\n" + "void main() {\n" + "    vec4 color;\n"
				+ "    if (useTexture == 1) {\n" + "        color = texture(uTexture, vTexCoord);\n" + "    } else {\n"
				+ "        color = vec4(vColor, 1.0);\n" + "    }\n" + "    float near = 0.1;\n"
				+ "    float far = 100.0;\n"
				+ "    float z_depth = (2.0 * near) / (far + near - gl_FragCoord.z * (far - near));\n"
				+ "    if (linearDepth == 1) {\n" + "        // W-Buffer: oscurecimiento suave\n"
				+ "        float factor = 1.0 - (z_depth * 0.15);\n" + "        color.rgb *= factor;\n"
				+ "    } else {\n" + "        // Z-Buffer: penaliza fuerte los objetos lejanos\n"
				+ "        float factor = 1.0 - pow(z_depth, 0.3) * 0.85;\n" + "        color.rgb *= factor;\n"
				+ "    }\n" + "    FragColor = color;\n" + "}\n";

		// Compilar
		int vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs, vertexSrc);
		glCompileShader(vs);
		checkShaderCompilation(vs, "VERTEX");

		int fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs, fragmentSrc);
		glCompileShader(fs);
		checkShaderCompilation(fs, "FRAGMENT");

		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vs);
		glAttachShader(shaderProgram, fs);
		glLinkProgram(shaderProgram);
		checkProgramLink(shaderProgram);

		glDeleteShader(vs);
		glDeleteShader(fs);
	}

	private void checkShaderCompilation(int shader, String tipo) {
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetShaderInfoLog(shader);
			throw new RuntimeException("Error compilando shader " + tipo + ":\n" + log);
		}
	}

	private void checkProgramLink(int program) {
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			String log = glGetProgramInfoLog(program);
			throw new RuntimeException("Error linkeando programa:\n" + log);
		}
	}

	// ---------- TEXTURA ----------
	private void initTextura(BufferedImage img) {
		if (img == null) {
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

			textureId = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureId);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 256, 256, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			return;
		}

		int ancho = img.getWidth();
		int alto = img.getHeight();
		ByteBuffer buffer = ByteBuffer.allocateDirect(ancho * alto * 3);
		for (int y = alto - 1; y >= 0; y--) {
			for (int x = 0; x < ancho; x++) {
				int rgb = img.getRGB(x, y);
				buffer.put((byte) ((rgb >> 16) & 0xFF));
				buffer.put((byte) ((rgb >> 8) & 0xFF));
				buffer.put((byte) (rgb & 0xFF));
			}
		}
		buffer.flip();

		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, ancho, alto, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
	}

	// ---------- BUFFERS (CUBO Y PISO) ----------
	private void initBuffers() {
		float[] cubeVertices = {
				// Cara frontal
				-0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.5f,
				0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, -0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.5f,
				0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, -0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				// Cara trasera
				-0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
				0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
				0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				// Cara izquierda
				-0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, -0.5f, 0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				-0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
				-0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
				// Cara derecha
				0.5f, 0.5f, 0.5f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f, -0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f, 0.5f,
				0.5f, -0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, -0.5f,
				0.5f, 1.0f, 0.5f, 0.0f, 0.0f, 1.0f, 0.5f, -0.5f, -0.5f, 1.0f, 0.5f, 0.0f, 1.0f, 1.0f,
				// Cara inferior
				-0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
				0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
				0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, -0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
				// Cara superior
				-0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.5f,
				0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, -0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, -0.5f,
				0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f };

		float[] floorVertices = { -5.0f, 0.0f, -5.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 5.0f, 0.0f, -5.0f, 1.0f, 1.0f, 1.0f,
				10.0f, 0.0f, 5.0f, 0.0f, 5.0f, 1.0f, 1.0f, 1.0f, 10.0f, 10.0f, -5.0f, 0.0f, -5.0f, 1.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 5.0f, 0.0f, 5.0f, 1.0f, 1.0f, 1.0f, 10.0f, 10.0f, -5.0f, 0.0f, 5.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				10.0f };

		// Subir cubo
		vaoCube = glGenVertexArrays();
		vboCube = glGenBuffers();
		uploadMesh(vaoCube, vboCube, cubeVertices);

		// Subir piso
		vaoFloor = glGenVertexArrays();
		vboFloor = glGenBuffers();
		uploadMesh(vaoFloor, vboFloor, floorVertices);
	}

	private void uploadMesh(int vao, int vbo, float[] vertices) {
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
		buffer.put(vertices).flip();
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		MemoryUtil.memFree(buffer);

		int stride = 8 * Float.BYTES;
		glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, 6 * Float.BYTES);
		glEnableVertexAttribArray(2);

		glBindVertexArray(0);
	}

	// ---------- LOOP PRINCIPAL ----------
	private void loop() {
		Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(45.0), (float) WIDTH / HEIGHT, 0.1f,
				100.0f);

		Matrix4f view = new Matrix4f().lookAt(new Vector3f(4, 3, 4), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));

		while (!glfwWindowShouldClose(window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			angle += 0.01f;
			if (angle > 360.0f)
				angle -= 360.0f;

			glUseProgram(shaderProgram);

			int projLoc = glGetUniformLocation(shaderProgram, "projection");
			glUniformMatrix4fv(projLoc, false, projection.get(new float[16]));

			int viewLoc = glGetUniformLocation(shaderProgram, "view");
			glUniformMatrix4fv(viewLoc, false, view.get(new float[16]));

			int linearDepthLoc = glGetUniformLocation(shaderProgram, "linearDepth");
			glUniform1i(linearDepthLoc, showLinearDepth ? 1 : 0);

			Matrix4f modelFloor = new Matrix4f().identity().scale(1, 1, 1);
			int modelLoc = glGetUniformLocation(shaderProgram, "model");
			glUniformMatrix4fv(modelLoc, false, modelFloor.get(new float[16]));

			glUniform1i(glGetUniformLocation(shaderProgram, "useTexture"), 1);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, textureId);
			glUniform1i(glGetUniformLocation(shaderProgram, "uTexture"), 0);

			glBindVertexArray(vaoFloor);
			glDrawArrays(GL_TRIANGLES, 0, 6);

			Matrix4f modelCube = new Matrix4f().identity().translate(0, 0.5f, 0).rotateY(angle);
			glUniformMatrix4fv(modelLoc, false, modelCube.get(new float[16]));

			glUniform1i(glGetUniformLocation(shaderProgram, "useTexture"), 0);

			glBindVertexArray(vaoCube);
			glDrawArrays(GL_TRIANGLES, 0, 36);

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}

	private void cleanup() {
		glDeleteProgram(shaderProgram);
		glDeleteBuffers(vboCube);
		glDeleteBuffers(vboFloor);
		glDeleteVertexArrays(vaoCube);
		glDeleteVertexArrays(vaoFloor);
		glDeleteTextures(textureId);

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}