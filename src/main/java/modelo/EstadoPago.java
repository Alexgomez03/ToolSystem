package modelo;

// Esto no se guarda en la base de datos: se calcula al vuelo (ver
// VentaModelo.getEstadoPago y CompraModelo.getEstadoPago) a partir de
// la forma de pago y de cuánto se pagó hasta ahora. Sirve para mostrar
// en las listas si una venta/compra a crédito ya se cobró/pagó del
// todo, en parte, o nada todavía.
public enum EstadoPago {
	PAGADO, PAGO_PARCIAL, PENDIENTE;

	// Devuelve el texto que se muestra en las tablas, en vez de mostrar
	// el nombre "crudo" del enum (PAGO_PARCIAL en vez de "Pago Parcial").
	public String getTextoParaMostrar() {
		switch (this) {
		case PAGADO:
			return "Pagado";
		case PAGO_PARCIAL:
			return "Pago Parcial";
		default:
			return "Pendiente";
		}
	}
}
