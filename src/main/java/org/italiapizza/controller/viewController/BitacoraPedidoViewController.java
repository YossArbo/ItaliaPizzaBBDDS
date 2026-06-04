package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.italiapizza.utils.WindowManager;

public class BitacoraPedidoViewController implements Initializable {

    @FXML
    private Label labelBitacora;
    @FXML
    private ComboBox<String> comboBoxPedidos;
    @FXML
    private Label labelEnProceso;
    @FXML
    private Label labelEntregado;
    @FXML
    private Label labelCancelado;
    @FXML
    private Button buttonVolver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonVolver.setOnAction(this::regresar);
        comboBoxPedidos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> actualizarFechasBitacora(newVal));
        cargarComboPedidos();
    }

    private void cargarComboPedidos() {
        comboBoxPedidos.getItems().addAll("Pedido #101", "Pedido #102", "Pedido #103");
    }

    private void actualizarFechasBitacora(String pedidoSeleccionado) {
        if (pedidoSeleccionado != null) {
            labelEnProceso.setText("En Proceso: 03/06/2026 14:00:22");
            labelEntregado.setText("Entregado: 03/06/2026 14:35:10");
            labelCancelado.setText("Cancelado: N/A");
        }
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/PedidosView.fxml", "Pedidos");
    }
}