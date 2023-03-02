package com.cameronm.scheduleconsult.views;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

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
     * The appointmentReminderPopup method displays a popup reminding of
     * appointments coming up within a certain amount of minutes
     *
     * @param minutes The amount of minutes
     * @param message The message that is displayed in the popup
     */
    static void appointmentReminderPopup(int minutes, String message) {
        popupPrompt(Alert.AlertType.INFORMATION,
                "Appointment Reminder",
                String.format("Appointments within %d minutes", minutes),
                message);
    }

    /**
     * The closeProgram method displays a popup asking if the program should be closed
     *
     * @return Returns the result of popupPrompt
     */
    static boolean closeProgramPrompt() {
        return popupPrompt(Alert.AlertType.CONFIRMATION,
                "Confirmation",
                "Are you sure you want to close the program?",
                "Any unsaved changes will be lost.");
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
                        , model.getTableName().substring(0, model.getTableName().length() - 1)),
                "This action cannot be undone.");
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
     * @param title The title of the popup screen
     * @param header The text displayed in the header of the popup
     * @param content The text displayed in the content of the popup
     * @return Returns true if OK is pressed
     */
    static boolean popupExpandablePrompt(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = setAlert(alertType, title, header);
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        alert.getDialogPane().setContent(textArea);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * The popupPrompt method sets up and displays fixed size popup
     *
     * @param alertType The type of alert
     * @param title The title of the popup screen
     * @param header The text displayed in the header of the popup
     * @param content The text displayed in the content of the popup
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
     * @param title The title of the popup screen
     * @param header The text displayed in the header of the popup
     * @return Return the alert
     */
    private static Alert setAlert(Alert.AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }
}