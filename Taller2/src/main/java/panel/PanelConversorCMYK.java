package panel;

import service.ImagenService;
import frame.VisorImagenPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class PanelConversorCMYK extends JPanel {

	private final ImagenService service;
	private final VisorImagenPanel visor;

	private JButton btnAplicar;
	private JButton btnRestaurar;
	private JButton btnTabla;

	public PanelConversorCMYK(ImagenService service, VisorImagenPanel visor) {

		this.service = service;
		this.visor = visor;

		setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
		setBackground(new Color(30, 30, 30));

		JLabel lbl = new JLabel("Conversión de color RGB → CMYK");
		lbl.setForeground(Color.WHITE);
		lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
		add(lbl);

		btnAplicar = new JButton("Aplicar Conversión");
		btnAplicar.setBackground(new Color(50, 120, 220));
		btnAplicar.setForeground(Color.WHITE);
		btnAplicar.setFocusPainted(false);
		btnAplicar.addActionListener(e -> aplicarConversion());
		add(btnAplicar);

		btnTabla = new JButton("Ver Tabla RGB → CMYK");
		btnTabla.setBackground(new Color(80, 170, 80));
		btnTabla.setForeground(Color.WHITE);
		btnTabla.setFocusPainted(false);
		btnTabla.addActionListener(e -> mostrarTabla());
		add(btnTabla);

		btnRestaurar = new JButton("Restaurar Original");
		btnRestaurar.setBackground(new Color(100, 100, 100));
		btnRestaurar.setForeground(Color.WHITE);
		btnRestaurar.setFocusPainted(false);
		btnRestaurar.addActionListener(e -> {

			if (service.getImagenOriginal() != null) {
				visor.setImagen(service.getImagenOriginal());
			}

		});

		add(btnRestaurar);
	}

	private void aplicarConversion() {

		if (visor.getImagenActual() == null) {
			JOptionPane.showMessageDialog(this, "Cargue una imagen primero.", "Sin imagen",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		BufferedImage resultado = service.convertirACmyk();

		if (resultado != null) {
			visor.setImagen(resultado);
		} else {
			JOptionPane.showMessageDialog(this, "No fue posible realizar la conversión.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void mostrarTabla() {
		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Conversión RGB → CMYK", true);
		dialog.setSize(700, 350);
		dialog.setLocationRelativeTo(this);
		String[] columnas = { "Color", "R", "G", "B", "C", "M", "Y", "K" };

		DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

		int[][] colores = { { 255, 0, 0 }, { 0, 255, 0 }, { 0, 0, 255 }, { 255, 255, 0 }, { 0, 255, 255 },
				{ 255, 0, 255 }, { 255, 255, 255 }, { 0, 0, 0 }, { 128, 128, 128 }, { 200, 120, 50 } };

		for (int[] rgb : colores) {

			int r = rgb[0];
			int g = rgb[1];
			int b = rgb[2];

			float rf = r / 255f;
			float gf = g / 255f;
			float bf = b / 255f;

			float k = 1 - Math.max(rf, Math.max(gf, bf));

			float c = 0;
			float m = 0;
			float y = 0;

			if (k < 1.0f) {
				c = (1 - rf - k) / (1 - k);
				m = (1 - gf - k) / (1 - k);
				y = (1 - bf - k) / (1 - k);
			}

			modelo.addRow(
					new Object[] { "", r, g, b, (int) (c * 100), (int) (m * 100), (int) (y * 100), (int) (k * 100) });
		}

		JTable tabla = new JTable(modelo) {

			@Override
			public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {

				Component c = super.prepareRenderer(renderer, row, column);

				if (column == 0) {

					int r = (Integer) getValueAt(row, 1);
					int g = (Integer) getValueAt(row, 2);
					int b = (Integer) getValueAt(row, 3);

					Color fondo = new Color(r, g, b);
					c.setBackground(fondo);

					int brillo = (r * 299 + g * 587 + b * 114) / 1000;
					c.setForeground(brillo < 128 ? Color.WHITE : Color.BLACK);

				} else {

					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				}

				return c;
			}
		};

		tabla.setRowHeight(28);
		JScrollPane scroll = new JScrollPane(tabla);
		JLabel info = new JLabel("Ejemplos de conversión RGB → CMYK (porcentaje)", SwingConstants.CENTER);

		info.setFont(new Font("Segoe UI", Font.BOLD, 14));
		dialog.setLayout(new BorderLayout());
		dialog.add(info, BorderLayout.NORTH);
		dialog.add(scroll, BorderLayout.CENTER);
		dialog.setVisible(true);
	}
}