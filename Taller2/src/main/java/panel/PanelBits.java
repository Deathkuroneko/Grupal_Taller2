package panel;

import service.ImagenService;
import javax.swing.*;
import frame.VisorImagenPanel;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelBits extends JPanel {

    private ImagenService service;
    private VisorImagenPanel visor;
    private int valorSeleccionado = 8;

    public PanelBits(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        setBackground(new Color(30, 30, 30));

        add(new JLabel("BITS:"));

        int[] niveles = {2, 4, 8, 16, 32};

        ButtonGroup grupo = new ButtonGroup();

        for (int n : niveles) {
            JRadioButton rb = new JRadioButton(n + " Bits");
            rb.setForeground(Color.WHITE);
            rb.setBackground(new Color(30, 30, 30));

            if (n == valorSeleccionado) rb.setSelected(true);

            rb.addActionListener(e -> valorSeleccionado = n);

            grupo.add(rb);
            add(rb);
        }

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(btnAplicar);
    }

    private void aplicarFiltro() {
        visor.setImagen(service.reducirBit(valorSeleccionado));
    }
}