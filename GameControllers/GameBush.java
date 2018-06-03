package FXX.GameControllers;

import FXX.Additions;
import FXX.Arrete;
import FXX.Main;
import FXX.Sommet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class GameBush implements Initializable {

    @FXML
    Label lplayer;
    @FXML
    Button bExit;
    @FXML
    Pane paneGameBush;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Additions.gameTime = true;
        Additions.gameBush = true;

        if (Additions.nodeList.size() != 0) {
            for (Node nono : Additions.nodeList) {
                if (nono instanceof Arrete) ((Arrete) nono).setParentPane(paneGameBush);
                if (nono instanceof Sommet) ((Sommet) nono).setParentPane(paneGameBush);
                paneGameBush.getChildren().add(nono);
            }
        }

        Additions.verifyBeforeStart();


        lplayer.textProperty().bind(Additions.playerTurn);
        starting();
        Additions.history.setCompteurCouleurs();
    }

    public void starting() {
        Additions.start(paneGameBush);
    }

    public void onActionExit(ActionEvent actionEvent) {
        System.exit(1);
    }

    public void paneClick(MouseEvent mouseEvent) {

    }

    public void onActionUndo(ActionEvent actionEvent) {
        if (Additions.history.getIndex() != 0 && Additions.gameTime) {
            Additions.history.setUtilisable(true);
            Additions.history.setIndex(Additions.history.getIndex() - 1);
            paneGameBush.getChildren().clear();
            for (Node node : Additions.history.getLastHistorique()) {
                paneGameBush.getChildren().add(node);
                if (node instanceof Sommet) {
                    String tmpKey = String.valueOf(((Sommet) node).getMyId());
                    ((Sommet) node).setData(Additions.history.getHistData(tmpKey));
                }
            }
            Additions.start(paneGameBush);
            Additions.changeTurn(Color.GREEN);
            Additions.history.setUtilisable(false);
            System.out.println("restore");
        }
    }
}
