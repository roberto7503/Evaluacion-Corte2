package org.example.evaluacioncorte2.DAO;

import org.example.evaluacioncorte2.connection.ConexionBD;
import org.example.evaluacioncorte2.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> buscarConFiltros(String texto, String categoria, boolean soloConStock) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id_producto, codigo, nombre, categoria, precio, stock FROM registros.producto WHERE 1=1 ");
        List<Object> parametros = new ArrayList<>();

        if (texto != null && !texto.trim().isEmpty()) {
            sql.append("AND (codigo ILIKE ? OR nombre ILIKE ?) ");
            String patron = "%" + texto.trim() + "%";
            parametros.add(patron);
            parametros.add(patron);
        }

        if (categoria != null && !categoria.equalsIgnoreCase("Todas") && !categoria.trim().isEmpty()) {
            sql.append("AND categoria = ? ");
            parametros.add(categoria);
        }

        if (soloConStock) {
            sql.append("AND stock > 0 ");
        }

        sql.append("ORDER BY codigo ASC");

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock")
                    ));
                }
            }
        }
        return lista;
    }

    public boolean insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO registros.producto (codigo, nombre, categoria, precio, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Producto p) throws SQLException {
        String sql = "UPDATE registros.producto SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ? WHERE id_producto = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setBigDecimal(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.setInt(6, p.getIdProducto());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idProducto) throws SQLException {
        String sql = "DELETE FROM registros.producto WHERE id_producto = ?";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            return ps.executeUpdate() > 0; // Control E3
        }
    }
}