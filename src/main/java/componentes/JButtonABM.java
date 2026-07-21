package componentes;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class JButtonABM extends JButton {

	private static final long serialVersionUID = 1L;

	public JButtonABM() {
		super();
		setSize(new Dimension(90, 62));
		setMinimumSize(new Dimension(90, 62));
		setFont(Tema.FUENTE_BOTON);
		setForeground(Tema.TEXTO_PRINCIPAL);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setIconTextGap(4);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setOpaque(false);
		setFocusable(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void setText(String text) {
		cargarIcono(text);
		super.setText(text);
	}

	private void cargarIcono(String icono) {
		try {
			URL url = JMenuIntemPersonalizado.class.getResource("/iconos/" + icono.toLowerCase().replace(" ", "_") + "32.png");
			this.setIcon(new ImageIcon(url));
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/" + icono.toLowerCase().replace(" ", "_") + "32.png");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int arc = 14;
		if (getModel().isPressed()) {
			g2.setColor(Tema.BORDE_SUAVE);
		} else if (!isEnabled()) {
			g2.setColor(new java.awt.Color(246, 246, 244));
		} else if (getModel().isRollover()) {
			g2.setColor(new java.awt.Color(240, 240, 238));
		} else {
			g2.setColor(Tema.FONDO_TARJETA);
		}
		g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);

		g2.setColor(Tema.BORDE_SUAVE);
		g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, arc, arc);
		g2.dispose();

		setForeground(isEnabled() ? Tema.TEXTO_PRINCIPAL : Tema.TEXTO_SECUNDARIO);
		super.paintComponent(g);
	}

}
