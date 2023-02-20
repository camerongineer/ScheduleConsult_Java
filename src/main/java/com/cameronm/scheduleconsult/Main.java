package com.cameronm.scheduleconsult;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Main class is the main class of the application
 *
 * @author Cameron M
 * @since 02-19-2023
 */
public class Main extends Application {

    /**
     * The location of the JavaFX fxml files
     */
    public static final String VIEWS_PATH = "/com/cameronm/scheduleconsult/views/";

    /**
     * The main method is the entry point for the application
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method sets up the JavaFX stage and displays it to the user.
     *
     * @param stage the primary stage for the JavaFX application
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(VIEWS_PATH + "LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("ScheduleConsult");
        stage.show();
//        stage.setOnCloseRequest(event -> {
//            event.consume();
//        });
    }
}
