package org.zerolift.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ZeroLiftFrontendApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
        Parent root = fxmlLoader.load();

        // 1. Die Szene einmalig erstellen
        Scene scene = new Scene(root, 1200, 800);

        // 2. Das CSS an diese Szene hängen
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        // 3. Dem Fenster genau DIESE fertige Szene übergeben
        primaryStage.setTitle("Zero Lift - Workout Tracker");
        primaryStage.setScene(scene); // <-- Hier stand vorher "new Scene(root...)"
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}