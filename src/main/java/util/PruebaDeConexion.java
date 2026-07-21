package util;

import dao.CategoriaDAO;
import modelo.CategoriaModelo;

public class PruebaDeConexion {
	
	public static void main(String[] args) {
		CategoriaModelo categoria = new CategoriaModelo();
		categoria.setNombre("Informática");
		categoria.setEstado(true);
		
		CategoriaDAO dao = new CategoriaDAO();
		try {
			dao.guardar(categoria);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
