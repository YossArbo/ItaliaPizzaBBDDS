/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.controller.exception;

/**
 * 
 * @author Yos
 */
public class RegistroProductoException extends ProductoException {

    public RegistroProductoException(String message) {
        super(message);
    }

    public RegistroProductoException(String message, Throwable cause) {
        super(message, cause);
    }
}