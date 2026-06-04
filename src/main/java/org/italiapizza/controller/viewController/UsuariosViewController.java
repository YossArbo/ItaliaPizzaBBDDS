package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.italiapizza.utils.WindowManager;

public class UsuariosViewController implements Initializable {

    @FXML
    private Label labelRegistrar;
    @FXML
    private Button buttonEmpleado;
    @FXML
    private Button buttonCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonEmpleado.setOnAction(this::abrirRegistroEmpleado);
        buttonCliente.setOnAction(this::abrirRegistroCliente);
    }

    private void abrirRegistroEmpleado(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/RegistroUsuario.fxml", "Registrar Empleado");
    }

    private void abrirRegistroCliente(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/RegistroCliente.fxml", "Registrar Cliente");
    }
}