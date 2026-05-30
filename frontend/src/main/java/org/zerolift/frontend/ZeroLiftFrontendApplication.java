package org.zerolift.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ZeroLiftFrontendApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1200, 800);

        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        primaryStage.setTitle("Zero Lift - Workout Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}