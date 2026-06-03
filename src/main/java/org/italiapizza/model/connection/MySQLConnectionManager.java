/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.italiapizza.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author Yoss
 */
public class MySQLConnectionManager {
    
    private static MySQLConnectionManager instancia;
    private Connection connection;
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/italiapizzabbdds?useTimezone=true&serverTimezone=UTC";
    
    
    private MySQLConnectionManager() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver MySQL: " + e.getMessage(), e);
        }
        
    }
    
    /**
     * Obtiene la instancia única de MySQLConnectionManager (Singleton)
     * @return la instancia única de MySQLConnectionManager 
     */
    public static synchronized MySQLConnectionManager getInstance() {
        if (instancia == null) {
            instancia = new MySQLConnectionManager();
        }
        return instancia;
    }
    
    /**
     * Recibe las credenciales segun el rol
     * @param user
     * @param password
     * @throws SQLException 
     */
    private void connect(String user, String password) throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, user, password);
        }
    }
    
    /**
     * Administrador
     * @throws SQLException 
     */
    public void connectionAdmin() throws SQLException {
        connect("admin_pizza", "AdminPizza@1234");
    }
    
    /**
     * Cajero
     * @throws SQLException 
     */
    public void connectionCajero() throws SQLException {
        connect("cajero_pizza", "Cajer@Pizza0987");
    }
    
    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
    
    
    /**
     * Cerrar conexion
     * @throws SQLException 
     */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    public Connection getConnection() {
        return this.connection;
    }
}