package panel;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import filtros.ColorMatriz;
import frame.VisorImagenPanel;
import service.ImagenService;

@SuppressWarnings("serial")
public class PanelMatrizColor extends JPanel {

    private final ImagenService service;
    private final VisorImagenPanel visor;

    private float[][] colorActual = ColorMatriz.filtroSepia;
    private String nombreActual = "Filtro Sepia";

    public PanelMatrizColor(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(30, 30, 30));

        ButtonGroup grupo = new ButtonGroup();

        add(crearRadio("BRILLO 30%", ColorMatriz.filtroAumentarBrillo, grupo));
        add(crearRadio("FILTRO GRIS", ColorMatriz.filtroGris, grupo));
        add(crearRadio("FILTRO VERDE", ColorMatriz.filtroVerdeNaturaleza, grupo));
        add(crearRadio("FILTRO HIELO", ColorMatriz.filtroHielo, grupo));

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(btnAplicar);
    }

    private JRadioButton crearRadio(String nombre, float[][] matriz, ButtonGroup grupo) {
        JRadioButton rb = new JRadioButton(nombre);
        rb.setForeground(Color.WHITE);
        rb.setBackground(new Color(30, 30, 30));

        rb.addActionListener(e -> {
            colorActual = matriz;
            nombreActual = nombre;
        });

        grupo.add(rb);
        return rb;
    }

    private void aplicarFiltro() {
        visor.setImagen(service.aplicarEfectoMatrizColor(colorActual));

    }
}
