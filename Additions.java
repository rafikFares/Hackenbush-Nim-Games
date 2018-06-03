package FXX;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Additions {

    public static Color defaultColor = Color.BLUE;
    public static Sommet circleOne = null;
    public static Sommet circleSeconde = null;
    public static boolean gameTime = false;
    public static boolean gameNim = false;
    public static boolean gameBush = false;
    public static boolean againstIA = false;
    public static int[] myHeaps;
    public static Nim nim;
    public static Nim.Move cup;
    public static boolean mode; // true - normal // false - misere
    public static boolean iCi = false;
    public static DoubleProperty sliderValue = new SimpleDoubleProperty();

    public static volatile boolean turn = false; // true = bleu / false = rouge
    public static StringProperty playerTurn = new SimpleStringProperty("RED");

    public static List<Node> nodeList = new ArrayList<>();
    public static int arreteBlue = 0;
    public static int arreteRed = 0;
    public static List<Long> allIds = new ArrayList<>();

    public static Historique history;
    public static DropShadow ds1 = new DropShadow();

    public static void initalizeEffect() {
        ds1.setColor(Color.CORAL);
    }

    public static void becomeNeighbors(Sommet sommet1, Sommet sommet2) {
        sommet1.addToMyNeighbors(sommet2);
        sommet2.addToMyNeighbors(sommet1);
    }

    public static void deleteNeighbors(Sommet sommet1, Sommet sommet2) {
        sommet1.removeSommetFromMyNeighbors(sommet2);
        sommet2.removeSommetFromMyNeighbors(sommet1);
    }

    public static void front(Sommet sommet1, Sommet sommet2) {
        sommet1.toFront();
        sommet2.toFront();
        sommet1.setEffect(null);
        sommet2.setEffect(null);
    }

    public static void updateForNimGame(String play, int tige, int rang) {
        for (Node node : nodeList) {
            if (node instanceof Arrete) {
                if (((Arrete) node).getMyTige() == tige) {
                    if (((Arrete) node).getRang() >= rang) {
                        ((Arrete) node).removeMe();
                    }
                }
            } else if (node instanceof Sommet) {
                if (((Sommet) node).getMyTige() == tige) {
                    if (((Sommet) node).getRang() > rang) {
                        ((Sommet) node).deleteMe();
                    } else if (((Sommet) node).getRang() == rang && rang == 0) {
                        ((Sommet) node).deleteMe();
                    }
                }
            }
        }

        Nim.applyMove(new Nim.Move(tige, rang), myHeaps);
        if (nim.isFinished(myHeaps)) {
            gameTime = !gameTime;
            Main.home = "VictoryFXML.fxml";
            try {
                Main.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mode) {//normal
                System.out.println("joueur I gagnant !");
                return;
            } else {//misere
                System.out.println("joueur I perdant !");
                return;
            }
        }

        if (play.equals("robot")) changeTurn(Color.GREEN);

        if (againstIA) {
            if (turn) {
                System.out.println("ia turn");
                changeTurn(Color.BLACK);
                cup = nim.nextMove(myHeaps);
                updateForNimGame("IA", cup.getIndex(), cup.getSize());
            }
        }
    }

    public static void saveNewHistory(Pane pane) {
        if (!history.isUtilisable()) {
            history.setUtilisable(true);
            history.clear();
            for (Node node : pane.getChildren()) {
                history.ajout(node);
            }
            history.saveIntoHistoriqueTotal();
            //  start(pane);
            history.setUtilisable(false);
        }
        System.out.println("sauvegarde");
    }

    public static boolean changeTurn(Color color) {
        if (color.equals(Color.BLACK)) {
            turn = !turn;
            playerTurn.set("Your Turn");
            return true;
        } else if (turn && color.equals(Color.BLUE)) {
            turn = !turn;
            playerTurn.set("RED");
            return true;
        } else if (!turn && color.equals(Color.RED)) {
            turn = !turn;
            playerTurn.set("BLUE");
            return true;
        } else if (color.equals(Color.GREEN)) {
            turn = !turn;
            if (turn) playerTurn.set("BLUE");
            else playerTurn.set("RED");
            return true;
        }
        return false;
    }


    public static boolean findRacine(Sommet sommet, Sommet caller, HashSet<Sommet> sommetList) {
        if (sommet.isJeSuisRacine()) {
            return true;
        } else {
            sommetList.add(sommet);
            for (Sommet child : sommet.getMyNeighbors()) {
                if (child != caller && !sommetList.contains(child)) {
                    boolean result = findRacine(child, sommet, sommetList);
                    if (result) {
                        return result;
                    }
                }
            }
        }
        return false;
    }

    public static void updateForBush(HashSet<Sommet> sommetList) {
        for (Sommet sommet : sommetList) {
            sommet.deleteMe();
        }
    }

    public static void substractCompteurs(Color color) {
        if (color.equals(Color.RED)) {
            arreteRed--;
        } else if (color.equals(Color.BLUE)) {
            arreteBlue--;
        } else if (color.equals(Color.GREEN)) {
            arreteRed--;
            arreteBlue--;
        }
        if ((arreteRed == 0 || arreteBlue == 0) && gameTime)
            whoWin();
    }

    public static void whoWin() {
        gameTime = !gameTime;
        Main.home = "VictoryFXML.fxml";
        try {
            Main.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (turn && arreteBlue == 0) {
            if (mode) {//normal
                System.out.println("joueur Bleu perdant !");
                return;
            } else {//misere
                System.out.println("joueur Bleu gagnant !");
                return;
            }
        } else if (!turn && arreteRed == 0) {
            if (mode) {//normal
                System.out.println("joueur Rouge perdant !");
                return;
            } else {//misere
                System.out.println("joueur Rouge gagnant !");
                return;
            }
        }

    }

    public static List<Sommet> getRacines() {
        List<Sommet> set = new ArrayList<>();
        for (Node node : nodeList) {
            if (node instanceof Sommet) {
                if (((Sommet) node).isJeSuisRacine()) set.add((Sommet) node);
            }
        }
        return set;
    }

    public static void browseTree(Sommet sommet, Sommet caller, HashSet<Sommet> sommetValide) {
        sommetValide.add(sommet);
        for (Sommet child : sommet.getMyNeighbors()) {
            if (child != caller && !sommetValide.contains(child)) {
                browseTree(child, sommet, sommetValide);
            }
        }
    }

    public static void refreshPane(HashSet<Sommet> sommets) {
        for (Node node : nodeList) {
            if (node instanceof Sommet) {
                if (!sommets.contains((Sommet) node)) {
                    ((Sommet) node).deleteMe();
                }
            }
        }
    }

    public static void verifyBeforeStart() {
        List<Sommet> set = getRacines();
        HashSet<Sommet> hashSet = new HashSet<>();
        for (Sommet sommet : set) {
            hashSet.add(sommet);
            for (Sommet neighbor : sommet.getMyNeighbors()) {
                browseTree(neighbor, sommet, hashSet);
            }
        }
        refreshPane(hashSet);
    }

    public static void start(Pane paneGameBush) {
        Additions.arreteRed = 0;
        Additions.arreteBlue = 0;
        for (Node node : paneGameBush.getChildren()) {
            if (node instanceof Arrete) {
                if (((Arrete) node).getMyColor().equals(Color.RED)) {
                    Additions.arreteRed++;
                } else if (((Arrete) node).getMyColor().equals(Color.BLUE)) {
                    Additions.arreteBlue++;
                } else if (((Arrete) node).getMyColor().equals(Color.GREEN)) {
                    Additions.arreteRed++;
                    Additions.arreteBlue++;
                }
            }
        }
    }
}
