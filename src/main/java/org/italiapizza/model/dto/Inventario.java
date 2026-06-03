/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dto;

/**
 * 
 * @author Yos
 */

public class Inventario {

    private int idInventario;
    private int idProducto;
    private String codigo;          
    private String nombre;          
    private double cantidadSistema;
    private double existenciaAlmacen;
    private double diferencia;
    private String unidadMedida;    

    public Inventario() {}

    public Inventario(int idInventario, int idProducto, double cantidadSistema,
                      double existenciaAlmacen, double diferencia) {
        this.idInventario = idInventario;
        this.idProducto = idProducto;
        this.cantidadSistema = cantidadSistema;
        this.existenciaAlmacen = existenciaAlmacen;
        this.diferencia = diferencia;
    }

    public int getIdInventario() { return idInventario; }
    public void setIdInventario(int idInventario) { this.idInventario = idInventario; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getCantidadSistema() { return cantidadSistema; }
    public void setCantidadSistema(double cantidadSistema) { this.cantidadSistema = cantidadSistema; }

    public double getExistenciaAlmacen() { return existenciaAlmacen; }
    public void setExistenciaAlmacen(double existenciaAlmacen) { this.existenciaAlmacen = existenciaAlmacen; }

    public double getDiferencia() { return diferencia; }
    public void setDiferencia(double diferencia) { this.diferencia = diferencia; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    // Utilidades para la Interfaz Gráfica :)

    public boolean hayFaltante() { 
        return diferencia < 0; 
    }
    
    public boolean haySobrante() { 
        return diferencia > 0; 
    }
    
    public boolean esCorrecto() { 
        return diferencia == 0; 
    }

    public String getEstadoTexto() {
        if (hayFaltante()) return "FALTANTE";
        if (haySobrante()) return "SOBRANTE";
        return "CORRECTO";
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nombre + " | Dif: " + diferencia + " (" + getEstadoTexto() + ")";
    }
}
