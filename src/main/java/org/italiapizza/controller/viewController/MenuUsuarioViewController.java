package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.italiapizza.model.dao.UsuarioDAO;
import org.italiapizza.model.dto.Usuario;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.SessionManager;
import org.italiapizza.utils.WindowManager;

public class MenuUsuarioViewController implements Initializable {

    @FXML
    private Label labelConsultarUsuario;
    @FXML
    private TableView<Usuario> tableViewUsuarios;
    @FXML
    private TableColumn<Usuario, String> tableColumnNombreCompleto;
    @FXML
    private TableColumn<Usuario, String> tableColumnTelefono;
    @FXML
    private TableColumn<Usuario, String> tableColumnEmail;
    @FXML
    private TableColumn<Usuario, String> tableColumnTipoUsuario;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonEliminar;
    @FXML
    private TextField textFieldBuscadorUsuario;
    @FXML
    private Button buttonNuevoUsuario;
    @FXML
    private Button buttonVolverAlMenu;
    @FXML
    private Button buttonRegresar;
    @FXML
    private TableColumn<Usuario, String> tableColumnEstatus;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ObservableList<Usuario> usuariosObservable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarUsuarios("");

        textFieldBuscadorUsuario.textProperty().addListener((obs, oldText, newText) -> cargarUsuarios(newText));

        buttonNuevoUsuario.setOnAction(e -> abrirModalNuevoUsuario());
        buttonEliminar.setOnAction(e -> eliminarUsuario());
        buttonRegresar.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/AdministracionView.fxml", "Administración"));
        buttonVolverAlMenu.setOnAction(e -> WindowManager.cambiarVista(e, "/org/italiapizza/view/MenuPrincipalView.fxml", "Menú Principal"));
    }

    private void configurarColumnas() {
        tableColumnNombreCompleto.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        tableColumnTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnTipoUsuario.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));

        tableColumnEstatus.setCellValueFactory(cellData -> {
            int estatus = cellData.getValue().getEstatus();
            String estadoTexto = (estatus == 1) ? "Activo" : "Inactivo";
            return new javafx.beans.property.SimpleStringProperty(estadoTexto);
        });
    }

    private void cargarUsuarios(String busqueda) {
        if (busqueda == null || busqueda.trim().length() < 2) {
            if (usuariosObservable != null) {
                usuariosObservable.clear();
            }
            return;
        }
        try {
            List<Usuario> lista = usuarioDAO.buscarUsuariosParaConsulta(busqueda);
            usuariosObservable = FXCollections.observableArrayList(lista);
            tableViewUsuarios.setItems(usuariosObservable);
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", "No se pudo cargar la lista de usuarios.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void abrirModalNuevoUsuario() {
        WindowManager.abrirModal("/org/italiapizza/view/RegistroUsuarioView.fxml", "Registrar Usuario");
        cargarUsuarios(textFieldBuscadorUsuario.getText());
    }

    private void eliminarUsuario() {
        Usuario seleccionado = tableViewUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertManager.mostrarAlerta("Advertencia", "Seleccione un usuario para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            usuarioDAO.eliminarUsuario(seleccionado.getIdUsuario(), SessionManager.getEmpleadoActual().getIdUsuario());
            AlertManager.mostrarAlerta("Éxito", "Usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
            cargarUsuarios(textFieldBuscadorUsuario.getText());
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
