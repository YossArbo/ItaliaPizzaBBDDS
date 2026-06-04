package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.WindowManager;

public class RegistroPedidoViewController implements Initializable {

    @FXML
    private Label labelTotalPagar;
    @FXML
    private ComboBox<String> comboBoxProducto;
    @FXML
    private TextField textFieldUnidades;
    @FXML
    private Button buttonAgregarProducto;
    @FXML
    private ComboBox<String> comboBoxCliente;
    @FXML
    private TextField textFieldCajero;
    @FXML
    private TableView<?> tableViewProductosPedido;
    @FXML
    private TableColumn<?, ?> tableColumnProducto;
    @FXML
    private TableColumn<?, ?> tableColumnUnidades;
    @FXML
    private Button buttonRegistrar;
    @FXML
    private Button buttonCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxProducto.getItems().addAll("Pizza Pepperoni", "Pizza Hawaiana", "Refresco 2L");
        comboBoxCliente.getItems().addAll("Juan Pérez", "María López", "Cliente General");
        textFieldCajero.setText("Cajero Actual");
        textFieldCajero.setEditable(false);
        
        buttonAgregarProducto.setOnAction(e -> agregarItemAPedido());
        buttonRegistrar.setOnAction(this::finalizarPedido);
        buttonCancelar.setOnAction(this::regresar);
    }

    private void agregarItemAPedido() {
        if (comboBoxProducto.getValue() == null || textFieldUnidades.getText().isEmpty()) {
            AlertManager.mostrarAlerta("Campos Incompletos", "Seleccione un producto e indique las unidades.", Alert.AlertType.WARNING);
            return;
        }
        labelTotalPagar.setText("$250.00");
    }

    private void finalizarPedido(ActionEvent event) {
        if (comboBoxCliente.getValue() == null) {
            AlertManager.mostrarAlerta("Falta Cliente", "Debe asignar un cliente al pedido.", Alert.AlertType.WARNING);
            return;
        }

        AlertManager.mostrarAlerta("Éxito", "Pedido registrado y enviado a cocina.", Alert.AlertType.INFORMATION);
        WindowManager.cambiarVista(event, "/org/italiapizza/view/Pedidos.fxml", "Gestión de Pedidos");
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/Pedidos.fxml", "Gestión de Pedidos");
    }
}