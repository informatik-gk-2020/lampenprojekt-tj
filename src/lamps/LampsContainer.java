package lamps;

import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

public class LampsContainer extends BorderPane {
    private Lamp draggingLamp = null;

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
    }
}
