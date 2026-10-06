package modelo;

// Esto dice si una venta o una compra se pagó de una sola vez
// (CONTADO) o si quedó como una deuda para pagar más adelante
// (CREDITO). Lo usan tanto Venta como Compra, por eso está en su
// propio archivo en vez de estar metido adentro de uno de los dos.
public enum FormaPago {
	CONTADO, CREDITO
}
