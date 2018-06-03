package FXX.Controllers;

import FXX.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;

import java.net.URL;
import java.util.ResourceBundle;

import static FXX.Additions.myHeaps;
import static FXX.Additions.nim;

public class ControllerNim implements Initializable {

    @FXML
    TextField tFieldnumberOfHeaps;
    @FXML
    TextField tFieldmaxSize;
    @FXML
    Pane paneNim;
    @FXML
    Line line;
    @FXML
    RadioButton misere;
    @FXML
    RadioButton normal;
    @FXML
    RadioButton ia;
    @FXML
    RadioButton iaContreIa;
    @FXML
    Button bStart;

    double[] espaceY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nim = new Nim(true);

    }

    public void onActionGenerate(ActionEvent actionEvent) {

        if (paneNim.getChildren().size() != 0)
            paneNim.getChildren().clear();

        paneNim.getChildren().add(line);

        myHeaps = nim.generateRandomNim(Integer.valueOf(tFieldnumberOfHeaps.getText()),
                Integer.valueOf(tFieldmaxSize.getText()));

        espaceY = new double[Integer.valueOf(tFieldnumberOfHeaps.getText())];


        startDraw();

    }

    private void startDraw() {
        double espaceX = (line.getEndX() - line.getStartX()) / (myHeaps.length + 1);

        for (int i = 0; i < myHeaps.length; i++) {
            Sommet circle = new Sommet(line.getStartX() + ((i + 1) * espaceX), line.getStartY(), 10, Color.WHITE, Color.BLACK, 2, paneNim);

            circle.setMyTige(i);
            circle.setRang(0);

            paneNim.getChildren().add(circle);

            espaceY[i] = (line.getStartY() - 35) / myHeaps[i];

            double tempy = espaceY[i] / myHeaps[i];

            double temp = line.getStartY();

            Sommet circleTmp = null;

            for (int j = 0; j < myHeaps[i]; j++) {
                temp -= espaceY[i];

                Sommet circle2 = new Sommet(line.getStartX() + ((j + 1) * 5) + ((i + 1) * espaceX),
                        temp, 10, Color.WHITE, Color.BLACK, 2, paneNim);

                temp += ((j * tempy) / 2);

                Arrete arrete;
                MoveTo moveTo = new MoveTo();
                LineTo lineTo = new LineTo();
                if (j == 0) {
                    moveTo.xProperty().bind(circle.centerXProperty());
                    moveTo.yProperty().bind(circle.centerYProperty());
                } else {
                    moveTo.xProperty().bind(circleTmp.centerXProperty());
                    moveTo.yProperty().bind(circleTmp.centerYProperty());
                }
                lineTo.xProperty().bind(circle2.centerXProperty());
                lineTo.yProperty().bind(circle2.centerYProperty());

                arrete = new Arrete(4, Color.GREEN, moveTo, lineTo, paneNim);
                arrete.setMyTige(i);
                arrete.setRang(j);

                circle2.setMyTige(i);
                circle2.setRang(j + 1);

                paneNim.getChildren().addAll(arrete, circle2);

                if (circleTmp != null) Additions.front(circle2, circleTmp);
                else Additions.front(circle, circle2);

                circleTmp = circle2;

            }

        }

    }

    public void onActionStart(ActionEvent actionEvent) throws Exception {
        for (Node node : paneNim.getChildren()) {
            Additions.nodeList.add(node);
        }
        if (iaContreIa.isSelected()) Additions.iCi = true;
        Main.home = "GameNimFXML.fxml";
        Main.refresh();
    }

    public void paneClick(MouseEvent mouseEvent) {

    }

    public void onMouseEnteredLine(MouseEvent mouseEvent) {
    }

    public void borderPaneClick(MouseEvent mouseEvent) {
    }

    public void onActionTypeDeJeux(ActionEvent actionEvent) {
        Additions.mode = normal.isSelected();
        bStart.setDisable(false);
    }

    public void onActionIA(ActionEvent actionEvent) {
        Additions.againstIA = ia.isSelected();
    }

    public void onActionIAContreIA(ActionEvent actionEvent) {
        Additions.againstIA = iaContreIa.isSelected();
    }

    public void onActionBack(ActionEvent actionEvent) throws Exception{
        Main.home = "StartFXML.fxml";
        Main.refresh();
    }
}
