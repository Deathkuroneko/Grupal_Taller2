package panel;

import service.ImagenService;
import javax.swing.*;
import frame.VisorImagenPanel;
import java.awt.*;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class PanelGrises extends JPanel {

    private ImagenService service;
    private VisorImagenPanel visor;
    private JSlider slider;
    private JLabel lblValor;

    public PanelGrises(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        setBackground(new Color(30, 30, 30));

        JLabel titulo = new JLabel("NIVELES DE GRIS:");
        titulo.setForeground(Color.WHITE);
        add(titulo);

        // Slider de 1 a 8
        slider = new JSlider(1, 8, 8); 
        slider.setBackground(new Color(30, 30, 30));
        slider.setForeground(Color.WHITE);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        // Etiquetas 2, 4, 8... 256
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 1; i <= 8; i++) {
            JLabel label = new JLabel(String.valueOf((int) Math.pow(2, i)));
            label.setForeground(Color.GRAY);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            labelTable.put(i, label);
        }
        slider.setLabelTable(labelTable);

        add(slider);

        JButton btnAplicar = new JButton("APLICAR FILTRO");
        btnAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(btnAplicar);
    }

    private void aplicarFiltro() {
        // Convertimos la posición del slider (1-8)
        int niveles = (int) Math.pow(2, slider.getValue());
        visor.setImagen(service.aplicarEscalaGrises(niveles));
    }
}