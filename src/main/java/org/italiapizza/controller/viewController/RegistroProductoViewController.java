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

public class RegistroProductoViewController implements Initializable {

    @FXML
    private TextField textFieldNombreProducto;
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
        comboBoxUnidadMedida.getItems().addAll("Kilogramos", "Gramos", "Litros", "Mililitros", "Piezas");
        comboBoxTipoProducto.getItems().addAll("Preparado", "Importado", "Insumo");
        buttonRegistrar.setOnAction(this::guardarProducto);
        buttonCancelar.setOnAction(this::regresar);
    }

    private void guardarProducto(ActionEvent event) {
        if (textFieldNombreProducto.getText().isEmpty() || textFieldCodigo.getText().isEmpty() || textFieldPrecio.getText().isEmpty()) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Los campos Nombre, Código y Precio son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        AlertManager.mostrarAlerta("Éxito", "Producto guardado en el sistema correctamente.", Alert.AlertType.INFORMATION);
        WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuProducto.fxml", "Menú de Productos");
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuProducto.fxml", "Menú de Productos");
    }
}