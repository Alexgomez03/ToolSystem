package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import vista.CategoriaVista;
import vista.ClienteVista;
import vista.MarcaVista;
import vista.PantallaPrincipalVista;
import vista.ProductoVista;
import vista.ProveedorVista;
import vista.VentaVista;

public class PantallaPrincipalController {

	private PantallaPrincipalVista vista;

	public PantallaPrincipalController(PantallaPrincipalVista pantallaPrincipalVista) {
		super();
		this.vista = pantallaPrincipalVista;
		setAcciones();
	}

	private void setAcciones() {

		this.vista.getmCategorias().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirCategorias();
			}
		});

		this.vista.getmClientes().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirClientes();
			}
		});

		this.vista.getmMarcas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirMarcas();
			}
		});

		this.vista.getmProductos().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirProductos();
			}
		});

		this.vista.getmProveedores().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirProveedores();
			}
		});

		this.vista.getmVentas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirVentas();
			}
		});

		this.vista.getBtnCliente().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirClientes();
			}
		});

		this.vista.getBtnProducto().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirProductos();
			}
		});

		this.vista.getBtnVentas().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirVentas();
			}
		});
	}

	private void abrirCategorias() {
		CategoriaVista dialog = new CategoriaVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirClientes() {
		ClienteVista dialog = new ClienteVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirMarcas() {
		MarcaVista dialog = new MarcaVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirProductos() {
		ProductoVista dialog = new ProductoVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirProveedores() {
		ProveedorVista dialog = new ProveedorVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

	private void abrirVentas() {
		VentaVista dialog = new VentaVista();
		dialog.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(vista);
		dialog.setControlador();
		dialog.setVisible(true);
	}

}
