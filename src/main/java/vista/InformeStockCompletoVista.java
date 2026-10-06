package vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import componentes.Tema;
import controlador.InformeStockCompletoController;

/**
 * Pantalla para generar el informe "Stock Completo": todo el catálogo
 * (activo e inactivo), pensado como planilla para inventario físico. Sin
 * filtros, igual que InformeStockBajoVista.
 */
public class InformeStockCompletoVista extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton btnGenerar;
	private JButton btnCerrar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InformeStockCompletoVista dialog = new InformeStockCompletoVista();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setControlador();
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setControlador() {
		new InformeStockCompletoController(this);
	}

	public InformeStockCompletoVista() {
		setTitle("Informe - Stock Completo");
		setBounds(100, 100, 440, 230);
		getContentPane().setBackground(Tema.FONDO_FORMULARIO);
		getContentPane().setLayout(null);

		try {
			URL url = InformeStockCompletoVista.class.getResource("/imagenes/logo32.png");
			if (url != null)
				setIconImage(new ImageIcon(url).getImage());
		} catch (Exception e) {
			// sin icono, no es crítico
		}

		JPanel panelHeader = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Tema.CARBON);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(Tema.NARANJA);
				g2.fillRect(0, getHeight() - 4, getWidth(), 4);
				g2.dispose();
			}
		};
		panelHeader.setLayout(null);
		panelHeader.setBounds(0, 0, 440, 54);
		getContentPane().add(panelHeader);

		JLabel lblTitulo = new JLabel("Stock Completo");
		lblTitulo.setFont(Tema.FUENTE_TITULO);
		lblTitulo.setForeground(Tema.TEXTO_CLARO);
		lblTitulo.setBounds(20, 8, 320, 34);
		panelHeader.add(lblTitulo);

		JLabel lblAyuda = new JLabel("<html>Genera una planilla con todo el catálogo (activo e<br>"
				+ "inactivo) y su stock actual, con columnas en blanco<br>"
				+ "para completar a mano durante un inventario físico.</html>");
		lblAyuda.setFont(Tema.FUENTE_CAMPO);
		lblAyuda.setForeground(Tema.TEXTO_SECUNDARIO);
		lblAyuda.setBounds(30, 75, 380, 60);
		getContentPane().add(lblAyuda);

		btnGenerar = crearBoton("Generar Informe");
		btnGenerar.setBounds(30, 150, 170, 36);
		getContentPane().add(btnGenerar);

		btnCerrar = crearBoton("Cerrar");
		btnCerrar.setBounds(220, 150, 170, 36);
		getContentPane().add(btnCerrar);
	}

	private JButton crearBoton(String texto) {
		JButton boton = new JButton(texto) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getModel().isPressed() ? Tema.NARANJA_OSCURO
						: getModel().isRollover() ? Tema.NARANJA : Tema.ACERO);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		boton.setFont(Tema.FUENTE_BOTON);
		boton.setForeground(Color.WHITE);
		boton.setHorizontalAlignment(SwingConstants.CENTER);
		boton.setContentAreaFilled(false);
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setOpaque(false);
		boton.setFocusable(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return boton;
	}

	public JButton getBtnGenerar() {
		return btnGenerar;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

}
