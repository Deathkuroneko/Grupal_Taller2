package panel;

import service.ImagenService;
import javax.swing.*;

import frame.FiltroConfigurable;
import frame.VisorImagenPanel;

import java.awt.*;

@SuppressWarnings("serial")
public class PanelBits extends JPanel implements FiltroConfigurable {
    private ImagenService service;
    private VisorImagenPanel visor;

    public PanelBits(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        setBackground(new Color(30, 30, 30));

        // Añadimos botones de acceso rápido (Presets)
        add(new JLabel("ACCESO RÁPIDO:"));
        int[] niveles = {2, 4, 8, 16, 32};
        for (int n : niveles) {
            JButton btn = new JButton(n + " Bits");
            btn.addActionListener(e -> aplicarFiltro(n));
            add(btn);
        }
    }

    @Override
    public void configurarSlider(JSlider slider, JLabel lblIntensidad) {
        lblIntensidad.setText("Intensidad:");
     
        
        slider.setEnabled(true);
        slider.setMinimum(1);
        slider.setMaximum(10);
        slider.setValue(5);
        slider.setMajorTickSpacing(1);
        
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.setLabelTable(slider.createStandardLabels(1));
    }
    
    @Override
    public void aplicarFiltro(int valor) {
        visor.setImagen(service.reducirBit(valor));
    }

    @Override
    public String getNombreFiltro() { return "REDUCCIÓN DE BITS"; }
}