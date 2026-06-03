/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.controller.exception.csv;

/**
 *
 * @author Yos
 */
public class ExportacionException extends RuntimeException {

    public ExportacionException(String message) {
        super(message);
    }

    public ExportacionException(String message, Throwable cause) {
        super(message, cause);
    }
}
