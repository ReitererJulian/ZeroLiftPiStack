package org.zerolift.frontend.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import org.zerolift.frontend.service.EquipmentService;

import java.util.List;

@Component
public class MainController {

    @FXML private Label backendStatusLabel;
    @FXML private ListView<String> equipmentListView;
    @FXML private TextField equipmentTitleInput;
    @FXML private TextField equipmentDescInput;
    @FXML private Label equipmentStatusLabel;

    private final ObservableList<String> equipmentItems = FXCollections.observableArrayList();
    private final EquipmentService equipmentService = new EquipmentService();

    @FXML
    public void initialize() {
        equipmentListView.setItems(equipmentItems);
        ladeAllerEquipment(); // Lädt die DB-Einträge direkt beim App-Start
    }

    @FXML
    public void saveEquipment(ActionEvent event) {
        String title = equipmentTitleInput.getText();
        String description = equipmentDescInput.getText();

        if (title == null || title.trim().isEmpty()) {
            equipmentStatusLabel.setText("Fehler: Name fehlt!");
            return;
        }

        equipmentStatusLabel.setText("Speichere...");
        equipmentService.saveEquipment(title, description).thenAccept(code -> {
            Platform.runLater(() -> {
                if (code == 200 || code == 201) {
                    equipmentStatusLabel.setText("Erfolgreich gespeichert!");
                    equipmentTitleInput.clear();
                    equipmentDescInput.clear();
                    ladeAllerEquipment(); // Liste neu laden
                } else {
                    equipmentStatusLabel.setText("Fehler! Code: " + code);
                }
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> equipmentStatusLabel.setText("Verbindungsfehler!"));
            return null;
        });
    }

    private void ladeAllerEquipment() {
        equipmentService.getAllEquipment().thenAccept(liste -> {
            List<String> titelListe = liste.stream().map(item -> (String) item.get("title")).toList();
            Platform.runLater(() -> equipmentItems.setAll(titelListe));
        }).exceptionally(ex -> {
            System.err.println("Fehler beim Laden des Equipments");
            return null;
        });
    }

    @FXML
    public void ladeDatenVomBackend(ActionEvent event) {
        try {
            backendStatusLabel.setText("Status: Verbinde mit Server...");
            java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://localhost:8080/api/ping")).build();
            java.net.http.HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                backendStatusLabel.setText("Status: Erfolg! Server antwortet: " + response.body());
            } else {
                backendStatusLabel.setText("Status: Fehler! HTTP Code: " + response.statusCode());
            }
        } catch (Exception e) {
            backendStatusLabel.setText("Status: Fehler - Backend nicht erreichbar!");
            e.printStackTrace();
        }
    }

    // --- Deine restlichen UI-Stubs ---
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
    @FXML void saveExercise(ActionEvent event) { System.out.println("Save Exercise"); }
}