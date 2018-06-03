package FXX.GameControllers;

import FXX.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GameNim implements Initializable {

    @FXML
    Label lplayer;
    @FXML
    Label lvite;
    @FXML
    Button bExit;
    @FXML
    Button bStartGame;
    @FXML
    Pane paneGameNim;
    @FXML
    Slider sVitesse;

    volatile boolean pause = false;
    Thread myRunnableThread;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Additions.gameTime = true;
        Additions.gameNim = true;
        if (Additions.iCi) {
            sVitesse.setVisible(true);
            lvite.setVisible(true);
            bStartGame.setVisible(true);
        }

        if (Additions.nodeList.size() != 0) {
            for (Node nono : Additions.nodeList) {
                if (nono instanceof Arrete) ((Arrete) nono).setParentPane(paneGameNim);
                if (nono instanceof Sommet) ((Sommet) nono).setParentPane(paneGameNim);
                paneGameNim.getChildren().add(nono);
            }
        }

        lplayer.textProperty().bind(Additions.playerTurn);
        Additions.sliderValue.bind(sVitesse.valueProperty());

    }


    public void onActionExit(ActionEvent actionEvent) {
        try {
            myRunnableThread.interrupt();
        } catch (Exception e) {

        }
        System.exit(1);

    }

    public void paneClick(MouseEvent mouseEvent) {
    }


    public void sliderDragDone(MouseEvent mouseEvent) throws InterruptedException {
        if (sVitesse.getValue() == 0) {
            pause = true;
        } else {
            pause = false;
        }
    }

    public void onActionStartGame(ActionEvent actionEvent) {
        MyRunnable myRunnable = new MyRunnable();
        myRunnableThread = new Thread(myRunnable);
        myRunnableThread.start();
        bStartGame.setDisable(true);
    }


    class MyRunnable implements Runnable {
        Nim.Move mave = Additions.nim.nextMove(Additions.myHeaps);

        @Override
        public void run() {
            while (!mave.equals(Nim.Move.EMPTY)) {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        if (!pause) {
                            Additions.updateForNimGame("robot", mave.getIndex(), mave.getSize());
                            mave = Additions.nim.nextMove(Additions.myHeaps);
                        }
                    }
                });
                try {
                    if (!pause) Thread.sleep((long) (100000 / Additions.sliderValue.getValue()));
                    else Thread.sleep(1000);
                } catch (InterruptedException ignored) {

                }
            }
        }
    }

}
