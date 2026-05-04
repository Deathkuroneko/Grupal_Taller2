package panel;

import service.ImagenService;
import javax.swing.*;

import frame.FiltroConfigurable;
import frame.VisorImagenPanel;

import java.awt.*;

@SuppressWarnings("serial")
public class PanelRetro extends JPanel implements FiltroConfigurable {

    private ImagenService service;
    private VisorImagenPanel visor;

    private JCheckBox cbR, cbG, cbB;

    public PanelRetro(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        setBackground(new Color(30, 30, 30));

        // Label
        JLabel lbl = new JLabel("CANALES:");
        lbl.setForeground(new Color(180, 180, 180));
        add(lbl);

        // Checkboxes
        cbR = crearCheck("R");
        cbG = crearCheck("G");
        cbB = crearCheck("B");

        add(cbR);
        add(cbG);
        add(cbB);
    }

    private JCheckBox crearCheck(String text) {
        JCheckBox cb = new JCheckBox(text, true);
        cb.setForeground(Color.WHITE);
        cb.setBackground(new Color(30, 30, 30));
        cb.setFocusPainted(false);
        return cb;
    }

    private String obtenerCanales() {
        StringBuilder sb = new StringBuilder();

        if (cbR.isSelected()) sb.append("R");
        if (cbG.isSelected()) sb.append("G");
        if (cbB.isSelected()) sb.append("B");

        return sb.toString();
    }

 
    @Override
    public void configurarSlider(JSlider slider, JLabel lblIntensidad) {
        lblIntensidad.setText("Intensidad:");
        slider.setEnabled(true);
        slider.setMinimum(1);
        slider.setMaximum(10);
        slider.setValue(1);
        slider.setMajorTickSpacing(1);
        
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.setLabelTable(slider.createStandardLabels(1));
    }

    // 🚀 APLICA EL FILTRO
    @Override
    public void aplicarFiltro(int valor) {
        String canales = obtenerCanales();
        visor.setImagen(service.aplicarRetro(valor, canales));
    }

    @Override
    public String getNombreFiltro() {
        return "RETRO (" + obtenerCanales() + ")";
    }
}