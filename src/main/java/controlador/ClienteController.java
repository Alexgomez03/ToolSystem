package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import dao.ClienteDAO;
import interfaces.InterfaceABM;
import modelo.ClienteModelo;
import tablas.ModeloTablaCliente;
import util.FechaUtil;
import vista.ClienteVista;

public class ClienteController implements InterfaceABM {

	private ClienteVista vista;
	private ClienteModelo cliente;
	private ClienteDAO dao;
	private List<ClienteModelo> clientes;
	private ModeloTablaCliente tabla;

	public ClienteController(ClienteVista clienteVista) {
		super();
		this.vista = clienteVista;
		this.vista.setInterfaceABM(this);
		dao = new ClienteDAO();
		tabla = new ModeloTablaCliente();
		this.vista.getTabla().setModel(tabla);
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	private void setAcciones() {
		this.vista.getTabla().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					seleccionarRegistro();
			}
		});

	}

	private void cargarTabla(String filtro) {
		clientes = dao.buscarPorFiltro(filtro);
		tabla.setLista(clientes);
	}

	private void estadoInicial() {
		// desactiva los botones
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		// desactiva los textfield
		this.vista.getTfFechaRegistro().setEnabled(false);
		this.vista.getTfNombre().setEnabled(false);
		this.vista.getTfApellido().setEnabled(false);
		this.vista.getTfDocumento().setEnabled(false);
		this.vista.getTfFechaNacimiento().setEnabled(false);
		this.vista.getTfCorreo().setEnabled(false);
		this.vista.getTfDireccion().setEnabled(false);
		this.vista.getTfTelefono().setEnabled(false);

		// Limpiar los campos
		this.vista.getTfFechaRegistro().setValue(null);
		this.vista.getTfFechaNacimiento().setValue(null);
		this.vista.getTfNombre().setText("");
		this.vista.getTfApellido().setText("");
		this.vista.getTfDocumento().setText("");
		this.vista.getTfCorreo().setText("");
		this.vista.getTfDireccion().setText("");
		this.vista.getTfTelefono().setText("");
		cliente = null;

	}

	@Override
	public void nuevo() {
		// desactiva los botones
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		// desactiva los textfield
		this.vista.getTfFechaRegistro().setEnabled(false);
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfApellido().setEnabled(true);
		this.vista.getTfDocumento().setEnabled(true);
		this.vista.getTfFechaNacimiento().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);

		// Carga el campo fecha y crea el cliente
		cliente = new ClienteModelo();
		this.vista.getTfFechaRegistro().setText(FechaUtil.fechaAString(LocalDate.now()));

	}

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		cliente = clientes.get(fila);

		this.vista.getTfFechaRegistro().setText(FechaUtil.fechaAString(cliente.getFechaRegistro()));
		this.vista.getTfNombre().setText(cliente.getNombre());
		this.vista.getTfApellido().setText(cliente.getApellido());
		this.vista.getTfDocumento().setText(cliente.getDocumento());
		this.vista.getTfTelefono().setText(cliente.getTelefono());
		this.vista.getTfCorreo().setText(cliente.getCorreo());
		this.vista.getTfFechaNacimiento().setText(FechaUtil.fechaAString(cliente.getFechaNacimiento()));
		this.vista.getTfDireccion().setText(cliente.getDireccion());

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);

	}

	@Override
	public void editar() {
		this.vista.getTfFechaRegistro().setEnabled(false);
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfApellido().setEnabled(true);
		this.vista.getTfDocumento().setEnabled(true);
		this.vista.getTfFechaNacimiento().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);
		
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
		
	}

	@Override
	public void guardar() {
		
		if(this.vista.getTfNombre().getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre es un campo obligatorio");
			return;
		}

		cliente.setFechaRegistro(FechaUtil.stringAFecha(this.vista.getTfFechaRegistro().getText()));
		cliente.setFechaNacimiento(FechaUtil.stringAFecha(this.vista.getTfFechaNacimiento().getText()));
		cliente.setNombre(this.vista.getTfNombre().getText());
		cliente.setApellido(this.vista.getTfApellido().getText());
		cliente.setDocumento(this.vista.getTfDocumento().getText());
		cliente.setTelefono(this.vista.getTfTelefono().getText());
		cliente.setCorreo(this.vista.getTfCorreo().getText());
		cliente.setDireccion(this.vista.getTfDireccion().getText());

		try {
			dao.guardar(cliente);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void eliminar() {
		if (cliente == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null, "Estas seguro que deseas eliminar al cliente "
				+ cliente.getNombre() + " " + cliente.getApellido() + "?", "Atención", JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(cliente);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void cancelar() {
		if (cliente == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

}
