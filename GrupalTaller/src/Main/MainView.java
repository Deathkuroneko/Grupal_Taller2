package Main;

import ui.TopPanel;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import service.ImagenService;
import ui.ControlsPanel;
import ui.DegradadosPanel;
import ui.ImagePanel;

public class MainView extends JFrame {

    private ImagenService service;
    private ImagePanel visor;

    public MainView() {

        // 1. Configuración de ventana
        setTitle("Editor de Fotos - Filtros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. Inicializar lógica
        service = new ImagenService();
        visor = new ImagePanel();

        // 🔵 PANEL SUPERIOR (solo una vez)
        TopPanel top = new TopPanel(service, this);
        add(top, BorderLayout.NORTH);

        // 3. Crear pestañas
        JTabbedPane tabs = new JTabbedPane();

        // 🟢 Retro
        ControlsPanel retroPanel = new ControlsPanel(service, this);
        tabs.addTab("Retro", retroPanel);

        // 🔵 Degradados
        DegradadosPanel degradadoPanel = new DegradadosPanel(service, this);
        tabs.addTab("Degradados", degradadoPanel);

        // 4. Layout
        add(new JScrollPane(visor), BorderLayout.CENTER);
        add(tabs, BorderLayout.WEST);
    }

    public ImagePanel getImagePanel() {
        return visor;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainView frame = new MainView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}