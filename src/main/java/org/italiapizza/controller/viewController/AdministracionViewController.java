package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.italiapizza.utils.WindowManager;

public class AdministracionViewController implements Initializable {

    @FXML
    private Label labelAdministracion;
    @FXML
    private Button buttonUsuarios;
    @FXML
    private Button buttonProductos;
    @FXML
    private Button buttonNegocio;
    @FXML
    private Button buttonAcercaDe;
    @FXML
    private Button buttonRegresar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buttonUsuarios.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/MenuUsuarioView.fxml", "Gestión de Usuarios"));
        buttonProductos.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/InventarioView.fxml", "Gestión de Productos"));
        buttonRegresar.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/MenuPrincipalView.fxml", "Menú Principal"));
    }
}