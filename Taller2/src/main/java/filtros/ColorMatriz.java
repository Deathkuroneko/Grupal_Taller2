package filtros;

public class ColorMatriz {
    public static final float[][] filtroSepia={
            {0.393f, 0.769f, 0.189f, 0f},
            {0.349f, 0.686f, 0.168f, 0f},
            {0.272f, 0.534f, 0.131f, 0f},
            {0f, 0f, 0f, 1f}
    };
    public static final float[][] filtroGris={
            {0.299f, 0.587f, 0.114f, 0f},
            {0.299f, 0.587f, 0.114f, 0f},
            {0.299f, 0.587f, 0.114f, 0f},
            {0f, 0f, 0f, 1f}
    };
    //aumenta en un 30% el brillo de cada canal
    public static final float[][] filtroAumentarBrillo={
            {1f, 0f, 0f, 0.3f},
            {0f, 1f, 0f, 0.3f},
            {0f, 0f, 1f, 0.3f},
            {0f, 0f, 0f, 1f}
    };
    public static final float[][] filtroVerdeNaturaleza={
            {0.2f, 0.8f, 0.2f, 0f},
            {0.1f, 1.0f, 0.1f, 0f},
            {0.1f, 0.6f, 0.1f, 0f},
            {0f, 0f, 0f, 1f}
    };
    public static final float[][] filtroHielo={
            {0.3f, 0.4f, 0.3f, 0f},
            {0.2f, 0.5f, 0.3f, 0f},
            {0.2f, 0.3f, 0.6f, 0f},
            {0f, 0f, 0f, 1f}
    };

}
