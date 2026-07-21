package componentes;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class JtextFieldGenerico extends JTextField {

	private static final long serialVersionUID = 1L;

	public JtextFieldGenerico() {
		super();
		setFont(Tema.FUENTE_CAMPO);
		setForeground(Tema.TEXTO_PRINCIPAL);
		setDisabledTextColor(Tema.TEXTO_SECUNDARIO);
		setBackground(Color.WHITE);
		setBorder(new CompoundBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true), new EmptyBorder(4, 8, 4, 8)));
		setSelectionColor(Tema.NARANJA);
		setSelectedTextColor(Color.WHITE);
	}

}
