package org.italiapizza.controller.viewController;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.italiapizza.model.dao.PedidoDAO;
import org.italiapizza.model.dto.Pedido;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.SessionManager;
import org.italiapizza.utils.WindowManager;

public class PedidosViewController implements Initializable {

    @FXML private Label labelConsultarUsuario;
    @FXML private TextField textFieldBuscadorPedido;
    @FXML private DatePicker datePickerFecha;
    @FXML private ComboBox<String> comboBoxEstatus;
    @FXML private ComboBox<String> comboBoxCambiarEstatus;
    @FXML private TableView<Pedido> tableViewPedidos;
    @FXML private TableColumn<Pedido, Integer> tableColumnIdPedido;
    @FXML private TableColumn<Pedido, String> tableColumnCliente;
    @FXML private TableColumn<Pedido, String> tableColumnCajero;
    @FXML private TableColumn<Pedido, Double> tableColumnMontoTotal;
    @FXML private TableColumn<Pedido, String> tableColumnEstatus;
    @FXML private Button buttonCambiarEstatus;
    @FXML private Button buttonBitacora;
    @FXML private Button buttonNuevoPedido;
    @FXML private Button buttonVolverAlMenu;
    @FXML private Button buttonRegresar;

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private ObservableList<Pedido> pedidosObservable;
    private FilteredList<Pedido> pedidosFiltrados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxEstatus.getItems().addAll("Entregado", "En Proceso", "Cancelado");
        comboBoxCambiarEstatus.getItems().addAll("Entregado", "En Proceso", "Cancelado");


        tableColumnIdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        tableColumnMontoTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
        tableColumnEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        tableColumnCliente.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCliente().getNombres() + " " + cellData.getValue().getCliente().getApellidos()));

        tableColumnCajero.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEmpleado().getNombres()));

        cargarTodosLosPedidos();

        textFieldBuscadorPedido.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        datePickerFecha.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        comboBoxEstatus.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());

        buttonRegresar.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/MenuPrincipalView.fxml", "Menú Principal"));
        buttonNuevoPedido.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/RegistroPedidoView.fxml", "Registro de Pedido"));
        buttonVolverAlMenu.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/MenuPrincipalView.fxml", "Menú Principal"));
        buttonCambiarEstatus.setOnAction(e -> cambiarEstatusPedido());
        buttonBitacora.setOnAction(this::verBitacora);
    }

    private void cargarTodosLosPedidos() {
        try {
            List<Pedido> lista = pedidoDAO.obtenerTodosLosPedidos();
            pedidosObservable = FXCollections.observableArrayList(lista);
            pedidosFiltrados = new FilteredList<>(pedidosObservable, p -> true);
            tableViewPedidos.setItems(pedidosFiltrados);
        } catch (Exception e) {
            pedidosObservable = FXCollections.observableArrayList();
            pedidosFiltrados = new FilteredList<>(pedidosObservable, p -> true);
            tableViewPedidos.setItems(pedidosFiltrados);
            AlertManager.mostrarAlerta("Error", "No se pudieron cargar los pedidos.", Alert.AlertType.ERROR);
        }
    }

    private void aplicarFiltros() {
        if (pedidosFiltrados == null) {
            return;
        }
        pedidosFiltrados.setPredicate(pedido -> {
            String busqueda = textFieldBuscadorPedido.getText();
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                busqueda = busqueda.trim().toLowerCase();
                boolean esNumerico = busqueda.matches("\\d+");

                if (esNumerico) {
                    if (!String.valueOf(pedido.getIdPedido()).contains(busqueda)) {
                        return false;
                    }
                } else {
                    if (busqueda.length() >= 2) {
                        String cliente = (pedido.getCliente().getNombres() + " " + pedido.getCliente().getApellidos()).toLowerCase();
                        String cajero = pedido.getEmpleado().getNombres().toLowerCase();
                        if (!cliente.contains(busqueda) && !cajero.contains(busqueda)) {
                            return false;
                        }
                    }
                }
            }

            if (datePickerFecha.getValue() != null) {
                LocalDate fechaPedido = pedido.getFechaPedido().toLocalDate();
                if (!fechaPedido.equals(datePickerFecha.getValue())) {
                    return false;
                }
            }

            String estatusSeleccionado = comboBoxEstatus.getValue();
            if (estatusSeleccionado != null && !estatusSeleccionado.isEmpty()) {
                if (!pedido.getEstatus().equalsIgnoreCase(estatusSeleccionado)) {
                    return false;
                }
            }

            return true;
        });
    }

    private void cambiarEstatusPedido() {
        Pedido pedidoSeleccionado = tableViewPedidos.getSelectionModel().getSelectedItem();
        String nuevoEstatus = comboBoxCambiarEstatus.getValue();

        if (pedidoSeleccionado == null || nuevoEstatus == null) {
            AlertManager.mostrarAlerta("Selección requerida", "Seleccione un pedido de la tabla y un estatus del menú.", Alert.AlertType.WARNING);
            return;
        }

        try {
            pedidoDAO.cambiarEstatus(pedidoSeleccionado.getIdPedido(), nuevoEstatus, SessionManager.getEmpleadoActual() != null ? SessionManager.getEmpleadoActual().getIdUsuario() : 1);
            AlertManager.mostrarAlerta("Éxito", "Estatus actualizado correctamente.", Alert.AlertType.INFORMATION);
            cargarTodosLosPedidos();
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo cambiar el estatus del pedido.", Alert.AlertType.ERROR);
        }
    }

    private void verBitacora(ActionEvent event) {
        Pedido pedidoSeleccionado = tableViewPedidos.getSelectionModel().getSelectedItem();

        if (pedidoSeleccionado == null) {
            AlertManager.mostrarAlerta("Selección requerida", "Seleccione un pedido para ver su bitácora.", Alert.AlertType.WARNING);
            return;
        }

        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/org/italiapizza/view/BitacoraPedidoView.fxml"));
            javafx.scene.Parent root = loader.load();

            BitacoraPedidoViewController controller = loader.getController();
            controller.initData(pedidoSeleccionado);

            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo abrir la bitácora.", Alert.AlertType.ERROR);
        }
    }
}