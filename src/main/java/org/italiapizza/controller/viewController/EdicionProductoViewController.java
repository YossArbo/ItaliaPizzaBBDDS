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

public class EdicionProductoViewController implements Initializable {

    @FXML private TextField textFieldNombre;
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
    private Producto productoActual;

    public void initData(Producto producto) {
        this.productoActual = producto;
        textFieldNombre.setText(producto.getNombre());
        textFieldCodigo.setText(producto.getCodigo());
        textFieldCodigo.setEditable(false);
        textFieldCodigo.setDisable(true);
        textFieldDescripcion.setText(producto.getDescripcion());
        textFieldPrecio.setText(String.valueOf(producto.getPrecio()));
        textFieldRestricciones.setText(producto.getRestriccion());
        textFieldContenido.setText(String.valueOf(producto.getContenido()));
        comboBoxUnidadMedida.setValue(producto.getUnidadMedida());
        comboBoxTipoProducto.setValue(producto.getTipoProducto());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxUnidadMedida.getItems().addAll("kg", "gr", "lt", "ml", "piezas");
        comboBoxTipoProducto.getItems().addAll("Preparado", "Importado", "Insumo");
        buttonRegistrar.setOnAction(this::guardarCambiosProducto);
        buttonCancelar.setOnAction(this::cerrarVentana);
    }

    private void guardarCambiosProducto(ActionEvent event) {
        if (textFieldNombre.getText().isEmpty() || textFieldPrecio.getText().isEmpty() || 
            comboBoxUnidadMedida.getValue() == null || comboBoxTipoProducto.getValue() == null || 
            textFieldContenido.getText().isEmpty()) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Complete todos los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            double precio = Double.parseDouble(textFieldPrecio.getText());
            double contenido = Double.parseDouble(textFieldContenido.getText());

            productoActual.setNombre(textFieldNombre.getText());
            productoActual.setDescripcion(textFieldDescripcion.getText());
            productoActual.setPrecio(precio);
            productoActual.setRestriccion(textFieldRestricciones.getText());
            productoActual.setContenido(contenido);
            productoActual.setUnidadMedida(comboBoxUnidadMedida.getValue());
            productoActual.setTipoProducto(comboBoxTipoProducto.getValue());

            productoDAO.editarProducto(productoActual);
            AlertManager.mostrarAlerta("Éxito", "Producto actualizado correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana(event);
        } catch (NumberFormatException e) {
            AlertManager.mostrarAlerta("Formato Inválido", "El precio y contenido deben ser valores numéricos.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo actualizar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
    }
}