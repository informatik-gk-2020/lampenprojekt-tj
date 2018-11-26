package lamps;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {
    private final ObservableList<Group> groups = FXCollections.observableArrayList();
    //private final ObservableList<Lamp> lamps = FXCollections.observableArrayList();

    private final SimpleObjectProperty<Group> selectedGroup = new SimpleObjectProperty<>(null);

    private LampsContainer lampsContainer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        lampsContainer = new LampsContainer();

        // Bind the selected property of all lamps that get added
        var selectedLamps = lampsContainer.getSelectedLamps();
        lampsContainer.getLamps().addListener((ListChangeListener<Lamp>) c -> {
            while(c.next()) {
                for (var lamp : c.getAddedSubList()) {
                    lamp.selectedProperty().bind(Bindings.createBooleanBinding(
                            () -> selectedLamps.contains(lamp),
                            selectedLamps
                    ));
                }

                for (var lamp : c.getRemoved()) {
                    lamp.selectedProperty().unbind();
                }
            }
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

    /**
     * Toggles multiple lamps
     * @param lamps the lamps to toggle
     */
    private void toggleLamps(List<Lamp> lamps) {
        // The lamps get turned off if all of them are turned on
        // if any of them is turned off, they are turned on to ensure they have the same state
        var newState = !lamps.stream().allMatch(Lamp::isOn);

        for (Lamp lamp : lamps) {
            lamp.setOn(newState);
        }
    }

    private ToolBar createToolbar() {
        // conditions
        var noLamp = Bindings.isEmpty(lampsContainer.getSelectedLamps());

        // toggle

        var toggleButton = new Button("Umschalten");
        toggleButton.disableProperty().bind(noLamp);
        toggleButton.setOnAction(event -> toggleLamps(lampsContainer.getSelectedLamps()));

        // add and remove

        var addButton = new Button("Neu");
        addButton.setOnAction(event -> {
            var lamp = new Lamp();
            lampsContainer.getLamps().add(lamp);
        });

        var removeButton = new Button("Entfernen");
        removeButton.disableProperty().bind(noLamp);
        removeButton.setOnAction(event -> {
            lampsContainer.getLamps().removeAll(lampsContainer.getSelectedLamps());
            lampsContainer.getSelectedLamps().clear();
        });

        // group related buttons

        var addToGroupButton = new Button("Zur Gruppe hinzufügen");
        addToGroupButton.setOnAction(event -> {
            var group = selectedGroup.get();
            for (var lamp : lampsContainer.getSelectedLamps()) {
                lamp.setGroup(group);
            }
        });

        // disable the button if there are either no lamps or groups selected
        // or all lamps are already in the selected group
        addToGroupButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            var group = selectedGroup.get();
            var lamps = lampsContainer.getSelectedLamps();

            return group == null || lamps.isEmpty() || lamps.stream().allMatch(lamp -> lamp.getGroup() == group);
        }, selectedGroup, lampsContainer.getSelectedLamps()));

        var removeFromGroupButton = new Button("Aus Gruppe entfernen");
        removeFromGroupButton.setOnAction(event -> {
            for (var lamp : lampsContainer.getSelectedLamps()) {
                lamp.setGroup(null);
            }
        });

        // disable the button if none of the selected lamps have a group
        removeFromGroupButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> lampsContainer.getSelectedLamps().stream().allMatch(lamp -> lamp.getGroup() == null),
                lampsContainer.getSelectedLamps()
        ));

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
            var selectedLamps = lampsContainer.getLamps().stream()
                    .filter(lamp -> selectedGroups.contains(lamp.getGroup()))
                    .collect(Collectors.toList());

            toggleLamps(selectedLamps);
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
            lampsContainer.getLamps().stream()
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
