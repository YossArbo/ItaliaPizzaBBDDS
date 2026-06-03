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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class MenuUsuarioViewController implements Initializable {

    @FXML
    private Label labelConsultarUsuario;
    @FXML
    private TableColumn<?, ?> tableColumnNombreCompleto;
    @FXML
    private TableColumn<?, ?> tableColumnTelefono;
    @FXML
    private TableColumn<?, ?> tableColumnEmail;
    @FXML
    private TableColumn<?, ?> tableColumnTipoUsuario;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonEliminar;
    @FXML
    private TextField textFieldBuscadorUsuario;
    @FXML
    private Button buttonNuevoUsuario;
    @FXML
    private Button buttonVolverAlMenu;
    @FXML
    private Button buttonRegresar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
