package lamps;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

/**
 * Die Fläche des Fensters, welche alle Lampen enthält
 */
public class LampsContainer extends BorderPane {
    /** die zurzeit bewegte Lampe */
    private Lamp draggingLamp = null;

    /** die ausgewählten Lampen */
    private final ObservableList<Lamp> selectedLamps = FXCollections.observableArrayList();

    /** alle Lampen in diesem Container */
    private final ObservableList<Lamp> lamps = FXCollections.observableArrayList();

    public LampsContainer() {
        // sorgt dafür, das dieses Objekt den Tastaturfokus erhalten kann (für Tastenkombinationen)
        setFocusTraversable(true);

        // bind the content to the lamps
        Bindings.bindContent(getChildren(), lamps);

        // wenn die Maus auf einer Lampe runtergedrückt wird, wird diese verschoben
        setOnMousePressed(event -> {
            var target = event.getTarget();
            if (event.getButton() == MouseButton.PRIMARY && target instanceof Lamp) {
                draggingLamp = (Lamp) target;
                event.consume();
            }
        });

        // beim Loslassen der Maus wird keine Lampe mehr verschoben
        setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY && draggingLamp != null) {
                event.consume();
                draggingLamp = null;
            }
        });

        // wenn eine Lampe verschoben wird, wird sie beime Bewegen der Maus ebenfalls bewegt
        setOnMouseDragged(event -> {
            if (draggingLamp != null) {
                event.consume();
                draggingLamp.setCenterX(event.getX());
                draggingLamp.setCenterY(event.getY());
            }
        });

        // Beim einfachen Klicken:
        setOnMouseClicked(event -> {
            requestFocus(); // Tastaturfokus auf dieses Objekt bringen

            // das Objekt, auf das geklickt wurde
            var target = event.getTarget();

            // WENN: linke Maustaste UND es wurde auf eine Lampe geklickt
            if (event.getButton() == MouseButton.PRIMARY && target instanceof Lamp) {
                if (event.isControlDown()) { // + Strg-Taste: Mehrfachauswahl
                    // Remove the lamp if it was already selected
                    if (!selectedLamps.remove(target))
                        selectedLamps.add((Lamp) target);
                } else { // sonst: nur diese Lampe auswählen
                    selectedLamps.setAll((Lamp) target);
                }
            } else { // wenn auf leere Fläche geklickt wurde:
                selectedLamps.clear(); // auswahl leeren
            }
        });

        // Tastenkombinationen
        setOnKeyPressed(event -> {
            // Strg + A
            if (event.isControlDown() && event.getCode() == KeyCode.A) {
                if (event.isShiftDown()) // + Umschalt -> Auswahl löschen
                    selectedLamps.clear();
                else // sonst: alles auswählen
                    selectedLamps.setAll(lamps);
            }
        });
    }

    /**
     * Gibt die ausgewählten Lampen zurück
     * @return die ausgewählten Lampen
     */
    public ObservableList<Lamp> getSelectedLamps() {
        return selectedLamps;
    }

    /**
     * Gibt die Lampen in diesem Container zurück
     * @return die Lampen
     */
    public ObservableList<Lamp> getLamps() {
        return lamps;
    }
}
