package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.italiapizza.model.dao.UsuarioDAO;
import org.italiapizza.model.dto.Empleado;
import org.italiapizza.utils.AlertManager;
import org.italiapizza.utils.WindowManager;

public class RegistroEmpleadoViewController implements Initializable {

    @FXML
    private TextField textFieldNombreUsuario;
    @FXML
    private PasswordField passwordFieldContrasena;
    @FXML
    private PasswordField passwordFieldConfirmacion;
    @FXML
    private ComboBox<String> comboBoxRol;
    @FXML
    private Label labelNombreCompleto;
    @FXML
    private VBox vboxTelefonos;
    @FXML
    private VBox vboxEmails;
    @FXML
    private Button buttonRegistrar;
    @FXML
    private Button buttonCancelar;

    private String nombres;
    private String apellidos;
    private List<String> telefonos;
    private List<String> emails;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void initData(String nombres, String apellidos, List<String> telefonos, List<String> emails) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefonos = telefonos;
        this.emails = emails;
        
        labelNombreCompleto.setText(nombres + " " + apellidos);
        telefonos.forEach(t -> vboxTelefonos.getChildren().add(new Label(t)));
        emails.forEach(e -> vboxEmails.getChildren().add(new Label(e)));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBoxRol.getItems().addAll("Administrador", "Cajero", "Cocinero");
        buttonRegistrar.setOnAction(this::registrarEmpleado);
        buttonCancelar.setOnAction(this::regresar);
    }

    private void registrarEmpleado(ActionEvent event) {
        String usuario = textFieldNombreUsuario.getText();
        String contrasena = passwordFieldContrasena.getText();
        String confirmacion = passwordFieldConfirmacion.getText();
        String rol = comboBoxRol.getValue();

        if (usuario.isEmpty() || contrasena.isEmpty() || confirmacion.isEmpty() || rol == null) {
            AlertManager.mostrarAlerta("Campos Vacíos", "Por favor, complete todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        if (!contrasena.equals(confirmacion)) {
            AlertManager.mostrarAlerta("Error", "Las contraseñas no coinciden.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Empleado empleado = new Empleado();
            empleado.setNombres(nombres);
            empleado.setApellidos(apellidos);
            
            String stringTelefonos = String.join(", ", telefonos);
            String stringEmails = String.join(", ", emails);
            empleado.setTelefono(stringTelefonos);
            empleado.setEmail(stringEmails);
            
            empleado.setTipoUsuario("Empleado");
            empleado.setEstatus(1);
            empleado.setNombreUsuario(usuario);
            empleado.setContrasena(contrasena);
            empleado.setRol(rol);

            usuarioDAO.registrarUsuario(empleado);
            AlertManager.mostrarAlerta("Éxito", "Empleado registrado correctamente.", Alert.AlertType.INFORMATION);
            WindowManager.cambiarVista(event, "/org/italiapizza/view/MenuUsuarioView.fxml", "Gestión de Usuarios");
        } catch (Exception e) {
            AlertManager.mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void regresar(ActionEvent event) {
        WindowManager.cambiarVista(event, "/org/italiapizza/view/UsuariosView.fxml", "Seleccionar Tipo");
    }
}