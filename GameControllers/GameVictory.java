package FXX.GameControllers;

import FXX.Additions;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameVictory implements Initializable {

    @FXML
    Label viLabel;
    @FXML
    Pane viPaneBas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Additions.gameBush){
            if (Additions.turn) {
                if (Additions.mode) {//normal
                    viLabel.setText("Joueur Rouge gagnant !");
                } else {//misere
                    viLabel.setText("Joueur Bleu gagnant !");
                }
            } else if (!Additions.turn) {
                if (Additions.mode) {//normal
                    viLabel.setText("Joueur Bleu gagnant !");
                } else {//misere
                    viLabel.setText("Joueur Rouge gagnant !");
                }
            }
        }else if (Additions.gameNim){
            if (Additions.mode) {//normal
                if (Additions.turn)viLabel.setText("Joueur I gagnant !");
                else if (!Additions.turn)viLabel.setText("Joueur II gagnant !");
            } else {//misere
                if (Additions.turn)viLabel.setText("Joueur II gagnant !");
                else if (!Additions.turn)viLabel.setText("Joueur I gagnant !");
            }
        }

        showAnimation();

    }

    private void showAnimation() {
        String im = GameVictory.class.getResource("../Images/winning.jpg").toExternalForm();
        Image image = new Image(im);
        ImageView iv1 = new ImageView();
        iv1.setImage(image);

        viPaneBas.getChildren().add(iv1);

        Timeline timeline = new Timeline();

        Duration time = new Duration(1000);
        KeyValue keyValue = new KeyValue(iv1.translateXProperty(), 300);
        KeyFrame keyFrame = new KeyFrame(time, keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(20);
        timeline.setAutoReverse(true);

        timeline.play();
    }

}
