package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import dao.ProveedorDAO;
import interfaces.InterfaceABM;
import modelo.ProveedorModelo;
import tablas.ModeloTablaProveedor;
import util.FechaUtil;
import vista.ProveedorVista;

public class ProveedorController implements InterfaceABM {

	private ProveedorVista vista;
	private ProveedorModelo proveedor;
	private ProveedorDAO dao;
	private List<ProveedorModelo> proveedores;
	private ModeloTablaProveedor tabla;

	public ProveedorController(ProveedorVista proveedorVista) {
		super();
		this.vista = proveedorVista;
		this.vista.setInterfaceABM(this);
		dao = new ProveedorDAO();
		tabla = new ModeloTablaProveedor();
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
		proveedores = dao.buscarPorFiltro(filtro);
		tabla.setLista(proveedores);
	}

	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		this.vista.getTfFechaRegistro().setEnabled(false);
		this.vista.getTfRazonSocial().setEnabled(false);
		this.vista.getTfNombreFantasia().setEnabled(false);
		this.vista.getTfRuc().setEnabled(false);
		this.vista.getTfTelefono().setEnabled(false);
		this.vista.getTfCorreo().setEnabled(false);
		this.vista.getTfDireccion().setEnabled(false);
		this.vista.getCbEstado().setEnabled(false);

		this.vista.getTfFechaRegistro().setValue(null);
		this.vista.getTfRazonSocial().setText("");
		this.vista.getTfNombreFantasia().setText("");
		this.vista.getTfRuc().setText("");
		this.vista.getTfTelefono().setText("");
		this.vista.getTfCorreo().setText("");
		this.vista.getTfDireccion().setText("");
		this.vista.getCbEstado().setSelected(false);
		proveedor = null;
	}

	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		this.vista.getTfFechaRegistro().setEnabled(false);
		this.vista.getTfRazonSocial().setEnabled(true);
		this.vista.getTfNombreFantasia().setEnabled(true);
		this.vista.getTfRuc().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		proveedor = new ProveedorModelo();
		this.vista.getTfFechaRegistro().setText(FechaUtil.fechaAString(LocalDate.now()));
		this.vista.getCbEstado().setSelected(true);
	}

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		proveedor = proveedores.get(fila);

		this.vista.getTfFechaRegistro().setText(
				proveedor.getFechaRegistro() != null ? FechaUtil.fechaAString(proveedor.getFechaRegistro()) : "");
		this.vista.getTfRazonSocial().setText(proveedor.getRazonSocial());
		this.vista.getTfNombreFantasia().setText(proveedor.getNombreFantasia());
		this.vista.getTfRuc().setText(proveedor.getRuc());
		this.vista.getTfTelefono().setText(proveedor.getTelefono());
		this.vista.getTfCorreo().setText(proveedor.getCorreo());
		this.vista.getTfDireccion().setText(proveedor.getDireccion());
		this.vista.getCbEstado().setSelected(!Boolean.FALSE.equals(proveedor.getEstado()));

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	@Override
	public void editar() {
		this.vista.getTfFechaRegistro().setEnabled(false);
		this.vista.getTfRazonSocial().setEnabled(true);
		this.vista.getTfNombreFantasia().setEnabled(true);
		this.vista.getTfRuc().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	@Override
	public void guardar() {
		if (this.vista.getTfRazonSocial().getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "La razón social es un campo obligatorio");
			return;
		}
		if (this.vista.getTfRuc().getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El RUC es un campo obligatorio");
			return;
		}

		proveedor.setFechaRegistro(FechaUtil.stringAFecha(this.vista.getTfFechaRegistro().getText()));
		proveedor.setRazonSocial(this.vista.getTfRazonSocial().getText());
		proveedor.setNombreFantasia(this.vista.getTfNombreFantasia().getText());
		proveedor.setRuc(this.vista.getTfRuc().getText());
		proveedor.setTelefono(this.vista.getTfTelefono().getText());
		proveedor.setCorreo(this.vista.getTfCorreo().getText());
		proveedor.setDireccion(this.vista.getTfDireccion().getText());
		proveedor.setEstado(this.vista.getCbEstado().isSelected());

		try {
			dao.guardar(proveedor);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar el proveedor. Verifique que el RUC no esté repetido.");
		}
	}

	@Override
	public void eliminar() {
		if (proveedor == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"Estas seguro que deseas eliminar al proveedor " + proveedor.getRazonSocial() + "?", "Atención",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(proveedor);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"No se pudo eliminar el proveedor. Puede que tenga compras asociadas; en ese caso, "
								+ "desmarque \"Activo\" y guarde para darlo de baja en vez de eliminarlo.");
			}
		}
	}

	@Override
	public void cancelar() {
		if (proveedor == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

}
