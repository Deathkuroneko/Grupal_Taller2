package panel;

import service.ImagenService;
import frame.VisorImagenPanel;
import filtros.Histograma;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class PanelEcualizador extends JPanel {

	private final ImagenService service;
	private final VisorImagenPanel visor;

	private JSlider sliderFactor;
	private JLabel lblValor;
	private JButton btnAplicar;
	private BufferedImage imgHistograma;

	public void setHistograma(BufferedImage img) {
	    this.imgHistograma = img;
	    repaint();
	}

	public PanelEcualizador(ImagenService service, VisorImagenPanel visor) {
		this.service = service;
		this.visor = visor;

		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(30, 30, 30));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// ---------- Panel inferior: controles ----------
		JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panelControles.setBackground(new Color(30, 30, 30));

		JLabel lbl = new JLabel("Intensidad:");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
		panelControles.add(lbl);

		sliderFactor = new JSlider(0, 100, 50);
		sliderFactor.setPreferredSize(new Dimension(250, 50));
		sliderFactor.setBackground(new Color(30, 30, 30));
		sliderFactor.setForeground(new Color(0, 200, 255));
		sliderFactor.setMajorTickSpacing(25);
		sliderFactor.setMinorTickSpacing(5);
		sliderFactor.setPaintTicks(true);
		sliderFactor.setPaintLabels(true);

		java.util.Hashtable<Integer, JLabel> tabla = new java.util.Hashtable<>();
		tabla.put(0, new JLabel("0%"));
		tabla.put(25, new JLabel("25%"));
		tabla.put(50, new JLabel("50%"));
		tabla.put(75, new JLabel("75%"));
		tabla.put(100, new JLabel("100%"));
		for (JLabel l : tabla.values()) {
			l.setForeground(Color.WHITE);
			l.setFont(new Font("Segoe UI", Font.BOLD, 11));
		}
		sliderFactor.setLabelTable(tabla);

		panelControles.add(sliderFactor);

		// Etiqueta con el valor numérico
		lblValor = new JLabel("50%");
		lblValor.setForeground(new Color(0, 255, 120));
		lblValor.setFont(new Font("Consolas", Font.BOLD, 16));
		sliderFactor.addChangeListener(e -> {
			int val = sliderFactor.getValue();
			lblValor.setText(val + "%");
		});
		panelControles.add(lblValor);

		// Botón Aplicar
		btnAplicar = new JButton("Aplicar Ecualización");
		btnAplicar.setBackground(new Color(50, 120, 220));
		btnAplicar.setForeground(Color.WHITE);
		btnAplicar.setFocusPainted(false);
		btnAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAplicar.addActionListener(e -> aplicarEcualizacion());
		panelControles.add(btnAplicar);

		// Botón Restaurar
		JButton btnReset = new JButton("Restaurar");
		btnReset.setBackground(new Color(100, 100, 100));
		btnReset.setForeground(Color.WHITE);
		btnReset.setFocusPainted(false);
		btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnReset.addActionListener(e -> {
			if (service.getImagenOriginal() != null) {
				visor.setImagen(service.getImagenOriginal());
				actualizarHistograma();
			}
		});
		panelControles.add(btnReset);

		add(panelControles, BorderLayout.SOUTH);
	}

	private void aplicarEcualizacion() {
		BufferedImage img = visor.getImagenActual();
		if (img == null) {
			JOptionPane.showMessageDialog(this, "Cargue una imagen primero.", "Sin imagen",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		float factor = sliderFactor.getValue() / 100f;
		BufferedImage resultado = service.aplicarEcualizacion(factor);
		if (resultado != null) {
			visor.setImagen(resultado);
			actualizarHistograma();
		} else {
			JOptionPane.showMessageDialog(this, "Error al ecualizar.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void actualizarHistograma() {
	    BufferedImage img = visor.getImagenActual();
	    if (img == null) return;
	    BufferedImage histoImg = Histograma.generarHistograma(img, true, true, true);
	    visor.setHistograma(histoImg); 
	}
}