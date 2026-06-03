/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dao;

import org.italiapizza.controller.exception.*;
import org.italiapizza.model.connection.MySQLConnectionManager;
import org.italiapizza.model.dto.*;
import java.sql.*;
import java.util.*;

/**
 * 
 * @author Yos
 */
public class PedidoDAO {

    private static final List<String> ESTATUS_VALIDOS = Arrays.asList("Entregado", "En Proceso", "Cancelado");

    public PedidoDAO() {}

    public int registrarNuevoPedido(Integer idCliente, int idEmpleado, List<DetallePedido> detalles) 
            throws RegistroPedidoException {

        if (detalles == null || detalles.isEmpty()) throw new RegistroPedidoException("El pedido debe tener al menos un producto");
        
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            conn.getConnection().setAutoCommit(false);

            int idPedido;
            try (CallableStatement cs = conn.getConnection().prepareCall("{CALL sp_registrar_pedido(?, ?, ?)}")) {
                if (idCliente != null) cs.setInt(1, idCliente); else cs.setNull(1, Types.INTEGER);
                cs.setInt(2, idEmpleado);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.execute();
                idPedido = cs.getInt(3);
            }

            String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, unidades, precio_unitario) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.getConnection().prepareStatement(sqlDetalle)) {
                for (DetallePedido d : detalles) {
                    ps.setInt(1, idPedido);
                    ps.setInt(2, d.getIdProducto());
                    ps.setInt(3, d.getUnidades());
                    ps.setDouble(4, d.getPrecioUnitario());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.getConnection().commit();
            return idPedido;
        } catch (SQLException e) {
            try { conn.getConnection().rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RegistroPedidoException("Error en transacción: " + e.getMessage(), e);
        } finally {
            try { conn.getConnection().setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void cambiarEstatus(int idPedido, String nuevoEstatus, int idEmpleadoCambio) throws CambioEstatusException {
        if (!ESTATUS_VALIDOS.contains(nuevoEstatus)) throw new CambioEstatusException("Estatus inválido");

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            conn.getConnection().setAutoCommit(false);

            try (PreparedStatement ps = conn.getConnection().prepareStatement("UPDATE pedido SET estatus = ? WHERE id_pedido = ?")) {
                ps.setString(1, nuevoEstatus);
                ps.setInt(2, idPedido);
                if (ps.executeUpdate() == 0) throw new CambioEstatusException("Pedido no encontrado");
            }

            try (PreparedStatement ps = conn.getConnection().prepareStatement("INSERT INTO bitacora (estatus, id_pedido, id_empleado) VALUES (?, ?, ?)")) {
                ps.setString(1, nuevoEstatus);
                ps.setInt(2, idPedido);
                ps.setInt(3, idEmpleadoCambio);
                ps.executeUpdate();
            }

            conn.getConnection().commit();
        } catch (SQLException e) {
            try { conn.getConnection().rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new CambioEstatusException("Error al cambiar estatus: " + e.getMessage(), e);
        } finally {
            try { conn.getConnection().setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // ========== MÉTODOS DE APOYO Y MAPEO ==========

    public Pedido obtenerPedidoPorId(int idPedido) throws PedidoException {
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            Pedido pedido = null;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM pedido WHERE id_pedido = ?")) {
                ps.setInt(1, idPedido);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) pedido = mapearPedido(rs);
                }
            }
            if (pedido != null) pedido.setDetalles(obtenerDetalles(conn, idPedido));
            return pedido;
        } catch (SQLException e) {
            throw new PedidoException("Error al obtener: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private List<DetallePedido> obtenerDetalles(MySQLConnectionManager conn, int idPedido) throws SQLException {
        String sql = "SELECT dp.*, p.nombre FROM detalle_pedido dp JOIN producto p ON dp.id_producto = p.id_producto WHERE dp.id_pedido = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                List<DetallePedido> lista = new ArrayList<>();
                while (rs.next()) {
                    DetallePedido d = new DetallePedido(rs.getInt("id_pedido"), rs.getInt("id_producto"), 
                                    rs.getInt("unidades"), rs.getDouble("precio_unitario"));
                    d.setNombreProducto(rs.getString("nombre"));
                    lista.add(d);
                }
                return lista;
            }
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("fecha_pedido");
        return new Pedido(rs.getInt("id_pedido"), ts != null ? ts.toLocalDateTime() : null,
                rs.getDouble("monto_total"), rs.getString("estatus"),
                (Integer) rs.getObject("id_cliente"), rs.getInt("id_empleado"));
    }
}