package org.italiapizza.controller.viewController;

import java.net.URL;
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
import javafx.stage.Stage;

public class UsuariosViewController implements Initializable {

    @FXML
    private Button buttonEmpleado;
    @FXML
    private Button buttonCliente;

    private String nombres;
    private String apellidos;
    private List<String> telefonos;
    private List<String> emails;

    public void initData(String nombres, String apellidos, List<String> telefonos, List<String> emails) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefonos = telefonos;
        this.emails = emails;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonEmpleado.setOnAction(this::abrirRegistroEmpleado);
        buttonCliente.setOnAction(this::abrirRegistroCliente);
    }

    private void abrirRegistroEmpleado(ActionEvent event) {
        cargarVistaFinal(event, "/org/italiapizza/view/RegistroEmpleadoView.fxml", true);
    }

    private void abrirRegistroCliente(ActionEvent event) {
        cargarVistaFinal(event, "/org/italiapizza/view/RegistroClienteView.fxml", false);
    }

    private void cargarVistaFinal(ActionEvent event, String rutaFxml, boolean esEmpleado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();
            
            if (esEmpleado) {
                RegistroEmpleadoViewController controller = loader.getController();
                controller.initData(nombres, apellidos, telefonos, emails);
            } else {
                RegistroClienteViewController controller = loader.getController();
                controller.initData(nombres, apellidos, telefonos, emails);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}