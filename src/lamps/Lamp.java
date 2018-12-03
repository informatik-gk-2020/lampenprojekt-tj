package lamps;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Eine Lampe wird als Kreis dargestellt.
 * Wenn die Lampe angeschaltet ist, ist ihr Hintergrund gelb und es erscheint ein Schein um sie herum.
 * Zudem wird durch einen Rahmen gezeigt, ob die Lampe ausgewählt ist
 */
public class Lamp extends Circle {
    private static final Color OFF_COLOR = Color.GREY;
    private static final Color ON_COLOR = Color.YELLOW;

    private final SimpleBooleanProperty on = new SimpleBooleanProperty(false);
    private final SimpleObjectProperty<Group> group = new SimpleObjectProperty<>(null);
    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public Lamp() {
        setRadius(20);

        // die Farbe, abhängig vom Zustand
        fillProperty().bind(Bindings.when(on).then(ON_COLOR).otherwise(OFF_COLOR));

        // der Rahmen, abhängig davon, ob die Lampe ausgewählt ist
        strokeProperty().bind(Bindings.when(selected).then(Color.BLACK).otherwise(Color.GREY));
        setStrokeWidth(1);

        // der Schein der Lampe
        var shadowEffect = new DropShadow(20, ON_COLOR);
        // wird nur aktiviert, wen die Lampe angeschaltet ist:
        effectProperty().bind(Bindings.when(on).then(shadowEffect).otherwise((DropShadow)null));

        // Beim Doppelklick wird die Lampe umgeschaltet
        setOnMouseClicked(event -> {
            // Überprüfen: Linke Maustaste UND Doppelklick
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                event.consume();
                setOn(!isOn()); // Lampe umschalten
            }
        });
    }

    /**
     * Gibt zurück, ob die Lampe angeschaltet ist
     * @return ob die Lampe angeschaltet ist
     */
    public boolean isOn() {
        return on.get();
    }

    /**
     * Die Eigenschaft, ob die Lampe angeschaltet ist
     * @return die Eigenschaft
     */
    public SimpleBooleanProperty onProperty() {
        return on;
    }

    /**
     * Legt fest, ob die Lampe angeschaltet ist
     * @param on ob die Lampe angeschaltet sein soll
     */
    public void setOn(boolean on) {
        this.on.set(on);
    }

    /**
     * Gibt die Gruppe der Lampe zurück
     * @return die Gruppe der Lampe
     */
    public Group getGroup() {
        return group.get();
    }

    /**
     * Die Eigenschaft der Gruppe der Lampe
     * @return die Eigenschaft
     */
    public SimpleObjectProperty<Group> groupProperty() {
        return group;
    }

    /**
     * Setzt die Gruppe der Lampe
     * @param group die neue Gruppe der Lampe
     */
    public void setGroup(Group group) {
        this.group.set(group);
    }

    /**
     * Gibt zurück, ob die Lampe ausgewählt ist
     * @return ob die Lampe ausgewählt ist
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Die Eigenschaft, ob die Lampe ausgewählt ist
     * @return die Eigenschaft
     */
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * Legt fest, ob die Lampe ausgewählt ist
     * @param selected ob die Lampe ausgewählt sein soll
     */
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
