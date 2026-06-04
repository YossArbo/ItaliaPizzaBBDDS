package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.WindowManager;

public class RegistroEmpleadoViewController implements Initializable {

    @FXML
    private TextField textFieldNombreUsuario;
    @FXML
    private PasswordField passwordFieldContrasena;
    @FXML
    private PasswordField passwordFieldConfirmacion;
    @FXML
    private ComboBox<String> comboBoxRol;
    @FXML
    private Button buttonRegistrar;
    @FXML
    private Button buttonCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxRol.getItems().addAll("Administrador", "Cajero", "Cocintero");
        buttonRegistrar.setOnAction(this::registrarEmpleado);
        buttonCancelar.setOnAction(this::regresar);
    }

    private void registrarEmpleado(ActionEvent event) {
        String usuario = textFieldNombreUsuario.getText();
        String contrasena = passwordFieldContrasena.getText();
        String confirmacion = passwordFieldConfirmacion.getText();
        String rol = comboBoxRol.getValue();

        if (usuario.isEmpty() || contrasena.isEmpty() || confirmacion.isEmpty() || rol == null) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos de credenciales.", Alert.AlertType.WARNING);
            return;
        }

        if (!contrasena.equals(confirmacion)) {
            AlertManager.mostrarAlerta("Error de Coincidencia", "Las contraseñas ingresadas no coinciden.", Alert.AlertType.ERROR);
            return;
        }

        AlertManager.mostrarAlerta("Éxito", "Empleado registrado y vinculado correctamente.", Alert.AlertType.INFORMATION);
        WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuUsuario.fxml", "Gestión de Usuarios");
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/RegistroUsuario.fxml", "Registrar Usuario");
    }
}