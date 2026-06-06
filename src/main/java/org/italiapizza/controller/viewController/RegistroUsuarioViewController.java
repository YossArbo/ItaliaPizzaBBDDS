package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.italiapizza.utils.WindowManager;

public class RegistroUsuarioViewController implements Initializable {

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
    private VBox vboxTelefonos;
    @FXML
    private VBox vboxEmails;
    @FXML
    private Button buttonSiguiente;
    @FXML
    private Button buttonCancelar;

    private List<String> telefonosList = new ArrayList<>();
    private List<String> emailsList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonAgregarTelefono.setOnAction(e -> agregarTelefono());
        buttonAgregarEmail.setOnAction(e -> agregarEmail());
        buttonSiguiente.setOnAction(this::irASeleccionTipo);
        buttonCancelar.setOnAction(this::regresar);
        
        textFieldNombres.textProperty().addListener((obs, oldVal, newVal) -> actualizarNombreCompleto());
        textFieldApellidos.textProperty().addListener((obs, oldVal, newVal) -> actualizarNombreCompleto());
    }

    private void actualizarNombreCompleto() {
        labelNombreCompleto.setText(textFieldNombres.getText() + " " + textFieldApellidos.getText());
    }

    private void agregarTelefono() {
        String textoLimpio = textFieldTelefono.getText().replaceAll("[^\\d]", "");
        if (textoLimpio.length() >= 10) {
            String formato = textoLimpio.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
            telefonosList.add(formato);
            agregarElementoAVBox(vboxTelefonos, formato, telefonosList);
            textFieldTelefono.clear();
        }
    }

    private void agregarEmail() {
        String email = textFieldEmail.getText();
        if (!email.isEmpty()) {
            emailsList.add(email);
            agregarElementoAVBox(vboxEmails, email, emailsList);
            textFieldEmail.clear();
        }
    }

    private void agregarElementoAVBox(VBox vbox, String texto, List<String> lista) {
        HBox hbox = new HBox(10);
        hbox.setStyle("-fx-alignment: CENTER");
        Label label = new Label(texto);
        label.setStyle("-fx-font-size: 10px");
        Button btnQuitar = new Button("⨉");
        
        btnQuitar.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 8px; -fx-background-radius: 100;");
        
        btnQuitar.setOnAction(e -> {
            lista.remove(texto);
            vbox.getChildren().remove(hbox);
        });
        
        hbox.getChildren().addAll(label, btnQuitar);
        vbox.getChildren().add(hbox);
    }

    private void irASeleccionTipo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/italiapizza/view/UsuariosView.fxml"));
            Parent root = loader.load();
            
            UsuariosViewController controller = loader.getController();
            controller.initData(textFieldNombres.getText(), textFieldApellidos.getText(), telefonosList, emailsList);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuUsuarioView.fxml", "Gestión de Usuarios");
    }
}