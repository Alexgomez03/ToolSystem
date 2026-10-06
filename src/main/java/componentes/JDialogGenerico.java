package componentes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import interfaces.InterfaceABM;

public class JDialogGenerico extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel panelHeader;
	private JLabel lblTitulo;
	private JPanel panelFormulario;
	private JTable tabla;
	private JButtonABM btnNuevo;
	private JButtonABM btnEditar;
	private JButtonABM btnGuardar;
	private JButtonABM btnEliminar;
	private JButtonABM btnCancelar;
	private JtextFieldGenerico tfBuscador;
	private InterfaceABM interfaceABM;

	public void setInterfaceABM(InterfaceABM interfaceABM) {
		this.interfaceABM = interfaceABM;
	}

	
	 // Título mostrado en la barra superior del formulario (p. ej. "Gestión de Clientes").
	 
	public void setTituloFormulario(String texto) {
		lblTitulo.setText(texto);
	}

	/**
	 * Create the dialog.
	 */
	public JDialogGenerico() {
		setBounds(100, 100, 1080, 720);
		getContentPane().setBackground(Tema.FONDO_FORMULARIO);
		getContentPane().setLayout(null);

		try {
			URL url = JDialogGenerico.class.getResource("/imagenes/logo32.png");
			if (url != null)
				setIconImage(new ImageIcon(url).getImage());
		} catch (Exception e) {
			// sin icono, no es crítico
		}

		// ---------- Encabezado ----------
		panelHeader = new JPanel() {
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
		panelHeader.setBounds(0, 0, 1080, 54);
		getContentPane().add(panelHeader);

		lblTitulo = new JLabel("Formulario");
		lblTitulo.setFont(Tema.FUENTE_TITULO);
		lblTitulo.setForeground(Tema.TEXTO_CLARO);
		lblTitulo.setBounds(20, 8, 500, 34);
		panelHeader.add(lblTitulo);

		// ---------- Barra de acciones ----------
		btnNuevo = new JButtonABM();
		btnNuevo.setText("Nuevo");
		btnNuevo.setToolTipText("Crear un nuevo registro");
		btnNuevo.setBounds(10, 66, 90, 62);
		getContentPane().add(btnNuevo);

		btnEditar = new JButtonABM();
		btnEditar.setToolTipText("Editar el registro seleccionado");
		btnEditar.setText("Editar");
		btnEditar.setBounds(106, 66, 90, 62);
		getContentPane().add(btnEditar);

		btnGuardar = new JButtonABM();
		btnGuardar.setToolTipText("Guardar los cambios");
		btnGuardar.setText("Guardar");
		btnGuardar.setBounds(202, 66, 90, 62);
		getContentPane().add(btnGuardar);

		btnEliminar = new JButtonABM();
		btnEliminar.setToolTipText("Eliminar el registro seleccionado");
		btnEliminar.setText("Eliminar");
		btnEliminar.setBounds(298, 66, 90, 62);
		getContentPane().add(btnEliminar);

		btnCancelar = new JButtonABM();
		btnCancelar.setToolTipText("Cancelar la operación en curso");
		btnCancelar.setText("Cancelar");
		btnCancelar.setBounds(394, 66, 90, 62);
		getContentPane().add(btnCancelar);

		// ---------- Buscador ----------
		JLabelGenerico lblgnrcBuscador = new JLabelGenerico((String) null);
		lblgnrcBuscador.setText("Buscador:");
		lblgnrcBuscador.setBounds(532, 76, 73, 25);
		getContentPane().add(lblgnrcBuscador);

		tfBuscador = new JtextFieldGenerico();
		tfBuscador.setBounds(610, 76, 450, 27);
		getContentPane().add(tfBuscador);

		// ---------- Panel de formulario (izquierda) ----------
		panelFormulario = new JPanel();
		panelFormulario.setBackground(Tema.FONDO_TARJETA);
		panelFormulario.setBorder(new CompoundBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true), new EmptyBorder(2, 2, 2, 2)));
		panelFormulario.setBounds(10, 138, 515, 565);
		getContentPane().add(panelFormulario);
		panelFormulario.setLayout(null);

		// ---------- Tabla (derecha) ----------
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Tema.BORDE_SUAVE, 1, true));
		scrollPane.setBounds(532, 138, 538, 565);
		getContentPane().add(scrollPane);

		tabla = new JTable();
		tabla.setRowHeight(26);
		tabla.setFont(Tema.FUENTE_CAMPO);
		tabla.setSelectionBackground(Tema.NARANJA);
		tabla.setSelectionForeground(Color.WHITE);
		tabla.setGridColor(new Color(232, 232, 230));
		tabla.getTableHeader().setFont(Tema.FUENTE_BOTON);
		tabla.getTableHeader().setBackground(Tema.ACERO);
		tabla.getTableHeader().setForeground(Color.WHITE);
		tabla.getTableHeader().setOpaque(true);
		scrollPane.setViewportView(tabla);

		setAcciones();

	}

	private void setAcciones() {
		btnNuevo.addActionListener(this);
		btnEditar.addActionListener(this);
		btnGuardar.addActionListener(this);
		btnEliminar.addActionListener(this);
		btnCancelar.addActionListener(this);
		tfBuscador.setActionCommand("Buscar");
		tfBuscador.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Nuevo":
			interfaceABM.nuevo();
			break;
		case "Editar":
			interfaceABM.editar();
			break;
		case "Guardar":
			interfaceABM.guardar();
			break;
		case "Eliminar":
			interfaceABM.eliminar();
			break;
		case "Cancelar":
			interfaceABM.cancelar();
			break;
		case "Buscar":
			interfaceABM.buscar();
			break;
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getPanelFormulario() {
		return panelFormulario;
	}

	public JTable getTabla() {
		return tabla;
	}

	public JButtonABM getBtnNuevo() {
		return btnNuevo;
	}

	public JButtonABM getBtnEditar() {
		return btnEditar;
	}

	public JButtonABM getBtnGuardar() {
		return btnGuardar;
	}

	public JButtonABM getBtnEliminar() {
		return btnEliminar;
	}

	public JButtonABM getBtnCancelar() {
		return btnCancelar;
	}

	public JtextFieldGenerico getTfBuscador() {
		return tfBuscador;
	}

	public InterfaceABM getInterfaceABM() {
		return interfaceABM;
	}
}
