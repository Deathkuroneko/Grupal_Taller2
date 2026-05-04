package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

@SuppressWarnings("serial")
public class BotonCircular extends JButton {

    public BotonCircular(String iconPath, String tooltip, ActionListener accion) {
        // Configuramos el icono
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            // Escalamos el icono a un tamaño estándar (ej: 24x24)
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + iconPath);
        }

        setToolTipText(tooltip);
        addActionListener(accion); // <--- Aquí se registra el e -> abrir()
        
        // Estilo del botón
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(45, 45));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Efecto visual al presionar o pasar el mouse
        if (getModel().isArmed()) {
            g2.setColor(new Color(60, 60, 60));
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(50, 50, 50));
        } else {
            g2.setColor(new Color(35, 35, 35));
        }

        g2.fill(new Ellipse2D.Double(0, 0, getWidth() - 1, getHeight() - 1));
        g2.dispose();
        super.paintComponent(g);
    }
}