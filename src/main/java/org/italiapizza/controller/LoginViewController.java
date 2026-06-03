package org.italiapizza.controller;

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
public class LoginViewController implements Initializable {

    @FXML
    private Label labelInicioDeSesión;
    @FXML
    private TextField textFieldUsuario;
    @FXML
    private TextField textFieldContrasenia;
    @FXML
    private Button buttonLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
