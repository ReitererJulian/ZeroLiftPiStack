package org.zerolift.frontend;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PingFrontendApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Elemente für die Oberfläche erstellen
        Label resultLabel = new Label("Noch kein Ping gesendet.");
        Button pingButton = new Button("Ping Backend");

        // 2. Was passiert bei einem Klick auf den Button?
        pingButton.setOnAction(event -> {
            try {
                // HTTP-Verbindung aufbauen und Anfrage senden
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/api/ping"))
                        .build();

                // Antwort vom Backend abfangen
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Text im Label direkt mit der Antwort ("pong") überschreiben
                resultLabel.setText("Antwort vom Backend: " + response.body());

            } catch (Exception e) {
                // Falls das Backend offline ist, zeige den Fehler an
                resultLabel.setText("Fehler: Backend nicht erreichbar!");
            }
        });

        // 3. Layout: Elemente untereinander anordnen
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(pingButton, resultLabel);

        // 4. Fenster anzeigen
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.setTitle("Einfacher Ping");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}