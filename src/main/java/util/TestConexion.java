package util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConexion {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/ToolSystem";
        String usuario = "postgres";
        String password = "12345";

        try {
            Connection conexion = DriverManager.getConnection(
                    url,
                    usuario,
                    password
            );

            System.out.println("=================================");
            System.out.println("CONEXIÓN EXITOSA A POSTGRESQL");
            System.out.println("=================================");

            System.out.println("Base de datos: "
                    + conexion.getCatalog());

            System.out.println("Usuario: "
                    + conexion.getMetaData().getUserName());

            conexion.close();

        } catch (SQLException e) {

            System.out.println("=================================");
            System.out.println("ERROR DE CONEXIÓN");
            System.out.println("=================================");

            e.printStackTrace();
        }
    }
}