package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

import Main.MainView;
import service.ImagenService;

public class DegradadosPanel extends JPanel {

    public DegradadosPanel(ImagenService service, MainView view) {
        // 1. Configuración del Panel Principal
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        // Dejamos que el ancho lo maneje el sidebar de la MainView

        // 2. Contenedor Colapsable
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- BOTONES DE DEGRADADO ---
        JButton btnIzqDer = crearBotonFiltro("Izquierda → Derecha");
        JButton btnDerIzq = crearBotonFiltro("Derecha → Izquierda");
        JButton btnRadial = crearBotonFiltro("Radial");
        JButton btnRadialInv = crearBotonFiltro("Radial Invertido");
        JButton btnArrAbj = crearBotonFiltro("Arriba → Abajo");
        JButton btnAbjArr = crearBotonFiltro("Abajo → Arriba");

        // Añadimos los botones con espacio entre ellos
        content.add(btnIzqDer);
        content.add(Box.createVerticalStrut(8));
        content.add(btnDerIzq);
        content.add(Box.createVerticalStrut(8));
        content.add(btnRadial);
        content.add(Box.createVerticalStrut(8));
        content.add(btnRadialInv);
        content.add(Box.createVerticalStrut(8));
        content.add(btnArrAbj);
        content.add(Box.createVerticalStrut(8));
        content.add(btnAbjArr);

        // 3. Botón de Cabecera (Acordeón)
        JButton btnHeader = new JButton("▼ DEGRADADOS");
        btnHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnHeader.setBorderPainted(false);
        btnHeader.setFocusPainted(false);
        btnHeader.setContentAreaFilled(false);
        btnHeader.setFont(new Font("SansSerif", Font.BOLD, 12));

        this.add(btnHeader);
        this.add(content);

        // =========================
        // EVENTOS
        // =========================

        // Lógica para colapsar/expandir
        btnHeader.addActionListener(e -> {
            boolean visible = content.isVisible();
            content.setVisible(!visible);
            btnHeader.setText(visible ? "► DEGRADADOS" : "▼ DEGRADADOS");
            this.revalidate();
        });

        // Eventos de Filtros
        btnIzqDer.addActionListener(e -> aplicarYMostrar(service.aplicarDegradadoDerIzq(), view));
        btnDerIzq.addActionListener(e -> aplicarYMostrar(service.aplicarDegradadoDerIzq(), view));
        btnRadial.addActionListener(e -> aplicarYMostrar(service.aplicarDegradadoRadial(), view));
        
        // Nota: Asegúrate de tener los métodos correspondientes en tu ImagenService para estos:
        // btnRadialInv.addActionListener(e -> aplicarYMostrar(service.aplicarDegradadoRadialInv(), view));
        // btnArrAbj.addActionListener(e -> aplicarYMostrar(service.aplicarDegradadoArrAbj(), view));
        // btnAbjArr.addActionListener(e -> aplicarYMostrar(service.aplicarDegradadoAbjArr(), view));
    }

    /**
     * Método auxiliar para crear botones con estilo uniforme
     */
    private JButton crearBotonFiltro(String texto) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Estilo moderno de FlatLaf
        btn.putClientProperty("JButton.buttonType", "textured"); 
        return btn;
    }

    /**
     * Método auxiliar para actualizar la vista
     */
    private void aplicarYMostrar(BufferedImage img, MainView view) {
        if (img != null) {
            view.getImagePanel().setImagen(img);
        } else {
            JOptionPane.showMessageDialog(this, "Primero carga una imagen.");
        }
    }
}