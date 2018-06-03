package FXX.Controllers;

import FXX.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerHackenbush implements Initializable {
    @FXML
    BorderPane borderpane;
    @FXML
    ToggleButton sommet;
    @FXML
    ToggleButton arrete;
    @FXML
    ToggleButton Delete;
    @FXML
    ToggleButton Edit;
    @FXML
    ComboBox colorsBox;
    @FXML
    Pane pane;
    @FXML
    ToggleButton Select;
    @FXML
    TextField tFieldRot;
    @FXML
    Line line;
    @FXML
    RadioButton misere;
    @FXML
    RadioButton normal;
    @FXML
    Button bStart;

    private Boolean mouseOnLine = false;

    public static boolean first = true;
    public static BooleanProperty bSommet = new SimpleBooleanProperty();
    public static BooleanProperty bArrete = new SimpleBooleanProperty();
    public static BooleanProperty bDelete = new SimpleBooleanProperty();
    public static BooleanProperty bEdit = new SimpleBooleanProperty();
    public static BooleanProperty bSelect = new SimpleBooleanProperty();
    private Rectangle rect;
    private Historique historique;
    private Selection selectionItems = new Selection();
    private PositionOriginal milieu = new PositionOriginal();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        rect = new Rectangle(0, 0, 0, 0);
        rect.setStroke(Color.BROWN);
        rect.setStrokeWidth(1);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.getStrokeDashArray().addAll(10.0);
        rect.setFill(Color.LIGHTGRAY.deriveColor(0, 1.2, 1, 0.6));

        bSommet.bind(sommet.selectedProperty());
        bArrete.bind(arrete.selectedProperty());
        bDelete.bind(Delete.selectedProperty());
        bEdit.bind(Edit.selectedProperty());
        bSelect.bind(Select.selectedProperty());


        colorsBox.setCellFactory(
                new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        return new ListCell<>() {
                            {
                                super.setPrefWidth(100);
                            }

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {

                                    setText(item);
                                    if (item.contains("Bleu"))
                                        setTextFill(Color.BLUE);
                                    else if (item.contains("Rouge"))
                                        setTextFill(Color.RED);
                                    else
                                        setTextFill(Color.GREEN);

                                } else
                                    setText(null);
                            }
                        };
                    }
                });

        colorsBox.valueProperty().addListener((ChangeListener<String>) (observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Bleu":
                    Additions.defaultColor = Color.BLUE;
                    break;
                case "Rouge":
                    Additions.defaultColor = Color.RED;
                    break;
                default:
                    Additions.defaultColor = Color.GREEN;
                    break;
            }
        });

        addEventHandlerForPane(pane);
        addEventHandlerForRect(rect);

        Additions.initalizeEffect();

        historique = new Historique();
    }


    private void addEventHandlerForPane(Pane parent) {

        final PositionOriginal positionOriginal = new PositionOriginal();

        parent.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            if (Select.isSelected() && me.getButton().equals(MouseButton.SECONDARY)) {
                milieu.startX = me.getX();
                milieu.startY = me.getY();

                selectionItems.clearSet();

                positionOriginal.x = me.getX();
                positionOriginal.y = me.getY();

                rect.setX(me.getX());
                rect.setY(me.getY());
                rect.setWidth(0);
                rect.setHeight(0);
                rect.setVisible(true);

                if (!parent.getChildren().contains(rect))
                    parent.getChildren().add(rect);
                else if (parent.getChildren().contains(rect)) {

                    parent.getChildren().remove(rect);
                    rect.setVisible(false);
                    rect.setX(0);
                    rect.setY(0);
                    rect.setWidth(0);
                    rect.setHeight(0);

                }
                //  me.consume();
            }
        });

        parent.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent me) -> {
            if (Select.isSelected() && me.getButton().equals(MouseButton.SECONDARY)) {

                if (me.getX() - positionOriginal.x > 0)
                    rect.setWidth(me.getX() - positionOriginal.x);
                else {
                    rect.setX(me.getX());
                    rect.setWidth(positionOriginal.x - rect.getX());
                }

                if (me.getY() - positionOriginal.y > 0)
                    rect.setHeight(me.getY() - positionOriginal.y);
                else {
                    rect.setY(me.getY());
                    rect.setHeight(positionOriginal.y - rect.getY());
                }
                //  me.consume();
            }
        });

        parent.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent me) -> {
            if (Select.isSelected() && me.getButton().equals(MouseButton.SECONDARY)) {
                for (Node n : pane.getChildren()) {
                    if (n instanceof Circle) {//ici ajouter circle

                        if (n.getBoundsInParent().intersects(rect.getBoundsInParent()))
                            selectionItems.addSommetToSet((Sommet) n);

                    } else if (n instanceof Path) {//ici ajouter line

                        if (rect.getBoundsInParent().contains(n.getBoundsInParent()))
                            selectionItems.addArreteToSet((Arrete) n);
                        else if (n.getBoundsInParent().intersects(rect.getBoundsInParent()))
                            selectionItems.addArreteToSetInterect((Arrete) n);

                    }
                }

                selectionItems.sommetSetToString();
                milieu.endX = me.getX();
                milieu.endY = me.getY();
                milieu.x = (milieu.startX + milieu.endX) / 2;
                milieu.y = (milieu.startY + milieu.endY) / 2;
                //  me.consume();
            }
        });
    }

    private void addEventHandlerForRect(Rectangle rectangle) {
        final PositionOriginal positionOriginal = new PositionOriginal();

        rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED, Event::consume);

        rectangle.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent me) -> {
            positionOriginal.x = rectangle.getTranslateX() - me.getSceneX();
            positionOriginal.y = rectangle.getTranslateY() - me.getSceneY();

            if (Select.isSelected() && me.getButton().equals(MouseButton.PRIMARY) && rect.isVisible()) {

                for (Sommet n : selectionItems.getSelectionSommetSet()) {
                    n.setTranslateX(positionOriginal.x + me.getSceneX());
                    n.setTranslateY(positionOriginal.y + me.getSceneY());
                }
                me.consume();

            }
        });

        rectangle.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent me) -> {
            rectangle.setTranslateX(positionOriginal.x + me.getSceneX());
            rectangle.setTranslateY(positionOriginal.y + me.getSceneY());

            if (Select.isSelected()) {

                for (Sommet n : selectionItems.getSelectionSommetSet()) {
                    n.setTranslateX(positionOriginal.x + me.getSceneX());
                    n.setTranslateY(positionOriginal.y + me.getSceneY());
                }

            }
            me.consume();
        });

        rectangle.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent me) -> {
            if (Select.isSelected()) {

                for (Circle n : selectionItems.getSelectionSommetSet()) {
                    n.setCenterX(n.getCenterX() + n.getTranslateX());
                    n.setCenterY(n.getCenterY() + n.getTranslateY());

                    n.setTranslateX(0);
                    n.setTranslateY(0);
                }
                rectangle.setX(rectangle.getX() + rectangle.getTranslateX());
                rectangle.setY(rectangle.getY() + rectangle.getTranslateY());
                rectangle.setTranslateX(0);
                rectangle.setTranslateY(0);

            }
            //me.consume();
        });

    }


    public void paneClick(MouseEvent me) {

        if (me.getButton().equals(MouseButton.PRIMARY)) {

            if (sommet.isSelected() && !Select.isSelected()) {

                if (me.getY() < line.getStartY() + 2) {

                    Sommet circle;
                    if (mouseOnLine) {
                        circle = new Sommet(me.getX(), line.getStartY(), 10, Color.WHITE, Color.BLACK, 2, pane);
                        circle.setJeSuisRacine(true);
                    } else
                        circle = new Sommet(me.getX(), me.getY(), 10, Color.WHITE, Color.BLACK, 2, pane);

                    pane.getChildren().add(circle);

                }

            } else if (arrete.isSelected() && !Select.isSelected()) {

                if (Additions.circleSeconde != null) {

                    Arrete arrete;
                    MoveTo moveTo = new MoveTo();
                    moveTo.xProperty().bind(Additions.circleOne.centerXProperty());
                    moveTo.yProperty().bind(Additions.circleOne.centerYProperty());

                    if (Additions.circleOne == Additions.circleSeconde) {

                        CubicCurveTo cubicTo = new CubicCurveTo();
                        cubicTo.setControlX1(Additions.circleOne.getCenterX() / 2);
                        cubicTo.setControlY1(Additions.circleOne.getCenterY() / 2);
                        cubicTo.setControlX2(-1 * Additions.circleOne.getCenterX() / 2);
                        cubicTo.setControlY2(Additions.circleOne.getCenterY() / 2);
                        cubicTo.xProperty().bind(Additions.circleSeconde.centerXProperty());
                        cubicTo.yProperty().bind(Additions.circleSeconde.centerYProperty());

                        arrete = new Arrete(5, Additions.defaultColor, moveTo, cubicTo, pane);

                    } else {

                        LineTo lineTo = new LineTo();
                        lineTo.xProperty().bind(Additions.circleSeconde.centerXProperty());
                        lineTo.yProperty().bind(Additions.circleSeconde.centerYProperty());
                        arrete = new Arrete(5, Additions.defaultColor, moveTo, lineTo, pane);

                    }

                    Additions.circleOne.addArreteToMyList(arrete);
                    Additions.circleSeconde.addArreteToMyList(arrete);
                    Additions.becomeNeighbors(Additions.circleOne, Additions.circleSeconde);
                    arrete.addSommetToMyList(Additions.circleOne, Additions.circleSeconde);
                    pane.getChildren().add(arrete);
                    Additions.front(Additions.circleOne, Additions.circleSeconde);
                    Additions.circleSeconde = null;
                }
            }
        }
    }

    public void onMouseEnteredLine() {
        line.setStroke(Color.BLUE);
        mouseOnLine = true;
    }

    public void onMouseExited() {
        line.setStroke(Color.BLACK);
        mouseOnLine = false;
    }

    public void onActionSave() {
        if (!historique.isUtilisable()) {
            historique.setUtilisable(true);
            historique.clear();
            for (Node node : pane.getChildren()) {
                historique.ajout(node);
            }
            historique.saveIntoHistoriqueTotal();
            historique.setUtilisable(false);
        }

        System.out.println("sauvegarde");
    }

    public void onActionQuit() {
        System.exit(1);
    }

    public void onActionRestore() {

        if (historique.getIndex() != 0) {
            historique.setUtilisable(true);
            historique.setIndex(historique.getIndex() - 1);
            pane.getChildren().clear();
            for (Node node : historique.getLastHistorique()) {
                pane.getChildren().add(node);
                if (node instanceof Sommet) {
                    String tmpKey = String.valueOf(((Sommet) node).getMyId());
                    ((Sommet) node).setData(historique.getHistData(tmpKey));
                }
            }
            historique.setUtilisable(false);
            System.out.println("restore");

        }
    }

    public void onActionReset() {
        if (pane.getChildren().contains(rect)) {

            pane.getChildren().remove(rect);
            rect.setVisible(false);
            rect.setX(0);
            rect.setY(0);
            rect.setWidth(0);
            rect.setHeight(0);

        }
    }

    public void onActionSelect() {
        if (Select.isSelected())
            Select.setTooltip(new Tooltip("Sélectionner"));
        else {
            sommet.setSelected(true);
            Select.setTooltip(new Tooltip("Désélectionner"));
        }
    }

    public void onActionRot() {
        if (Select.isSelected()) {

            for (Sommet n : selectionItems.getSelectionSommetSet()) {

                double xu = n.getCenterX() - milieu.x;
                double yu = n.getCenterY() - milieu.y;

                double degre = Double.valueOf(tFieldRot.getText());
                double radian = Math.toRadians(degre);

                double cos = Math.cos(radian);
                double sin = Math.sin(radian);

                double xv = xu * cos - yu * sin;
                double yv = xu * sin + yu * cos;
                double cx = milieu.x + xv;
                double cy = milieu.y + yv;

                n.setCenterX(cx);
                n.setCenterY(cy);
            }

        }
    }

    public void onActionStart() throws Exception {
        Additions.nodeList.addAll(pane.getChildren());
        onActionSave();
        Main.home = "GameBushFXML.fxml";
        Main.refresh();
    }

    public void onActionCopier() {
        if (!selectionItems.isSommetSetEmpty()) selectionItems.faireCopieDesSommets();
    }

    public void onActionColler() {
        for (Sommet n : selectionItems.getSelectionSommetSetCopie())
            pane.getChildren().add(n);
    }

    public void onActionCouper() {
    }

    public void onActionRemove() {
        for (Node n : pane.getChildren()) {
            if (selectionItems.setContainsElement(n)) {
                pane.getChildren().remove(n);
                onActionRemove();
                return;
            }
        }
        selectionItems.clearSet();
    }

    public void onActionTypeDeJeux() {
        Additions.mode = normal.isSelected();
        bStart.setDisable(false);
    }

    public void onActionArrete() {

    }

    public void onActionSommet() {

    }

    public void onActionEdit() {

    }

    public void onActionDelete() {

    }

    public void borderPaneClick() {

    }

    public void onActionColorsBox() {

    }

    public void onActionBack() throws Exception{
        Main.home = "StartFXML.fxml";
        Main.refresh();
    }
}
