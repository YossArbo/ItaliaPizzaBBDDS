/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dto;


/**
 * 
 * @author Yos
 */
public class DetallePedido {

    private int idPedido;
    private int idProducto;
    private int unidades;
    private double precioUnitario;
    private String nombreProducto; 

    public DetallePedido() {
    }

    /**
     * Constructor para armar "tomar el pedido" antes de registrarlo
     * @param idProducto
     * @param unidades
     * @param precioUnitario 
     */
    public DetallePedido(int idProducto, int unidades, double precioUnitario) {
        this.idProducto = idProducto;
        this.unidades = unidades;
        this.precioUnitario = precioUnitario;
    }
    
    /**
     * Constructor para un pedido registrado en la BD.
     * @param idPedido
     * @param idProducto
     * @param unidades
     * @param precioUnitario 
     */
    public DetallePedido(int idPedido, int idProducto, int unidades, double precioUnitario) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.unidades = unidades;
        this.precioUnitario = precioUnitario;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getUnidades() { return unidades; }
    public void setUnidades(int unidades) { this.unidades = unidades; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public double getSubtotal() {
        return unidades * precioUnitario;
    }

    @Override
    public String toString() {
        String nombreDisplay = (nombreProducto != null) ? nombreProducto : "Prod ID:" + idProducto;
        return unidades + "x " + nombreDisplay + " | Unitario: $" + precioUnitario + " | Subtotal: $" + getSubtotal();
    }
}