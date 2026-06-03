package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class AdministracionViewController implements Initializable {

    @FXML
    private Label labelAdministracion;
    @FXML
    private Button buttonUsuarios;
    @FXML
    private Button buttonProductos;
    @FXML
    private Button buttonNegocio;
    @FXML
    private Button buttonAcercaDe;
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
