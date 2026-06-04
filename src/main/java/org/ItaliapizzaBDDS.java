package org;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.italiapizza.utils.AlertManager;

/**
 *
 * @author ELLIN JV
 */
public class ItaliapizzaBDDS extends Application{

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/Views/LoginView.fxml"));
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Italia Pizza LOGIN");
            stage.show();

        } catch (IOException e) {
            AlertManager.mostrarAlerta("Error", "Error: No se encontro la ventana principal Login", Alert.AlertType.ERROR);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}
