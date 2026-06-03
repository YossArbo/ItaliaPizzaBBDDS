/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.model.dto;

/**
 * @author Yoss
 */
public class Usuario {
    
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private int estatus; // 1 = activo, 0 = inactivo
    private String tipoUsuario; // "Cliente" o "Empleado"
    
    public Usuario() {
        this.estatus = 1; // Activo por defecto
    }
    
    public Usuario(String nombres, String apellidos, String telefono, String email, String tipoUsuario) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        this.estatus = 1;
    }
    
    public Usuario(int idUsuario, String nombres, String apellidos, String telefono, 
                   String email, int estatus, String tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.estatus = estatus;
        this.tipoUsuario = tipoUsuario;
    }
    
    // ========== Getters y Setters ==========
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidos() {
        return apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getEstatus() {
        return estatus;
    }
    
    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }
    
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
    
    public boolean estaActivo() {
        return estatus == 1;
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", estatus=" + estatus +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
    
}
