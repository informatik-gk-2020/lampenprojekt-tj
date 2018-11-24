package lamps;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main extends Application {
    private final ObservableList<Group> groups = FXCollections.observableArrayList();
    private final ObservableList<Lamp> lamps = FXCollections.observableArrayList();

    private final SimpleObjectProperty<Lamp> selectedLamp = new SimpleObjectProperty<>(null);
    private final SimpleObjectProperty<Group> selectedGroup = new SimpleObjectProperty<>(null);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var lampsContainer = new LampsContainer();
        Bindings.bindContent(lampsContainer.getChildren(), lamps);
        lampsContainer.setOnMouseClicked(event -> {
            var target = event.getTarget();
            if (event.getButton() == MouseButton.PRIMARY && target instanceof Lamp)
                selectedLamp.set((Lamp) target);
        });

        var toolbar = createToolbar();
        var groupsPane = createGroupsPane();

        var rootPane = new BorderPane(
                lampsContainer,
                toolbar,
                groupsPane,
                null,
                null
        );

        var scene = new Scene(rootPane);
        primaryStage.setScene(scene);

        primaryStage.setHeight(400);
        primaryStage.setWidth(650);
        primaryStage.setTitle("Lampen");
        primaryStage.show();
    }

    private ToolBar createToolbar() {
        // conditions
        var noLamp = selectedLamp.isNull();
        var noLampOrGroup = noLamp.or(selectedGroup.isNull());
        var selectedLampInGroup = Bindings.equal(Bindings.select(selectedLamp, "group"), selectedGroup);

        // toggle

        var toggleButton = new Button("Umschalten");
        toggleButton.disableProperty().bind(noLamp);
        toggleButton.setOnAction(event -> {
            var selection = selectedLamp.get();
            selection.setOn(!selection.isOn());
        });

        // add and remove

        var addButton = new Button("Neu");
        addButton.setOnAction(event -> {
            var lamp = new Lamp();
            lamps.add(lamp);
        });

        var removeButton = new Button("Entfernen");
        removeButton.disableProperty().bind(noLamp);
        removeButton.setOnAction(event -> {
            lamps.remove(selectedLamp.get());
            selectedLamp.set(null);
        });

        // group related buttons

        var addToGroupButton = new Button("Zur Gruppe hinzufügen");
        addToGroupButton.disableProperty().bind(
                noLampOrGroup.or(selectedLampInGroup)
        );
        addToGroupButton.setOnAction(event -> selectedLamp.get().setGroup(selectedGroup.get()));

        var removeFromGroupButton = new Button("Aus Gruppe entfernen");
        removeFromGroupButton.disableProperty().bind(
                noLampOrGroup.or(selectedLampInGroup.not())
        );
        removeFromGroupButton.setOnAction(event -> selectedLamp.get().setGroup(null));

        // create the toolbar

        return new ToolBar(
                toggleButton,
                new Separator(),
                addButton,
                removeButton,
                new Separator(),
                addToGroupButton,
                removeFromGroupButton
        );
    }

    private BorderPane createGroupsPane() {
        var listView = new ListView<Group>();
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setItems(groups);
        listView.setEditable(true);
        selectedGroup.bind(listView.getSelectionModel().selectedItemProperty());

        listView.setCellFactory(list -> {
            var cell = new TextFieldListCell<Group>();
            cell.updateListView(list);
            cell.setConverter(new GroupNameStringConverter(cell));
            return cell;
        });

        var toolbar = createGroupsToolBar(listView);
        return new BorderPane(listView, null, null, toolbar, null);
    }

    private ToolBar createGroupsToolBar(ListView<Group> listView) {
        // conditions
        var noGroup = Bindings.isEmpty(listView.getSelectionModel().getSelectedItems());

        // toggle

        var toggleButton = new Button("Umschalten");
        toggleButton.disableProperty().bind(noGroup);
        toggleButton.setOnAction(event -> {
            var selectedGroups = listView.getSelectionModel().getSelectedItems();
            var selectedLamps = lamps.stream()
                    .filter(lamp -> selectedGroups.contains(lamp.getGroup()))
                    .collect(Collectors.toList());

            // The lamps get turned off if all of them are turned on
            // if any of them is turned off, they are turned on to ensure they have the same state
            var newState = !selectedLamps.stream().allMatch(Lamp::isOn);

            for (Lamp lamp : selectedLamps) {
                lamp.setOn(newState);
            }
        });

        // add and remove

        var addButton = new Button("Neu");
        addButton.setOnAction(event -> {
            var group = new Group("Neue Gruppe");
            groups.add(group);
            var itemIndex = groups.indexOf(group);

            listView.layout(); // necessary to update the ListView before editing the item
            listView.scrollTo(itemIndex);
            listView.getSelectionModel().clearAndSelect(itemIndex); // replace the current selection
            listView.edit(itemIndex);
        });

        var removeButton = new Button("Entfernen");
        removeButton.disableProperty().bind(noGroup);
        removeButton.setOnAction(event -> {
            // create a copy of the current selection
            var groupsToRemove = new ArrayList<>(listView.getSelectionModel().getSelectedItems());

            // remove groups
            groupsToRemove.forEach(groups::remove);

            // remove all lamps from those groups
            lamps.stream()
                    .filter(lamp -> groupsToRemove.contains(lamp.getGroup()))
                    .forEach(lamp -> lamp.setGroup(null));
        });

        return new ToolBar(
                toggleButton,
                new Separator(),
                addButton,
                removeButton
        );
    }
}
