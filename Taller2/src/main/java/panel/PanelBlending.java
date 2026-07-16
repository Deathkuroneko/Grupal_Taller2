package panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;

import frame.VisorImagenPanel;
import service.ImagenService;

public class PanelBlending extends JPanel {
	private ImagenService servicio;
	private VisorImagenPanel visor;
	private JSlider sliderAlpha;

	private JRadioButton rbNormal;
	private JRadioButton rbSuma;
	private JRadioButton rbMultiplicar;

	public PanelBlending(ImagenService servicio, VisorImagenPanel visor) {
		this.servicio = servicio;
		this.visor = visor;
		construirPanel();

	}

	private void construirPanel() {

		setLayout(new BorderLayout());

		JPanel controles = new JPanel(new FlowLayout());

		JButton btnCargar = new JButton("Cargar imagen");

		JButton btnAplicar = new JButton("Aplicar");

		controles.add(btnCargar);
		controles.add(new JLabel("Alpha"));

		sliderAlpha = new JSlider(0, 100, 50);

		sliderAlpha.setMajorTickSpacing(25);
		sliderAlpha.setPaintTicks(true);
		sliderAlpha.setPaintLabels(true);

		controles.add(sliderAlpha);

		rbNormal = new JRadioButton("Normal", true);

		rbSuma = new JRadioButton("Suma");

		rbMultiplicar = new JRadioButton("Multiplicar");

		ButtonGroup grupo = new ButtonGroup();

		grupo.add(rbNormal);
		grupo.add(rbSuma);
		grupo.add(rbMultiplicar);

		controles.add(rbNormal);
		controles.add(rbSuma);
		controles.add(rbMultiplicar);

		controles.add(btnAplicar);

		add(controles, BorderLayout.CENTER);

		btnCargar.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg"));
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				try {
					File archivo = chooser.getSelectedFile();
					BufferedImage img = ImageIO.read(archivo);
					servicio.cargarImagenSecundaria(img);
					visor.setBlending(img);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});

		btnAplicar.addActionListener(e -> {
			String tipo = "NORMAL";
			if (rbSuma.isSelected())tipo = "SUMA";
			if (rbMultiplicar.isSelected()) tipo = "MULTIPLICAR";

			float alpha = sliderAlpha.getValue() / 100f;
			BufferedImage resultado = servicio.aplicarBlending(alpha, tipo);
			if (resultado != null) {
				visor.setImagen(resultado);
			}

		});

	}

}