package panel;

import service.ImagenService;
import javax.swing.*;

import frame.VisorImagenPanel;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelHSV extends JPanel {

    private ImagenService service;
    private VisorImagenPanel visor;

    private JSpinner spinSatu, spinBrillo, spinOpacidad;

    public PanelHSV(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBackground(new Color(30, 30, 30));

        spinSatu = new JSpinner(new SpinnerNumberModel(100, 0, 200, 5));
        spinBrillo = new JSpinner(new SpinnerNumberModel(100, 0, 200, 5));
        spinOpacidad = new JSpinner(new SpinnerNumberModel(255, 0, 255, 5));

        add(new JLabel("SATURACION:")); add(spinSatu);
        add(new JLabel("BRILLO:")); add(spinBrillo);
        add(new JLabel("OPACIDAD:")); add(spinOpacidad);

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(btnAplicar);
    }

    private void aplicarFiltro() {
        float s = ((Integer) spinSatu.getValue()) / 100f;
        float b = ((Integer) spinBrillo.getValue()) / 100f;
        float o = ((Integer) spinOpacidad.getValue()) / 255f;

        visor.setImagen(service.aplicarHSV(o, s, b));
    }
}