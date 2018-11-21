package lamps;

import javafx.beans.property.SimpleStringProperty;

public class Group {
    private SimpleStringProperty name;

    public Group(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
