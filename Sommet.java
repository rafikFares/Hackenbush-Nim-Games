package FXX;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static FXX.Controllers.ControllerHackenbush.*;

public class Sommet extends Circle {

    Pane parentPane;
    Color myColor;
    boolean jeSuisRacine = false;
    int myTige = -1;
    int rang = -1;
    long id;

    List<Node> myArreteList;
    List<Sommet> myNeighbors;


    public Sommet() {
        super();
    }

    public Sommet(double centerX, double centerY, double radius, Color color, Color stroke, double strokeWidth, Pane parent) {
        super(centerX, centerY, radius, color);
        this.setStroke(stroke);
        this.setCursor(Cursor.CROSSHAIR);
        this.myColor = color;
        this.setStrokeWidth(strokeWidth);
        this.parentPane = parent;
        this.myArreteList = new ArrayList<>();
        this.myNeighbors = new ArrayList<>();
        generateId();
        this.addEventHandlerForSommet();
    }

    public long getMyId() {
        return id;
    }

    public void setMyId(long id) {
        this.id = id;
    }

    public void generateId() {
        id = new Random().nextLong();
        if (!Additions.allIds.isEmpty() && Additions.allIds.contains(id)) this.generateId();
        else Additions.allIds.add(id);
    }

    public List<Node> getCopyArreteList() {
        return new ArrayList<Node>(myArreteList);
    }

    public List<Sommet> getCopyNeighbors() {
        return new ArrayList<Sommet>(myNeighbors);
    }

    public HashMap<List<Node>, List<Sommet>> getData() {
        HashMap<List<Node>, List<Sommet>> map = new HashMap<>();
        map.put(this.getCopyArreteList(), this.getCopyNeighbors());
        return map;
    }

    public void setData(HashMap<List<Node>, List<Sommet>> data) {
        this.setMyArreteList(data.keySet().stream().findFirst().get());
        this.setMyNeighbors(data.get(data.keySet().stream().findFirst().get()));
    }


    public void addEventHandlerForSommet() {

        final PositionOriginal positionOriginal = new PositionOriginal();

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent me) -> {

            if (bDelete.get() && !bSelect.get()) {

                deleteMe();

            } else if (bArrete.get()) {

                if (first) {
                    Additions.circleOne = this;
                    first = false;
                    System.out.println("first");
                } else {
                    Additions.circleSeconde = this;
                    first = true;
                    System.out.println("seconde");
                }
            }
            //  me.consume(); // ca
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {

            positionOriginal.x = this.getTranslateX() - me.getSceneX();
            positionOriginal.y = this.getTranslateY() - me.getSceneY();

        });

        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent me) -> {

            if (bEdit.get() && !bSelect.get()) {
                this.setCenterX(me.getX());
                this.setCenterY(me.getY());

            }

        });

        this.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent me) -> {
            this.setEffect(Additions.ds1);
            this.setFill(Color.BROWN);
            // me.consume();

        });

        this.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent me) -> {
            this.setEffect(null);
            this.setFill(this.myColor);
            me.consume();
        });

    }

    public void addArreteToMyList(Node n) {
        myArreteList.add(n);
    }

    public boolean myListContains(Node n) {
        return myArreteList.contains(n);
    }

    public void removeArreteFromMyList(Node n) {
        myArreteList.remove(n);
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

    public List<Node> getMyArreteList() {
        return myArreteList;
    }

    public void setMyArreteList(List<Node> myArreteList) {
        this.myArreteList = myArreteList;
    }

    public boolean isJeSuisRacine() {
        return jeSuisRacine;
    }

    public void setJeSuisRacine(boolean jeSuisRacine) {
        this.jeSuisRacine = jeSuisRacine;
    }

    public void deleteMe() {

        List<Node> myArreteListTemp = new ArrayList<>(this.getMyArreteList());

        for (Node aret : myArreteListTemp) {
            ((Arrete) aret).removeMe();
            Additions.substractCompteurs(((Arrete) aret).myColor);
        }

        parentPane.getChildren().remove(this); // pour suppression

    }

    public void removeMe() {

        parentPane.getChildren().remove(this); // pour suppression

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

    public List<Sommet> getMyNeighbors() {
        return myNeighbors;
    }

    public void setMyNeighbors(List<Sommet> myNeighbors) {
        this.myNeighbors = myNeighbors;
    }

    public void addToMyNeighbors(Sommet s) {
        this.myNeighbors.add(s);
    }

    public void removeSommetFromMyNeighbors(Sommet n) {
        if (myNeighbors.contains(n)) myNeighbors.remove(n);
    }
}

