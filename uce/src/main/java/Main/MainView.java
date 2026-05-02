package Main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.formdev.flatlaf.FlatDarkLaf;

import service.ImagenService;
import ui.TopPanel;
import ui.RetroPanel;
import ui.DegradadosPanel;
import ui.VidrioPanel;
import ui.BlancoNegroPanel;
import ui.EscalaGrisesNPanel;
import ui.ImagePanel;

public class MainView extends JFrame {

    private ImagenService service;
    private ImagePanel visor;

    public MainView() {
        // 1. Configuración de ventana (Estilo Cámara Oscura)
        setTitle("Lumix Edit - Taller 2 UCE"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. Inicializar lógica
        service = new ImagenService();
        visor = new ImagePanel();

        // 🔵 PANEL SUPERIOR (Archivo y Reset)
        TopPanel top = new TopPanel(service, this);
        add(top, BorderLayout.NORTH);

        // 🛠️ PANEL LATERAL DE FILTROS (Estilo Acordeón)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 🟢 Sección: Efectos Artísticos
        RetroPanel retroPanel = new RetroPanel(service, this);
        retroPanel.setBorder(new TitledBorder("Retro & Vintage"));
        sidebar.add(retroPanel);
        sidebar.add(Box.createVerticalStrut(15)); // Espaciado

        // 🔵 Sección: Gradientes
        DegradadosPanel degradadoPanel = new DegradadosPanel(service, this);
        degradadoPanel.setBorder(new TitledBorder("Degradados"));
        sidebar.add(degradadoPanel);
        sidebar.add(Box.createVerticalStrut(15));

        // 🔴 Aquí irán tus 11 filtros nuevos agrupados en otros paneles...
        // Ejemplo: sidebar.add(new FiltrosColorPanel(service, this));

        VidrioPanel vidrioPanel = new VidrioPanel(service, this);
        vidrioPanel.setBorder(new TitledBorder("Texturas"));
        sidebar.add(vidrioPanel);
        sidebar.add(Box.createVerticalStrut(15));

        BlancoNegroPanel bnPanel = new BlancoNegroPanel(service, this);
        bnPanel.setBorder(new TitledBorder("Básico"));
        sidebar.add(bnPanel);
        sidebar.add(Box.createVerticalStrut(15));

        EscalaGrisesNPanel grisesPanel = new EscalaGrisesNPanel(service, this);
        grisesPanel.setBorder(new TitledBorder("Escala de Grises"));
        sidebar.add(grisesPanel);
        sidebar.add(Box.createVerticalStrut(15));

        // 3. Scroll para el menú lateral (Evita que los 11 filtros se corten)
        JScrollPane scrollFiltros = new JScrollPane(sidebar);
        scrollFiltros.setPreferredSize(new Dimension(300, 0));
        scrollFiltros.getVerticalScrollBar().setUnitIncrement(16); // Scroll suave
        scrollFiltros.setBorder(null);
        add(scrollFiltros, BorderLayout.WEST);

        // 4. Visor Central
        JScrollPane scrollVisor = new JScrollPane(visor);
        scrollVisor.setBackground(new Color(30, 30, 30)); // Fondo neutro para la foto
        add(scrollVisor, BorderLayout.CENTER);
    }

    public ImagePanel getImagePanel() {
        return visor;
    }

    public static void main(String[] args) {
        try {
            // FlatDarkLaf es clave para la apariencia de cámara
            FlatDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

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