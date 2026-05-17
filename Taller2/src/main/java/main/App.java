package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import frame.MainFrame;


public class App {
    public static void main(String[] args) throws Exception {
        try {
            // usa la implementación de FlatLaf
            UIManager.setLookAndFeel(new FlatDarkLaf());
            
            //Bordes redondeados para que se vea más moderno
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            
        } catch (IllegalArgumentException ex) {
            System.err.println("Error al inicializar FlatLaf: " + ex.getMessage());
        }

        // Inicia el Frame
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}