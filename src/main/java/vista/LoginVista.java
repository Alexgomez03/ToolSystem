package vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import componentes.JLabelGenerico;
import componentes.JPanelPersonalizado;
import componentes.JtextFieldGenerico;
import componentes.Tema;
import controlador.LoginController;


 // Pantalla de inicio de sesión. Es la primera ventana que se muestra al
 // arrancar la aplicación (ver {@code PantallaPrincipalVista.main}); recién
 // si el login es correcto se abre la pantalla principal.
 
public class LoginVista extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanelPersonalizado panelFondo = new JPanelPersonalizado("fondo_pantalla.jpg");
	private JtextFieldGenerico tfUsuario;
	private JPasswordField tfPassword;
	private JButton btnIngresar;
	private JLabel lblMensaje;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginVista frame = new LoginVista();
					frame.setControlador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setControlador() {
		new LoginController(this);
	}

	public LoginVista() {
		setTitle("ToolSystem - Iniciar Sesión");
		setBounds(100, 100, 1920, 1080);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		try {
			URL url = LoginVista.class.getResource("/imagenes/logo32.png");
			if (url != null)
				setIconImage(new ImageIcon(url).getImage());
		} catch (Exception e) {
			// sin icono, no es crítico
		}

		setContentPane(panelFondo);
		panelFondo.setLayout(null);

		// ---------- Tarjeta central ----------
		int anchoTarjeta = 420;
		int altoTarjeta = 380;
		int x = (1920 - anchoTarjeta) / 2;
		int y = (1080 - altoTarjeta) / 2 - 40;

		javax.swing.JPanel panelTarjeta = new javax.swing.JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 255, 255, 245));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
				g2.setColor(Tema.NARANJA);
				g2.fillRoundRect(0, 0, getWidth(), 6, 18, 18);
				g2.dispose();
			}
		};
		panelTarjeta.setOpaque(false);
		panelTarjeta.setBounds(x, y, anchoTarjeta, altoTarjeta);
		panelTarjeta.setLayout(null);
		panelFondo.add(panelTarjeta);

		JLabel lblTitulo = new JLabel("ToolSystem", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTitulo.setForeground(Tema.CARBON);
		lblTitulo.setBounds(0, 30, anchoTarjeta, 40);
		panelTarjeta.add(lblTitulo);

		JLabel lblSubtitulo = new JLabel(" Iniciar sesión", SwingConstants.CENTER);
		lblSubtitulo.setFont(Tema.FUENTE_CAMPO);
		lblSubtitulo.setForeground(Tema.TEXTO_SECUNDARIO);
		lblSubtitulo.setBounds(0, 68, anchoTarjeta, 25);
		panelTarjeta.add(lblSubtitulo);

		JLabelGenerico lblUsuario = new JLabelGenerico((String) null);
		lblUsuario.setText("Usuario:");
		lblUsuario.setBounds(40, 130, 340, 25);
		panelTarjeta.add(lblUsuario);

		tfUsuario = new JtextFieldGenerico();
		tfUsuario.setBounds(40, 156, 340, 32);
		panelTarjeta.add(tfUsuario);

		JLabelGenerico lblPassword = new JLabelGenerico((String) null);
		lblPassword.setText("Contraseña:");
		lblPassword.setBounds(40, 198, 340, 25);
		panelTarjeta.add(lblPassword);

		tfPassword = new JPasswordField();
		tfPassword.setFont(Tema.FUENTE_CAMPO);
		tfPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(
				javax.swing.BorderFactory.createLineBorder(Tema.BORDE_SUAVE, 1, true),
				javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		tfPassword.setBounds(40, 224, 340, 32);
		panelTarjeta.add(tfPassword);

		lblMensaje = new JLabel(" ", SwingConstants.CENTER);
		lblMensaje.setFont(Tema.FUENTE_CAMPO);
		lblMensaje.setForeground(Tema.ROJO);
		lblMensaje.setBounds(20, 264, anchoTarjeta - 40, 25);
		panelTarjeta.add(lblMensaje);

		btnIngresar = crearBoton("Ingresar");
		btnIngresar.setBounds(40, 300, 340, 42);
		panelTarjeta.add(btnIngresar);

		getRootPane().setDefaultButton(btnIngresar);
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
		boton.setFocusable(true);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return boton;
	}

	public JtextFieldGenerico getTfUsuario() {
		return tfUsuario;
	}

	public JPasswordField getTfPassword() {
		return tfPassword;
	}

	public JButton getBtnIngresar() {
		return btnIngresar;
	}

	public JLabel getLblMensaje() {
		return lblMensaje;
	}

}
