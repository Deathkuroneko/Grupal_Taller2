package panel;

import service.ImagenService;
import javax.swing.*;

import frame.VisorImagenPanel;
import java.awt.*;

@SuppressWarnings("serial")
public class PanelSimple extends JPanel {

    private String tipo;
    private ImagenService service;
    private VisorImagenPanel visor;

    public PanelSimple(ImagenService service, VisorImagenPanel visor, String tipo) {
        this.service = service;
        this.visor = visor;
        this.tipo = tipo;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        JLabel info = new JLabel("EFECTO DE PASO ÚNICO");
        info.setForeground(Color.GRAY);
        info.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(info, BorderLayout.CENTER);
        add(btnAplicar, BorderLayout.SOUTH);
    }

    private void aplicarFiltro() {
        switch (tipo) {
            case "BN":
                visor.setImagen(service.aplicarBlancoNegro());
                break;
            case "NEG":
                visor.setImagen(service.aplicarNegativo());
                break;
            case "ESM":
                visor.setImagen(service.aplicarVidrioEsmerilado());
                break;
        }
    }
}