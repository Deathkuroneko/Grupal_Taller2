package panel;

import service.ImagenService;
import javax.swing.*;
import frame.VisorImagenPanel;
import java.awt.*;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class PanelOperacionesFragmentos extends JPanel {

    private final ImagenService service;
    private final VisorImagenPanel visor;

    private final JComboBox<String> selectorEfecto;
    private final JSlider sliderPrincipal;
    private final JLabel lblValorPrincipal;
    private final JLabel lblParametro;

    // Controles para Efecto Estela (se muestran/ocultan)
    private final JLabel lblMuestras;
    private final JSlider sliderMuestras;
    private final JLabel lblValorMuestras;
    private final JLabel lblDesplazamiento;
    private final JSlider sliderDesplazamiento;
    private final JLabel lblValorDesplazamiento;

    private final JButton btnAplicar;

    public PanelOperacionesFragmentos(ImagenService service, VisorImagenPanel visor) {
        this.service = service;
        this.visor = visor;


        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        setBackground(new Color(30, 30, 30));

        JLabel lblEfecto = new JLabel("EFECTO:");
        lblEfecto.setForeground(Color.WHITE);
        add(lblEfecto);

        selectorEfecto = new JComboBox<>(new String[]{
                "Z-Test",
                "Alpha Test",
                "Alpha To Coverage",
                "Efecto Estela",
                "Día a Noche"
        });
        add(selectorEfecto);

        lblParametro = new JLabel("Depth Ref:");
        lblParametro.setForeground(Color.WHITE);
        lblParametro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        add(lblParametro);

        lblValorPrincipal = new JLabel("128");
        lblValorPrincipal.setForeground(new Color(0, 255, 120));
        lblValorPrincipal.setFont(new Font("Consolas", Font.BOLD, 16));
        add(lblValorPrincipal);

        sliderPrincipal = new JSlider(0, 255, 128);
        sliderPrincipal.setPreferredSize(new Dimension(280, 55));
        sliderPrincipal.setBackground(new Color(30, 30, 30));
        sliderPrincipal.setForeground(new Color(0, 200, 255));

        sliderPrincipal.setMajorTickSpacing(64);
        sliderPrincipal.setMinorTickSpacing(16);
        sliderPrincipal.setPaintTicks(true);
        sliderPrincipal.setPaintLabels(true);

        Hashtable<Integer, JLabel> tabla = new Hashtable<>();
        tabla.put(0, new JLabel("0"));
        tabla.put(64, new JLabel("64"));
        tabla.put(128, new JLabel("128"));
        tabla.put(192, new JLabel("192"));
        tabla.put(255, new JLabel("255"));
        for (JLabel l : tabla.values()) {
            l.setForeground(Color.WHITE);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        }
        sliderPrincipal.setLabelTable(tabla);

        sliderPrincipal.addChangeListener(e ->
                lblValorPrincipal.setText(String.valueOf(sliderPrincipal.getValue()))
        );
        add(sliderPrincipal);

        // --- Controles para Efecto Estela (ocultos por defecto) ---
        // Muestras
        lblMuestras = new JLabel("Muestras:");
        lblMuestras.setForeground(Color.WHITE);
        lblMuestras.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMuestras.setVisible(false);
        add(lblMuestras);

        lblValorMuestras = new JLabel("10");
        lblValorMuestras.setForeground(new Color(0, 255, 120));
        lblValorMuestras.setFont(new Font("Consolas", Font.BOLD, 16));
        lblValorMuestras.setVisible(false);
        add(lblValorMuestras);

        sliderMuestras = new JSlider(1, 20, 10);
        sliderMuestras.setPreferredSize(new Dimension(120, 55));
        sliderMuestras.setBackground(new Color(30, 30, 30));
        sliderMuestras.setForeground(new Color(0, 200, 255));
        sliderMuestras.setMajorTickSpacing(5);
        sliderMuestras.setMinorTickSpacing(1);
        sliderMuestras.setPaintTicks(true);
        sliderMuestras.setPaintLabels(true);
        Hashtable<Integer, JLabel> tablaM = new Hashtable<>();
        tablaM.put(1, new JLabel("1"));
        tablaM.put(5, new JLabel("5"));
        tablaM.put(10, new JLabel("10"));
        tablaM.put(15, new JLabel("15"));
        tablaM.put(20, new JLabel("20"));
        for (JLabel l : tablaM.values()) {
            l.setForeground(Color.WHITE);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        }
        sliderMuestras.setLabelTable(tablaM);
        sliderMuestras.addChangeListener(e ->
                lblValorMuestras.setText(String.valueOf(sliderMuestras.getValue()))
        );
        sliderMuestras.setVisible(false);
        add(sliderMuestras);

        lblDesplazamiento = new JLabel("Desplaz.:");
        lblDesplazamiento.setForeground(Color.WHITE);
        lblDesplazamiento.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesplazamiento.setVisible(false);
        add(lblDesplazamiento);

        lblValorDesplazamiento = new JLabel("5");
        lblValorDesplazamiento.setForeground(new Color(0, 255, 120));
        lblValorDesplazamiento.setFont(new Font("Consolas", Font.BOLD, 16));
        lblValorDesplazamiento.setVisible(false);
        add(lblValorDesplazamiento);

        sliderDesplazamiento = new JSlider(1, 50, 5);
        sliderDesplazamiento.setPreferredSize(new Dimension(120, 55));
        sliderDesplazamiento.setBackground(new Color(30, 30, 30));
        sliderDesplazamiento.setForeground(new Color(0, 200, 255));
        sliderDesplazamiento.setMajorTickSpacing(10);
        sliderDesplazamiento.setMinorTickSpacing(2);
        sliderDesplazamiento.setPaintTicks(true);
        sliderDesplazamiento.setPaintLabels(true);
        Hashtable<Integer, JLabel> tablaD = new Hashtable<>();
        tablaD.put(1, new JLabel("1"));
        tablaD.put(10, new JLabel("10"));
        tablaD.put(20, new JLabel("20"));
        tablaD.put(30, new JLabel("30"));
        tablaD.put(50, new JLabel("50"));
        for (JLabel l : tablaD.values()) {
            l.setForeground(Color.WHITE);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        }
        sliderDesplazamiento.setLabelTable(tablaD);
        sliderDesplazamiento.addChangeListener(e ->
                lblValorDesplazamiento.setText(String.valueOf(sliderDesplazamiento.getValue()))
        );
        sliderDesplazamiento.setVisible(false);
        add(sliderDesplazamiento);

        btnAplicar = new JButton("APLICAR FILTRO");
        btnAplicar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAplicar.addActionListener(e -> aplicarFiltro());
        add(btnAplicar);

        selectorEfecto.addActionListener(e -> {
            String efecto = (String) selectorEfecto.getSelectedItem();
            boolean esEstela = efecto.equals("Efecto Estela");

            // Mostrar/ocultar controles de estela
            lblMuestras.setVisible(esEstela);
            lblValorMuestras.setVisible(esEstela);
            sliderMuestras.setVisible(esEstela);
            lblDesplazamiento.setVisible(esEstela);
            lblValorDesplazamiento.setVisible(esEstela);
            sliderDesplazamiento.setVisible(esEstela);

            lblParametro.setVisible(!esEstela);
            lblValorPrincipal.setVisible(!esEstela);
            sliderPrincipal.setVisible(!esEstela);

            if (!esEstela) {
                switch (efecto) {
                    case "Alpha Test":
                        lblParametro.setText("Alpha Ref:");
                        break;
                    case "Alpha To Coverage":
                        lblParametro.setText("Cobertura:");
                        break;
                    case "Día a Noche":
                        lblParametro.setText("Factor (0-255):");
                        break;
                    default:
                        lblParametro.setText("Depth Ref:");
                        break;
                      
                }
            }

            revalidate();
            repaint();
        });

        selectorEfecto.setSelectedIndex(0);
    }

    private void aplicarFiltro() {
        String efecto = (String) selectorEfecto.getSelectedItem();

        if (efecto.equals("Efecto Estela")) {
            int muestras = sliderMuestras.getValue();
            int desplazamiento = sliderDesplazamiento.getValue();
            visor.setImagen(service.aplicarFiltroFragmento(efecto, muestras, desplazamiento));
        } else {
            int valor = sliderPrincipal.getValue();
            visor.setImagen(service.aplicarFiltroFragmento(efecto, valor));
        }
    }
}