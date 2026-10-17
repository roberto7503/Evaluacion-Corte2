package org.example.evaluacioncorte2.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConexionBD.class.getResourceAsStream("/org/example/evaluacioncorte2/database.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                System.err.println("[AVISO] No se encontró el archivo database.properties en la ruta configurada.");
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error al leer database.properties: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/papeleria_campus?currentSchema=registros");
        String user = props.getProperty("db.user", "postgres");
        String password = props.getProperty("db.password", "");

        return DriverManager.getConnection(url, user, password);
    }
}