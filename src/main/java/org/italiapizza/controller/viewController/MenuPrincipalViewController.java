package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.SessionManager;
import org.italiapizza.utils.WindowManager;

public class MenuPrincipalViewController implements Initializable {

    @FXML
    private Label labelMenuPrincipal;
    @FXML
    private Button buttonAdmin;
    @FXML
    private Button buttonPedidos;
    @FXML
    private Button buttonInventarios;
    @FXML
    private Button buttonInfo;
    @FXML
    private Button buttonCerrarSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SessionManager.getEmpleadoActual() != null && "Cajero".equals(SessionManager.getEmpleadoActual().getRol())) {
            buttonAdmin.setDisable(true);
            buttonInventarios.setDisable(true);
            buttonCerrarSesion.setOnAction(this::cerrarSesion);
            buttonInfo.setOnAction(e -> mostrarInfoProyecto());
        }

        buttonAdmin.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/AdministracionView.fxml", "Administración"));
        buttonPedidos.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/PedidosView.fxml", "Pedidos"));
        buttonInventarios.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/InventarioView.fxml", "Inventario"));
        buttonCerrarSesion.setOnAction(this::cerrarSesion);
        buttonInfo.setOnAction(e -> mostrarInfoProyecto());

    }

    public void cerrarSesion(ActionEvent event) {
        SessionManager.setEmpleadoActual(null);
        WindowManager.cambiarVista(event, "/org/italiapizza/view/LoginView.fxml", "Inicio de Sesión");
    }

    private void mostrarInfoProyecto() {
        String titulo = "Información del Proyecto";
        String mensaje = "🍕 Sistema Italia Pizza\n\n"
                + "Equipo 5:\n" + "Arroyo Bonilla Heli Yosseline\n Jiménez Villanueva Ellin Alejandra\n"
                + "Materia: Bases de datos para el desarrollo de Software\n"
                + "Institución: Universidad Veracruzana\n"
                + "Versión: 1.0";

        AlertManager.mostrarAlerta(titulo, mensaje, Alert.AlertType.INFORMATION);
    }
}
