package org.italiapizza.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;

/**
 *
 * @author ELLIN JV
 */
public class WindowManager {

    public static void cambiarVista(ActionEvent event, String fxmlPath, String titulo) {
        try {
            Parent root = FXMLLoader.load(WindowManager.class.getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            AlertManager.mostrarAlerta("Error de Interfaz", "No se pudo cargar la vista.", Alert.AlertType.ERROR);
        }
    }

    public static void abrirModal(String fxmlPath, String titulo) {
        try {
            Parent root = FXMLLoader.load(WindowManager.class.getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            AlertManager.mostrarAlerta("Error de Interfaz", "No se pudo cargar la vista.", Alert.AlertType.ERROR);
        }
    }
}
