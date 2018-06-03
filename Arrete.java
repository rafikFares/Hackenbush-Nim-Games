package FXX;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static FXX.Additions.*;
import static FXX.Controllers.ControllerHackenbush.bDelete;
import static FXX.Controllers.ControllerHackenbush.bSelect;

public class Arrete extends Path {

    Pane parentPane;
    Color myColor;
    int myTige = -1;
    int rang = -1;

    List<Node> mySommetList = new ArrayList<>();

    public Arrete() {
        super();
    }


    public Arrete(double stroke, Color couleur, MoveTo moveTo, CubicCurveTo cubicCurveTo, Pane pane) {
        super();
        this.setStrokeWidth(stroke);
        this.setStroke(couleur);
        this.myColor = couleur;
        this.setCursor(Cursor.CLOSED_HAND);
        this.parentPane = pane;
        this.getElements().addAll(moveTo, cubicCurveTo, new ClosePath());
        this.addEventHandlerForArrete();
    }

    public Arrete(double stroke, Color couleur, MoveTo moveTo, LineTo lineTo, Pane pane) {
        super();
        this.setStrokeWidth(stroke);
        this.setStroke(couleur);
        this.myColor = couleur;
        this.parentPane = pane;
        this.setCursor(Cursor.CLOSED_HAND);
        this.getElements().addAll(moveTo, lineTo);
        this.addEventHandlerForArrete();
    }

    public void addEventHandlerForArrete() {

        final PositionOriginal positionOriginal = new PositionOriginal();

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            if (!bDelete.get() && !bSelect.get() && !gameTime && me.getButton().equals(MouseButton.SECONDARY)) {
                if (this.getMyColor().equals(Color.RED)) this.setMyColor(Color.BLUE);
                else if (this.getMyColor().equals(Color.BLUE)) this.setMyColor(Color.GREEN);
                else if (this.getMyColor().equals(Color.GREEN)) this.setMyColor(Color.RED);
            } else if (bDelete.get() && !bSelect.get() && !gameTime) deleteMe();
            else if (gameTime) {
                if (changeTurn(this.getMyColor())) {
                    if (Additions.gameBush) Additions.saveNewHistory(this.getParentPane());
                    deleteMe();
                } else afficherAlert();
            }

            me.consume();

        });

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent me) -> {

            double tx = me.getX();
            double ty = me.getY();

            positionOriginal.x = tx;
            positionOriginal.y = ty;

            if (this.getElements().get(1) instanceof QuadCurveTo) {
                ((QuadCurveTo) this.getElements().get(1)).setControlX(me.getX());
                ((QuadCurveTo) this.getElements().get(1)).setControlY(me.getY());
            } else if (this.getElements().get(1) instanceof LineTo) {
                QuadCurveTo qt = new QuadCurveTo();
                qt.xProperty().bind(((LineTo) this.getElements().get(1)).xProperty());
                qt.yProperty().bind(((LineTo) this.getElements().get(1)).yProperty());
                qt.setControlX(me.getX());
                qt.setControlY(me.getY());
                this.getElements().remove(1);
                this.getElements().add(qt);
            } else if (this.getElements().get(1) instanceof CubicCurveTo) {
                ((CubicCurveTo) this.getElements().get(1)).setControlX1(me.getX());
                ((CubicCurveTo) this.getElements().get(1)).setControlX2(me.getY());
            }

            me.consume();
        });

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent me) -> {

            positionOriginal.x = me.getX();
            positionOriginal.y = me.getY();
            this.setStroke(Color.BLACK);
            //me.consume();

        });

        this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent me) -> {

            this.setStroke(this.myColor);
            me.consume();
        });

    }

    private void afficherAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "C'est pas ta couleur !", ButtonType.OK);
        alert.setTitle("Information");
        alert.showAndWait();
    }

    public void deleteMe() {
        if (gameNim) Additions.updateForNimGame("moi", this.myTige, this.rang);
        else if (gameBush) {
            boolean bool1 = false;
            boolean bool2 = false;
            if (this.getMySommetList().get(0) == this.getMySommetList().get(1)) {
                Additions.substractCompteurs(this.getMyColor());
                // this.removeMe();
            } else {
                try {
                    System.out.println("deleteneighbors");
                    Additions.deleteNeighbors((Sommet) this.getMySommetList().get(0), (Sommet) this.getMySommetList().get(1));
                } catch (Exception e) {

                }
                HashSet set1 = new HashSet();
                HashSet set2 = new HashSet();
                if (Additions.findRacine((Sommet) this.getMySommetList().get(0), (Sommet) this.getMySommetList().get(1), set1)) {
                    bool1 = true;
                } else {
                    Additions.updateForBush(set1);
                }
                if (Additions.findRacine((Sommet) this.getMySommetList().get(1), (Sommet) this.getMySommetList().get(0), set2)) {
                    bool2 = true;
                } else {
                    Additions.updateForBush(set2);
                }
            }
            if (bool1 && bool2) Additions.substractCompteurs(this.getMyColor());
        }

        removeMe();
    }


    public void removeMe() {

        try {
            Additions.deleteNeighbors((Sommet) this.getMySommetList().get(0), (Sommet) this.getMySommetList().get(1));
        } catch (Exception e) {

        }

        for (Node somet : this.getMySommetList()) {
            ((Sommet) somet).removeArreteFromMyList(this);
        }

        parentPane.getChildren().remove(this); // pour suppression

    }

    public void addSommetToMyList(Node n) {
        mySommetList.add(n);
    }

    public void addSommetToMyList(Node n1, Node n2) {
        mySommetList.add(n1);
        mySommetList.add(n2);
    }

    public int getMyTige() {
        return myTige;
    }

    public void setMyTige(int myTige) {
        this.myTige = myTige;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public boolean myListContains(Node n) {
        return mySommetList.contains(n);
    }

    public void removeSommetFromMyList(Node n) {
        mySommetList.remove(n);
    }

    public Pane getParentPane() {
        return parentPane;
    }

    public void setParentPane(Pane parentPane) {
        this.parentPane = parentPane;
    }

    public Color getMyColor() {
        return myColor;
    }

    public void setMyColor(Color myColor) {
        this.myColor = myColor;
    }

    public List<Node> getMySommetList() {
        return mySommetList;
    }

    public void setMySommetList(List<Node> mySommetList) {
        this.mySommetList = mySommetList;
    }
}
