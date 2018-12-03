package lamps;

import javafx.beans.property.SimpleStringProperty;

/**
 * Eine Gruppe von Lampen
 */
public class Group {
    private SimpleStringProperty name;

    public Group(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * Gibt den Namen der Gruppe zurück
     * @return  der Name der Gruppe
     */
    public String getName() {
        return name.get();
    }

    /**
     * Der Name der Gruppe
     * @return die Namenseigenschaft
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Setzt den Namen der Gruppe
     * @param name der neue Name der Gruppe
     */
    public void setName(String name) {
        this.name.set(name);
    }
}
