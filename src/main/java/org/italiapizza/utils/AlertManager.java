package org.italiapizza.utils;

import javafx.scene.control.Alert;


/**
 *
 * @author ELLIN JV
 */
public class AlertManager {

    public static void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}