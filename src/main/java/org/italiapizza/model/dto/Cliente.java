/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dto;

/**
 *
 * @author Yos
 */

public class Cliente extends Usuario {
    
    private String calle;
    private String numero;
    private String ciudad;
    private String codigoPostal;
    
    public Cliente() {
        super();
        this.tipoUsuario = "Cliente";
    }
    
    /**
     * Constructor para registrar un nuevo cliente
     * @param nombres
     * @param apellidos
     * @param telefono
     * @param email
     * @param calle
     * @param numero
     * @param ciudad
     * @param codigoPostal 
     */
    public Cliente(String nombres, String apellidos, String telefono, String email,
                   String calle, String numero, String ciudad, String codigoPostal) {
        super(nombres, apellidos, telefono, email, "Cliente");
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }
    
/**
 * Constructor para consultar un cliente registrado en la BD
 * @param idUsuario
 * @param nombres
 * @param apellidos
 * @param telefono
 * @param email
 * @param estatus
 * @param calle
 * @param numero
 * @param ciudad
 * @param codigoPostal 
 */
    public Cliente(int idUsuario, String nombres, String apellidos, String telefono,
                   String email, int estatus, String calle, String numero,
                   String ciudad, String codigoPostal) {
        super(idUsuario, nombres, apellidos, telefono, email, estatus, "Cliente");
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }
    
    // ========== Getters, Setters y Utilidades ==========
    
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    
    public String getDireccionCompleta() {
        return calle + " " + numero + ", " + ciudad + ", " + codigoPostal;
    }
    
    @Override
    public String toString() {
        return "Cliente: " + getNombres() + " " + getApellidos() + " | Tel: " + getTelefono();
    }
}
