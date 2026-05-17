package panel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import frame.VisorImagenPanel;
import service.ImagenService;

@SuppressWarnings("serial")
public class PanelSimple extends JPanel {

    private final String tipo;
    private final ImagenService service;
    private final VisorImagenPanel visor;

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
            case "BN"->visor.setImagen(service.aplicarBlancoNegro());
            case "NEG"->visor.setImagen(service.aplicarNegativo());
            case "ESM"->visor.setImagen(service.aplicarVidrioEsmerilado());
            
        }
    }
}