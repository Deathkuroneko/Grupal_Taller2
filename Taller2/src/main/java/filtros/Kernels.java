package filtros;

public class Kernels {
	
	//No modifica imagen
	public static final float[] kNormal = {
			0f,0f,0f,
			0f,1f,0f,
			0f,0f,0f
	};
	//Enfoque (shaarpen)
	public static final float[] kEnfoque = {
			0f,-1f,0f,
			-1f,5f,-1f,
			0f,-1f,0f
	};
	//Desenfoue (Blur)
	public static final float[] kDesenfoque = {
			1f/9,1f/9,1f/9,
			1f/9,1f/9,1f/9,
			1f/9,1f/9,1f/9
	};
	public static final float[] kDesenfoque9 = { 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81,
			1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81,
			1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81,
			1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81,
			1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81,
			1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81,
			1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81,
			1f / 81, 1f / 81, 1f / 81, 1f / 81, 1f / 81 };

	//Deteccion de bordes (una de las muchas) (da cero no se mormaliza?)
	public static final float[] kBordes = {
			-0.5f,-0.5f,-0.5f,
			-0.5f,4f,-0.5f,
			-0.5f,-0.5f,-0.5f,
	};
	//bordes 8
	public static final float[] kBordes8 = {
			-1f,-1f,-1f,
			-1f,8,-1f,
			-1f,-1f,-1f,
	};
	//Aclarar
	public static final float[] kAclaracion = {
			0.1f,0.1f,0.1f,
			0.1f,1.0f,0.1f,
			0.1f,0.1f,0.1f,
	};
	//oscurecer
	public static final float[] kOscurecer = {
			0.01f,0.01f,0.01f,
			0.01f,0.5f,0.01f,
			0.01f,0.01f,0.01f,
	};
	

}
