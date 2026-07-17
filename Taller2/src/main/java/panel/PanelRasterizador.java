package panel;

import frame.VisorImagenPanel;
import service.ImagenService;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class PanelRasterizador extends JPanel {

    private final ImagenService service;
    private final VisorImagenPanel visor;
    private final JComboBox<String> selectorModo;
    private final JButton btnAplicar;

    public PanelRasterizador(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 12));
        setBackground(new Color(30, 30, 30));

        // Etiqueta de estilo moderno
        JLabel lblModo = new JLabel("SELECCIONAR MODO:");
        lblModo.setForeground(Color.WHITE);
        lblModo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(lblModo);

        // Selector con el mismo estilo del otro panel
        selectorModo = new JComboBox<>(new String[]{
                "1 - Textura",
                "2 - Color",
                "3 - Profundidad",
                "4 - W-Buffer",
                "5 - Completo"
        });
        selectorModo.setPreferredSize(new Dimension(250, 30));
        add(selectorModo);

        // Botón de acción con estilo consistente
        btnAplicar = new JButton("RENDERIZAR");
        btnAplicar.setPreferredSize(new Dimension(160, 30));
        btnAplicar.setForeground(Color.WHITE);
        btnAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicar.addActionListener(e -> aplicarRasterizador());
        add(btnAplicar);
    }

    private void aplicarRasterizador() {
        int modo = selectorModo.getSelectedIndex() + 1;

        BufferedImage resultado = service.aplicarRasterizador(modo, visor.getWidth(), visor.getHeight());

        visor.setImagen(resultado);
        visor.repaint();
    }
}