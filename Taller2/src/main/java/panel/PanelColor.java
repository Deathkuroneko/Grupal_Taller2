package panel;

import service.ImagenService;
import javax.swing.*;
import frame.VisorImagenPanel;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelColor extends JPanel {

    private ImagenService service;
    private VisorImagenPanel visor;

    private Color colorSeleccionado = Color.ORANGE;

    public PanelColor(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(30, 30, 30));

        Color[] colores = {
        	    new Color(255, 140, 0),
        	    new Color(34, 139, 34),
        	    new Color(0, 120, 215),
        	    new Color(128, 0, 128),
        	    new Color(220, 20, 60),
        	    new Color(105, 105, 105),
        	    new Color(255, 20, 147),
        	    new Color(0, 255, 255),
        	    new Color(255, 215, 0),
        	    new Color(75, 0, 130),
        	    new Color(0, 255, 127),
        	    new Color(255, 99, 71),
        	    new Color(173, 216, 230),
        	    new Color(240, 230, 140),
        	    new Color(47, 79, 79),
        	    new Color(139, 69, 19),
        	    new Color(57, 255, 20),
        	    new Color(255, 0, 255),
        	    new Color(0, 191, 255),
        	    new Color(160, 82, 45),
        	    new Color(128, 128, 0)
        	};

        	for (Color c : colores) {
        	    agregarBotonColor(c);
        	}

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(btnAplicar);
    }

    private void agregarBotonColor(Color c) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setBackground(c);

        btn.addActionListener(e -> colorSeleccionado = c);

        add(btn);
    }

    private void aplicarFiltro() {
        visor.setImagen(service.aplicarTinte(colorSeleccionado));
    }
}