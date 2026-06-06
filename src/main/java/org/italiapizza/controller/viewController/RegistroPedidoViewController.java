package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.italiapizza.model.dao.PedidoDAO;
import org.italiapizza.model.dao.ProductoDAO;
import org.italiapizza.model.dao.UsuarioDAO;
import org.italiapizza.model.dto.Cliente;
import org.italiapizza.model.dto.DetallePedido;
import org.italiapizza.model.dto.Empleado;
import org.italiapizza.model.dto.Producto;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.SessionManager;
import org.italiapizza.utils.WindowManager;

public class RegistroPedidoViewController implements Initializable {

    @FXML
    private Label labelTotalPagar;
    @FXML
    private ComboBox<Producto> comboBoxProducto;
    @FXML
    private TextField textFieldUnidades;
    @FXML
    private Button buttonAgregarProducto;
    @FXML
    private ComboBox<Cliente> comboBoxCliente;
    @FXML
    private ComboBox<Empleado> comboBoxCajero;
    @FXML
    private VBox vboxProductos;
    @FXML
    private Button buttonRegistrar;
    @FXML
    private Button buttonCancelar;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    private final List<DetallePedido> detallesList = new ArrayList<>();
    private double totalAPagar = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarFormatosComboBox();
        cargarProductos();
        cargarClientes();
        cargarCajeros();

        buttonAgregarProducto.setOnAction(e -> agregarItemAPedido());
        buttonRegistrar.setOnAction(this::finalizarPedido);
        buttonCancelar.setOnAction(this::regresar);
    }

    private void configurarFormatosComboBox() {
        comboBoxCliente.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombres() + " " + item.getApellidos());
            }
        });
        comboBoxCliente.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cliente item) {
                return item == null ? "" : item.getNombres() + " " + item.getApellidos();
            }

            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });

        comboBoxCajero.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Empleado item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombres() + " " + item.getApellidos());
            }
        });
        comboBoxCajero.setConverter(new StringConverter<>() {
            @Override
            public String toString(Empleado item) {
                return item == null ? "" : item.getNombres() + " " + item.getApellidos();
            }

            @Override
            public Empleado fromString(String string) {
                return null;
            }
        });
    }

    private void cargarProductos() {
        try {
            List<Producto> productos = productoDAO.listarMenu();
            comboBoxProducto.setItems(FXCollections.observableArrayList(productos));
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudieron cargar los productos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void cargarClientes() {
        try {
            List<Cliente> clientes = usuarioDAO.obtenerTodosLosClientes();
            comboBoxCliente.setItems(FXCollections.observableArrayList(clientes));
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudieron cargar los clientes.", Alert.AlertType.ERROR);
        }
    }

    private void cargarCajeros() {
        try {
            List<Empleado> empleados = usuarioDAO.obtenerTodosLosEmpleados();
            comboBoxCajero.setItems(FXCollections.observableArrayList(empleados));

            if (SessionManager.getEmpleadoActual() != null) {
                int idActual = SessionManager.getEmpleadoActual().getIdUsuario();
                for (Empleado emp : empleados) {
                    if (emp.getIdUsuario() == idActual) {
                        comboBoxCajero.getSelectionModel().select(emp);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudieron cargar los cajeros.", Alert.AlertType.ERROR);
        }
    }

    private void agregarItemAPedido() {
        Producto productoSeleccionado = comboBoxProducto.getValue();
        String unidadesStr = textFieldUnidades.getText();

        if (productoSeleccionado == null || unidadesStr.isEmpty()) {
            AlertManager.mostrarAlerta("Campos Incompletos", "Seleccione un producto e indique las unidades.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int unidades = Integer.parseInt(unidadesStr);
            if (unidades <= 0) {
                throw new NumberFormatException();
            }

            Optional<DetallePedido> existente = detallesList.stream()
                    .filter(d -> d.getIdProducto() == productoSeleccionado.getIdProducto())
                    .findFirst();

            if (existente.isPresent()) {
                existente.get().setUnidades(existente.get().getUnidades() + unidades);
            } else {
                DetallePedido detalle = new DetallePedido(productoSeleccionado.getIdProducto(), unidades, productoSeleccionado.getPrecio());
                detalle.setNombreProducto(productoSeleccionado.getNombre());
                detallesList.add(detalle);
            }

            comboBoxProducto.getSelectionModel().clearSelection();
            textFieldUnidades.clear();
            renderizarListaProductos();

        } catch (NumberFormatException e) {
            AlertManager.mostrarAlerta("Formato Inválido", "Las unidades deben ser un número entero mayor a 0.", Alert.AlertType.WARNING);
        }
    }

    private void renderizarListaProductos() {
        vboxProductos.getChildren().clear();
        totalAPagar = 0.0;

        for (DetallePedido detalle : detallesList) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);

            Label lblNombre = new Label(detalle.getNombreProducto() + " ($" + String.format("%.2f", detalle.getPrecioUnitario()) + ")");
            lblNombre.setPrefWidth(150);

            Button btnMenos = new Button("-");
            btnMenos.setOnAction(e -> {
                if (detalle.getUnidades() > 1) {
                    detalle.setUnidades(detalle.getUnidades() - 1);
                    renderizarListaProductos();
                }
            });

            Label lblCantidad = new Label(String.valueOf(detalle.getUnidades()));
            lblCantidad.setPrefWidth(20);
            lblCantidad.setAlignment(Pos.CENTER);

            Button btnMas = new Button("+");
            btnMas.setOnAction(e -> {
                detalle.setUnidades(detalle.getUnidades() + 1);
                renderizarListaProductos();
            });

            Button btnQuitar = new Button("X");
            btnQuitar.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-background-radius: 15;");
            btnQuitar.setOnAction(e -> {
                detallesList.remove(detalle);
                renderizarListaProductos();
            });

            row.getChildren().addAll(lblNombre, btnMenos, lblCantidad, btnMas, btnQuitar);
            vboxProductos.getChildren().add(row);

            totalAPagar += detalle.getSubtotal();
        }

        labelTotalPagar.setText(String.format("$%.2f", totalAPagar));
    }

    private void finalizarPedido(ActionEvent event) {
        Cliente clienteSeleccionado = comboBoxCliente.getValue();
        Empleado cajeroSeleccionado = comboBoxCajero.getValue();

        if (clienteSeleccionado == null) {
            AlertManager.mostrarAlerta("Falta Cliente", "Debe asignar un cliente al pedido.", Alert.AlertType.WARNING);
            return;
        }

        if (cajeroSeleccionado == null) {
            AlertManager.mostrarAlerta("Falta Cajero", "Debe asignar un cajero al pedido.", Alert.AlertType.WARNING);
            return;
        }

        if (detallesList.isEmpty()) {
            AlertManager.mostrarAlerta("Pedido Vacío", "Debe agregar al menos un producto.", Alert.AlertType.WARNING);
            return;
        }

        try {
            pedidoDAO.registrarNuevoPedido(clienteSeleccionado.getIdUsuario(), cajeroSeleccionado.getIdUsuario(), detallesList);
            AlertManager.mostrarAlerta("Éxito", "Pedido registrado y enviado a cocina.", Alert.AlertType.INFORMATION);
            WindowManager.cambiarVista(event, "/org/italiapizza/view/PedidosView.fxml", "Gestión de Pedidos");
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "Ocurrió un error al registrar el pedido.", Alert.AlertType.ERROR);
        }
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/PedidosView.fxml", "Gestión de Pedidos");
    }
}
