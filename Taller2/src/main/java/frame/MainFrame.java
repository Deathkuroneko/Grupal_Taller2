package frame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import com.formdev.flatlaf.FlatClientProperties;

import filtros.Kernels;
import service.ImagenService;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel lblDigital;
	private JSlider sliderNiveles;
	private String filtroActual = "RETRO";
	private Font fuenteDigital;

	private ImagenService imagenService = new ImagenService();
	private VisorImagenInteligente visorInteligente;
	// Esta variable guardará el último tipo de degradado que el usuario pulsó
	private String ultimoDegradado = "IZQ_DER";
	private Color ultimoColorSeleccionado = new Color(255, 140, 0); // Naranja por defecto
	private JSpinner spinSaturacion;
	private JSpinner spinBrillo;
	private JSpinner spinOpacidad;
	private final String[] NOMBRES_COLORES = { "NARANJA", "VERDE FOREST", "AZUL MODERNO", "PURPURA", "CARMESI",
			"GRIS SEPIA", "ROSA FUCSIA", "CIAN ELECTRICO", "DORADO", "INDIGO", "VERDE PRIMAVERA", "TOMATE",
			"AZUL PASTEL", "CAQUI", "GRIS PIZARRA", "MARRON CUERO", "VERDE NEON", "MAGENTA", "AZUL ELECTRICO", " SIENA",
			"AZUL PASTEL" };

	// Atributos de la clase MainFrame
	private String ultimoNombreColor = "NINGUNO";
	private String canalActual = "RGB";

	private float[] kernelActual = Kernels.kNormal;
	private String nombreKernelActual = "NORMAL";
	
	private JLabel lblIntensidad;

	public MainFrame() {
		this.fuenteDigital = cargarFuenteDigital();

		setTitle("Editor de Fotos - UCE PhotoSystem");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1150, 750);
		setResizable(false);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon(getClass().getResource("/icons/camara-de-fotos.png")).getImage());
		getContentPane().setLayout(new BorderLayout());

		// --- PANEL SUPERIOR ---
		JPanel panelSuperior = new JPanel(new BorderLayout());
		panelSuperior.setBackground(new Color(20, 20, 20));
		panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		JPanel panelLCD = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelLCD.setBackground(Color.BLACK);
		panelLCD.setBorder(new LineBorder(new Color(0, 50, 0), 2, true));

		lblDigital = new JLabel(" ESPERANDO IMAGEN... ");
		lblDigital.setForeground(new Color(0, 255, 0));
		lblDigital.setFont(fuenteDigital);
		panelLCD.add(lblDigital);

		JPanel panelBotonesTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
		panelBotonesTop.setOpaque(false);

		JButton btnAbrir = crearBotonCircular("/icons/abrir.png", "Abrir");
		btnAbrir.addActionListener(e -> accionAbrir());

		JButton btnGuardar = crearBotonCircular("/icons/save.png", "Guardar");
		btnGuardar.addActionListener(e -> accionGuardar());

		JButton btnReset = crearBotonCircular("/icons/reset.png", "Reset");
		btnReset.addActionListener(e -> accionReset());

		panelBotonesTop.add(btnAbrir);
		panelBotonesTop.add(btnGuardar);
		panelBotonesTop.add(btnReset);

		panelSuperior.add(panelLCD, BorderLayout.WEST);
		panelSuperior.add(panelBotonesTop, BorderLayout.EAST);
		getContentPane().add(panelSuperior, BorderLayout.NORTH);

		// --- PANEL DE CONTROLES (IZQUIERDA) ---
		JPanel panelControles = new JPanel();
		panelControles.setPreferredSize(new Dimension(300, 0));
		panelControles.setBackground(new Color(35, 35, 35));
		panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
		panelControles.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

		JLabel lblTituloFiltro = new JLabel("CONTROLES");
		lblTituloFiltro.setFont(new Font("Serif", Font.BOLD, 22));
		lblTituloFiltro.setForeground(new Color(212, 175, 55));
		lblTituloFiltro.setAlignmentX(Component.CENTER_ALIGNMENT);

		sliderNiveles = new JSlider(2, 10, 5);
		sliderNiveles.setMajorTickSpacing(1);
		sliderNiveles.setPaintTicks(true);
		sliderNiveles.setPaintLabels(true);
		sliderNiveles.setBackground(new Color(35, 35, 35));
		sliderNiveles.setForeground(Color.WHITE);
		sliderNiveles.addChangeListener(e -> actualizarPantalla());

		JButton btnAplicar = new JButton("APLICAR");
		btnAplicar.setPreferredSize(new Dimension(150, 150));
		btnAplicar.setMaximumSize(new Dimension(150, 150));
		btnAplicar.setBackground(new Color(180, 20, 20));
		btnAplicar.setForeground(Color.WHITE);
		btnAplicar.putClientProperty(FlatClientProperties.STYLE, "arc: 999; borderWidth: 4; borderColor: #1a1a1a");
		btnAplicar.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAplicar.addActionListener(e -> accionEjecutarFiltro());

		panelControles.add(lblTituloFiltro);
		panelControles.add(Box.createVerticalStrut(40));
		lblIntensidad = new JLabel("Intensidad:");
		panelControles.add(lblIntensidad);
		panelControles.add(sliderNiveles);
		panelControles.add(Box.createVerticalGlue());
		panelControles.add(btnAplicar);

		getContentPane().add(panelControles, BorderLayout.WEST);

		// --- PESTAÑAS DE FILTROS (SUR) ---
		JTabbedPane tabsFiltros = new JTabbedPane();
		tabsFiltros.addTab("Retro", crearPanelRetro());
		tabsFiltros.addTab("Degradados", crearPanelDegradados());
		tabsFiltros.addTab("Color", crearPanelColor());
		tabsFiltros.addTab("HSV", crearPanelHSV());
		tabsFiltros.addTab("B/N", crearPanelBN());
		tabsFiltros.addTab("NEGATIVO", crearPanelNegativo());
		tabsFiltros.addTab("REDUCIR BITS", crearPanelEscalaReducirBits());
		tabsFiltros.addTab("GRISES", crearPanelEscalaGris()); // Antes decía "Escala de Grises"
		tabsFiltros.addTab("ESMERILADO", crearPanelEsmerilado());
		tabsFiltros.addTab("CONVOLUCIONES", crearPanelConvoluciones());

		tabsFiltros.addChangeListener(e -> {
			// 1. Obtener el nombre de la pestaña y normalizarlo
			filtroActual = tabsFiltros.getTitleAt(tabsFiltros.getSelectedIndex()).toUpperCase();

			// 2. Configurar el Slider según el filtro

			switch (filtroActual) {

			case "GRISES":
				configurarSlider(2, 255, 128, 32);
				break;
			case "REDUCIR BITS":
				configurarSlider(1, 8, 8, 1);
				break;
			case "RETRO":
				configurarSlider(1, 10, 5, 1);
				break;
			case "CONVOLUCIONES":
			    configurarSlider(1, 15, 1, 1);
			    resetSlider();
			    break;
			}

			// 3. Forzar al slider a redibujar sus etiquetas y actualizar la pantalla LCD
			sliderNiveles.setLabelTable(sliderNiveles.createStandardLabels(sliderNiveles.getMajorTickSpacing()));
			actualizarPantalla();
		});
		getContentPane().add(tabsFiltros, BorderLayout.SOUTH);

		// --- VISOR DE IMAGEN (CENTRO) ---
		visorInteligente = new VisorImagenInteligente();
		getContentPane().add(visorInteligente, BorderLayout.CENTER);
	}

	// cambios en el Slider
	private void configurarSlider(int min, int max, int value, int tick) {
		sliderNiveles.setMinimum(min);
		sliderNiveles.setMaximum(max);
		sliderNiveles.setValue(value);
		sliderNiveles.setMajorTickSpacing(tick);
	}
	private void resetSlider() {
	    sliderNiveles.setValue(sliderNiveles.getMinimum());
	}

	// --- MÉTODOS DE PANELES ---

	private JPanel crearPanelConvoluciones() {
	    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
	    panel.setBackground(new Color(30, 30, 30));

	    ButtonGroup grupo = new ButtonGroup();

	    panel.add(crearRadioKernel("ENFOQUE", Kernels.kEnfoque, grupo));
	    panel.add(crearRadioKernel("DESENFOQUE", Kernels.kDesenfoque, grupo));
	    panel.add(crearRadioKernel("BORDES", Kernels.kBordes8, grupo));
	    panel.add(crearRadioKernel("ACLARAR", Kernels.kAclaracion, grupo));
	    panel.add(crearRadioKernel("OSCURECER", Kernels.kOscurecer, grupo));
	    panel.add(crearRadioKernel("BLUR 9x9", Kernels.kDesenfoque9, grupo));
	    panel.add(crearRadioKernel("NORMAL VAR", null, grupo));

	    return panel;
	}

	// Método auxiliar para crear botones de kernel
	private JRadioButton crearRadioKernel(String nombre, float[] matriz, ButtonGroup grupo) {
	    JRadioButton rb = new JRadioButton(nombre);
	    rb.setForeground(Color.WHITE);
	    rb.setBackground(new Color(30, 30, 30));
	    rb.setFocusPainted(false);

	    grupo.add(rb);

	    rb.addActionListener(e -> {
	        if (rb.isSelected()) {
	            this.nombreKernelActual = nombre;
	            
	            if (nombre.equals("NORMAL VAR")) {
	            	this.kernelActual = Kernels.kNormal;
	            } else {
	                resetSlider();
	                this.kernelActual = matriz;
	                BufferedImage res = imagenService.aplicarEfectoConvolucion(matriz, sliderNiveles.getValue());
	                visorInteligente.setImagen(res);
	            }
	            
	            actualizarPantalla();
	        }
	    });

	    return rb;
	}

	private JPanel crearPanelDegradados() {
		// Mantenemos el FlowLayout horizontal alineado a la izquierda
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 15));
		panel.setBackground(new Color(30, 30, 30));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		JLabel titulo = new JLabel("DEGRADADOS:");
		titulo.setFont(new Font("SansSerif", Font.BOLD, 11));
		titulo.setForeground(new Color(150, 150, 150));
		panel.add(titulo);

		// Botones de acción inmediata
		panel.add(crearBotonAccion("Izquierda → Derecha", e -> ejecutarDegradado("IZQ_DER")));
		panel.add(crearBotonAccion("Derecha → Izquierda", e -> ejecutarDegradado("DER_IZQ")));
		panel.add(crearBotonAccion("Arriba → Abajo", e -> ejecutarDegradado("ARRIBA_ABAJO")));
		panel.add(crearBotonAccion("Abajo → Arriba", e -> ejecutarDegradado("ABAJO_ARRIBA")));
		panel.add(crearBotonAccion("Radial", e -> ejecutarDegradado("RADIAL")));
		panel.add(crearBotonAccion("Radial Inverso", e -> ejecutarDegradado("RADIAL_INVERSO")));

		return panel;
	}

	// Método auxiliar para crear botones estilizados rápidamente
	private JButton crearBotonAccion(String texto, java.awt.event.ActionListener accion) {
		JButton btn = new JButton(texto);
		btn.setBackground(new Color(55, 55, 55));
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setFont(new Font("SansSerif", Font.BOLD, 10));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.addActionListener(accion);

		// Borde sutil para que parezca un botón moderno
		btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		return btn;
	}

	private void ejecutarDegradado(String tipo) {
		if (imagenService.getImagenOriginal() == null)
			return;

		// GUARDAMOS el tipo seleccionado para que el botón rojo lo sepa después
		this.ultimoDegradado = tipo;

		BufferedImage resultado = null;
		switch (tipo) {
		case "IZQ_DER":
			resultado = imagenService.aplicarDegradadoIzqDer();
			break;
		case "DER_IZQ":
			resultado = imagenService.aplicarDegradadoDerIzq();
			break;
		case "ARRIBA_ABAJO":
			resultado = imagenService.aplicarDegradadoArriAbajo();
			break;
		case "ABAJO_ARRIBA":
			resultado = imagenService.aplicarDegradadoAbajoArriba();
			break;
		case "RADIAL":
			resultado = imagenService.aplicarDegradadoRadial();
			break;
		case "RADIAL_INVERSO":
			resultado = imagenService.aplicarDegradadoRadialInverso();
			break;
		case "CONVOLUCIONES":
			int iters = sliderNiveles.getValue();
			resultado = imagenService.aplicarEfectoConvolucion(kernelActual, iters);
			lblDigital.setText(nombreKernelActual + " x" + iters);
			break;
		}

		if (resultado != null) {
			visorInteligente.setImagen(resultado);
			lblDigital.setText(" DEGRADADO: " + tipo);
		}
	}

	private JPanel crearPanelHSV() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 15));
		panel.setBackground(new Color(30, 30, 30));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		Font fuenteLabel = new Font("SansSerif", Font.BOLD, 11);
		Color colorTexto = new Color(200, 200, 200);

		// Inicializamos las variables globales usando tu método auxiliar
		// Nota: Debes modificar crearItemCompacto para que devuelva el Spinner o
		// asignarlo dentro
		spinSaturacion = new JSpinner(new SpinnerNumberModel(100, 0, 200, 5));
		spinBrillo = new JSpinner(new SpinnerNumberModel(100, 0, 200, 5));
		spinOpacidad = new JSpinner(new SpinnerNumberModel(255, 0, 255, 5));

		panel.add(crearEstructuraItem("SATURACIÓN", spinSaturacion, fuenteLabel, colorTexto));
		panel.add(crearEstructuraItem("BRILLO", spinBrillo, fuenteLabel, colorTexto));
		panel.add(crearEstructuraItem("OPACIDAD", spinOpacidad, fuenteLabel, colorTexto));

		JButton btnAplicar = new JButton("Aplicar");
		btnAplicar.setBackground(new Color(60, 60, 60));
		btnAplicar.setForeground(Color.WHITE);

		// ACCIÓN DEL BOTÓN
		btnAplicar.addActionListener(e -> {
			if (imagenService.getImagenOriginal() == null)
				return;

			// Convertimos los valores de 0-100 a escala 0.0-1.0 (o más para saturar)
			float fSatu = ((Integer) spinSaturacion.getValue()) / 100f;
			float fBri = ((Integer) spinBrillo.getValue()) / 100f;
			float fTran = ((Integer) spinOpacidad.getValue()) / 255f;

			BufferedImage res = imagenService.aplicarHSV(fTran, fSatu, fBri);
			visorInteligente.setImagen(res);
			lblDigital.setText(" HSV: S=" + spinSaturacion.getValue() + "% B=" + spinBrillo.getValue() + "%");
		});

		panel.add(btnAplicar);
		return panel;
	}

	// Método auxiliar ajustado para recibir el spinner ya creado
	private JPanel crearEstructuraItem(String titulo, JSpinner spinner, Font f, Color c) {
		JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		item.setOpaque(false);
		JLabel label = new JLabel(titulo);
		label.setFont(f);
		label.setForeground(c);
		spinner.setPreferredSize(new Dimension(55, 24));
		item.add(label);
		item.add(spinner);
		return item;
	}

	private JPanel crearPanelColor() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		panel.setBackground(new Color(30, 30, 30));

		// Tu arreglo de colores actual
		Color[] colores = { new Color(255, 140, 0), new Color(34, 139, 34), new Color(0, 120, 215),
				new Color(128, 0, 128), new Color(220, 20, 60), new Color(105, 105, 105), new Color(255, 20, 147),
				new Color(0, 255, 255), new Color(255, 215, 0), new Color(75, 0, 130), new Color(0, 255, 127),
				new Color(255, 99, 71), new Color(173, 216, 230), new Color(240, 230, 140), new Color(47, 79, 79),
				new Color(139, 69, 19), new Color(57, 255, 20), new Color(255, 0, 255), new Color(0, 191, 255),
				new Color(160, 82, 45), new Color(173, 216, 230) };

		// Usamos un for indexado para acceder al nombre correspondiente
		for (int i = 0; i < colores.length; i++) {
			// Necesitamos variables finales para usarlas dentro del lambda (ActionListener)
			final Color colorActual = colores[i];
			final String nombreActual = NOMBRES_COLORES[i];

			JButton btnColor = new JButton();
			btnColor.setPreferredSize(new Dimension(40, 40));
			btnColor.setBackground(colorActual);
			btnColor.setCursor(new Cursor(Cursor.HAND_CURSOR));

			btnColor.addActionListener(e -> {
				// Guardamos la selección para el botón "APLICAR"
				this.ultimoColorSeleccionado = colorActual;
				this.ultimoNombreColor = nombreActual; // Asegúrate de declarar este String como atributo

				// Aplicación inmediata
				BufferedImage res = imagenService.aplicarTinte(colorActual);
				visorInteligente.setImagen(res);

				// CAMBIO CLAVE: Aquí mostramos el nombre en lugar del Hexadecimal
				lblDigital.setText(" MODO COLOR: " + nombreActual);
			});

			panel.add(btnColor);
		}

		return panel;
	}

	private JPanel crearPanelBN() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panel.setBackground(new Color(30, 30, 30));
		panel.add(new JLabel("Modo Blanco/Negro"));
		return panel;
	}

	private JPanel crearPanelNegativo() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panel.setBackground(new Color(30, 30, 30));
		panel.add(new JLabel("Modo Negativo"));
		return panel;
	}

	private JPanel crearPanelEsmerilado() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panel.setBackground(new Color(30, 30, 30));
		panel.add(new JLabel("Modo Esmerilado"));
		return panel;
	}

	private JPanel crearPanelEscalaGris() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panel.setBackground(new Color(30, 30, 30));
		panel.add(new JLabel("Modo Escala de Grises"));
		return panel;
	}

	private JPanel crearPanelEscalaReducirBits() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panel.setBackground(new Color(30, 30, 30));
		panel.add(new JLabel("Modo Reducir Bits"));
		return panel;
	}

	private JPanel crearPanelRetro() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panel.setBackground(new Color(30, 30, 30));

		JCheckBox cbR = new JCheckBox("R", true);
		JCheckBox cbG = new JCheckBox("G", true);
		JCheckBox cbB = new JCheckBox("B", true);

		JCheckBox[] checks = { cbR, cbG, cbB };

		for (JCheckBox cb : checks) {
			cb.setForeground(Color.WHITE);
			cb.setBackground(panel.getBackground());
			cb.setFocusPainted(false);

			cb.addActionListener(e -> actualizarCanales(cbR, cbG, cbB));
			panel.add(cb);
		}

		// inicial (RGB)
		actualizarCanales(cbR, cbG, cbB);

		return panel;
	}

	private void actualizarCanales(JCheckBox r, JCheckBox g, JCheckBox b) {
		StringBuilder sb = new StringBuilder();

		if (r.isSelected())
			sb.append("R");
		if (g.isSelected())
			sb.append("G");
		if (b.isSelected())
			sb.append("B");

		canalActual = sb.toString();
	}

	// --- ACCIONES ---

	private void accionAbrir() {
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedImage img = ImageIO.read(chooser.getSelectedFile());
				imagenService.cargarImagen(img);
				visorInteligente.setImagen(img);
				lblDigital.setText(" IMAGEN LISTA ");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error al abrir imagen.");
			}
		}
	}

	private void accionGuardar() {
		if (imagenService.getImagenActual() == null) {
			JOptionPane.showMessageDialog(this, "No hay imagen.");
			return;
		}
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				imagenService.guardarImagen(fileChooser.getSelectedFile(), "png");
				lblDigital.setText(" GUARDADO ");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error al guardar.");
			}
		}
	}

	private void accionReset() {
		imagenService.restablecer();
		if (imagenService.getImagenOriginal() != null) {
			visorInteligente.setImagen(imagenService.getImagenOriginal());
			actualizarPantalla();
		}
	}

	private void accionEjecutarFiltro() {
		if (imagenService.getImagenOriginal() == null) {
			lblDigital.setText(" CARGUE UNA IMAGEN ");
			return;
		}

		BufferedImage resultado = null;

		// Usamos switch para evaluar la pestaña activa
		switch (filtroActual) {

		case "RETRO":
			resultado = imagenService.aplicarRetro(sliderNiveles.getValue(), canalActual);
			break;

		case "DEGRADADOS":
			ejecutarDegradado(ultimoDegradado);
			return;

		case "COLOR":
			resultado = imagenService.aplicarTinte(ultimoColorSeleccionado);
			lblDigital.setText(" MODO COLOR: " + ultimoNombreColor + " APLICADO");
			break;
		case "HSV":
			// Extraemos los valores de los Spinners globales
			float fSatu = ((Integer) spinSaturacion.getValue()) / 100f;
			float fBri = ((Integer) spinBrillo.getValue()) / 100f;
			float fTran = ((Integer) spinOpacidad.getValue()) / 255f;

			resultado = imagenService.aplicarHSV(fTran, fSatu, fBri);
			break;

		case "B/N":
			resultado = imagenService.aplicarBlancoNegro();
			lblDigital.setText(" MODO B/N  ");
			break;

		case "NEGATIVO":
			resultado = imagenService.aplicarNegativo();
			break;

		case "REDUCIR BITS":
			int cantidadBits = sliderNiveles.getValue();

			resultado = imagenService.reducirBit(cantidadBits);
			lblDigital.setText(" CALIDAD: " + cantidadBits + " BITS");
			break;

		case "GRISES":
			int niveles = sliderNiveles.getValue();
			resultado = imagenService.aplicarEscalaGrises(niveles);
			lblDigital.setText("GRISES: " + niveles + " NIVELES");
			break;

		case "ESMERILADO":
			resultado = imagenService.aplicarVidrioEsmerilado();
			lblDigital.setText("MODO: VIDRIO ESMERILADO");
			break;
		case "CONVOLUCIONES":
			int iteraciones = sliderNiveles.getValue();
			resultado = imagenService.aplicarEfectoConvolucion(kernelActual, iteraciones);
			lblDigital.setText("KERNEL: " + nombreKernelActual + " x" + iteraciones);
			break;

		default:
			lblDigital.setText(" SELECCIONE UN FILTRO ");
			break;
		}

		// Actualización centralizada del visor
		if (resultado != null) {
			visorInteligente.setImagen(resultado);
			lblDigital.setText(" " + filtroActual + " ");
		}
	}

	private void actualizarPantalla() {
	    if (lblIntensidad == null) return;

	    if (filtroActual.equals("GRISES") ||
	        filtroActual.equals("REDUCIR BITS") ||
	        filtroActual.equals("RETRO") ||
	        filtroActual.equals("CONVOLUCIONES")) {

	        int valor = sliderNiveles.getValue();

	        if (filtroActual.equals("CONVOLUCIONES")) {
	            lblIntensidad.setText("Iteraciones:"); 
	            lblDigital.setText(String.format(" %s - ITERACIONES: %d ", filtroActual, valor));
	        } else if (filtroActual.equals("REDUCIR BITS")) {
	            lblIntensidad.setText("Bits:"); // 
	            lblDigital.setText(String.format(" %s - BITS: %d ", filtroActual, valor));
	        } else {
	            lblIntensidad.setText("Intensidad:");
	            lblDigital.setText(String.format(" %s - NIVEL: %d ", filtroActual, valor));
	        }
	     // Dentro de actualizarPantalla()
	        if (filtroActual.equals("CONVOLUCIONES") && "NORMAL VAR".equals(nombreKernelActual)) {
	            lblIntensidad.setText("Centro:");
	            float valorCentro = valor / 10.0f; 
	            
	            BufferedImage res = imagenService.aplicarKernelPersonalizado(valorCentro);
	            
	            visorInteligente.setImagen(res);
	            lblDigital.setText(String.format(" CENTRO KERNEL: %.1f ", valorCentro));
	        }

	    } else {
	        // Para filtros que no usan el slider (como Negativo o B/N)
	        lblIntensidad.setText("Intensidad:"); 
	        lblDigital.setText(" " + filtroActual + " ");
	    }
	}

	private Font cargarFuenteDigital() {
		try {
			InputStream is = getClass().getResourceAsStream("/digital-7.ttf");
			return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(22f);
		} catch (Exception e) {
			return new Font("Monospaced", Font.BOLD, 20);
		}
	}

	private JButton crearBotonCircular(String ruta, String tip) {
		JButton btn = new JButton();
		btn.setToolTipText(tip);
		try {
			URL url = getClass().getResource(ruta);
			if (url != null) {
				Image img = new ImageIcon(url).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
				btn.setIcon(new ImageIcon(img));
			}
		} catch (Exception e) {
		}
		btn.setPreferredSize(new Dimension(50, 50));
		btn.setBackground(new Color(60, 60, 60));
		btn.putClientProperty(FlatClientProperties.STYLE, "arc: 999");
		return btn;
	}
}

// Clase VisorImagenInteligente integrada o en el mismo archivo
class VisorImagenInteligente extends JPanel {
	private BufferedImage imagenOriginal;
	private BufferedImage imagenMarco;

	public VisorImagenInteligente() {
		setBackground(Color.BLACK);
		cargarMarco();
	}

	private void cargarMarco() {
		try {
			InputStream is = getClass().getResourceAsStream("/marco_visor.png");
			if (is != null)
				imagenMarco = ImageIO.read(is);
		} catch (IOException e) {
		}
	}

	public void setImagen(BufferedImage img) {
		this.imagenOriginal = img;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		if (imagenOriginal != null) {
			double scale = Math.min((double) getWidth() / imagenOriginal.getWidth(),
					(double) getHeight() / imagenOriginal.getHeight());
			int w = (int) (imagenOriginal.getWidth() * scale);
			int h = (int) (imagenOriginal.getHeight() * scale);
			g2d.drawImage(imagenOriginal, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
		}
		if (imagenMarco != null) {
			g2d.drawImage(imagenMarco, 0, 0, getWidth(), getHeight(), null);
		}
	}
}