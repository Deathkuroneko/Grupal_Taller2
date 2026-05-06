package main;

import com.formdev.flatlaf.FlatDarkLaf;

import frame.MainFrame;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;


public class App {
    public static void main(String[] args) {
        try {
            // usa la implementación de FlatLaf
            UIManager.setLookAndFeel(new FlatDarkLaf());
            
            //Bordes redondeados para que se vea más moderno
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            
        } catch (Exception ex) {
            System.err.println("Error al inicializar FlatLaf: " + ex.getMessage());
        }

        // Inicia el Frame
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}