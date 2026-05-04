package frame;
import javax.swing.JSlider;
import javax.swing.JLabel;

public interface FiltroConfigurable {
    void configurarSlider(JSlider slider, JLabel lblIntensidad);
    void aplicarFiltro(int valorSlider);
    String getNombreFiltro();
}