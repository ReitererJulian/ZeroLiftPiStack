package org.zerolift.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;


@Component
public class MainController {

    @FXML
    private Label backendStatusLabel;

    @FXML
    public void ladeDatenVomBackend(ActionEvent event) {
        try {
            backendStatusLabel.setText("Status: Verbinde mit Server...");

            java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://localhost:8080/api/ping"))
                    .build();

            java.net.http.HttpResponse<String> response = httpClient.send(
                    request,
                    java.net.http.HttpResponse.BodyHandlers.ofString()
            );

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

    @FXML
    void finishWorkout(ActionEvent event) {
        System.out.println("Finish Workout");
    }

    @FXML
    void saveSet(ActionEvent event) {
        System.out.println("Save Set");
    }

    @FXML
    void editSet(ActionEvent event) {
        System.out.println("Edit Set");
    }

    @FXML
    void skipSet(ActionEvent event) {
        System.out.println("Skipped Set");
    }

    @FXML
    void startTimer(ActionEvent event) {
        System.out.println("Timer Started");
    }

    @FXML
    void newWorkout(ActionEvent event) {
        System.out.println("New Workout");
    }

    @FXML
    void deleteWorkout(ActionEvent event) {
        System.out.println("Delete Workout");
    }

    @FXML
    void saveWorkout(ActionEvent event) {
        System.out.println("Save Workout");
    }

    @FXML
    void removeExercise(ActionEvent event) {
        System.out.println("Remove Exercise");
    }

    @FXML
    void addExerciseToWorkout(ActionEvent event) {
        System.out.println("Add Exercise to Workout");
    }

    @FXML
    void searchCommunity(ActionEvent event) {
        System.out.println("Search Community");
    }

    @FXML
    void editProfile(ActionEvent event) {
        System.out.println("Edit Profile");
    }

    @FXML
    void exportData(ActionEvent event) {
        System.out.println("Export Workout");
    }

    @FXML
    void signOut(ActionEvent event) {
        System.out.println("Sign Out");
    }

    @FXML
    void sync(ActionEvent event) {
        System.out.println("Sync");
    }

    @FXML
    void settings(ActionEvent event) {
        System.out.println("Settings");
    }

    @FXML
    void profile(ActionEvent event) {
        System.out.println("Profile");
    }

    @FXML
    void saveExercise(ActionEvent event) {
        System.out.println("Save Exercise");
    }
}