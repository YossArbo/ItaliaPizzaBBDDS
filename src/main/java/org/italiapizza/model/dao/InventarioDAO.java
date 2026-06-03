/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dao;

import org.italiapizza.controller.exception.inventario.InventarioException;
import org.italiapizza.model.connection.MySQLConnectionManager;
import org.italiapizza.model.dto.Inventario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Yos
 */
public class InventarioDAO {

    public InventarioDAO() {}

    public Inventario validarInventarioFisico(int idProducto, double cantidadFisica) throws InventarioException {
        if (idProducto <= 0 || cantidadFisica < 0) throw new InventarioException("Datos inválidos");

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            try (CallableStatement cs = conn.getConnection().prepareCall("{CALL sp_validar_inventario(?, ?)}")) {
                cs.setInt(1, idProducto);
                cs.setDouble(2, cantidadFisica);
                cs.execute();
            }
            
            //Releer la fila ya calculada por el trigger
            Inventario actualizado = obtenerInventarioPorProducto(conn, idProducto);
            if (actualizado == null) throw new InventarioException("No existe registro para ID " + idProducto);
            return actualizado;
        } catch (SQLException e) {
            throw new InventarioException("Error al validar inventario: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Inventario> listarReporteInventario() throws InventarioException {
        String sql = "SELECT i.id_inventario, i.id_producto, i.cantidad_sistema, i.existencia_almacen, i.diferencia, p.codigo, p.nombre, p.unidad_medida " +
                     "FROM inventario i JOIN producto p ON i.id_producto = p.id_producto WHERE p.estatus = 1 ORDER BY p.nombre";

        MySQLConnectionManager conn = MySQLConnectionManager.getInstance();
        try {
            conn.connectionAdmin();
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                List<Inventario> reporte = new ArrayList<>();
                while (rs.next()) { reporte.add(mapearReporte(rs)); }
                return reporte;
            }
        } catch (SQLException e) {
            throw new InventarioException("Error al listar reporte: " + e.getMessage(), e);
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private Inventario obtenerInventarioPorProducto(MySQLConnectionManager conn, int idProducto) throws SQLException {
        String sql = "SELECT i.id_inventario, i.id_producto, i.cantidad_sistema, i.existencia_almacen, i.diferencia, p.codigo, p.nombre, p.unidad_medida " +
                     "FROM inventario i JOIN producto p ON i.id_producto = p.id_producto WHERE i.id_producto = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Inventario inv = new Inventario(rs.getInt("id_inventario"), rs.getInt("id_producto"), 
                                    rs.getDouble("cantidad_sistema"), rs.getDouble("existencia_almacen"), rs.getDouble("diferencia"));
                    inv.setCodigo(rs.getString("codigo")); inv.setNombre(rs.getString("nombre")); inv.setUnidadMedida(rs.getString("unidad_medida"));
                    return inv;
                }
            }
        }
        return null;
    }

    /**
     * Convertir/traducir datos de un formato a otro requerido
     * @param rs
     * @return
     * @throws SQLException 
     */
    private Inventario mapearReporte(ResultSet rs) throws SQLException {
        Inventario inv = new Inventario(rs.getInt("id_inventario"), 
                        rs.getInt("id_producto"), 
                        rs.getDouble("cantidad_sistema"), 
                        rs.getDouble("existencia_almacen"), 
                        rs.getDouble("diferencia"));
                        inv.setCodigo(rs.getString("codigo")); 
                        inv.setNombre(rs.getString("nombre")); 
                        inv.setUnidadMedida(rs.getString("unidad_medida"));
                        return inv;
    }
}