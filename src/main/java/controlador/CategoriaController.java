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
		categorias = dao.buscarPorFiltro(filtro);
		tabla.setLista(categorias);
	}

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

	@Override
	public void editar() {
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getCbEstado().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(false);
	}

	@Override
	public void guardar() {
		if (this.vista.getTfNombre().getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "El nombre es un campo obligatorio");
			return;
		}

		categoria.setNombre(this.vista.getTfNombre().getText());
		categoria.setEstado(this.vista.getCbEstado().isSelected());

		try {
			dao.guardar(categoria);
			cargarTabla("");
			estadoInicial();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	@Override
	public void cancelar() {
		if (categoria == null)
			this.vista.dispose();
		else
			estadoInicial();
	}

	@Override
	public void buscar() {
		cargarTabla(vista.getTfBuscador().getText());
	}

}
