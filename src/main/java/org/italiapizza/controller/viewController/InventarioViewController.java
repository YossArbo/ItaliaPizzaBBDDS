package org.italiapizza.controller.viewController;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.italiapizza.model.dao.InventarioDAO;
import org.italiapizza.model.dao.ProductoDAO;
import org.italiapizza.model.dto.Inventario;
import org.italiapizza.model.dto.Producto;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.WindowManager;
import org.italiapizza.utils.csv.ExportadorCSV;

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
    private TableColumn<Inventario, String> tableColumnDescripcion;
    @FXML
    private TableColumn<Inventario, Double> tableColumnPrecio;
    @FXML
    private TableColumn<Inventario, String> tableColumnRestricciones;
    @FXML
    private TableColumn<Inventario, String> tableColumnUnidadMedida;
    @FXML
    private TableColumn<Inventario, Double> tableColumnCantidadSistema;
    @FXML
    private Button buttonNuevoProducto;
    @FXML
    private Button buttonVolverAlMenu;
    @FXML
    private Button buttonRegresar;

    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private ObservableList<Inventario> inventarioObservable;
    private FilteredList<Inventario> inventarioFiltrado;
    private final Map<Integer, Producto> mapaProductos = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarInventario();

        textFieldBuscadorProducto.textProperty().addListener((obs, oldVal, newVal) -> filtrarInventario(newVal));

        buttonNuevoProducto.setOnAction(e -> abrirModalNuevoProducto());
        buttonEditar.setOnAction(e -> abrirModalEditarProducto());
        buttonEliminar.setOnAction(e -> eliminarProductoLogico());
        buttonExportar.setOnAction(e -> exportarInventarioCSV());
        
        buttonRegresar.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/AdministracionView.fxml", "Administración"));
        buttonVolverAlMenu.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/MenuPrincipalView.fxml", "Menú Principal"));
    }

    private void configurarColumnas() {
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        tableColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tableColumnCantidadSistema.setCellValueFactory(new PropertyValueFactory<>("cantidadSistema"));
        tableColumnUnidadMedida.setCellValueFactory(new PropertyValueFactory<>("unidadMedida"));

        tableColumnDescripcion.setCellValueFactory(cellData -> {
            Producto p = mapaProductos.get(cellData.getValue().getIdProducto());
            return new SimpleStringProperty(p != null ? p.getDescripcion() : "");
        });

        tableColumnPrecio.setCellValueFactory(cellData -> {
            Producto p = mapaProductos.get(cellData.getValue().getIdProducto());
            return new SimpleObjectProperty<>(p != null ? p.getPrecio() : 0.0);
        });

        tableColumnRestricciones.setCellValueFactory(cellData -> {
            Producto p = mapaProductos.get(cellData.getValue().getIdProducto());
            return new SimpleStringProperty(p != null ? p.getRestriccion() : "");
        });
    }

    private void cargarInventario() {
        try {
            mapaProductos.clear();
            List<Producto> productosList = productoDAO.listarProductosActivos();
            for (Producto prod : productosList) {
                mapaProductos.put(prod.getIdProducto(), prod);
            }

            List<Inventario> lista = inventarioDAO.listarReporteInventario();
            inventarioObservable = FXCollections.observableArrayList(lista);
            inventarioFiltrado = new FilteredList<>(inventarioObservable, p -> true);
            tableViewInventario.setItems(inventarioFiltrado);
        } catch (Exception e) {
            inventarioObservable = FXCollections.observableArrayList();
            inventarioFiltrado = new FilteredList<>(inventarioObservable, p -> true);
            tableViewInventario.setItems(inventarioFiltrado);
            AlertManager.mostrarAlerta("Error", "No se pudo cargar el inventario de productos.", Alert.AlertType.ERROR);
        }
    }

    private void filtrarInventario(String busqueda) {
        if (inventarioFiltrado == null) {
            return;
        }
        inventarioFiltrado.setPredicate(item -> {
            if (busqueda == null || busqueda.trim().isEmpty()) {
                return true;
            }
            String lowerCaseFilter = busqueda.trim().toLowerCase();
            String codigo = item.getCodigo() != null ? item.getCodigo().toLowerCase() : "";
            String nombre = item.getNombre() != null ? item.getNombre().toLowerCase() : "";

            return codigo.contains(lowerCaseFilter) || nombre.contains(lowerCaseFilter);
        });
    }

    private void abrirModalNuevoProducto() {
        WindowManager.abrirModal("/org/italiapizza/view/RegistroProductoView.fxml", "Nuevo Producto");
        cargarInventario();
    }

    private void abrirModalEditarProducto() {
        Inventario seleccionado = tableViewInventario.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertManager.mostrarAlerta("Advertencia", "Seleccione un producto para editar.", Alert.AlertType.WARNING);
            return;
        }

        Producto productoSeleccionado = mapaProductos.get(seleccionado.getIdProducto());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/italiapizza/view/EdicionProductoView.fxml"));
            Parent root = loader.load();
            
            EdicionProductoViewController controller = loader.getController();
            controller.initData(productoSeleccionado);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Producto");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            cargarInventario();
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo abrir la ventana de edición.", Alert.AlertType.ERROR);
        }
    }

    private void eliminarProductoLogico() {
        Inventario seleccionado = tableViewInventario.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertManager.mostrarAlerta("Advertencia", "Seleccione un producto para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            productoDAO.eliminarProductoLogico(seleccionado.getIdProducto());
            AlertManager.mostrarAlerta("Éxito", "Producto eliminado correctamente de forma lógica.", Alert.AlertType.INFORMATION);
            cargarInventario();
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo eliminar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void exportarInventarioCSV() {
        if (inventarioObservable == null || inventarioObservable.isEmpty()) {
            AlertManager.mostrarAlerta("Advertencia", "No hay datos disponibles para exportar.", Alert.AlertType.WARNING);
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte de Inventario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV (*.csv)", "*.csv"));
        fileChooser.setInitialFileName("reporte_inventario.csv");

        Stage stage = (Stage) buttonExportar.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                ExportadorCSV.exportarInventario(inventarioObservable, file.getAbsolutePath());
                AlertManager.mostrarAlerta("Éxito", "El archivo CSV se ha exportado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                AlertManager.mostrarAlerta("Error", "Ocurrió un error al exportar el archivo CSV: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}