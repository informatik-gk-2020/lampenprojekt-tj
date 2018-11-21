package lamps;

import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

class GroupNameStringConverter extends StringConverter<Group> {
    private final ListCell<Group> cell;

    public GroupNameStringConverter(ListCell<Group> cell) {
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
