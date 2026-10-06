package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import dao.CategoriaDAO;
import interfaces.InterfaceABM;
import modelo.CategoriaModelo;
import tablas.ModeloTablaCategoria;
import vista.CategoriaVista;

public class CategoriaController implements InterfaceABM {

	private CategoriaVista vista;
	private CategoriaModelo categoria;
	private CategoriaDAO dao;
	private List<CategoriaModelo> categorias;
	private ModeloTablaCategoria tabla;

	// Esto arranca todo apenas se abre la pantalla: prepara la tabla,
	// trae la lista de categorías ya cargadas y deja todo listo para
	// empezar a trabajar.
	public CategoriaController(CategoriaVista categoriaVista) {
		super();
		this.vista = categoriaVista;
		this.vista.setInterfaceABM(this);
		dao = new CategoriaDAO();
		tabla = new ModeloTablaCategoria();
		this.vista.getTabla().setModel(tabla);
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	// Conecta el doble clic en la tabla con la acción de mostrar esa
	// categoría en el formulario.
	private void setAcciones() {
		this.vista.getTabla().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					seleccionarRegistro();
			}
		});
	}

	// Trae de la base las categorías que coinciden con el filtro escrito
	// y las muestra en la tabla.
	private void cargarTabla(String filtro) {
		categorias = dao.buscarPorFiltro(filtro);
		tabla.setLista(categorias);
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
		categoria = null;
	}

	// Se ejecuta al tocar "Nuevo": habilita el formulario para cargar
	// una categoría desde cero, con "Activo" tildado de entrada.
	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		this.vista.getTfNombre().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		categoria = new CategoriaModelo();
		this.vista.getCbEstado().setSelected(true);
	}

	// Al hacer doble clic en una fila, muestra esa categoría en el
	// formulario para poder verla o editarla.
	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0)
			return;
		categoria = categorias.get(fila);

		this.vista.getTfNombre().setText(categoria.getNombre());
		this.vista.getCbEstado().setSelected(Boolean.TRUE.equals(categoria.getEstado()));

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
	}

	// Habilita el formulario para poder cambiar los datos de la
	// categoría que está seleccionada.
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
	// bien, guarda la categoría en la base de datos.
	@Override
	public void guardar() {
		String nombre = this.vista.getTfNombre().getText().trim();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre es un campo obligatorio");
			return;
		}
		if (dao.existeNombre(nombre, categoria.getId())) {
			JOptionPane.showMessageDialog(null, "Ya existe una categoría con ese nombre");
			return;
		}

		categoria.setNombre(nombre);
		categoria.setEstado(this.vista.getCbEstado().isSelected());

		try {
			dao.guardar(categoria);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No se pudo guardar la categoría.");
		}
	}

	// Borra definitivamente la categoría seleccionada, después de
	// preguntar si está seguro. Si tiene productos asociados, la base
	// de datos no va a dejar borrarla (para eso está la baja lógica:
	// desmarcar "Activo" y guardar, en vez de eliminar).
	@Override
	public void eliminar() {
		if (categoria == null)
			return;
		int confirmacion = JOptionPane.showConfirmDialog(null,
				"Estas seguro que deseas eliminar la categoría " + categoria.getNombre() + "?", "Atención",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion == JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(categoria);
				estadoInicial();
				cargarTabla("");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"No se pudo eliminar la categoría. Puede que tenga productos asociados.");
			}
		}
	}

	// Si no había nada seleccionado, cierra la ventana. Si había algo
	// cargado en el formulario, lo descarta y deja todo en blanco.
	@Override
	public void cancelar() {
		if (categoria == null)
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
