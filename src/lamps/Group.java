package lamps;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

/**
 * Eine Gruppe von Lampen
 */
public class Group {
    private final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(Lamp.DEFAULT_COLOR);
    private final SimpleStringProperty name;

    public Group(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * Gibt den Namen der Gruppe zurück
     *
     * @return der Name der Gruppe
     */
    public String getName() {
        return name.get();
    }

    /**
     * Der Name der Gruppe
     *
     * @return die Namenseigenschaft
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Setzt den Namen der Gruppe
     *
     * @param name der neue Name der Gruppe
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Gibt die Farbe der Gruppe zurück
     *
     * @return die Farbe der Gruppe zurück
     */
    public Color getColor() {
        return color.get();
    }

    /**
     * Die Eigenschaft der Farbe der Gruppe
     *
     * @return die Eigenschaft
     */
    public SimpleObjectProperty<Color> colorProperty() {
        return color;
    }

    /**
     * Setzt die Farbe der Gruppe
     *
     * @param color die neue Farbe der Gruppe
     */
    public void setColor(Color color) {
        this.color.set(color);
    }
}
