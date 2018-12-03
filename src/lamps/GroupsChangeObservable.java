package lamps;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Beobachtet eine Liste von Lampen und löst eine Änderung aus, wenn sich die Gruppe einer Lampe geändert hat
 */
public class GroupsChangeObservable implements Observable, InvalidationListener {
    private ArrayList<InvalidationListener> listeners = new ArrayList<>();

    /**
     * Erstellt eine neue Instanz und beobachtet die Liste und die Gruppen der enthaltenen Lampen
     * @param lamps die Liste der Lampen
     */
    public GroupsChangeObservable(ObservableList<Lamp> lamps) {
        lamps.addListener((ListChangeListener<Lamp>) c -> {
            while (c.next()) {
                // Neue Lampen beobachten
                for (var lamp : c.getAddedSubList()) {
                    lamp.groupProperty().addListener(GroupsChangeObservable.this);
                }

                // Entfernte Lampen nicht mehr beoachten
                for (var lamp : c.getRemoved()) {
                    lamp.groupProperty().removeListener(GroupsChangeObservable.this);
                }
            }
        });
    }

    /**
     * Wird aufgerufen, wenn sich die Gruppe einer Lampe geändert hat
     * @param observable die Lampe
     */
    @Override
    public void invalidated(Observable observable) {
        for (var listener : listeners) {
            listener.invalidated(this);
        }
    }

    /**
     * Fügt einen {@link InvalidationListener} hinzu
     */
    @Override
    public void addListener(InvalidationListener listener) {
        if (!listeners.contains(listener)) // wenn nicht schon vorhanden
            listeners.add(listener);
    }

    /**
     * Entfernt einen {@link InvalidationListener}
     */
    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }
}
