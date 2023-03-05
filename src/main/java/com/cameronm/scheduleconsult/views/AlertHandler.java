package com.cameronm.scheduleconsult.views;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.Main;
import com.cameronm.scheduleconsult.models.NamedEntity;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;

/**
 * The AlertHandler Interface is responsible for displaying alert popups
 *
 * @author Cameron M
 * @since 03-01-2023
 */
public interface AlertHandler {

    /**
     * The aboutPopup method displays a popup showing information about the program
     */
    static void aboutPopup() {
        popupPrompt(Alert.AlertType.INFORMATION,
                    "About Program",
                    Main.WINDOW_TITLE,
                    "Developed by Cameron M / 2023");
    }

    /**
     * The confirmAction method displays a popup asking to confirm a specific action
     *
     * @param header  The text displayed in the header of the popup
     * @param content The text displayed in the content of the popup
     * @return Returns the result of popupPrompt
     */
    static boolean confirmAction(String header, String content) {
        return popupPrompt(Alert.AlertType.CONFIRMATION,
                           "Confirmation Dialog",
                           header,
                           content);
    }

    /**
     * The closeProgram method displays a popup asking if the program should be closed
     *
     * @return Returns the result of popupPrompt
     */
    static boolean closePrompt() {
        return popupPrompt(Alert.AlertType.CONFIRMATION,
                           "Confirmation",
                           "Are you sure you want to close?",
                           "Any unsaved changes will be lost.");
    }

    /**
     * The customErrorPopup method displays a customized error message
     */
    static void customErrorPopup(String title, String header, String content) {
        popupPrompt(Alert.AlertType.ERROR,
                    title,
                    header,
                    content);
    }

    /**
     * The deleteEntity method displays a popup asking if an entity should be deleted
     *
     * @param model The type of database entity model
     * @return Returns the result of popupPrompt
     */
    static boolean deleteEntityPrompt(DBModels model) {
        return popupPrompt(Alert.AlertType.CONFIRMATION,
                           "Confirm Delete",
                           String.format("Are you sure you want to delete this %s?"
                                   ,
                                         model.getTableName()
                                              .substring(0,
                                                         model.getTableName()
                                                              .length() - 1)),
                           "This action cannot be undone.");
    }

    /**
     * The entityModified method displays a popup confirming that an entity as successfully been saved or deleted
     *
     * @param model   The type of database entity model
     * @param entity  The entity that has been saved
     * @param deleted Boolean specifying whether the boolean was deleted or saved
     */
    static <T extends NamedEntity> void entityModified(DBModels model, T entity, boolean deleted) {
        String type = model.getTableName()
                           .toUpperCase()
                           .charAt(0) +
                model.getTableName()
                     .substring(1,
                                model.getTableName()
                                     .length() - 1);
        String id = String.valueOf(entity.getId());
        String name = entity.getName();
        String modificationType = deleted ? "deleted" : "saved";
        notification(String.format("%s %s\n\nID: %s - \"%s\"", type, modificationType, id, name), Color.GREEN);
    }

    /**
     * The logOut method displays a popup asking if the user would like to log out
     *
     * @return Returns the result of popupPrompt
     */
    static boolean logOutPrompt() {
        return popupPrompt(Alert.AlertType.CONFIRMATION,
                           "Confirmation",
                           "Are you sure you want to log out?",
                           "Any unsaved changes will be lost.");
    }

    /**
     * The popupExpandablePrompt method sets up and displays a popup where the text is expandable
     *
     * @param alertType The type of alert
     * @param title     The title of the popup screen
     * @param header    The text displayed in the header of the popup
     * @param content   The text displayed in the content of the popup
     */
    static void popupExpandablePrompt(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = setAlert(alertType, title, header);
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane()
             .setContent(textArea);
        alert.getDialogPane()
             .setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * The popupPrompt method sets up and displays fixed size popup
     *
     * @param alertType The type of alert
     * @param title     The title of the popup screen
     * @param header    The text displayed in the header of the popup
     * @param content   The text displayed in the content of the popup
     * @return Returns true if OK is pressed
     */
    static boolean popupPrompt(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = setAlert(alertType, title, header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * The setAlert method sets the title and header of the alert
     *
     * @param alertType The type of alert
     * @param title     The title of the popup screen
     * @param header    The text displayed in the header of the popup
     * @return Return the alert
     */
    private static Alert setAlert(Alert.AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    /**
     * The notification method creates a popup that disappears after a few seconds
     *
     * @param message The message contained in the notification
     * @param color   The color of the background of the notification
     */
    static void notification(String message, Color color) {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setMinWidth(500);
        popup.setMinHeight(150);
        popup.setAlwaysOnTop(true);

        Label label = new Label(message);
        label.setFont(new Font("Arial", 18));
        VBox layout = new VBox(10);
        layout.getChildren()
              .add(label);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setBorder(new Border(new BorderStroke(Color.BLACK,
                                                     BorderStrokeStyle.SOLID,
                                                     CornerRadii.EMPTY,
                                                     BorderWidths.DEFAULT)));
        layout.setPrefSize(500, 150);

        Scene scene = new Scene(layout);
        popup.setScene(scene);

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> popup.hide());
        popup.show();
        delay.play();
    }
}