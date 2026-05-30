package org.zerolift.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.zerolift.frontend.service.EquipmentService;
import org.zerolift.frontend.service.ExerciseService;
import org.zerolift.frontend.service.WorkoutService;

import java.util.List;
import java.util.Map;

public class MainController {

    // ── Equipment ────────────────────────────────────────────────────
    @FXML private ListView<String> equipmentListView;
    @FXML private TextField equipmentTitleInput;
    @FXML private TextField equipmentDescInput;
    @FXML private Label equipmentStatusLabel;
    @FXML private ListView<Map<String, Object>> equipmentSelectionList;

    // ── Exercises ────────────────────────────────────────────────────
    @FXML private ListView<Map<String, Object>> exerciseLibraryList;
    @FXML private TextField exerciseNameField;
    @FXML private TextArea exerciseDescriptionArea;

    // ── Workouts ─────────────────────────────────────────────────────
    @FXML private ListView<Map<String, Object>> workoutList;
    @FXML private ListView<Map<String, Object>> exerciseList;
    @FXML private ListView<Map<String, Object>> exerciseLibraryListWorkout;
    @FXML private TextField workoutNameField;
    @FXML private TextArea workoutDescriptionArea;

    // ── Workouts ─────────────────────────────────────────────────────
    @FXML private ListView<Map<String, Object>> homeWorkoutList;
    @FXML private TextArea workoutDetailArea;

    private final ObservableList<String> equipmentItems = FXCollections.observableArrayList();
    private final EquipmentService equipmentService = new EquipmentService();
    private final ExerciseService exerciseService = new ExerciseService();
    private final WorkoutService workoutService = new WorkoutService();

    @FXML
    public void initialize() {
        equipmentListView.setItems(equipmentItems);

        equipmentSelectionList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        equipmentSelectionList.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (String) item.get("title"));
            }
        });

        exerciseLibraryList.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (String) item.get("title"));
            }
        });

        exerciseLibraryListWorkout.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        exerciseLibraryListWorkout.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (String) item.get("title"));
            }
        });

        exerciseList.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (String) item.get("title"));
            }
        });

        workoutList.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (String) item.get("title"));
            }
        });

        homeWorkoutList.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : (String) item.get("title"));
            }
        });

        homeWorkoutList.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected == null) return;
            StringBuilder sb = new StringBuilder();
            sb.append("Title:       ").append(selected.get("title")).append("\n");
            sb.append("Description: ").append(selected.get("description")).append("\n\n");
            sb.append("Exercises:\n");

            List<?> exercises = (List<?>) selected.get("exercises");
            if (exercises == null || exercises.isEmpty()) {
                sb.append("  (none)");
            } else {
                for (Object ex : exercises) {
                    Map<?, ?> exercise = (Map<?, ?>) ex;
                    sb.append("  - ").append(exercise.get("title")).append("\n");
                }
            }

            workoutDetailArea.setText(sb.toString());
        });

        loadAllEquipments();
        loadAllExercises();
        loadAllWorkouts();
    }

    // ── Equipment ────────────────────────────────────────────────────

    @FXML
    public void saveEquipment(ActionEvent event) {
        String title = equipmentTitleInput.getText();
        String description = equipmentDescInput.getText();

        if (title == null || title.trim().isEmpty()) {
            equipmentStatusLabel.setText("Error: Title is empty");
            return;
        }

        equipmentStatusLabel.setText("Saving...");
        equipmentService.saveEquipment(title, description).thenAccept(code -> {
            Platform.runLater(() -> {
                if (code == 200 || code == 201) {
                    equipmentStatusLabel.setText("Saved!");
                    equipmentTitleInput.clear();
                    equipmentDescInput.clear();
                    loadAllEquipments();
                } else {
                    equipmentStatusLabel.setText("Error! Code: " + code);
                }
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> equipmentStatusLabel.setText("Connection Error!"));
            return null;
        });
    }

    private void loadAllEquipments() {
        equipmentService.getAllEquipment().thenAccept(liste -> {
            List<String> titelListe = liste.stream().map(i -> (String) i.get("title")).toList();
            Platform.runLater(() -> {
                equipmentItems.setAll(titelListe);
                equipmentSelectionList.getItems().setAll(liste);
            });
        }).exceptionally(ex -> {
            System.err.println("Error loading equipment: " + ex.getMessage());
            return null;
        });
    }

    // ── Exercises ────────────────────────────────────────────────────

    @FXML
    public void saveExercise(ActionEvent event) {
        String name = exerciseNameField.getText();
        String description = exerciseDescriptionArea.getText();

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Exercise name is empty");
            return;
        }

        List<String> equipmentIds = equipmentSelectionList.getSelectionModel()
                .getSelectedItems().stream()
                .map(m -> m.get("id").toString())
                .toList();

        exerciseService.saveExercise(name, description, equipmentIds).thenAccept(code -> {
            Platform.runLater(() -> {
                if (code == 200 || code == 201) {
                    exerciseNameField.clear();
                    exerciseDescriptionArea.clear();
                    equipmentSelectionList.getSelectionModel().clearSelection();
                    loadAllExercises();
                    exerciseLibraryListWorkout.getItems().setAll(exerciseLibraryList.getItems());
                } else {
                    System.out.println("Error saving exercise! Code: " + code);
                }
            });
        }).exceptionally(ex -> {
            System.err.println("Connection Error: " + ex.getMessage());
            return null;
        });
    }

    private void loadAllExercises() {
        exerciseService.getAllExercises().thenAccept(liste -> {
            Platform.runLater(() -> {
                exerciseLibraryList.getItems().setAll(liste);
                exerciseLibraryListWorkout.getItems().setAll(liste);
            });
        }).exceptionally(ex -> {
            System.err.println("Error loading exercises: " + ex.getMessage());
            return null;
        });
    }

    // ── Workouts ─────────────────────────────────────────────────────

    @FXML
    void addExerciseToWorkout(ActionEvent event) {
        List<Map<String, Object>> selected = exerciseLibraryListWorkout
                .getSelectionModel().getSelectedItems();
        for (Map<String, Object> ex : selected) {
            if (!exerciseList.getItems().contains(ex)) {
                exerciseList.getItems().add(ex);
            }
        }
    }

    @FXML
    void removeExercise(ActionEvent event) {
        Map<String, Object> selected = exerciseList.getSelectionModel().getSelectedItem();
        if (selected != null) exerciseList.getItems().remove(selected);
    }

    @FXML
    void saveWorkout(ActionEvent event) {
        String name = workoutNameField.getText();
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Workout name is empty");
            return;
        }

        List<String> exerciseIds = exerciseList.getItems().stream()
                .map(m -> m.get("id").toString())
                .toList();

        String description = workoutDescriptionArea.getText();

        workoutService.saveWorkout(name, description, exerciseIds).thenAccept(code -> {
            Platform.runLater(() -> {
                if (code == 200 || code == 201) {
                    workoutNameField.clear();
                    exerciseList.getItems().clear();
                    loadAllWorkouts();
                } else {
                    System.out.println("Error saving workout! Code: " + code);
                }
            });
        }).exceptionally(ex -> {
            System.err.println("Connection Error: " + ex.getMessage());
            return null;
        });
    }

    @FXML
    void newWorkout(ActionEvent event) {
        workoutNameField.clear();
        exerciseList.getItems().clear();
    }

    @FXML
    void deleteWorkout(ActionEvent event) {
        System.out.println("Delete Workout - not yet implemented");
    }

    private void loadAllWorkouts() {
        workoutService.getAllWorkouts().thenAccept(liste -> {
            Platform.runLater(() -> {
                workoutList.getItems().setAll(liste);
                homeWorkoutList.getItems().setAll(liste);
            });
        }).exceptionally(ex -> {
            System.err.println("Error loading workouts: " + ex.getMessage());
            return null;
        });
    }

    // ── Stubs ────────────────────────────────────────────────────────

    @FXML void sync(ActionEvent event) {}
    @FXML void settings(ActionEvent event) {}
    @FXML void profile(ActionEvent event) {}
}