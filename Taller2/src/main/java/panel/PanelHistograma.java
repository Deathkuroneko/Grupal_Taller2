package panel;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import frame.VisorImagenPanel;
import service.ImagenService;

@SuppressWarnings("serial")
public class PanelHistograma extends JPanel {

    private final ImagenService service;
    private final VisorImagenPanel visor;

    // Checkboxes
    private final JCheckBox chkRed;
    private final JCheckBox chkGreen;
    private final JCheckBox chkBlue;

    public PanelHistograma(
            ImagenService service,
            VisorImagenPanel visor) {

        this.service = service;
        this.visor = visor;

        // Layout
        setLayout(new FlowLayout(
                FlowLayout.LEFT,
                10,
                10));

        setBackground(new Color(30, 30, 30));

        // CHECKBOX RED
        chkRed = new JCheckBox("RED");
        chkRed.setSelected(true);
        chkRed.setForeground(Color.RED);
        chkRed.setBackground(new Color(30, 30, 30));

        add(chkRed);

        // CHECKBOX GREEN
        chkGreen = new JCheckBox("GREEN");
        chkGreen.setSelected(true);
        chkGreen.setForeground(Color.GREEN);
        chkGreen.setBackground(new Color(30, 30, 30));

        add(chkGreen);

        // CHECKBOX BLUE-

        chkBlue = new JCheckBox("BLUE");
        chkBlue.setSelected(true);
        chkBlue.setForeground(Color.CYAN);
        chkBlue.setBackground(new Color(30, 30, 30));

        add(chkBlue);

        // BOTON APLICAR

        JButton btnAplicar =
                new JButton("GENERAR HISTOGRAMA");

        btnAplicar.addActionListener(
                e -> aplicarHistograma());

        add(btnAplicar);
    }


    private void aplicarHistograma() {

        BufferedImage histograma =
                service.generarHistograma(
                        chkRed.isSelected(),
                        chkGreen.isSelected(),
                        chkBlue.isSelected());

        visor.setImagen(histograma);
    }
}