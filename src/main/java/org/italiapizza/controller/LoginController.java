/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.italiapizza.controller;

import org.italiapizza.controller.exception.LoginException;
import org.italiapizza.model.dao.UsuarioDAO;
import org.italiapizza.model.dto.Empleado;

/**
 * Logica de autenticacion
 * @author Yos
 */
public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autenticar a un empleado
     * @param nombreUsuario
     * @param password
     * @return
     * @throws LoginException 
     */
    public Empleado autenticar(String nombreUsuario, String password) throws LoginException {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new LoginException("El nombre de usuario no puede estar vacío.");
        }
        if (password == null || password.isEmpty()) {
            throw new LoginException("La contraseña no puede estar vacía.");
        }

        return usuarioDAO.login(nombreUsuario, password);
    }
}