package panel;

import service.ImagenService;
import javax.swing.*;

import frame.FiltroConfigurable;
import frame.VisorImagenPanel;

import java.awt.*;

@SuppressWarnings("serial")
public class PanelDegradados extends JPanel implements FiltroConfigurable {
    private ImagenService service;
    private VisorImagenPanel visor;
    private String ultimoTipo = "IZQ_DER"; // Memoria del último pulsado

    public PanelDegradados(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 15));
        setBackground(new Color(30, 30, 30));

        String[] tipos = {"IZQ_DER", "DER_IZQ", "ARRIBA_ABAJO", "ABAJO_ARRIBA", "RADIAL", "RADIAL_INVERSO"};
        for (String t : tipos) {
            JButton btn = new JButton(t.replace("_", " "));
            btn.setFont(new Font("SansSerif", Font.BOLD, 10));
            btn.addActionListener(e -> {
                this.ultimoTipo = t;
                aplicarFiltro(0); // Aplicación inmediata al tocar el botón pequeño
            });
            add(btn);
        }
    }

    @Override
    public void configurarSlider(JSlider slider, JLabel lblIntensidad) {
        slider.setEnabled(false); // Los degradados no suelen usar este slider
    }

    @Override
    public void aplicarFiltro(int valor) {
        switch (ultimoTipo) {
            case "IZQ_DER": visor.setImagen(service.aplicarDegradadoIzqDer()); break;
            case "DER_IZQ": visor.setImagen(service.aplicarDegradadoDerIzq()); break;
            case "ARRIBA_ABAJO": visor.setImagen(service.aplicarDegradadoArriAbajo()); break;
            case "ABAJO_ARRIBA": visor.setImagen(service.aplicarDegradadoAbajoArriba()); break;
            case "RADIAL": visor.setImagen(service.aplicarDegradadoRadial()); break;
            case "RADIAL_INVERSO": visor.setImagen(service.aplicarDegradadoRadialInverso()); break;
        }
    }

    @Override
    public String getNombreFiltro() { return "DEGRADADO: " + ultimoTipo; }
}