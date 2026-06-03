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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class RegistroPedidoViewController implements Initializable {

    @FXML
    private Label labelRegistroPedido;
    @FXML
    private ComboBox<?> comboBoxProductos;
    @FXML
    private TextField textFieldCantidad;
    @FXML
    private Button buttonAgregarProducto;
    @FXML
    private ComboBox<?> comboBoxClientes;
    @FXML
    private ComboBox<?> comboBoxCajero;
    @FXML
    private VBox vBoxVistaCliente;
    @FXML
    private Label labelCliente;
    @FXML
    private Label labelTelefonos;
    @FXML
    private Label labelNombreProducto1;
    @FXML
    private Spinner<?> spinnerCantidad;
    @FXML
    private Label labelTotalAPagar;
    @FXML
    private TextField textFieldTotalAPagar;
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
