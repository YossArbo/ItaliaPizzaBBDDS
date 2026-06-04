/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dao;

import org.italiapizza.controller.exception.pedido.CambioEstatusException;
import org.italiapizza.controller.exception.pedido.RegistroPedidoException;
import org.italiapizza.controller.exception.pedido.PedidoException;
import org.italiapizza.model.connection.MySQLConnectionManager;
import org.italiapizza.model.dto.*;
import java.sql.*;
import java.time.LocalDate;
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
    
        MySQLConnectionManager connManager = MySQLConnectionManager.getInstance();
        Connection conexionDB = null; 
        
        try {
            
            connManager.conectarSegunUsuarioActivo();
            conexionDB = connManager.getConnection();
            conexionDB.setAutoCommit(false);
        
            if (detalles == null || detalles.isEmpty()) {
                throw new RegistroPedidoException("El pedido debe tener al menos un producto");
            }
            
            int idPedido;
            String sqlEncabezado = "{CALL sp_registrar_pedido(?, ?, ?)}";
            try (CallableStatement cs = conexionDB.prepareCall(sqlEncabezado)) {
                if (idCliente != null) {
                    cs.setInt(1, idCliente); 
                } else {
                    cs.setNull(1, Types.INTEGER);
                }
                cs.setInt(2, idEmpleado);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.execute();
                idPedido = cs.getInt(3);
        }

        
            String sqlDetalle = "INSERT INTO detalle_pedido (id_pedido, id_producto, unidades, precio_unitario) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conexionDB.prepareStatement(sqlDetalle)) {
                for (DetallePedido d : detalles) {
                    ps.setInt(1, idPedido);
                    ps.setInt(2, d.getIdProducto());
                    ps.setInt(3, d.getUnidades());
                    ps.setDouble(4, d.getPrecioUnitario());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String sqlBitacora = "INSERT INTO bitacora (estatus, id_pedido, id_empleado) VALUES (?, ?, ?)";
            try (PreparedStatement psBitacora = conexionDB.prepareStatement(sqlBitacora)) {
                psBitacora.setString(1, "En Proceso"); 
                psBitacora.setInt(2, idPedido);
                psBitacora.setInt(3, idEmpleado);
                psBitacora.executeUpdate();
            }
            
            conexionDB.commit();
            return idPedido;

        } catch (SQLException e) {
            if (conexionDB != null) {
                try { conexionDB.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RegistroPedidoException("Error en transacción: " + e.getMessage(), e);
        } finally {
            if (conexionDB != null) {
                try { conexionDB.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
            }
            try { connManager.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }   

    public void cambiarEstatus(int idPedido, String nuevoEstatus, int idEmpleadoCambio) throws CambioEstatusException {
        if (!ESTATUS_VALIDOS.contains(nuevoEstatus)) throw new CambioEstatusException("Estatus inválido");

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.conectarSegunUsuarioActivo();
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

    public Pedido obtenerPedidoPorId(int idPedido) throws PedidoException {
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.conectarSegunUsuarioActivo();
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
    
    /**
     * Buscar por fecha, estatus o usuarios
     * @param idCliente
     * @param fecha
     * @param estatus
     * @return
     * @throws PedidoException 
     */
    public List<Pedido> buscarPedidos(Integer idCliente, String fecha, String estatus) throws PedidoException {
        List<Pedido> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM pedido WHERE 1=1 ");
        List<Object> parametros = new ArrayList<>();

        if (idCliente != null && idCliente > 0) {
            sql.append("AND id_cliente = ? ");
            parametros.add(idCliente);
        }
        if (fecha != null && !fecha.trim().isEmpty()) {
            sql.append("AND DATE(fecha_pedido) = ? "); 
            parametros.add(fecha);
        }
        if (estatus != null && !estatus.trim().isEmpty()) {
            sql.append("AND LOWER(estatus) = LOWER(?) ");
            parametros.add(estatus);
        }

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.conectarSegunUsuarioActivo();
            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < parametros.size(); i++) {
                    ps.setObject(i + 1, parametros.get(i));
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearPedido(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new PedidoException("Error al buscar pedidos: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
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
    
    
    public void quitarProductoDePedido(int idPedido, int idProducto) throws PedidoException {
        String sql = "DELETE FROM detalle_pedido WHERE id_pedido = ? AND id_producto =?";
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        
        try{
            conn.conectarSegunUsuarioActivo();
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idPedido);
                ps.setInt(2, idProducto);
                
                int filas = ps.executeUpdate();
                if (filas == 0) {
                    throw new PedidoException("El producto no esta registrado en este pedido.");
                }
            }
        }catch (SQLException e) {
            throw new PedidoException("Hubo un error al quitar el producto del pedido: " + e.getMessage(), e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void agregarProductoAPedido(int idPedido, int idProducto, int unidades, double precioUnitario) throws PedidoException {
        String sql = "INSERT INTO detalle_pedido (id_pedido, id_producto, unidades, precio_unitario) VALUES (?, ?, ?, ?)";
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        
        try {
            conn.conectarSegunUsuarioActivo();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idPedido);
                ps.setInt(2, idProducto);
                ps.setInt(3, unidades);
                ps.setDouble(4, precioUnitario);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new PedidoException("Error al agregar producto al pedido: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    public List<BitacoraPedido> obtenerBitacora(int idPedido) throws PedidoException {
        List<BitacoraPedido> historial = new ArrayList<>();
        String sql = "SELECT b.estatus, b.fecha, u.nombre " + 
                     "FROM bitacora b " +
                     "INNER JOIN usuario u ON b.id_empleado = u.id_usuario " +
                     "WHERE b.id_pedido = ? ORDER BY b.fecha DESC";
        
        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.conectarSegunUsuarioActivo();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idPedido);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        BitacoraPedido bp = new BitacoraPedido();
                        bp.setEstatus(rs.getString("estatus"));
                        bp.setFecha(rs.getTimestamp("fecha").toString()); 
                        bp.setNombreEmpleado(rs.getString("nombre"));
                        historial.add(bp);
                    }
                }
            }
        } catch (SQLException e) {
            throw new PedidoException("Error al consultar bitácora: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return historial;
    }
}