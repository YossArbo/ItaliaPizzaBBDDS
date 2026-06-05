/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Yos
 */
public class Pedido {

    private int idPedido;
    private LocalDateTime fechaPedido;
    private double montoTotal;
    private String estatus;          
    private Integer idCliente;       
    private int idEmpleado;

    private List<DetallePedido> detalles;

    /**
     * Constructorque Inicializa la lista para evitar NullPointerException
     */
    public Pedido() {
        this.detalles = new ArrayList<>();
        this.estatus = "En Proceso";
    }

    public Pedido(int idPedido, LocalDateTime fechaPedido, double montoTotal,
                  String estatus, Integer idCliente, int idEmpleado) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.montoTotal = montoTotal;
        this.estatus = estatus;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.detalles = new ArrayList<>();
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    public void agregarDetalle(DetallePedido detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
    }

    @Override
    public String toString() {
        return "Pedido #" + idPedido + " - " + estatus + " ($" + montoTotal + ") | Items: " + 
               (detalles != null ? detalles.size() : 0);
    }
}