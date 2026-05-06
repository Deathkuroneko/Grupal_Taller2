package frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.net.URL;

@SuppressWarnings("serial")
public class BotonCircular extends JButton {

    private Shape shape; // cache de forma para mejor rendimiento

    public BotonCircular(String iconPath, String tooltip, ActionListener accion) {

        cargarIcono(iconPath);

        setToolTipText(tooltip);
        addActionListener(accion);

        // Estilo limpio
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false); // importante para evitar fondos raros

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(45, 45));
    }

    private void cargarIcono(String iconPath) {
        try {
            URL url = getClass().getResource(iconPath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(img));
            } else {
                System.err.println("Icono no encontrado: " + iconPath);
            }
        } catch (Exception e) {
            System.err.println("Error cargando icono: " + iconPath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // Color según estado
        if (getModel().isPressed()) {
            g2.setColor(new Color(70, 70, 70));
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(55, 55, 55));
        } else {
            g2.setColor(new Color(40, 40, 40));
        }

        g2.fill(getShape());
        g2.dispose();

        super.paintComponent(g);
    }

    // Mejora: reutiliza la forma en vez de crearla siempre
    private Shape getShape() {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth() - 1, getHeight() - 1);
        }
        return shape;
    }

    // Esto hace que el click SOLO funcione dentro del círculo (UX PRO)
    @Override
    public boolean contains(int x, int y) {
        return getShape().contains(x, y);
    }
}