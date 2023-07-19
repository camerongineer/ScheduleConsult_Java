package com.cameronm.scheduleconsult.utilities;

import com.cameronm.scheduleconsult.Main;
import com.cameronm.scheduleconsult.controllers.LoginController;
import com.cameronm.scheduleconsult.controllers.MainController;
import com.cameronm.scheduleconsult.views.AlertHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The ScreenLoader class is responsible for loading the screens of the program
 *
 * @author Cameron M
 * @since 03-04-2023
 */
public abstract class ScreenLoader {

    private static Stage activeStage;

    /**
     * The loadScreen method loads a screen within the program
     *
     * @param <T>                A type of controller class
     * @param screenLocation     The location of the fxml screen file
     * @param title              The title displayed in the title bar of the screen
     * @param controlObject      The control object passed used to pass a controller to another controller class
     * @param nextController     The controller class of the next screen
     * @param currentController  The instance of the currentController
     * @param onHiddenCallback   The runnable performed when a window is hidden
     * @param isResizable        Boolean specifying if the screen is resizable
     * @param closeProgramOnExit Boolean specifying program should close after window is closed
     * @param popupOnClose       Boolean specifying if a popup should occur when attempting the close the window
     * @return Returns a controller object
     * @throws IOException Throws an IOException if controllerClass doesn't match the class of the screen
     */
    public static <T> T loadScreen(String screenLocation,
                                   String title,
                                   Control controlObject,
                                   Class<T> nextController,
                                   Object currentController, Runnable onHiddenCallback,
                                   boolean isResizable,
                                   boolean closeProgramOnExit,
                                   boolean popupOnClose) throws IOException {
        if (activeStage != null &&
                (!(currentController instanceof MainController) || nextController == LoginController.class)) {
            activeStage.close();
        }
        Stage stage = new Stage();
        activeStage = stage;
        stage.initModality(Modality.APPLICATION_MODAL);
        if (controlObject != null) {
            stage.initOwner(controlObject.getScene()
                                         .getWindow());
        }
        FXMLLoader loader = new FXMLLoader(nextController.getResource(screenLocation));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Image logoImage = new Image(Main.IMAGES_PATH + "title_icon.png");
        stage.getIcons().add(logoImage);
        scene.getStylesheets().add(Objects.requireNonNull(ScreenLoader.class.getResource(Main.CSS_PATH + "style.css")).toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(isResizable);
        if (onHiddenCallback != null) {
            stage.setOnHidden(event -> onHiddenCallback.run());
        }
        stage.setOnCloseRequest(event -> {
            event.consume();
            if (!popupOnClose || AlertHandler.closePrompt()) {
                if (closeProgramOnExit) {
                    Main.safeExit();
                } else {
                    stage.close();
                }
            }
        });
        T controller = loader.getController();
        if (nextController.isInstance(controller)) {
            loader.setController(controller);
            stage.show();
            return controller;
        } else {
            throw new ClassCastException("Controller class " + nextController.getSimpleName() + " not found");
        }
    }
}