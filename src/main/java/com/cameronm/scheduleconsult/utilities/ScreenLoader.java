package com.cameronm.scheduleconsult.utilities;

import com.cameronm.scheduleconsult.Main;
import com.cameronm.scheduleconsult.controllers.MainController;
import com.cameronm.scheduleconsult.views.AlertHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The ScreenLoader class is responsible for loading the screens of the program
 *
 * @author Cameron M
 * @since 03-04-2023
 */
public abstract class ScreenLoader {

    /**
     * The loadScreen method loads a screen within the program
     *
     * @param screenLocation The location of the fxml screen file
     * @param title The title displayed in the title bar of the screen
     * @param controlObject The control object passed used to pass a controller to another controller class
     * @param controllerClass The controller class of the screen
     * @param onHiddenCallback The runnable performed when a window is hidden
     * @param isResizable Boolean specifying if the screen is resizable
     * @param closeProgramOnExit Boolean specifying program should close after window is closed
     * @param popupOnClose Boolean specifying if a popup should occur when attempting the close the window
     * @return Returns a controller object
     * @param <T> A type of controller class
     * @throws IOException Throws an IOException if controllerClass doesn't match the class of the screen
     */
    public static <T> T loadScreen(String screenLocation, String title, Control controlObject,
                                   Class<T> controllerClass, Runnable onHiddenCallback, boolean isResizable,
                                   boolean closeProgramOnExit, boolean popupOnClose) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        if (controlObject != null) {
            stage.initOwner(controlObject.getScene().getWindow());
        }
        FXMLLoader loader = new FXMLLoader(MainController.class.getResource(screenLocation));
        Parent root = loader.load();
        Scene scene = new Scene(root);
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
        if (controllerClass.isInstance(controller)) {
            loader.setController(controller);
            stage.show();
            return controller;
        } else {
            throw new ClassCastException("Controller class " + controllerClass.getSimpleName() + " not found");
        }
    }
}