/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dao;

import org.italiapizza.controller.exception.producto.EliminacionProductoException;
import org.italiapizza.controller.exception.producto.RegistroProductoException;
import org.italiapizza.model.connection.MySQLConnectionManager;
import org.italiapizza.model.dto.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yos
 */
public class ProductoDAO {

    public ProductoDAO() {
    }

    public int registrarProducto(Producto producto) throws RegistroProductoException {
        if (producto == null) {
            throw new RegistroProductoException("Producto nulo");
        }

        String sql = "INSERT INTO producto "
                + "(codigo, nombre, descripcion, precio, restriccion, foto, tipo_producto, estatus, unidad_medida, contenido)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            try (PreparedStatement ps = conn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, producto.getCodigo());
                ps.setString(2, producto.getNombre());
                ps.setString(3, producto.getDescripcion());
                ps.setDouble(4, producto.getPrecio());
                ps.setString(5, producto.getRestriccion());
                ps.setString(6, producto.getFoto());
                ps.setString(7, producto.getTipoProducto());
                ps.setInt(8, producto.getEstatus());
                ps.setString(9, producto.getUnidadMedida());
                ps.setDouble(10, producto.getContenido());

                ps.executeUpdate();
                return obtenerLlaveGenerada(ps);
            }
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                throw new RegistroProductoException("Código duplicado: " + producto.getCodigo(), e);
            }
            throw new RegistroProductoException("Error al registrar: " + e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void editarProducto(Producto producto) throws RegistroProductoException {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, restriccion = ?, foto = ?, tipo_producto = ?, unidad_medida = ?, contenido = ? WHERE id_producto = ?";
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setDouble(3, producto.getPrecio());
                ps.setString(4, producto.getRestriccion());
                ps.setString(5, producto.getFoto());
                ps.setString(6, producto.getTipoProducto());
                ps.setString(7, producto.getUnidadMedida());
                ps.setDouble(8, producto.getContenido());
                ps.setInt(9, producto.getIdProducto());
                if (ps.executeUpdate() == 0) {
                    throw new RegistroProductoException("Producto no encontrado");
                }
            }
        } catch (SQLException e) {
            throw new RegistroProductoException("Error al editar: " + e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminarProductoLogico(int idProducto) throws EliminacionProductoException {
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            try (PreparedStatement ps = conn.prepareStatement("UPDATE producto SET estatus = 0 WHERE id_producto = ?")) {
                ps.setInt(1, idProducto);
                if (ps.executeUpdate() == 0) {
                    throw new EliminacionProductoException("Producto no encontrado");
                }
            }
        } catch (SQLException e) {
            throw new EliminacionProductoException("Error en eliminación lógica", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminarProductoFisico(int idProducto) throws EliminacionProductoException {
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM producto WHERE id_producto = ?")) {
                ps.setInt(1, idProducto);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            if ("45000".equals(e.getSQLState())) {
                throw new EliminacionProductoException("Producto tiene pedidos asociados", e);
            }
            throw new EliminacionProductoException("Error al eliminar físicamente", e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(rs.getInt("id_producto"), rs.getString("codigo"), rs.getString("nombre"),
                rs.getString("descripcion"), rs.getDouble("precio"), rs.getString("restriccion"),
                rs.getString("foto"), rs.getString("tipo_producto"), rs.getInt("estatus"),
                rs.getString("unidad_medida"), rs.getDouble("contenido"));
    }

    private int obtenerLlaveGenerada(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No se generó ID");
        }
    }

    public Producto obtenerProductoPorId(int idProducto) throws RegistroProductoException {
        if (idProducto <= 0) {
            throw new RegistroProductoException("ID inválido");
        }

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();

            String sql = "SELECT id_producto, codigo, nombre, descripcion, precio, restriccion, "
                    + "foto, tipo_producto, estatus, unidad_medida, contenido "
                    + "FROM producto WHERE id_producto = ?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idProducto);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapearProducto(rs);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RegistroProductoException("Error al obtener: " + e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Producto> buscarPorNombreOCodigo(String termino) throws RegistroProductoException {
        if (termino == null) {
            termino = "";
        }

        List<Producto> productos = new ArrayList<>();
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT id_producto, codigo, nombre, descripcion, precio, restriccion, foto, tipo_producto, estatus, unidad_medida, contenido "
                    + "FROM producto WHERE (LOWER(nombre) LIKE LOWER(?) OR LOWER(codigo) LIKE LOWER(?)) AND estatus = 1";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String patron = "%" + termino + "%";
                ps.setString(1, patron);
                ps.setString(2, patron);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        productos.add(mapearProducto(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RegistroProductoException("Error al buscar productos: " + e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return productos;
    }

    public List<Producto> listarProductosActivos() throws RegistroProductoException {
        List<Producto> productos = new ArrayList<>();
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT id_producto, codigo, nombre, descripcion, precio, restriccion, foto, tipo_producto, estatus, unidad_medida, contenido "
                    + "FROM producto WHERE estatus = 1 ORDER BY nombre";

            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            throw new RegistroProductoException("Error al listar productos: " + e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return productos;
    }

    public List<Producto> listarMenu() throws RegistroProductoException {
        List<Producto> menu = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, descripcion, precio FROM vw_menu_productos";

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.conectarSegunUsuarioActivo();
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setEstatus(1);

                    menu.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RegistroProductoException("Error al cargar el menú de ventas: " + e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return menu;
    }

    public List<Producto> obtenerProductosActivos() throws Exception {
        List<Producto> lista = new ArrayList<>();
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            String sql = "SELECT id_producto, codigo, nombre, descripcion, precio FROM vw_menu_productos";
            try (PreparedStatement ps = conn.getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("id_producto"));
                    p.setCodigo(rs.getString("codigo"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecio(rs.getDouble("precio"));
                    lista.add(p);
                }
            }
        } finally {
            conn.close();
        }
        return lista;
    }
}
