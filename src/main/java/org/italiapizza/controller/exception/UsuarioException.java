/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.controller.exception;

/**
 * @author Yoss
 */
public class UsuarioException extends RuntimeException {
    public UsuarioException(String message) {
        super(message);
    }
    
    public UsuarioException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
