package lamps;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Lamp extends Circle {
    private static final Color OFF_COLOR = Color.GREY;
    private static final Color ON_COLOR = Color.YELLOW;

    private final SimpleBooleanProperty on = new SimpleBooleanProperty(false);
    private final SimpleObjectProperty<Group> group = new SimpleObjectProperty<>(null);
    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public Lamp() {
        setRadius(20);
        fillProperty().bind(Bindings.when(on).then(ON_COLOR).otherwise(OFF_COLOR));

        strokeProperty().bind(Bindings.when(selected).then(Color.BLACK).otherwise(Color.GREY));
        setStrokeWidth(1);

        var shadowEffect = new DropShadow(20, ON_COLOR);
        effectProperty().bind(Bindings.when(on).then(shadowEffect).otherwise((DropShadow)null));

        setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                event.consume();
                setOn(!isOn());
            }
        });
    }

    public boolean isOn() {
        return on.get();
    }

    public SimpleBooleanProperty onProperty() {
        return on;
    }

    public void setOn(boolean on) {
        this.on.set(on);
    }

    public Group getGroup() {
        return group.get();
    }

    public SimpleObjectProperty<Group> groupProperty() {
        return group;
    }

    public void setGroup(Group group) {
        this.group.set(group);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
