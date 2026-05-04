package frame;

import panel.*;
import service.ImagenService;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.formdev.flatlaf.FlatClientProperties;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JLabel lblDigital;
	private JLabel lblIntensidad;
	private JSlider sliderNiveles;

	private ImagenService imagenService = new ImagenService();
	private VisorImagenPanel visor;

	private Map<String, FiltroConfigurable> filtros = new HashMap<>();
	private FiltroConfigurable filtroActivo;

	public MainFrame() {
		setTitle("Editor de Fotos - UCE PhotoSystem");
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Icono de la ventana
		try {
			setIconImage(new ImageIcon(getClass().getResource("/icons/camara-de-fotos.png")).getImage());
		} catch (Exception e) {
			System.out.println("Icono principal no encontrado");
		}

		// Inicialización de componentes en orden lógico
		initTopPanel();
		initCenterPanel();
		initBottomTabs(); // Registra los filtros en el mapa
		initLeftPanel(); // Crea el slider y los botones rápidos

		// Estado inicial
		filtroActivo = filtros.get("RETRO");
		if (filtroActivo != null) {
			filtroActivo.configurarSlider(sliderNiveles, lblIntensidad);
			actualizarPantallaLCD();
		}
	}

	private void initTopPanel() {
		JPanel top = new JPanel(new BorderLayout());
		top.setBackground(new Color(20, 20, 20));
		top.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		// Pantalla LCD
		JPanel lcd = new JPanel();
		lcd.setBackground(Color.BLACK);
		lcd.setBorder(new LineBorder(new Color(0, 50, 0), 2, true));

		lblDigital = new JLabel(" ESPERANDO IMAGEN ");
		lblDigital.setForeground(Color.GREEN);
		lcd.add(lblDigital);

		lblDigital.setFont(new Font("DialogInput", Font.BOLD, 16));

		// Botones de archivo
		JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		acciones.setOpaque(false);

		acciones.add(new BotonCircular("/icons/abrir.png", "Abrir", e -> abrir()));
		acciones.add(new BotonCircular("/icons/save.png", "Guardar", e -> guardar()));
		acciones.add(new BotonCircular("/icons/reset.png", "Reset", e -> reset()));

		top.add(lcd, BorderLayout.WEST);
		top.add(acciones, BorderLayout.EAST);
		add(top, BorderLayout.NORTH);
	}

	private void initLeftPanel() {
		JPanel left = new JPanel();
		left.setPreferredSize(new Dimension(260, 0));
		left.setBackground(new Color(35, 35, 35));
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

		JLabel titulo = new JLabel("CONTROLES");
		titulo.setForeground(new Color(212, 175, 55));
		titulo.setFont(new Font("Verdana", Font.BOLD, 14));
		titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		lblIntensidad = new JLabel("Ajuste:");
		// Para los Botones y Labels del Slider (Legibilidad máxima)
		lblIntensidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblIntensidad.setForeground(Color.WHITE);

		// Configuración del Slider
		sliderNiveles = new JSlider(1, 10, 5);
		sliderNiveles.setBackground(left.getBackground());
		sliderNiveles.setForeground(Color.WHITE);
		sliderNiveles.addChangeListener(e -> {

			// Actualización dinámica del label de intensidad
			String base = lblIntensidad.getText();
			if (base.contains(":")) {
				lblIntensidad.setText(base.substring(0, base.indexOf(":") + 1) + " " + sliderNiveles.getValue());
			}
		});

		// --- NUEVA SECCIÓN: EFECTOS DIRECTOS ---
		JLabel lblRapido = new JLabel("EFECTOS DIRECTOS");
		lblRapido.setForeground(Color.GRAY);
		lblRapido.setFont(new Font("SansSerif", Font.BOLD, 10));
		lblRapido.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel gridBotones = new JPanel(new GridLayout(2, 2, 8, 8));
		gridBotones.setOpaque(false);
		gridBotones.setMaximumSize(new Dimension(220, 100));

		gridBotones.add(crearBotonFiltroRapido("B / N", "B/N"));
		gridBotones.add(crearBotonFiltroRapido("GRISES", "GRISES"));
		gridBotones.add(crearBotonFiltroRapido("NEGATIVO", "NEGATIVO"));
		gridBotones.add(crearBotonFiltroRapido("ESM", "ESMERILADO"));
		// ---------------------------------------

		JButton btnAplicar = new JButton("APLICAR CAMBIOS");
		btnAplicar.setBackground(new Color(180, 20, 20));
		btnAplicar.setForeground(Color.WHITE);
		btnAplicar.setFont(new Font("SansSerif", Font.BOLD, 12));
		btnAplicar.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnAplicar.setMaximumSize(new Dimension(200, 40));
		btnAplicar.putClientProperty(FlatClientProperties.STYLE, "arc:999");
		btnAplicar.addActionListener(e -> aplicarFiltro());

		// Ensamblado del panel izquierdo
		left.add(titulo);
		left.add(Box.createVerticalStrut(25));
		left.add(lblIntensidad);
		left.add(sliderNiveles);
		left.add(Box.createVerticalStrut(30));
		left.add(lblRapido);
		left.add(Box.createVerticalStrut(10));
		left.add(gridBotones);
		left.add(Box.createVerticalGlue());
		left.add(btnAplicar);

		add(left, BorderLayout.WEST);
	}

	private void initCenterPanel() {
		visor = new VisorImagenPanel();
		add(visor, BorderLayout.CENTER);
	}

	private void initBottomTabs() {
		JTabbedPane tabs = new JTabbedPane();

		// Instanciar paneles (Asegúrate de que estas clases existan en tu package
		// panel)
		PanelRetro retro = new PanelRetro(imagenService, visor);
		PanelBits bits = new PanelBits(imagenService, visor);
		PanelConvoluciones conv = new PanelConvoluciones(imagenService, visor);
		PanelSimple bn = new PanelSimple(imagenService, visor, "BN");
		PanelSimple neg = new PanelSimple(imagenService, visor, "NEG");
		PanelSimple esm = new PanelSimple(imagenService, visor, "ESM");
		PanelGrises grises = new PanelGrises(imagenService, visor);
		PanelDegradados deg = new PanelDegradados(imagenService, visor);
		PanelHSV hsv = new PanelHSV(imagenService, visor);
		PanelColor color = new PanelColor(imagenService, visor);
		// Registrar en el Mapa (Las llaves deben coincidir con crearBotonFiltroRapido)
		filtros.put("RETRO", retro);
		filtros.put("REDUCIR BITS", bits);
		filtros.put("CONVOLUCIONES", conv);
		filtros.put("B/N", bn);
		filtros.put("NEGATIVO", neg);
		filtros.put("ESMERILADO", esm);
		filtros.put("GRISES", grises);
		filtros.put("DEGRADADOS", deg);
		filtros.put("HSV", hsv);
		filtros.put("Color", color);
		// Añadir a las pestañas inferiores
		tabs.addTab("Retro", retro);
		tabs.addTab("Reducir Bits", bits);
		tabs.addTab("Convoluciones", conv);
		tabs.add("Degradados", deg);
		tabs.add("HSV", hsv);
		tabs.add("Color", color);
		// Los filtros rápidos ya están en el panel izquierdo, pero se pueden dejar aquí
		// también

		tabs.addChangeListener(e -> cambiarFiltro(tabs));
		add(tabs, BorderLayout.SOUTH);
	}

	// ===================== LÓGICA DE INTERFAZ =====================

	private void actualizarPantallaLCD() {
		if (filtroActivo == null)
			return;

		// Obtenemos el nombre del filtro (ej: "REDUCCIÓN DE BITS")
		String nombre = filtroActivo.getNombreFiltro();

		// Ahora solo mandamos el nombre al LCD, sin el valor del slider
		lblDigital.setText(" " + nombre.toUpperCase() + " ");
	}

	private JButton crearBotonFiltroRapido(String texto, String filtroKey) {
		JButton btn = new JButton(texto);
		btn.setFont(new Font("SansSerif", Font.BOLD, 10));
		btn.setBackground(new Color(55, 55, 55));
		btn.setForeground(Color.LIGHT_GRAY);
		btn.setFocusPainted(false);

		btn.addActionListener(e -> {
			filtroActivo = filtros.get(filtroKey);
			if (filtroActivo != null) {
				filtroActivo.configurarSlider(sliderNiveles, lblIntensidad);
				aplicarFiltro();
			}
		});
		return btn;
	}

	private void cambiarFiltro(JTabbedPane tabs) {
		String nombreTab = tabs.getTitleAt(tabs.getSelectedIndex()).toUpperCase();
		filtroActivo = filtros.get(nombreTab);
		if (filtroActivo != null) {
			filtroActivo.configurarSlider(sliderNiveles, lblIntensidad);
			actualizarPantallaLCD();
		}
	}

	private void aplicarFiltro() {
		if (imagenService.getImagenOriginal() == null) {
			lblDigital.setText(" CARGUE UNA IMAGEN ");
			return;
		}
		if (filtroActivo != null) {
			filtroActivo.aplicarFiltro(sliderNiveles.getValue());
			actualizarPantallaLCD();
		}
	}

	// ===================== MÉTODOS DE ARCHIVO =====================

	private void abrir() {
		JFileChooser ch = new JFileChooser();
		if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedImage img = ImageIO.read(ch.getSelectedFile());
				imagenService.cargarImagen(img);
				visor.setImagen(img);
				lblDigital.setText(" IMAGEN CARGADA ");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error al abrir imagen");
			}
		}
	}

	private void guardar() {
		if (imagenService.getImagenActual() == null)
			return;
		JFileChooser ch = new JFileChooser();
		if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				imagenService.guardarImagen(ch.getSelectedFile(), "png");
				lblDigital.setText(" IMAGEN GUARDADA ");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error al guardar");
			}
		}
	}

	private void reset() {
		imagenService.restablecer();
		if (imagenService.getImagenOriginal() != null) {
			visor.setImagen(imagenService.getImagenOriginal());
			lblDigital.setText(" SISTEMA RESETEADO ");
		}
	}
}