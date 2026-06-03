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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class PedidosViewController implements Initializable {

    @FXML
    private Label labelConsultarUsuario;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonCambiarEstatus;
    @FXML
    private Button buttonBitacora;
    @FXML
    private TextField textFieldBuscadorPedido;
    @FXML
    private TextField textFieldFecha;
    @FXML
    private ComboBox<?> comboBoxEstatus;
    @FXML
    private TableView<?> tableViewPedidos;
    @FXML
    private TableColumn<?, ?> tableColumnCodigo;
    @FXML
    private TableColumn<?, ?> tableColumnNombre;
    @FXML
    private TableColumn<?, ?> tableColumnDescripcion;
    @FXML
    private TableColumn<?, ?> tableColumnPrecio;
    @FXML
    private TableColumn<?, ?> tableColumnRestricciones;
    @FXML
    private TableColumn<?, ?> tableColumnUnidadMedida;
    @FXML
    private Button buttonNuevoProducto;
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
