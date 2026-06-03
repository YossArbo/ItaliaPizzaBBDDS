/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.controller.exception;

/**
 * 
 * @author Yos
 */
public class RegistroPedidoException extends PedidoException {

    public RegistroPedidoException(String message) {
        super(message);
    }

    public RegistroPedidoException(String message, Throwable cause) {
        super(message, cause);
    }
}