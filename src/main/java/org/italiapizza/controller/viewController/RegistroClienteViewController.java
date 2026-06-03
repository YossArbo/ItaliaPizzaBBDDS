package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class RegistroClienteViewController implements Initializable {

    @FXML
    private Label labelDireccionCliente;
    @FXML
    private TextField textFieldCodigoPostal;
    @FXML
    private TextField textFieldCiudad;
    @FXML
    private TextField textFieldCalle;
    @FXML
    private TextField textFieldNoExterior;
    @FXML
    private VBox vBoxVistaCliente;
    @FXML
    private Label labelCliente;
    @FXML
    private Label labelNombreCompleto;
    @FXML
    private Label labelTelefonos;
    @FXML
    private Label labelTelefono1;
    @FXML
    private Label labelEmails;
    @FXML
    private Label labelEmail1;
    @FXML
    private Label labelDireccion;
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
