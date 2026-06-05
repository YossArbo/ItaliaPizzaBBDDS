package org.italiapizza.controller.viewController;

import org.italiapizza.controller.exception.usuario.LoginException;
import org.italiapizza.model.dto.Empleado;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.italiapizza.controller.LoginController;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.SessionManager;
import org.italiapizza.utils.WindowManager;



public class LoginViewController {

    @FXML
    private TextField textFieldUsuario;
    @FXML
    private PasswordField passwordFieldContrasenia;
    @FXML
    private Button buttonLogin;

    private final LoginController loginController = new LoginController();

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String usuario = textFieldUsuario.getText();
        String pass = passwordFieldContrasenia.getText();
        
        try {
            Empleado empleado = loginController.autenticar(usuario, pass);
            SessionManager.setEmpleadoActual(empleado);
            AlertManager.mostrarAlerta("Éxito", "Bienvenido, " + empleado.getNombres(), Alert.AlertType.INFORMATION);
            WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuPrincipalView.fxml", "Menú Principal");
        } catch (LoginException e) {
            AlertManager.mostrarAlerta("Error de Acceso", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error del Sistema", "Ha ocurrido un error inesperado.", Alert.AlertType.ERROR);
        }
    }
}