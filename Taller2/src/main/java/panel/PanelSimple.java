package panel;

import service.ImagenService;
import javax.swing.*;

import frame.FiltroConfigurable;
import frame.VisorImagenPanel;

import java.awt.*;

@SuppressWarnings("serial")
public class PanelSimple extends JPanel implements FiltroConfigurable {
    private String tipo;
    private ImagenService service;
    private VisorImagenPanel visor;

    public PanelSimple(ImagenService service, VisorImagenPanel visor, String tipo) {
        this.service = service;
        this.visor = visor;
        this.tipo = tipo;
        
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
        
        // En lugar de dejarlo vacío, ponemos un mensaje elegante
        JLabel info = new JLabel("EFECTO DE PASO ÚNICO - PRESIONE APLICAR");
        info.setForeground(new Color(100, 100, 100));
        info.setHorizontalAlignment(SwingConstants.CENTER);
        add(info, BorderLayout.CENTER);
    }

    @Override
    public void configurarSlider(JSlider slider, JLabel lblIntensidad) {
        lblIntensidad.setText("Nivel:");
        slider.setEnabled(false); // No se necesita slider para Negativo o B/N
    }

    @Override
    public void aplicarFiltro(int valor) {
        if(tipo.equals("BN")) visor.setImagen(service.aplicarBlancoNegro());
        if(tipo.equals("NEG")) visor.setImagen(service.aplicarNegativo());
        if(tipo.equals("ESM")) visor.setImagen(service.aplicarVidrioEsmerilado());
    }

    @Override
    public String getNombreFiltro() { return tipo; }
}