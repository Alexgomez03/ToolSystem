package componentes;

import javax.swing.JLabel;

public class JLabelGenerico extends JLabel {

	private static final long serialVersionUID = 1L;

	public JLabelGenerico(String text) {
		super();
		setFont(Tema.FUENTE_LABEL);
		setForeground(Tema.TEXTO_PRINCIPAL);
		setText(text);
	}

}
