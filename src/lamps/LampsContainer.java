package lamps;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

public class LampsContainer extends BorderPane {
    private Lamp draggingLamp = null;

    private final ObservableList<Lamp> selectedLamps = FXCollections.observableArrayList();

    private final ObservableList<Lamp> lamps = FXCollections.observableArrayList();

    public LampsContainer() {
        // accept focus
        setFocusTraversable(true);

        // bind the content to the lamps
        Bindings.bindContent(getChildren(), lamps);

        setOnMousePressed(event -> {
            var target = event.getTarget();
            if (event.getButton() == MouseButton.PRIMARY && target instanceof Lamp) {
                draggingLamp = (Lamp) target;
                event.consume();
            }
        });

        setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY && draggingLamp != null) {
                event.consume();
                draggingLamp = null;
            }
        });

        setOnMouseDragged(event -> {
            if (draggingLamp != null) {
                event.consume();
                draggingLamp.setCenterX(event.getX());
                draggingLamp.setCenterY(event.getY());
            }
        });

        setOnMouseClicked(event -> {
            requestFocus();

            var target = event.getTarget();
            if (event.getButton() == MouseButton.PRIMARY && target instanceof Lamp) {
                // Remove the lamp if it was already selected
                if (selectedLamps.remove(target))
                    return;

                if (event.isControlDown())
                    selectedLamps.add((Lamp) target);
                else
                    selectedLamps.setAll((Lamp) target);
            } else {
                selectedLamps.clear();
            }
        });

        setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.A) {
                if (event.isShiftDown())
                    selectedLamps.clear();
                else
                    selectedLamps.setAll(lamps);
            }
        });
    }


    public ObservableList<Lamp> getSelectedLamps() {
        return selectedLamps;
    }

    public ObservableList<Lamp> getLamps() {
        return lamps;
    }
}
