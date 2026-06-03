/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class RegistroUsuarioViewController implements Initializable {

    @FXML
    private Label labelRegistroUsuario;
    @FXML
    private TextField textFieldNombres;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private Button buttonAgregarTelefono;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private Button buttonAgregarEmail;
    @FXML
    private Label labelNombreCompleto;
    @FXML
    private Label labelTelefonos;
    @FXML
    private Label labelTelefono1;
    @FXML
    private Button buttonQuitarTelefono1;
    @FXML
    private Label labelEmails;
    @FXML
    private Label labelEmail1;
    @FXML
    private Button buttonQuitarEmail1;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonRegistrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
