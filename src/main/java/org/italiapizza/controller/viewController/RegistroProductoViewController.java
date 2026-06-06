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
import javafx.stage.Stage;
import org.italiapizza.model.dao.ProductoDAO;
import org.italiapizza.model.dto.Producto;
import org.italiapizza.utils.AlertManager;

public class RegistroProductoViewController implements Initializable {

    @FXML private TextField textFieldNombreProducto;
    @FXML private TextField textFieldCodigo;
    @FXML private TextField textFieldDescripcion;
    @FXML private TextField textFieldPrecio;
    @FXML private TextField textFieldRestricciones;
    @FXML private TextField textFieldContenido;
    @FXML private ComboBox<String> comboBoxUnidadMedida;
    @FXML private ComboBox<String> comboBoxTipoProducto;
    @FXML private Button buttonRegistrar;
    @FXML private Button buttonCancelar;

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxUnidadMedida.getItems().addAll("kg", "gr", "lt", "ml", "piezas");
        comboBoxTipoProducto.getItems().addAll("Preparado", "Importado", "Insumo");
        buttonRegistrar.setOnAction(this::guardarProducto);
        buttonCancelar.setOnAction(this::cerrarVentana);
    }

    private void guardarProducto(ActionEvent event) {
        if (textFieldNombreProducto.getText().isEmpty() || textFieldCodigo.getText().isEmpty() || 
            textFieldPrecio.getText().isEmpty() || comboBoxUnidadMedida.getValue() == null || 
            comboBoxTipoProducto.getValue() == null || textFieldContenido.getText().isEmpty()) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Complete todos los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            double precio = Double.parseDouble(textFieldPrecio.getText());
            double contenido = Double.parseDouble(textFieldContenido.getText());

            Producto producto = new Producto(
                textFieldCodigo.getText(),
                textFieldNombreProducto.getText(),
                textFieldDescripcion.getText(),
                precio,
                textFieldRestricciones.getText(),
                null,
                comboBoxTipoProducto.getValue(),
                comboBoxUnidadMedida.getValue(),
                contenido
            );

            productoDAO.registrarProducto(producto);
            AlertManager.mostrarAlerta("Éxito", "Producto guardado en el sistema correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana(event);
        } catch (NumberFormatException e) {
            AlertManager.mostrarAlerta("Formato Inválido", "El precio y contenido deben ser valores numéricos.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo registrar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
    }
}