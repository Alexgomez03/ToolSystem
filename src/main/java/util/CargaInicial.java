package util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import dao.CategoriaDAO;
import dao.ClienteDAO;
import dao.MarcaDAO;
import dao.ProductoDAO;
import dao.ProveedorDAO;
import modelo.CategoriaModelo;
import modelo.ClienteModelo;
import modelo.MarcaModelo;
import modelo.ProductoModelo;
import modelo.ProveedorModelo;

/**
 * Clase utilitaria para poblar la base de datos con datos de ejemplo
 * correspondientes a una ferretería (categorías, marcas, productos,
 * proveedores y clientes).
 *
 * Ejecutar una sola vez, con la base de datos ya creada/actualizada
 * por Hibernate (hbm2ddl.auto = update).
 */
public class CargaInicial {

	public static void main(String[] args) {
		try {
			Map<String, CategoriaModelo> categorias = cargarCategorias();
			Map<String, MarcaModelo> marcas = cargarMarcas();
			cargarProveedores();
			cargarClientes();
			cargarProductos(categorias, marcas);

			System.out.println("Carga inicial de datos de la ferretería finalizada con éxito.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ==================== CATEGORIAS ====================

	private static Map<String, CategoriaModelo> cargarCategorias() throws Exception {
		String[] nombres = {
				"Herramientas Manuales",
				"Herramientas Eléctricas",
				"Tornillería y Fijaciones",
				"Pinturas y Afines",
				"Plomería",
				"Electricidad",
				"Materiales de Construcción",
				"Seguridad e Higiene"
		};

		CategoriaDAO dao = new CategoriaDAO();
		for (String nombre : nombres) {
			CategoriaModelo categoria = new CategoriaModelo();
			categoria.setNombre(nombre);
			categoria.setEstado(true);
			dao.guardar(categoria);
		}

		// Se recupera de la base para obtener los ID generados
		Map<String, CategoriaModelo> mapa = new HashMap<>();
		for (CategoriaModelo c : dao.recuperarTodo()) {
			mapa.put(c.getNombre(), c);
		}
		return mapa;
	}

	// ==================== MARCAS ====================

	private static Map<String, MarcaModelo> cargarMarcas() throws Exception {
		String[] nombres = {
				"Truper",
				"Stanley",
				"Bosch",
				"Pretul",
				"Urrea",
				"Black + Decker",
				"Tramontina",
				"Sherwin Williams"
		};

		MarcaDAO dao = new MarcaDAO();
		for (String nombre : nombres) {
			MarcaModelo marca = new MarcaModelo();
			marca.setNombre(nombre);
			marca.setEstado(true);
			dao.guardar(marca);
		}

		Map<String, MarcaModelo> mapa = new HashMap<>();
		for (MarcaModelo m : dao.recuperarTodo()) {
			mapa.put(m.getNombre(), m);
		}
		return mapa;
	}

	// ==================== PROVEEDORES ====================

	private static void cargarProveedores() throws Exception {
		ProveedorDAO dao = new ProveedorDAO();

		ProveedorModelo p1 = new ProveedorModelo();
		p1.setRazonSocial("Distribuidora Ferretera del Este S.A.");
		p1.setNombreFantasia("Ferretera del Este");
		p1.setRuc("80012345-6");
		p1.setTelefono("021-555123");
		p1.setCorreo("ventas@ferreteradeleste.com.py");
		p1.setDireccion("Av. Mariscal López 1234, Asunción");
		p1.setFechaRegistro(LocalDate.now());
		dao.guardar(p1);

		ProveedorModelo p2 = new ProveedorModelo();
		p2.setRazonSocial("Herramientas y Construcción SRL");
		p2.setNombreFantasia("HerraConstru");
		p2.setRuc("80054321-2");
		p2.setTelefono("021-555987");
		p2.setCorreo("contacto@herraconstru.com.py");
		p2.setDireccion("Ruta 2 Km 15, San Lorenzo");
		p2.setFechaRegistro(LocalDate.now());
		dao.guardar(p2);

		ProveedorModelo p3 = new ProveedorModelo();
		p3.setRazonSocial("Pinturas Sur S.A.");
		p3.setNombreFantasia("Pinturas Sur");
		p3.setRuc("80098765-1");
		p3.setTelefono("021-555456");
		p3.setCorreo("pedidos@pinturasur.com.py");
		p3.setDireccion("Av. Eusebio Ayala 2500, Fernando de la Mora");
		p3.setFechaRegistro(LocalDate.now());
		dao.guardar(p3);
	}

	// ==================== CLIENTES ====================

	private static void cargarClientes() throws Exception {
		ClienteDAO dao = new ClienteDAO();

		ClienteModelo c1 = new ClienteModelo();
		c1.setNombre("Carlos");
		c1.setApellido("Gómez");
		c1.setDocumento("3456789");
		c1.setTelefono("0981-111222");
		c1.setCorreo("carlos.gomez@gmail.com");
		c1.setDireccion("Barrio Obrero, Asunción");
		c1.setFechaNacimiento(LocalDate.of(1985, 4, 12));
		c1.setFechaRegistro(LocalDate.now());
		dao.guardar(c1);

		ClienteModelo c2 = new ClienteModelo();
		c2.setNombre("Laura");
		c2.setApellido("Benítez");
		c2.setDocumento("4123456");
		c2.setTelefono("0982-333444");
		c2.setCorreo("laura.benitez@hotmail.com");
		c2.setDireccion("Villa Elisa");
		c2.setFechaNacimiento(LocalDate.of(1990, 9, 30));
		c2.setFechaRegistro(LocalDate.now());
		dao.guardar(c2);

		ClienteModelo c3 = new ClienteModelo();
		c3.setNombre("Construcciones");
		c3.setApellido("Los Pinos S.A.");
		c3.setDocumento("80011223");
		c3.setTelefono("021-444555");
		c3.setCorreo("compras@lospinos.com.py");
		c3.setDireccion("Ñemby");
		c3.setFechaNacimiento(null);
		c3.setFechaRegistro(LocalDate.now());
		dao.guardar(c3);
	}

	// ==================== PRODUCTOS ====================

	private static void cargarProductos(Map<String, CategoriaModelo> categorias, Map<String, MarcaModelo> marcas)
			throws Exception {
		ProductoDAO dao = new ProductoDAO();

		agregar(dao, "HM-001", "Martillo carpintero 16 oz", 65000.0, 40.0, "unidad",
				categorias.get("Herramientas Manuales"), marcas.get("Truper"));

		agregar(dao, "HM-002", "Destornillador plano 1/4 x 6\"", 22000.0, 60.0, "unidad",
				categorias.get("Herramientas Manuales"), marcas.get("Stanley"));

		agregar(dao, "HM-003", "Juego de llaves combinadas 8-19mm", 185000.0, 15.0, "juego",
				categorias.get("Herramientas Manuales"), marcas.get("Urrea"));

		agregar(dao, "HM-004", "Cinta métrica 5m", 28000.0, 50.0, "unidad",
				categorias.get("Herramientas Manuales"), marcas.get("Stanley"));

		agregar(dao, "HE-001", "Taladro percutor 1/2\" 750W", 780000.0, 12.0, "unidad",
				categorias.get("Herramientas Eléctricas"), marcas.get("Bosch"));

		agregar(dao, "HE-002", "Amoladora angular 4 1/2\" 850W", 495000.0, 10.0, "unidad",
				categorias.get("Herramientas Eléctricas"), marcas.get("Black + Decker"));

		agregar(dao, "HE-003", "Sierra circular 7 1/4\" 1400W", 890000.0, 6.0, "unidad",
				categorias.get("Herramientas Eléctricas"), marcas.get("Bosch"));

		agregar(dao, "TF-001", "Tornillo autorroscante 8x1\" (caja x100)", 18000.0, 80.0, "caja",
				categorias.get("Tornillería y Fijaciones"), marcas.get("Pretul"));

		agregar(dao, "TF-002", "Tarugo plástico Fischer 8mm (bolsa x50)", 15000.0, 70.0, "bolsa",
				categorias.get("Tornillería y Fijaciones"), marcas.get("Pretul"));

		agregar(dao, "TF-003", "Clavo para madera 2 1/2\" (kg)", 12000.0, 100.0, "kg",
				categorias.get("Tornillería y Fijaciones"), marcas.get("Tramontina"));

		agregar(dao, "PI-001", "Pintura látex interior blanco 20L", 420000.0, 18.0, "balde",
				categorias.get("Pinturas y Afines"), marcas.get("Sherwin Williams"));

		agregar(dao, "PI-002", "Esmalte sintético brillante negro 1L", 65000.0, 25.0, "lata",
				categorias.get("Pinturas y Afines"), marcas.get("Sherwin Williams"));

		agregar(dao, "PI-003", "Rodillo para pintura 9\" con mango", 25000.0, 40.0, "unidad",
				categorias.get("Pinturas y Afines"), marcas.get("Truper"));

		agregar(dao, "PL-001", "Caño PVC 1/2\" (barra 6m)", 32000.0, 55.0, "barra",
				categorias.get("Plomería"), marcas.get("Tramontina"));

		agregar(dao, "PL-002", "Codo PVC 90° 1/2\"", 3500.0, 200.0, "unidad",
				categorias.get("Plomería"), marcas.get("Tramontina"));

		agregar(dao, "PL-003", "Cinta de teflón 1/2\"", 4500.0, 150.0, "rollo",
				categorias.get("Plomería"), marcas.get("Pretul"));

		agregar(dao, "EL-001", "Cable unipolar 2.5mm (rollo x100m)", 650000.0, 8.0, "rollo",
				categorias.get("Electricidad"), marcas.get("Pretul"));

		agregar(dao, "EL-002", "Toma corriente doble con tierra", 18000.0, 60.0, "unidad",
				categorias.get("Electricidad"), marcas.get("Bosch"));

		agregar(dao, "MC-001", "Cemento Portland 50kg", 95000.0, 90.0, "bolsa",
				categorias.get("Materiales de Construcción"), marcas.get("Tramontina"));

		agregar(dao, "SH-001", "Guantes de cuero para trabajo (par)", 30000.0, 45.0, "par",
				categorias.get("Seguridad e Higiene"), marcas.get("Truper"));

		agregar(dao, "SH-002", "Lentes de seguridad transparentes", 20000.0, 55.0, "unidad",
				categorias.get("Seguridad e Higiene"), marcas.get("Urrea"));
	}

	private static void agregar(ProductoDAO dao, String codigo, String descripcion, Double precioVenta,
			Double stock, String unidadMedida, CategoriaModelo categoria, MarcaModelo marca) throws Exception {
		ProductoModelo producto = new ProductoModelo();
		producto.setCodigo(codigo);
		producto.setDescripcion(descripcion);
		producto.setPrecioVenta(precioVenta);
		producto.setStock(stock);
		producto.setUnidadMedida(unidadMedida);
		producto.setEstado(true);
		producto.setCategoria(categoria);
		producto.setMarca(marca);
		dao.guardar(producto);
	}

}
