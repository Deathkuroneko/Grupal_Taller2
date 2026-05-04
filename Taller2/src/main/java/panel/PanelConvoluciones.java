package panel;

import filtros.Kernels;
import frame.FiltroConfigurable;
import frame.VisorImagenPanel;
import service.ImagenService;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelConvoluciones extends JPanel implements FiltroConfigurable {
	private ImagenService service;
	private VisorImagenPanel visor;
	private float[] kernelActual = Kernels.kNormal;
	private String nombreKernelActual = "NORMAL";

	public PanelConvoluciones(ImagenService service, VisorImagenPanel visor) {
		this.service = service;
		this.visor = visor;
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		setBackground(new Color(30, 30, 30));

		ButtonGroup grupo = new ButtonGroup();
		add(crearRadio("ENFOQUE", Kernels.kEnfoque, grupo));
		add(crearRadio("DESENFOQUE", Kernels.kDesenfoque, grupo));
		add(crearRadio("BORDES", Kernels.kBordes8, grupo));
		add(crearRadio("NORMAL VAR", null, grupo));
		add(crearRadio("ACLARAR", Kernels.kAclaracion, grupo));
		add(crearRadio("OSCURECER", Kernels.kOscurecer, grupo));
		add(crearRadio("BLUR 9x9", Kernels.kDesenfoque9, grupo));
	}

	private JRadioButton crearRadio(String nombre, float[] matriz, ButtonGroup grupo) {
		JRadioButton rb = new JRadioButton(nombre);
		rb.setForeground(Color.WHITE);
		rb.setBackground(new Color(30, 30, 30));
		grupo.add(rb);
		rb.addActionListener(e -> {
			this.kernelActual = matriz;
			this.nombreKernelActual = nombre;
		});
		return rb;
	}

	@Override
	public void configurarSlider(JSlider slider, JLabel lblIntensidad) {
	    lblIntensidad.setText("Iteraciones:");
	    slider.setMinimum(1);
	    slider.setMaximum(10); 
	    slider.setValue(1);
	    

	    slider.setPaintTicks(true);
	    slider.setMajorTickSpacing(1); 
	    slider.setPaintLabels(true);
	    slider.setLabelTable(slider.createStandardLabels(1));
	    
	    // Aseguramos que el slider esté habilitado
	    slider.setEnabled(true);
	}

	@Override
	public void aplicarFiltro(int valor) {
		if ("NORMAL VAR".equals(nombreKernelActual)) {
			visor.setImagen(service.aplicarKernelPersonalizado(valor / 10.0f));
		} else {
			visor.setImagen(service.aplicarEfectoConvolucion(kernelActual, valor));
		}
	}

	@Override
	public String getNombreFiltro() {
		return "CONVOLUCIÓN: " + nombreKernelActual;
	}
}