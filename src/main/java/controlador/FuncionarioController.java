package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

import dao.FuncionarioDAO;
import interfaces.InterfaceABM;
import modelo.FuncionarioModelo;
import tablas.ModeloTablaFuncionario;
import util.FechaUtil;
import vista.FuncionarioVista;

public class FuncionarioController implements InterfaceABM {

	private FuncionarioVista vista;
	private FuncionarioModelo funcionario;
	private FuncionarioDAO dao;
	private List<FuncionarioModelo> funcionarios;
	private ModeloTablaFuncionario tabla;

	// Esto arranca todo apenas se abre la pantalla: prepara la tabla,
	// trae la lista de funcionarios ya cargados y deja todo listo para
	// empezar a trabajar.
	public FuncionarioController(FuncionarioVista funcionarioVista) {
		super();
		this.vista = funcionarioVista;
		this.vista.setInterfaceABM(this);
		dao = new FuncionarioDAO();
		tabla = new ModeloTablaFuncionario();
		this.vista.getTabla().setModel(tabla);
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	// Conecta el doble clic en la tabla con la acción de mostrar ese
	// funcionario en el formulario.
	private void setAcciones() {
		this.vista.getTabla().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					seleccionarRegistro();
			}
		});
	}

	// Trae de la base los funcionarios que coinciden con el filtro
	// escrito y los muestra en la tabla.
	private void cargarTabla(String filtro) {
		funcionarios = dao.buscarPorFiltro(filtro);
		tabla.setLista(funcionarios);
	}

	// Deja la pantalla como recién abierta: todo bloqueado y vacío.
	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		this.vista.getTfFechaIngreso().setEnabled(false);
		this.vista.getTfNombre().setEnabled(false);
		this.vista.getTfApellido().setEnabled(false);
		this.vista.getTfDocumento().setEnabled(false);
		this.vista.getTfCargo().setEnabled(false);
		this.vista.getTfTelefono().setEnabled(false);
		this.vista.getTfCorreo().setEnabled(false);
		this.vista.getTfDireccion().setEnabled(false);
		this.vista.getCbEstado().setEnabled(false);

		this.vista.getTfFechaIngreso().setValue(null);
		this.vista.getTfNombre().setText("");
		this.vista.getTfApellido().setText("");
		this.vista.getTfDocumento().setText("");
		this.vista.getTfCargo().setText("");
		this.vista.getTfTelefono().setText("");
		this.vista.getTfCorreo().setText("");
		this.vista.getTfDireccion().setText("");
		this.vista.getCbEstado().setSelected(false);
		funcionario = null;
	}

	// Se ejecuta al tocar "Nuevo": habilita el formulario para cargar
	// un funcionario desde cero, con la fecha de ingreso de hoy y
	// "Activo" tildado de entrada.
	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		this.vista.getTfFechaIngreso().setEnabled(false);
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfApellido().setEnabled(true);
		this.vista.getTfDocumento().setEnabled(true);
		this.vista.getTfCargo().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		funcionario = new FuncionarioModelo();
		this.vista.getTfFechaIngreso().setText(FechaUtil.fechaAString(LocalDate.now()));
		this.vista.getCbEstado().setSelected(true);
	}

	// Al hacer doble clic en una fila, muestra ese funcionario en el
	// formulario para poder verlo o editarlo.
	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		funcionario = funcionarios.get(fila);

		this.vista.getTfFechaIngreso().setText(FechaUtil.fechaAString(funcionario.getFechaIngreso()));
		this.vista.getTfNombre().setText(funcionario.getNombre());
		this.vista.getTfApellido().setText(funcionario.getApellido());
		this.vista.getTfDocumento().setText(funcionario.getDocumento());
		this.vista.getTfCargo().setText(funcionario.getCargo());
		this.vista.getTfTelefono().setText(funcionario.getTelefono());
		this.vista.getTfCorreo().setText(funcionario.getCorreo());
		this.vista.getTfDireccion().setText(funcionario.getDireccion());
		this.vista.getCbEstado().setSelected(Boolean.TRUE.equals(funcionario.getEstado()));

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	// Habilita el formulario para poder cambiar los datos del
	// funcionario que está seleccionado.
	@Override
	public void editar() {
		this.vista.getTfFechaIngreso().setEnabled(false);
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfApellido().setEnabled(true);
		this.vista.getTfDocumento().setEnabled(true);
		this.vista.getTfCargo().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	// Revisa que el nombre y el documento estén cargados y que el
	// documento no esté repetido, y si está todo bien, guarda el
	// funcionario en la base de datos.
	@Override
	public void guardar() {
		String nombre = this.vista.getTfNombre().getText().trim();
		String documento = this.vista.getTfDocumento().getText().trim();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre es un campo obligatorio");
			return;
		}
		if (documento.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El documento es un campo obligatorio");
			return;
		}
		if (dao.existeDocumento(documento, funcionario.getId())) {
			JOptionPane.showMessageDialog(null, "Ya existe un funcionario con ese documento");
			return;
		}

		funcionario.setFechaIngreso(FechaUtil.stringAFecha(this.vista.getTfFechaIngreso().getText()));
		funcionario.setNombre(nombre);
		funcionario.setApellido(this.vista.getTfApellido().getText().trim());
		funcionario.setDocumento(documento);
		funcionario.setCargo(this.vista.getTfCargo().getText().trim());
		funcionario.setTelefono(this.vista.getTfTelefono().getText().trim());
		funcionario.setCorreo(this.vista.getTfCorreo().getText().trim());
		funcionario.setDireccion(this.vista.getTfDireccion().getText().trim());
		funcionario.setEstado(this.vista.getCbEstado().isSelected());

		try {
			dao.guardar(funcionario);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"No se pudo guardar el funcionario. Verifique que el documento no esté repetido.");
		}
	}

	// Borra definitivamente al funcionario seleccionado, después de
	// preguntar si está seguro. Si tiene ventas asociadas (como
	// vendedor), la base de datos no va a dejar borrarlo (para eso está
	// la baja lógica: desmarcar "Activo" y guardar, en vez de eliminar).
	@Override
	public void eliminar() {
		if (funcionario == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null, "Estas seguro que deseas eliminar al funcionario "
				+ funcionario.getNombre() + " " + funcionario.getApellido() + "?", "Atención",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(funcionario);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se pudo eliminar el funcionario.");
			}
		}
	}

	// Si no había nada seleccionado, cierra la ventana. Si había algo
	// cargado en el formulario, lo descarta y deja todo en blanco.
	@Override
	public void cancelar() {
		if (funcionario == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	// Se ejecuta al escribir en el buscador: vuelve a cargar la tabla
	// filtrada con ese texto.
	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

}
