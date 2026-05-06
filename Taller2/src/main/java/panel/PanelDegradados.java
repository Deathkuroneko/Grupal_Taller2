package panel;

import service.ImagenService;
import javax.swing.*;
import frame.VisorImagenPanel;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelDegradados extends JPanel {

    private ImagenService service;
    private VisorImagenPanel visor;
    private String tipoSeleccionado = "IZQ_DER";

    public PanelDegradados(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 15));
        setBackground(new Color(30, 30, 30));

        String[] tipos = {
            "IZQ_DER", "DER_IZQ", "ARRIBA_ABAJO",
            "ABAJO_ARRIBA", "RADIAL", "RADIAL_INVERSO"
        };

        ButtonGroup grupo = new ButtonGroup();

        for (String t : tipos) {
            JRadioButton rb = new JRadioButton(t.replace("_", " "));
            rb.setForeground(Color.WHITE);
            rb.setBackground(new Color(30, 30, 30));

            if (t.equals(tipoSeleccionado)) rb.setSelected(true);

            rb.addActionListener(e -> tipoSeleccionado = t);

            grupo.add(rb);
            add(rb);
        }

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(btnAplicar);
    }

    private void aplicarFiltro() {
        switch (tipoSeleccionado) {
            case "IZQ_DER": visor.setImagen(service.aplicarDegradadoIzqDer()); break;
            case "DER_IZQ": visor.setImagen(service.aplicarDegradadoDerIzq()); break;
            case "ARRIBA_ABAJO": visor.setImagen(service.aplicarDegradadoArribaAbajo()); break;
            case "ABAJO_ARRIBA": visor.setImagen(service.aplicarDegradadoAbajoArriba()); break;
            case "RADIAL": visor.setImagen(service.aplicarDegradadoRadial()); break;
            case "RADIAL_INVERSO": visor.setImagen(service.aplicarDegradadoRadialInverso()); break;
        }
    }
}