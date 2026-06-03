/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dto;

public class Empleado extends Usuario {
    
    private String nombreUsuario;
    private String contrasena; // Almacenada hashada en BD
    private String rol; // "Administrador" o "Cajero"
    
    public Empleado() {
        super();
        this.tipoUsuario = "Empleado";
    }
    
    /**
     * Constructor para registrar un nuevo empleado en la interfaz
     * @param nombres
     * @param apellidos
     * @param telefono
     * @param email
     * @param nombreUsuario
     * @param contrasena
     * @param rol 
     */
    public Empleado(String nombres, String apellidos, String telefono, String email,
                    String nombreUsuario, String contrasena, String rol) {
        super(nombres, apellidos, telefono, email, "Empleado");
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }
    
    /**
     * Constructor para consultar un empleado ya existente en la BD
     * @param idUsuario
     * @param nombres
     * @param apellidos
     * @param telefono
     * @param email
     * @param estatus
     * @param nombreUsuario
     * @param rol 
     */
    public Empleado(int idUsuario, String nombres, String apellidos, String telefono,
                    String email, int estatus, String nombreUsuario, String rol) {
        super(idUsuario, nombres, apellidos, telefono, email, estatus, "Empleado");
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
    }
    
    // ========== Getters y Setters ==========
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    /**
     * Verifica si el empleado tiene rol de Administrador.
     * 
     * @return true si el rol es "Administrador"
     */
    public boolean esAdministrador() {
        return "Administrador".equalsIgnoreCase(rol);
    }
    
    /**
     * Verifica si el empleado tiene rol de Cajero.
     * 
     * @return true si el rol es "Cajero"
     */
    public boolean esCajero() {
        return "Cajero".equalsIgnoreCase(rol);
    }
    
    @Override
    public String toString() {
        return "Empleado{" +
                "idUsuario=" + getIdUsuario() +
                ", nombres='" + getNombres() + '\'' +
                ", apellidos='" + getApellidos() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", estatus=" + getEstatus() +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
    
}
