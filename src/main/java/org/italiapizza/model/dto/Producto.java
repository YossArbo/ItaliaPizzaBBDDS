/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dto;

/**
 * 
 * @author Yos
 */
public class Producto {

    private int idProducto;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private String restriccion;     
    private String foto;     //VARCHAR || null       
    private String tipoProducto;   // 'Preparado', 'Insumo', 'Importado'
    private int estatus;         // 1 = activo, 0 = eliminado lógicamente    
    private String unidadMedida;   // 'gr', 'ml', 'piezas', 'kg', 'lt'
    private double contenido;

    /**
     * Constructor vacío (Estatus activo por defecto)
     */
    public Producto() {
        this.estatus = 1;
    }

    /**
     * Constructor para registrar un nuevo producto
     * @param codigo
     * @param nombre
     * @param descripcion
     * @param precio
     * @param restriccion
     * @param foto
     * @param tipoProducto
     * @param unidadMedida
     * @param contenido 
     */
    public Producto(String codigo, String nombre, String descripcion, double precio,
                    String restriccion, String foto, String tipoProducto,
                    String unidadMedida, double contenido) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.restriccion = restriccion;
        this.foto = foto;
        this.tipoProducto = tipoProducto;
        this.unidadMedida = unidadMedida;
        this.contenido = contenido;
        this.estatus = 1;
    }

    public Producto(String codigo, String nombre, String descripcion, double precio,
            String restriccion, String unidadMedida) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.restriccion = restriccion;
        this.unidadMedida = unidadMedida;
    }
    
    public Producto(int idProducto, String codigo, String nombre, String descripcion,
                    double precio, String restriccion, String foto, String tipoProducto,
                    int estatus, String unidadMedida, double contenido) {
        this.idProducto = idProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.restriccion = restriccion;
        this.foto = foto;
        this.tipoProducto = tipoProducto;
        this.estatus = estatus;
        this.unidadMedida = unidadMedida;
        this.contenido = contenido;
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getRestriccion() { return restriccion; }
    public void setRestriccion(String restriccion) { this.restriccion = restriccion; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }

    public int getEstatus() { return estatus; }
    public void setEstatus(int estatus) { this.estatus = estatus; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public double getContenido() { return contenido; }
    public void setContenido(double contenido) { this.contenido = contenido; }

    public boolean estaActivo() {
        return estatus == 1;
    }

    @Override
    public String toString() {
        return "[" + codigo + "] " + nombre + " - $" + precio;
    }
}