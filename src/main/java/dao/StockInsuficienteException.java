package dao;


 // Se lanza al intentar guardar una venta cuyo detalle pide más cantidad de
 // un producto que la disponible en stock. Se distingue de una excepción
 // genérica para que la capa de controlador pueda mostrarle al usuario el
 // mensaje puntual en vez de un error genérico de guardado.
 
public class StockInsuficienteException extends Exception {

	private static final long serialVersionUID = 1L;

	public StockInsuficienteException(String mensaje) {
		super(mensaje);
	}

}
