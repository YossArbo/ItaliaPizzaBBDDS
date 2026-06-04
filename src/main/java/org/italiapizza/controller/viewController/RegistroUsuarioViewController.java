package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.italiapizza.model.dao.UsuarioDAO;
import org.italiapizza.model.dto.Empleado;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.WindowManager;

public class RegistroUsuarioViewController implements Initializable {

    @FXML
    private Label labelRegistroUsuario;
    @FXML
    private TextField textFieldNombres;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private Button buttonAgregarTelefono;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private Button buttonAgregarEmail;
    @FXML
    private Label labelNombreCompleto;
    @FXML
    private Label labelTelefonos;
    @FXML
    private Label labelTelefono1;
    @FXML
    private Button buttonQuitarTelefono1;
    @FXML
    private Label labelEmails;
    @FXML
    private Label labelEmail1;
    @FXML
    private Button buttonQuitarEmail1;
    @FXML
    private Button buttonRegistrar;
    @FXML
    private Button buttonCancelar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonAgregarTelefono.setOnAction(e -> agregarTelefono());
        buttonQuitarTelefono1.setOnAction(e -> quitarTelefono());
        buttonAgregarEmail.setOnAction(e -> agregarEmail());
        buttonQuitarEmail1.setOnAction(e -> quitarEmail());
        buttonRegistrar.setOnAction(this::guardarUsuarioEmpleado);
        buttonCancelar.setOnAction(this::regresar);
        
        textFieldNombres.textProperty().addListener((obs, oldVal, newVal) -> actualizarNombreCompleto());
        textFieldApellidos.textProperty().addListener((obs, oldVal, newVal) -> actualizarNombreCompleto());
    }

    private void actualizarNombreCompleto() {
        labelNombreCompleto.setText(textFieldNombres.getText() + " " + textFieldApellidos.getText());
    }

    private void agregarTelefono() {
        if (!textFieldTelefono.getText().isEmpty()) {
            labelTelefono1.setText(textFieldTelefono.getText());
        }
    }

    private void quitarTelefono() {
        labelTelefono1.setText("");
    }

    private void agregarEmail() {
        if (!textFieldEmail.getText().isEmpty()) {
            labelEmail1.setText(textFieldEmail.getText());
        }
    }

    private void quitarEmail() {
        labelEmail1.setText("");
    }

    private void guardarUsuarioEmpleado(ActionEvent event) {
        if (textFieldNombres.getText().isEmpty() || textFieldApellidos.getText().isEmpty()) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Por favor llene los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Empleado empleado = new Empleado();
            empleado.setNombres(textFieldNombres.getText());
            empleado.setApellidos(textFieldApellidos.getText());
            empleado.setTelefono(labelTelefono1.getText());
            empleado.setEmail(labelEmail1.getText());
            empleado.setTipoUsuario("Empleado");
            empleado.setNombreUsuario(textFieldNombres.getText().toLowerCase() + "123");
            empleado.setContrasena("Temporal123");
            empleado.setRol("Cajero");
            
            usuarioDAO.registrarUsuario(empleado);
            AlertManager.mostrarAlerta("Éxito", "Empleado registrado correctamente.", Alert.AlertType.INFORMATION);
            WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuUsuarioView.fxml", "Gestión de Usuarios");
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuUsuarioView.fxml", "Gestión de Usuarios");
    }
}