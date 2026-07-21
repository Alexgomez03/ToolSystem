package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class JButtonAccesoDirecto extends JButton {

	private static final long serialVersionUID = 1L;

	public JButtonAccesoDirecto() {
		super();
		setSize(new Dimension(160, 160));
		setMinimumSize(new Dimension(160, 160));
		setFont(Tema.FUENTE_BOTON_ACCESO);
		setForeground(Tema.TEXTO_PRINCIPAL);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setIconTextGap(10);
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
			URL url = JMenuIntemPersonalizado.class.getResource("/iconos/" + icono.toLowerCase().replace(" ", "_") + "64.png");
			this.setIcon(new ImageIcon(url));
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/" + icono.toLowerCase().replace(" ", "_") + "64.png");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int arc = 22;
		Color fondo = getModel().isRollover() ? new Color(255, 255, 255, 235) : new Color(255, 255, 255, 210);
		g2.setColor(fondo);
		g2.fillRoundRect(0, 0, getWidth(), getHeight() - 1, arc, arc);

		g2.setColor(Tema.NARANJA);
		g2.setStroke(new java.awt.BasicStroke(getModel().isRollover() ? 3f : 2f));
		g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);

		// Barra de acento inferior
		g2.setColor(Tema.NARANJA);
		g2.fillRoundRect(getWidth() / 2 - 18, getHeight() - 10, 36, 4, 4, 4);
		g2.dispose();

		super.paintComponent(g);
	}

}
