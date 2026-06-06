package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.italiapizza.model.dao.PedidoDAO;
import org.italiapizza.model.dto.BitacoraPedido;
import org.italiapizza.model.dto.Pedido;
import org.italiapizza.utils.AlertManager;
import javafx.scene.control.Alert;
import org.italiapizza.utils.WindowManager;

public class BitacoraPedidoViewController implements Initializable {

    @FXML
    private Label labelBitacora;
    @FXML
    private ComboBox<Pedido> comboBoxPedidos;
    @FXML
    private Label labelEnProceso;
    @FXML
    private Label labelEntregado;
    @FXML
    private Label labelCancelado;
    @FXML
    private Button buttonVolver;

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonVolver.setOnAction(this::regresar);
        comboBoxPedidos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> actualizarFechasBitacora(newVal));
        cargarComboPedidos();
    }

    public void initData(Pedido pedidoSeleccionado) {
        if (pedidoSeleccionado != null) {
            for (Pedido p : comboBoxPedidos.getItems()) {
                if (p.getIdPedido() == pedidoSeleccionado.getIdPedido()) {
                    comboBoxPedidos.getSelectionModel().select(p);
                    break;
                }
            }
        }
    }

    private void cargarComboPedidos() {
        try {
            List<Pedido> pedidos = pedidoDAO.buscarPedidos(null, null, null, null);
            comboBoxPedidos.setItems(FXCollections.observableArrayList(pedidos));
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudieron cargar los pedidos.", Alert.AlertType.ERROR);
        }
    }

    private void actualizarFechasBitacora(Pedido pedidoSeleccionado) {
        labelEnProceso.setText("En Proceso: N/A");
        labelEntregado.setText("Entregado: N/A");
        labelCancelado.setText("Cancelado: N/A");

        if (pedidoSeleccionado != null) {
            try {
                List<BitacoraPedido> historial = pedidoDAO.obtenerBitacora(pedidoSeleccionado.getIdPedido());
                for (BitacoraPedido entrada : historial) {
                    switch (entrada.getEstatus()) {
                        case "En Proceso":
                            labelEnProceso.setText("En Proceso: " + entrada.getFecha());
                            break;
                        case "Entregado":
                            labelEntregado.setText("Entregado: " + entrada.getFecha());
                            break;
                        case "Cancelado":
                            labelCancelado.setText("Cancelado: " + entrada.getFecha());
                            break;
                    }
                }
            } catch (Exception e) {
                AlertManager.mostrarAlerta("Error", "No se pudo cargar la bitácora.", Alert.AlertType.ERROR);
            }
        }
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/PedidosView.fxml", "Pedidos");
    }
}
