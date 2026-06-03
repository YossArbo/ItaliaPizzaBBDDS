package org.italiapizza.controller.viewController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author ELLIN JV
 */
public class BitacoraPedidoViewController implements Initializable {

    @FXML
    private Label labelBitacora;
    @FXML
    private ComboBox<?> comboBoxPedidos;
    @FXML
    private Label labelEnProceso;
    @FXML
    private Label labelEntregado;
    @FXML
    private Label labelCancelado;
    @FXML
    private Button buttonVolverAlMenu;
    @FXML
    private Button buttonRegresar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
