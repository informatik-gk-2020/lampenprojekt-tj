package lamps;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

public class LampsContainer extends BorderPane {
    private Lamp draggingLamp = null;

    private ObservableList<Lamp> selectedLamps = FXCollections.observableArrayList();

    public LampsContainer() {
        setOnMousePressed(event -> {
            var target = event.getTarget();
            if(event.getButton() == MouseButton.PRIMARY && target instanceof Lamp) {
                draggingLamp = (Lamp) target;
                event.consume();
            }
        });

        setOnMouseReleased(event -> {
            if(event.getButton() == MouseButton.PRIMARY && draggingLamp != null) {
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
            var target = event.getTarget();
            if (event.getButton() == MouseButton.PRIMARY && target instanceof Lamp) {
                // Remove the lamp if it was already selected
                if(selectedLamps.remove(target))
                    return;

                if(event.isControlDown())
                    selectedLamps.add((Lamp)target);
                else
                    selectedLamps.setAll((Lamp)target);
            }
        });
    }

    public ObservableList<Lamp> getSelectedLamps() {
        return selectedLamps;
    }
}
