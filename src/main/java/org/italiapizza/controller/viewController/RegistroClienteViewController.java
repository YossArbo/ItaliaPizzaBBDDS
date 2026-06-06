package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.italiapizza.model.dao.UsuarioDAO;
import org.italiapizza.model.dto.Cliente;
import org.italiapizza.utils.AlertManager;

public class RegistroClienteViewController implements Initializable {

    @FXML
    private TextField textFieldCalle;
    @FXML
    private TextField textFieldNoExterior;
    @FXML
    private TextField textFieldCiudad;
    @FXML
    private TextField textFieldCodigoPostal;
    @FXML
    private Label labelNombreCompleto;
    @FXML
    private VBox vboxTelefonos;
    @FXML
    private VBox vboxEmails;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonRegistrar;

    private String nombres;
    private String apellidos;
    private List<String> telefonos;
    private List<String> emails;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void initData(String nombres, String apellidos, List<String> telefonos, List<String> emails) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefonos = telefonos;
        this.emails = emails;
        
        labelNombreCompleto.setText(nombres + " " + apellidos);
        telefonos.forEach(t -> vboxTelefonos.getChildren().add(new Label(t)));
        emails.forEach(e -> vboxEmails.getChildren().add(new Label(e)));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonRegistrar.setOnAction(this::registrarCliente);
        buttonCancelar.setOnAction(this::cerrarVentana);
    }

    private void registrarCliente(ActionEvent event) {
        if (textFieldCalle.getText().isEmpty() || textFieldNoExterior.getText().isEmpty() || 
            textFieldCiudad.getText().isEmpty() || textFieldCodigoPostal.getText().isEmpty()) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos de dirección.", Alert.AlertType.WARNING);
            return;
        }

        try {
            String stringTelefonos = String.join(", ", telefonos);
            String stringEmails = String.join(", ", emails);

            Cliente cliente = new Cliente(
                0, 
                nombres, 
                apellidos,
                stringTelefonos, 
                stringEmails, 
                1,
                textFieldCalle.getText(), 
                textFieldNoExterior.getText(),
                textFieldCiudad.getText(), 
                textFieldCodigoPostal.getText()
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