package FXX.Controllers;

import FXX.Main;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerStart implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onActionHack(ActionEvent actionEvent) throws Exception {
        Main.home = "HackenbushFXML.fxml";
        Main.refresh();
    }

    public void onActionNim(ActionEvent actionEvent) throws Exception {
        Main.home = "NimFXML.fxml";
        Main.refresh();
    }

}
