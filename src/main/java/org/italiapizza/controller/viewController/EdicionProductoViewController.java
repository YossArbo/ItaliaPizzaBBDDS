package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class EdicionProductoViewController implements Initializable {

    @FXML
    private Label labelRegistroDeProducto;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldCódigo;
    @FXML
    private TextArea textAreaDescripcion;
    @FXML
    private TextField textFieldPrecio;
    @FXML
    private TextField textFieldRestricciones;
    @FXML
    private TextField textFieldContenido;
    @FXML
    private ComboBox<?> comboBoxTipoProducto;
    @FXML
    private ComboBox<?> comboBoxUnidadMedida;
    @FXML
    private Button buttonSeleccionarFoto;
    @FXML
    private TextField textFieldUrlFoto;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonGuardarCambios;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
