# Diagrama de Entidad y Relación — Ferretería San Martín (Sistema de Ventas)

Este diagrama refleja las entidades reales del código (`src/main/java/modelo`),
adaptadas al contexto de una ferretería.

```mermaid
erDiagram
  MARCA ||--o{ PRODUCTO : tiene
  CATEGORIA ||--o{ PRODUCTO : clasifica
  CLIENTE ||--o{ VENTA : realiza
  VENTA ||--|{ VENTA_DETALLE : contiene
  PRODUCTO ||--o{ VENTA_DETALLE : "incluido en"

  PROVEEDOR {
    int id PK
    string razonSocial
    string nombreFantasia
    string ruc UK
    string telefono
    string correo
    string direccion
    date fechaRegistro
  }
  CLIENTE {
    int id PK
    string nombre
    string apellido
    string documento UK
    string telefono
    string correo
    string direccion
    date fechaNacimiento
    date fechaRegistro
  }
  CATEGORIA {
    int id PK
    string nombre
    boolean estado
  }
  MARCA {
    int id PK
    string nombre
    boolean estado
  }
  PRODUCTO {
    int id PK
    string descripcion
    string codigo UK
    double precioVenta
    double stock
    string unidadMedida
    boolean estado
    int categoria_id FK
    int marca_id FK
  }
  VENTA {
    int id PK
    date fecha
    double total
    date fechaRegistro
    boolean anulada
    string observacion
    int cliente_id FK
  }
  VENTA_DETALLE {
    int id PK
    double cantidad
    double precio
    int producto_id FK
    int venta_id FK
  }
```

## Notas sobre los cambios respecto al diagrama original

- **`funcionario` → no existe en el código actual.** El código real no tiene
  esa entidad; en su lugar existe `PROVEEDOR`, que hoy es un catálogo
  independiente (sin relación directa con `PRODUCTO` todavía).
- **`ProductoModelo`** ahora incluye `stock` y `unidadMedida` (unidad, kg,
  metro, bolsa, rollo, etc.), útil para artículos de ferretería que se venden
  por peso, longitud o packs.
- **`ClienteModelo`** incluye `fechaNacimiento` y `fechaRegistro`, además de
  los campos de contacto.
- **`VentaModelo`** incluye `anulada` (para anular una venta) y
  `observacion` (texto libre), y se relaciona 1—N con `VENTA_DETALLE`.
- Los nombres de tabla reales (anotación `@Entity(name=...)`) son:
  `tb_categorias`, `tb_clientes`, `tb_marcas`, `tb_productos`,
  `tb_proveedores`, `tb_ventas`, `tb_venta_detalle`.
