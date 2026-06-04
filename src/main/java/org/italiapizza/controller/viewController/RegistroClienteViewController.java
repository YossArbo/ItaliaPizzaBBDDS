package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.italiapizza.model.dao.UsuarioDAO;
import org.italiapizza.model.dto.Cliente;
import org.italiapizza.utils.AlertManager;

public class RegistroClienteViewController implements Initializable {

    @FXML
    private TextField textFieldNombres;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldCalle;
    @FXML
    private TextField textFieldNoExterior;
    @FXML
    private TextField textFieldCiudad;
    @FXML
    private TextField textFieldCodigoPostal;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonRegistrar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonRegistrar.setOnAction(this::registrarCliente);
        buttonCancelar.setOnAction(this::cerrarVentana);
    }

    private void registrarCliente(ActionEvent event) {
        try {
            Cliente cliente = new Cliente(
                0, textFieldNombres.getText(), textFieldApellidos.getText(),
                textFieldTelefono.getText(), textFieldEmail.getText(), 1,
                textFieldCalle.getText(), textFieldNoExterior.getText(),
                textFieldCiudad.getText(), textFieldCodigoPostal.getText()
            );
            
            usuarioDAO.registrarUsuario(cliente);
            AlertManager.mostrarAlerta("Éxito", "Cliente registrado correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana(event);
            
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
    }
}