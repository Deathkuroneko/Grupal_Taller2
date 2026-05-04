package panel;

import service.ImagenService;
import javax.swing.*;

import frame.FiltroConfigurable;
import frame.VisorImagenPanel;

import java.awt.*;

@SuppressWarnings("serial")
public class PanelGrises extends JPanel implements FiltroConfigurable {
    private ImagenService service;
    private VisorImagenPanel visor;

    public PanelGrises(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;
        setBackground(new Color(30, 30, 30));
    }

    @Override
    public void configurarSlider(JSlider slider, JLabel lblIntensidad) {
        lblIntensidad.setText("Niveles:");
        slider.setEnabled(true);
        slider.setMinimum(2);
        slider.setMaximum(256);
        slider.setValue(1); // Un valor inicial razonable
        
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        
        slider.setMajorTickSpacing(64); 
        slider.setMinorTickSpacing(16);
        slider.setLabelTable(slider.createStandardLabels(64));
    }

    @Override
    public void aplicarFiltro(int valor) {
        visor.setImagen(service.aplicarEscalaGrises(valor));
    }

    @Override
    public String getNombreFiltro() { return "ESCALA DE GRISES"; }
}