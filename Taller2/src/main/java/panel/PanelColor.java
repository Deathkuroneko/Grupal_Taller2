package panel;
import service.ImagenService;
import javax.swing.*;

import frame.FiltroConfigurable;
import frame.VisorImagenPanel;

import java.awt.*;

@SuppressWarnings("serial")
public class PanelColor extends JPanel implements FiltroConfigurable {
    private ImagenService service;
    private VisorImagenPanel visor;
    private Color colorSeleccionado = Color.ORANGE;
    private String nombreColor = "NARANJA";

    public PanelColor(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(30, 30, 30));

        
        agregarBotonColor(new Color(255, 140, 0), "NARANJA");
        agregarBotonColor(new Color(34, 139, 34), "VERDE FOREST");
        agregarBotonColor(new Color(0, 120, 215), "AZUL MODERNO");
        agregarBotonColor(new Color(128, 0, 128), "PURPURA");
        agregarBotonColor(new Color(220, 20, 60), "CARMESI");
        agregarBotonColor(new Color(105, 105, 105), "GRIS SEPIA");
        agregarBotonColor(new Color(255, 20, 147), "ROSA FUCSIA");
        agregarBotonColor(new Color(0, 255, 255), "CIAN ELECTRICO");
        agregarBotonColor(new Color(255, 215, 0), "DORADO");
        agregarBotonColor(new Color(75, 0, 130), "INDIGO");
        agregarBotonColor(new Color(0, 255, 127), "VERDE PRIMAVERA");
        agregarBotonColor(new Color(255, 99, 71), "TOMATE");
        agregarBotonColor(new Color(173, 216, 230), "AZUL PASTEL");
        agregarBotonColor(new Color(240, 230, 140), "CAQUI");
        agregarBotonColor(new Color(47, 79, 79), "GRIS PIZARRA");
        agregarBotonColor(new Color(139, 69, 19), "MARRON CUERO");
        agregarBotonColor(new Color(57, 255, 20), "VERDE NEON");
        agregarBotonColor(new Color(255, 0, 255), "MAGENTA");
        agregarBotonColor(new Color(0, 191, 255), "AZUL ELECTRICO");
        agregarBotonColor(new Color(160, 82, 45), " SIENA");
        agregarBotonColor(new Color(128, 128, 0), "OLIVA");
    }

    private void agregarBotonColor(Color c, String nombre) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setBackground(c);
        btn.addActionListener(e -> {
            this.colorSeleccionado = c;
            this.nombreColor = nombre;
            aplicarFiltro(0);
        });
        add(btn);
    }

    @Override
    public void configurarSlider(JSlider slider, JLabel lblIntensidad) {
        lblIntensidad.setText("N/A:");
        slider.setEnabled(false);
    }

    @Override
    public void aplicarFiltro(int valor) {
        visor.setImagen(service.aplicarTinte(colorSeleccionado));
    }

    @Override
    public String getNombreFiltro() { return "TINTE: " + nombreColor; }
}