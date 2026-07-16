package panel;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import frame.VisorImagenPanel;
import service.ImagenService;


@SuppressWarnings("serial")
public class PanelBufferAcumulacion extends JPanel {


    private ImagenService servicio;
    private VisorImagenPanel visor;


    private JSpinner spinnerMuestras;
    private JSpinner spinnerDesplazamiento;


    private JRadioButton rbDia;
    private JRadioButton rbTarde;
    private JRadioButton rbNoche;
    private JRadioButton rbOscuro;



    public PanelBufferAcumulacion(ImagenService servicio, VisorImagenPanel visor){
        this.servicio = servicio;
        this.visor = visor;
        construirPanel();
    }

    private void construirPanel(){
        setLayout(new FlowLayout());
        add(new JLabel("Muestras:"));

        spinnerMuestras = new JSpinner( new SpinnerNumberModel( 25, 1, 100, 1));

        add(spinnerMuestras);
        add(new JLabel("Desplazamiento:"));
        spinnerDesplazamiento = new JSpinner( new SpinnerNumberModel(8, 1, 50, 1));
        add(spinnerDesplazamiento);

        JButton btnEstela =new JButton("Aplicar Estela");

        add(btnEstela);
        add(new JLabel("Día → Noche"));

        rbDia = new JRadioButton( "Día", true);
        rbTarde = new JRadioButton( "Tarde");
        rbNoche = new JRadioButton("Noche");
        rbOscuro = new JRadioButton( "Oscuro");

        ButtonGroup grupo = new ButtonGroup();

        grupo.add(rbDia);
        grupo.add(rbTarde);
        grupo.add(rbNoche);
        grupo.add(rbOscuro);

        add(rbDia);
        add(rbTarde);
        add(rbNoche);
        add(rbOscuro);

        JButton btnNoche = new JButton( "Aplicar Día/Noche");

        add(btnNoche);

        btnEstela.addActionListener(e->{
            int muestras = (int)spinnerMuestras.getValue();
            int desplazamiento = (int)spinnerDesplazamiento.getValue();
            BufferedImage resultado = servicio.aplicarBufferAcumulacion( muestras, desplazamiento);

            if(resultado!=null){
                visor.setImagen(resultado);
            }
        });

        btnNoche.addActionListener(e->{
            float factor = 1.0f;

            if(rbTarde.isSelected())factor=0.75f;
            if(rbNoche.isSelected())factor=0.50f;
            if(rbOscuro.isSelected()) factor=0.25f;


            BufferedImage resultado = servicio.aplicarDiaNoche(factor);

            if(resultado!=null){ visor.setImagen(resultado);

            }
       });

    }
}