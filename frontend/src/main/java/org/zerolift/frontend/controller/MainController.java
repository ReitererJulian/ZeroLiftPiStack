package org.zerolift.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import org.zerolift.frontend.service.EquipmentService;
import org.zerolift.frontend.service.ExerciseService;

import java.util.List;

@Component
public class MainController {

    @FXML private Label backendStatusLabel;
    @FXML private ListView<String> equipmentListView;
    @FXML private TextField equipmentTitleInput;
    @FXML private TextField equipmentDescInput;
    @FXML private Label equipmentStatusLabel;

    @FXML private TextField exerciseNameField;
    @FXML private TextArea exerciseDescriptionArea;

    private final ObservableList<String> equipmentItems = FXCollections.observableArrayList();
    private final EquipmentService equipmentService = new EquipmentService();
    private final ExerciseService exerciseService = new ExerciseService();

    @FXML
    public void initialize() {
        equipmentListView.setItems(equipmentItems);
        loadAllEquipments();
    }

    @FXML
    public void saveEquipment(ActionEvent event) {
        String title = equipmentTitleInput.getText();
        String description = equipmentDescInput.getText();

        if (title == null || title.trim().isEmpty()) {
            equipmentStatusLabel.setText("Error: Title is empty");
            return;
        }

        equipmentStatusLabel.setText("Ssaving...");
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
            List<String> titelListe = liste.stream().map(item -> (String) item.get("title")).toList();
            Platform.runLater(() -> equipmentItems.setAll(titelListe));
        }).exceptionally(ex -> {
            System.err.println("Error loading equipment:");
            return null;
        });
    }

    @FXML
    public void loadDataFromBackend(ActionEvent event) {
        try {
            backendStatusLabel.setText("Status: Connecting to backend...");
            java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("https://api.zerolift.at/api/ping")).build();
            java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                backendStatusLabel.setText("Status: Backend response: " + response.body());
            } else {
                backendStatusLabel.setText("Status: Error! HTTP Code: " + response.statusCode());
            }
        } catch (Exception e) {
            backendStatusLabel.setText("Status: Error! Backend not reachable.");
            e.printStackTrace();
        }
    }
    
    @FXML void finishWorkout(ActionEvent event) { System.out.println("Finish Workout"); }
    @FXML void saveSet(ActionEvent event) { System.out.println("Save Set"); }
    @FXML void editSet(ActionEvent event) { System.out.println("Edit Set"); }
    @FXML void skipSet(ActionEvent event) { System.out.println("Skipped Set"); }
    @FXML void startTimer(ActionEvent event) { System.out.println("Timer Started"); }
    @FXML void newWorkout(ActionEvent event) { System.out.println("New Workout"); }
    @FXML void deleteWorkout(ActionEvent event) { System.out.println("Delete Workout"); }
    @FXML void saveWorkout(ActionEvent event) { System.out.println("Save Workout"); }
    @FXML void removeExercise(ActionEvent event) { System.out.println("Remove Exercise"); }
    @FXML void addExerciseToWorkout(ActionEvent event) { System.out.println("Add Exercise to Workout"); }
    @FXML void searchCommunity(ActionEvent event) { System.out.println("Search Community"); }
    @FXML void editProfile(ActionEvent event) { System.out.println("Edit Profile"); }
    @FXML void exportData(ActionEvent event) { System.out.println("Export Workout"); }
    @FXML void signOut(ActionEvent event) { System.out.println("Sign Out"); }
    @FXML void sync(ActionEvent event) { System.out.println("Sync"); }
    @FXML void settings(ActionEvent event) { System.out.println("Settings"); }
    @FXML void profile(ActionEvent event) { System.out.println("Profile"); }
    @FXML void saveExercise(ActionEvent event) {
        String name = exerciseNameField.getText();
        String description = exerciseDescriptionArea.getText();

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Exercise name is empty");
            return;
        }

        System.out.println("Saving exercise...");
        exerciseService.saveExercise(name, description).thenAccept(code -> {
            Platform.runLater(() -> {
                if (code == 200 || code == 201) {
                    System.out.println("Exercise saved successfully!");
                    exerciseNameField.clear();
                    exerciseDescriptionArea.clear();
                    // Wenn du eine Übungs-Liste hast, könntest du sie hier analog zu loadAllEquipments() neu laden
                } else {
                    System.out.println("Error saving exercise! Code: " + code);
                }
            });
        }).exceptionally(ex -> {
            System.err.println("Connection Error while saving exercise!");
            ex.printStackTrace();
            return null;
        });
    }
}