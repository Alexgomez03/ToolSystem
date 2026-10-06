package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import dao.MarcaDAO;
import interfaces.InterfaceABM;
import modelo.MarcaModelo;
import tablas.ModeloTablaMarca;
import vista.MarcaVista;

public class MarcaController implements InterfaceABM {

	private MarcaVista vista;
	private MarcaModelo marca;
	private MarcaDAO dao;
	private List<MarcaModelo> marcas;
	private ModeloTablaMarca tabla;

	// Esto arranca todo apenas se abre la pantalla: prepara la tabla,
	// trae la lista de marcas ya cargadas y deja todo listo para
	// empezar a trabajar.
	public MarcaController(MarcaVista marcaVista) {
		super();
		this.vista = marcaVista;
		this.vista.setInterfaceABM(this);
		dao = new MarcaDAO();
		tabla = new ModeloTablaMarca();
		this.vista.getTabla().setModel(tabla);
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	// Conecta el doble clic en la tabla con la acción de mostrar esa
	// marca en el formulario.
	private void setAcciones() {
		this.vista.getTabla().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					seleccionarRegistro();
			}
		});
	}

	// Trae de la base las marcas que coinciden con el filtro escrito y
	// las muestra en la tabla.
	private void cargarTabla(String filtro) {
		marcas = dao.buscarPorFiltro(filtro);
		tabla.setLista(marcas);
	}

	// Deja la pantalla como recién abierta: todo bloqueado y vacío.
	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		this.vista.getTfNombre().setEnabled(false);
		this.vista.getCbEstado().setEnabled(false);

		this.vista.getTfNombre().setText("");
		this.vista.getCbEstado().setSelected(false);
		marca = null;
	}

	// Se ejecuta al tocar "Nuevo": habilita el formulario para cargar
	// una marca desde cero, con "Activo" tildado de entrada.
	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		this.vista.getTfNombre().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		marca = new MarcaModelo();
		this.vista.getCbEstado().setSelected(true);
	}

	// Al hacer doble clic en una fila, muestra esa marca en el
	// formulario para poder verla o editarla.
	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		marca = marcas.get(fila);

		this.vista.getTfNombre().setText(marca.getNombre());
		this.vista.getCbEstado().setSelected(Boolean.TRUE.equals(marca.getEstado()));

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	// Habilita el formulario para poder cambiar los datos de la marca
	// que está seleccionada.
	@Override
	public void editar() {
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	// Revisa que el nombre no esté vacío ni repetido, y si está todo
	// bien, guarda la marca en la base de datos.
	@Override
	public void guardar() {
		String nombre = this.vista.getTfNombre().getText().trim();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre es un campo obligatorio");
			return;
		}
		if (dao.existeNombre(nombre, marca.getId())) {
			JOptionPane.showMessageDialog(null, "Ya existe una marca con ese nombre");
			return;
		}

		marca.setNombre(nombre);
		marca.setEstado(this.vista.getCbEstado().isSelected());

		try {
			dao.guardar(marca);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar la marca.");
		}
	}

	// Borra definitivamente la marca seleccionada, después de preguntar
	// si está seguro. Si tiene productos asociados, la base de datos no
	// va a dejar borrarla (para eso está la baja lógica: desmarcar
	// "Activo" y guardar, en vez de eliminar).
	@Override
	public void eliminar() {
		if (marca == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"Estas seguro que deseas eliminar la marca " + marca.getNombre() + "?", "Atención",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(marca);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No se pudo eliminar la marca. Puede que tenga productos asociados.");
			}
		}
	}

	// Si no había nada seleccionado, cierra la ventana. Si había algo
	// cargado en el formulario, lo descarta y deja todo en blanco.
	@Override
	public void cancelar() {
		if (marca == null)
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
