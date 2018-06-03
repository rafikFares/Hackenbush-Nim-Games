package FXX;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Selection {

    private Set<Sommet> selectionSommetSet = new HashSet<>();
    private Set<Sommet> selectionSommetSetCopie = new HashSet<>();
    private Set<Arrete> selectionArreteSet = new HashSet<>();
    private Set<Arrete> selectionArreteSetCopie = new HashSet<>();
    private Set<Arrete> selectionArreteSetInterect = new HashSet<>();

    public void addSommetToSet(Sommet node) {

        if (!node.getStyleClass().contains("highlight")) {
            node.getStyleClass().add("highlight");
        }

        selectionSommetSet.add(node);
    }

    public void removeSommetFromSet(Sommet node) {
        node.getStyleClass().remove("highlight");
        selectionSommetSet.remove(node);
    }

    public void addArreteToSet(Arrete node) {

        selectionArreteSet.add(node);
    }

    public void addArreteToSetInterect(Arrete node) {

        selectionArreteSetInterect.add(node);
    }

    public void removeArreteFromSet(Arrete node) {

        selectionArreteSet.remove(node);
    }

    public void removeArreteFromSetInterect(Arrete node) {

        selectionArreteSetInterect.remove(node);
    }

    public void clearSet() {

        while (!selectionSommetSet.isEmpty()) {
            removeSommetFromSet(selectionSommetSet.iterator().next());
        }
        while (!selectionArreteSet.isEmpty()) {
            removeArreteFromSet(selectionArreteSet.iterator().next());
        }
        while (!selectionArreteSetInterect.isEmpty()) {
            removeArreteFromSetInterect(selectionArreteSetInterect.iterator().next());
        }

    }

    public void faireCopieDesSommets() {
        selectionSommetSetCopie.clear();
        for (Sommet circle : selectionSommetSet) {
            Sommet c = new Sommet(circle.getCenterX() + 5, circle.getCenterY() + 5,
                    10, Color.WHITE, Color.BLACK, 2, circle.getParentPane());
            c.addEventHandlerForSommet();
            selectionSommetSetCopie.add(c);
        }
        System.out.println("copie faite de " + selectionSommetSet.size());
    }

    public boolean setContainsSommet(Sommet node) {
        return selectionSommetSet.contains(node);
    }

    public boolean setContainsArrete(Arrete node) {
        return selectionArreteSet.contains(node);
    }

    public boolean setContainsElement(Node node) {
        if (selectionArreteSet.contains(node) || selectionSommetSet.contains(node)) return true;
        return (selectionArreteSetInterect.contains(node));
    }

    public int sizeOfSommetSet() {
        return selectionSommetSet.size();
    }

    public int sizeOfArreteSet() {
        return selectionArreteSet.size();
    }

    public void sommetSetToString() {
        System.out.println("mes sommets " + Arrays.asList(selectionSommetSet.toArray()));
    }

    public void arreteSetToString() {
        System.out.println("mes arretes " + Arrays.asList(selectionArreteSet.toArray()));
    }

    public boolean isSommetSetEmpty() {
        return selectionSommetSet.isEmpty();
    }

    public boolean isArreteSetEmpty() {
        return selectionArreteSet.isEmpty();
    }

    public Set<Sommet> getSelectionSommetSet() {
        return selectionSommetSet;
    }

    public void setSelectionSommetSet(Set<Sommet> selectionSommetSet) {
        this.selectionSommetSet = selectionSommetSet;
    }

    public Set<Sommet> getSelectionSommetSetCopie() {
        return selectionSommetSetCopie;
    }

    public void setSelectionSommetSetCopie(Set<Sommet> selectionSommetSetCopie) {
        this.selectionSommetSetCopie = selectionSommetSetCopie;
    }

    public Set<Arrete> getSelectionArreteSet() {
        return selectionArreteSet;
    }

    public void setSelectionArreteSet(Set<Arrete> selectionArreteSet) {
        this.selectionArreteSet = selectionArreteSet;
    }

    public Set<Arrete> getSelectionArreteSetCopie() {
        return selectionArreteSetCopie;
    }

    public void setSelectionArreteSetCopie(Set<Arrete> selectionArreteSetCopie) {
        this.selectionArreteSetCopie = selectionArreteSetCopie;
    }

    public Set<Arrete> getSelectionArreteSetInterect() {
        return selectionArreteSetInterect;
    }

    public void setSelectionArreteSetInterect(Set<Arrete> selectionArreteSetInterect) {
        this.selectionArreteSetInterect = selectionArreteSetInterect;
    }
}
