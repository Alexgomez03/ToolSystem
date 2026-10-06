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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import componentes.JLabelGenerico;
import componentes.Tema;
import controlador.InformeComprasController;
import util.FechaUtil;

/**
 * Pantalla para generar el informe "Compras por Período". Igual estructura
 * que InformeVentasVista.
 */
public class InformeComprasVista extends JDialog {

	private static final long serialVersionUID = 1L;

	private JFormattedTextField tfDesde;
	private JFormattedTextField tfHasta;
	private JButton btnGenerar;
	private JButton btnCerrar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InformeComprasVista dialog = new InformeComprasVista();
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
		new InformeComprasController(this);
	}

	public InformeComprasVista() {
		setTitle("Informe - Compras por Período");
		setBounds(100, 100, 460, 260);
		getContentPane().setBackground(Tema.FONDO_FORMULARIO);
		getContentPane().setLayout(null);

		try {
			URL url = InformeComprasVista.class.getResource("/imagenes/logo32.png");
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
		panelHeader.setBounds(0, 0, 460, 54);
		getContentPane().add(panelHeader);

		JLabel lblTitulo = new JLabel("Compras por Período");
		lblTitulo.setFont(Tema.FUENTE_TITULO);
		lblTitulo.setForeground(Tema.TEXTO_CLARO);
		lblTitulo.setBounds(20, 8, 400, 34);
		panelHeader.add(lblTitulo);

		JLabelGenerico lblDesde = new JLabelGenerico((String) null);
		lblDesde.setText("Desde:");
		lblDesde.setBounds(30, 80, 80, 25);
		getContentPane().add(lblDesde);

		tfDesde = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfDesde.setBounds(120, 80, 110, 27);
		getContentPane().add(tfDesde);

		JLabelGenerico lblHasta = new JLabelGenerico((String) null);
		lblHasta.setText("Hasta:");
		lblHasta.setBounds(250, 80, 60, 25);
		getContentPane().add(lblHasta);

		tfHasta = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfHasta.setBounds(310, 80, 110, 27);
		getContentPane().add(tfHasta);

		JLabel lblAyuda = new JLabel("Dejá los campos vacíos para incluir todas las compras.");
		lblAyuda.setFont(Tema.FUENTE_CAMPO);
		lblAyuda.setForeground(Tema.TEXTO_SECUNDARIO);
		lblAyuda.setBounds(30, 115, 400, 20);
		getContentPane().add(lblAyuda);

		btnGenerar = crearBoton("Generar Informe");
		btnGenerar.setBounds(30, 165, 180, 36);
		getContentPane().add(btnGenerar);

		btnCerrar = crearBoton("Cerrar");
		btnCerrar.setBounds(240, 165, 180, 36);
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

	public JFormattedTextField getTfDesde() {
		return tfDesde;
	}

	public JFormattedTextField getTfHasta() {
		return tfHasta;
	}

	public JButton getBtnGenerar() {
		return btnGenerar;
	}

	public JButton getBtnCerrar() {
		return btnCerrar;
	}

}
