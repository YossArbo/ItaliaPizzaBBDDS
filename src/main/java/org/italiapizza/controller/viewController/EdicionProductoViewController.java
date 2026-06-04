package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.WindowManager;

public class EdicionProductoViewController implements Initializable {

    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldCodigo;
    @FXML
    private TextField textFieldDescripcion;
    @FXML
    private TextField textFieldPrecio;
    @FXML
    private TextField textFieldRestricciones;
    @FXML
    private TextField textFieldContenido;
    @FXML
    private ComboBox<String> comboBoxUnidadMedida;
    @FXML
    private ComboBox<String> comboBoxTipoProducto;
    @FXML
    private Button buttonRegistrar;
    @FXML
    private Button buttonCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
        buttonRegistrar.setOnAction(this::guardarCambiosProducto);
        buttonCancelar.setOnAction(this::regresar);
    }

    private void cargarCombos() {
        comboBoxUnidadMedida.getItems().addAll("Kilogramos", "Gramos", "Litros", "Mililitros", "Piezas");
        comboBoxTipoProducto.getItems().addAll("Preparado", "Importado", "Insumo");
    }

    private void guardarCambiosProducto(ActionEvent event) {
        if (textFieldNombre.getText().isEmpty() || textFieldCodigo.getText().isEmpty() || textFieldPrecio.getText().isEmpty()) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Por favor llene los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            AlertManager.mostrarAlerta("Éxito", "Producto actualizado correctamente.", Alert.AlertType.INFORMATION);
            WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuProducto.fxml", "Menú de Productos");
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo actualizar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuProducto.fxml", "Menú de Productos");
    }
}