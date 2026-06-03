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
public class MenuPrincipalViewController implements Initializable {

    @FXML
    private Label labelMenuPrincipal;
    @FXML
    private Button buttonAdmin;
    @FXML
    private Button buttonPedidos;
    @FXML
    private Button buttonInventarios;
    @FXML
    private Button buttonAyuda;
    @FXML
    private Button buttonCerrarSesión;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
