package frame;

import panel.*;
import service.ImagenService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private JLabel lblDigital;

    private ImagenService imagenService = new ImagenService();
    private VisorImagenPanel visor;

    public MainFrame() {
        setTitle("Editor de Fotos - UCE PhotoSystem");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Icono
        try {
            setIconImage(new ImageIcon(getClass().getResource("/icons/camara-de-fotos.png")).getImage());
        } catch (Exception e) {
            System.out.println("Icono no encontrado");
        }

        initTopPanel();
        initCenterPanel();
        initBottomTabs();
    }

    // ===================== TOP =====================
    private void initTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(20, 20, 20));
        top.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel lcd = new JPanel();
        lcd.setBackground(Color.BLACK);
        lcd.setBorder(new LineBorder(new Color(0, 50, 0), 2, true));

        lblDigital = new JLabel(" ESPERANDO IMAGEN ");
        lblDigital.setForeground(Color.GREEN);
        lblDigital.setFont(new Font("DialogInput", Font.BOLD, 16));

        lcd.add(lblDigital);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        acciones.setOpaque(false);

        acciones.add(new BotonCircular("/icons/abrir.png", "Abrir", e -> abrir()));
        acciones.add(new BotonCircular("/icons/save.png", "Guardar", e -> guardar()));
        acciones.add(new BotonCircular("/icons/reset.png", "Reset", e -> reset()));

        top.add(lcd, BorderLayout.WEST);
        top.add(acciones, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
    }

    // ===================== CENTRO =====================
    private void initCenterPanel() {
        visor = new VisorImagenPanel();
        add(visor, BorderLayout.CENTER);
    }

    // ===================== TABS =====================
    private void initBottomTabs() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Retro", new PanelRetro(imagenService, visor));
        tabs.addTab("Bits", new PanelBits(imagenService, visor));
        tabs.addTab("Convoluciones", new PanelConvoluciones(imagenService, visor));
        tabs.addTab("B/N", new PanelSimple(imagenService, visor, "BN"));
        tabs.addTab("Negativo", new PanelSimple(imagenService, visor, "NEG"));
        tabs.addTab("Esmerilado", new PanelSimple(imagenService, visor, "ESM"));
        tabs.addTab("Grises", new PanelGrises(imagenService, visor));
        tabs.addTab("Degradados", new PanelDegradados(imagenService, visor));
        tabs.addTab("HSV", new PanelHSV(imagenService, visor));
        tabs.addTab("Color", new PanelColor(imagenService, visor));

        // Actualiza LCD
        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            String nombre = tabs.getTitleAt(i);
            lblDigital.setText(" " + nombre.toUpperCase() + " ");
        });

        add(tabs, BorderLayout.SOUTH);
    }

    // ===================== ARCHIVOS =====================

    private void abrir() {
        JFileChooser ch = new JFileChooser();
        if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage img = ImageIO.read(ch.getSelectedFile());
                imagenService.cargarImagen(img);
                visor.setImagen(img);
                lblDigital.setText(" IMAGEN CARGADA ");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al abrir imagen");
            }
        }
    }

    private void guardar() {
        if (imagenService.getImagenActual() == null) return;

        JFileChooser ch = new JFileChooser();

        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File archivo = ch.getSelectedFile();

                // agregar extensión 
                if (!archivo.getName().toLowerCase().endsWith(".png")) {
                    archivo = new File(archivo.getAbsolutePath() + ".png");
                }

                imagenService.guardarImagen(archivo, "png");
                lblDigital.setText(" IMAGEN GUARDADA ");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar");
            }
        }
    }

    private void reset() {
        imagenService.restablecer();
        if (imagenService.getImagenOriginal() != null) {
            visor.setImagen(imagenService.getImagenOriginal());
            lblDigital.setText(" SISTEMA RESETEADO ");
        }
    }
}