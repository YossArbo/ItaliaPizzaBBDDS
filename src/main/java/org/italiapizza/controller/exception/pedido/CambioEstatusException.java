/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.controller.exception.pedido;

import org.italiapizza.controller.exception.pedido.PedidoException;

/**
 * 
 * @author Yos
 */
public class CambioEstatusException extends PedidoException {

    public CambioEstatusException(String message) {
        super(message);
    }

    public CambioEstatusException(String message, Throwable cause) {
        super(message, cause);
    }
}