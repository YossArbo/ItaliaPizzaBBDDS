package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button buttonAyuda;
    @FXML
    private Button buttonCerrarSesion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SessionManager.getEmpleadoActual() != null && "Cajero".equals(SessionManager.getEmpleadoActual().getRol())) {
            buttonAdmin.setDisable(true);
            buttonInventarios.setDisable(true);
        }

        buttonAdmin.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/AdministracionView.fxml", "Administración"));
        buttonPedidos.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/PedidosView.fxml", "Pedidos"));
        buttonInventarios.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/InventarioView.fxml", "Inventario"));
        buttonCerrarSesion.setOnAction(this::cerrarSesion);
    }

    private void cerrarSesion(ActionEvent event) {
        SessionManager.setEmpleadoActual(null);
        WindowManager.cambiarVista(event, "/org/italiapizza/view/LoginView.fxml", "Inicio de Sesión");
    }
}
