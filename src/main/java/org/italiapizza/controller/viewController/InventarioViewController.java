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
public class InventarioViewController implements Initializable {

    @FXML
    private Label labelConsultarUsuario;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonEliminar;
    @FXML
    private Button buttonExportar;
    @FXML
    private TextField textFieldBuscadorProducto;
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
