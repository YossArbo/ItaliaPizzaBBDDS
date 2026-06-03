/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.controller.exception;

public class LoginException extends UsuarioException {
    
    public LoginException(String message) {
        super(message);
    }
   
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
    
}