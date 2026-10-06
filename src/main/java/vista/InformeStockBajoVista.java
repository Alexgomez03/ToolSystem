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
import controlador.InformeStockBajoController;

/**
 * Pantalla para generar el informe "Stock Bajo": no necesita filtros, así
 * que solo tiene un botón para generarlo y otro para cerrar.
 */
public class InformeStockBajoVista extends JDialog {

	private static final long serialVersionUID = 1L;

	private JButton btnGenerar;
	private JButton btnCerrar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InformeStockBajoVista dialog = new InformeStockBajoVista();
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
		new InformeStockBajoController(this);
	}

	public InformeStockBajoVista() {
		setTitle("Informe - Stock Bajo");
		setBounds(100, 100, 420, 220);
		getContentPane().setBackground(Tema.FONDO_FORMULARIO);
		getContentPane().setLayout(null);

		try {
			URL url = InformeStockBajoVista.class.getResource("/imagenes/logo32.png");
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
		panelHeader.setBounds(0, 0, 420, 54);
		getContentPane().add(panelHeader);

		JLabel lblTitulo = new JLabel("Stock Bajo");
		lblTitulo.setFont(Tema.FUENTE_TITULO);
		lblTitulo.setForeground(Tema.TEXTO_CLARO);
		lblTitulo.setBounds(20, 8, 300, 34);
		panelHeader.add(lblTitulo);

		JLabel lblAyuda = new JLabel("<html>Genera un listado de todos los productos activos<br>"
				+ "cuyo stock actual llegó a su stock mínimo definido.</html>");
		lblAyuda.setFont(Tema.FUENTE_CAMPO);
		lblAyuda.setForeground(Tema.TEXTO_SECUNDARIO);
		lblAyuda.setBounds(30, 75, 360, 40);
		getContentPane().add(lblAyuda);

		btnGenerar = crearBoton("Generar Informe");
		btnGenerar.setBounds(30, 135, 170, 36);
		getContentPane().add(btnGenerar);

		btnCerrar = crearBoton("Cerrar");
		btnCerrar.setBounds(220, 135, 170, 36);
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
