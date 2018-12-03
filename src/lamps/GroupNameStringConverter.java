package lamps;

import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

/**
 * Konvertiert {@link Group}s zu {@link String}s und umgekehrt.
 * Diese Klasse wird benötigt, um den Namen einer Gruppe in der Liste zu ändern.
 */
class GroupNameStringConverter extends StringConverter<Group> {
    private final ListCell<Group> cell;

    GroupNameStringConverter(ListCell<Group> cell) {
        this.cell = cell;
    }

    @Override
    public String toString(Group object) {
        return object == null ? "" : object.getName();
    }

    @Override
    public Group fromString(String newName) {
        var item = cell.getItem();
        item.setName(newName);
        return item;
    }
}
