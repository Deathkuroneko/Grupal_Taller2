package panel;

import filtros.Kernels;
import frame.VisorImagenPanel;
import service.ImagenService;

import javax.swing.*;
import java.awt.*;


@SuppressWarnings("serial")
public class PanelConvoluciones extends JPanel {

    private ImagenService service;
    private VisorImagenPanel visor;

    private float[] kernelActual = Kernels.kNormal;
    private String nombreKernelActual = "NORMAL";
    private JSlider slider;

    public PanelConvoluciones(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(new Color(30, 30, 30));

        ButtonGroup grupo = new ButtonGroup();

        add(crearRadio("ENFOQUE", Kernels.kEnfoque, grupo));
        add(crearRadio("DESENFOQUE", Kernels.kDesenfoque, grupo));
        add(crearRadio("BORDES", Kernels.kBordes, grupo));
        add(crearRadio("NORMAL VAR", null, grupo));
        add(crearRadio("ACLARAR", Kernels.kAclaracion, grupo));
        add(crearRadio("OSCURECER", Kernels.kOscurecer, grupo));
        add(crearRadio("BLUR 9x9", Kernels.kDesenfoque9, grupo));

        // 🔥 Slider propio
        slider = new JSlider(1, 10, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        add(slider);

        JButton btnAplicar = new JButton("APLICAR");
        btnAplicar.addActionListener(e -> aplicarFiltro());

        add(btnAplicar);
    }

    private JRadioButton crearRadio(String nombre, float[] matriz, ButtonGroup grupo) {
        JRadioButton rb = new JRadioButton(nombre);
        rb.setForeground(Color.WHITE);
        rb.setBackground(new Color(30, 30, 30));

        rb.addActionListener(e -> {
            kernelActual = matriz;
            nombreKernelActual = nombre;
        });

        grupo.add(rb);
        return rb;
    }

    private void aplicarFiltro() {
        if ("NORMAL VAR".equals(nombreKernelActual)) {
            visor.setImagen(service.aplicarKernelPersonalizado(slider.getValue() / 10.0f));
        } else {
            visor.setImagen(service.aplicarEfectoConvolucion(kernelActual, slider.getValue()));
        }
    }
}