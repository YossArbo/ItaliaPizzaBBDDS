package org.italiapizza.controller;

import org.italiapizza.controller.exception.usuario.LoginException;
import org.italiapizza.model.dto.Empleado;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class LoginViewController {

    @FXML
    private TextField textFieldUsuario;
    @FXML
    private PasswordField textFieldContrasenia;
    @FXML
    private Button buttonLogin;
    
    private final LoginController loginController = new LoginController();

    @FXML
    private void handleLoginAction() {
        String usuario = textFieldUsuario.getText();
        String pass = textFieldContrasenia.getText();
        
        try {
            Empleado empleado = loginController.autenticar(usuario, pass);
            
            mostrarAlerta("Éxito", "Bienvenido, " + empleado.getNombres(), Alert.AlertType.INFORMATION);
                        
        } catch (LoginException e) {
            mostrarAlerta("Error de Acceso", e.getMessage(), Alert.AlertType.ERROR);
        }
    }    
        
    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
            Alert alert = new Alert(tipo);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(contenido);
            alert.showAndWait();
        }
    }
