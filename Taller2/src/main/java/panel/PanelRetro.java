package panel;

import service.ImagenService;
import javax.swing.*;

import frame.VisorImagenPanel;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelRetro extends JPanel {

    private ImagenService service;
    private VisorImagenPanel visor;

    private JCheckBox cbR, cbG, cbB;
    private JSlider slider;

    public PanelRetro(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        setBackground(new Color(30, 30, 30));

        add(new JLabel("CANALES:"));

        cbR = crearCheck("R");
        cbG = crearCheck("G");
        cbB = crearCheck("B");

        add(cbR);
        add(cbG);
        add(cbB);

        // 🔥 Slider propio
        slider = new JSlider(1, 10, 5);
        add(slider);

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());
        add(btnAplicar);
    }

    private JCheckBox crearCheck(String text) {
        JCheckBox cb = new JCheckBox(text, true);
        cb.setForeground(Color.WHITE);
        cb.setBackground(new Color(30, 30, 30));
        return cb;
    }

    private String obtenerCanales() {
        StringBuilder sb = new StringBuilder();
        if (cbR.isSelected()) sb.append("R");
        if (cbG.isSelected()) sb.append("G");
        if (cbB.isSelected()) sb.append("B");
        return sb.toString();
    }

    private void aplicarFiltro() {
        visor.setImagen(service.aplicarRetro(slider.getValue(), obtenerCanales()));
    }
}