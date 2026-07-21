package componentes;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelPersonalizado extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Image image;

	public JPanelPersonalizado(String imagen) {
		try {
			this.image = new ImageIcon(getClass().getResource("/imagenes/"+imagen)).getImage();
		} catch (Exception e) {
			System.err.println("No se pudo encontrar la imagen /imagenes/"+imagen);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}

}
