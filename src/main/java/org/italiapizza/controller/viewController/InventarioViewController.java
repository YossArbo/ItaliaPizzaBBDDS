package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.italiapizza.model.dao.InventarioDAO;
import org.italiapizza.model.dto.Inventario;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.WindowManager;

public class InventarioViewController implements Initializable {

    @FXML
    private Label labelConsultarUsuario;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonEliminar;
    @FXML
    private Button buttonExportar;
    @FXML
    private TextField textFieldBuscadorProducto;
    @FXML
    private TableView<Inventario> tableViewInventario;
    @FXML
    private TableColumn<Inventario, String> tableColumnCodigo;
    @FXML
    private TableColumn<Inventario, String> tableColumnNombre;
    @FXML
    private TableColumn<Inventario, Double> tableColumnCantidadSistema;
    @FXML
    private TableColumn<Inventario, Double> tableColumnCantidadFisica;
    @FXML
    private TableColumn<Inventario, Double> tableColumnDiferencia;
    @FXML
    private Button buttonNuevoProducto;
    @FXML
    private Button buttonVolverAlMenu;
    @FXML
    private Button buttonRegresar;

    private final InventarioDAO inventarioDAO = new InventarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarInventario();

        buttonNuevoProducto.setOnAction(e -> abrirModalNuevoProducto());
        buttonRegresar.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/Administracion.fxml", "Administración"));
        buttonVolverAlMenu.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/MenuPrincipal.fxml", "Menú Principal"));
    }

    private void configurarColumnas() {
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tableColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tableColumnCantidadSistema.setCellValueFactory(new PropertyValueFactory<>("cantidadSistema"));
        tableColumnCantidadFisica.setCellValueFactory(new PropertyValueFactory<>("existenciaAlmacen"));
        tableColumnDiferencia.setCellValueFactory(new PropertyValueFactory<>("diferencia"));
    }

    private void cargarInventario() {
        try {
            List<Inventario> lista = inventarioDAO.listarReporteInventario();
            ObservableList<Inventario> obsList = FXCollections.observableArrayList(lista);
            tableViewInventario.setItems(obsList);
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo cargar el inventario.", Alert.AlertType.ERROR);
        }
    }

    private void abrirModalNuevoProducto() {
        WindowManager.abrirModal("/org/italiapizza/view/RegistroProductoView.fxml", "Nuevo Producto");
        cargarInventario();
    }
    
    private void abrirModalEditarProducto() {
        WindowManager.abrirModal("/org/italiapizza/view/EdicionProductoView.fxml", "Nuevo Producto");
        cargarInventario();
    }
}