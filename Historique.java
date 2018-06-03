package FXX;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Historique {

    private ArrayList<Node> histoire;
    private boolean utilisable;
    private HashMap<Integer, ArrayList<Node>> historiqueTotal;
    private HashMap<String, HashMap<List<Node>, List<Sommet>>> historiqueData;
    private HashMap<Integer,HashMap<String, HashMap<List<Node>, List<Sommet>>>> historiqueDataTotal;
    private int index;
    private int compteurR;
    private int CompteurB;


    public Historique() {
        this.utilisable = false;
        this.index = 0;
        this.histoire = new ArrayList<>();
        this.historiqueTotal = new HashMap<Integer, ArrayList<Node>>();
        this.historiqueData = new HashMap<>();
        this.historiqueDataTotal = new HashMap<>();
    }

    public boolean isUtilisable() {
        return utilisable;
    }

    public void setUtilisable(boolean utilisable) {
        this.utilisable = utilisable;
    }


    public void clear() {
        this.histoire.clear();
    }

    public void setHistoire(ArrayList<Node> histoire) {
        this.histoire = histoire;
    }

    public void ajout(Node node) {
        this.histoire.add(node);
        if (node instanceof Sommet)
            this.historiqueData.put(String.valueOf(((Sommet) node).id), ((Sommet) node).getData());

    }

    public HashMap<List<Node>, List<Sommet>> getHistData(String key) {
        return  this.historiqueDataTotal.get(index).get(key);
       // return this.historiqueData.get(key);
    }

    public ArrayList<Node> getHistoire() {
        return this.histoire;
    }

    public Node getElement(int index) {
        return this.histoire.get(index);
    }

    public void saveIntoHistoriqueTotal() {
        this.historiqueTotal.put(index, new ArrayList<>(histoire));
        this.historiqueDataTotal.put(index, new HashMap<>(historiqueData));
        this.index++;
        Additions.history = this;
    }

    public void setCompteurCouleurs() {
        this.setCompteurB(Additions.arreteBlue);
        this.setCompteurR(Additions.arreteRed);
    }

    public void editCompteurCouleurs() {
        Additions.arreteBlue = this.getCompteurB();
        Additions.arreteRed = this.getCompteurR();
    }

    public ArrayList<Node> getHistoriqueAt(int in) {
        return historiqueTotal.get(in);
    }

    public ArrayList<Node> getLastHistorique() {
        return historiqueTotal.get(index);
    }


    public HashMap<Integer, ArrayList<Node>> getHistoriqueTotal() {
        return historiqueTotal;
    }

    public void setHistoriqueTotal(HashMap<Integer, ArrayList<Node>> historiqueTotal) {
        this.historiqueTotal = historiqueTotal;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCompteurR() {
        return compteurR;
    }

    public void setCompteurR(int compteurR) {
        this.compteurR = compteurR;
    }

    public int getCompteurB() {
        return CompteurB;
    }

    public void setCompteurB(int compteurB) {
        CompteurB = compteurB;
    }

}
