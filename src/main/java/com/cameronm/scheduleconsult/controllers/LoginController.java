package com.cameronm.scheduleconsult.controllers;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.Main;
import com.cameronm.scheduleconsult.models.User;
import com.cameronm.scheduleconsult.utilities.CredentialLogger;
import com.cameronm.scheduleconsult.services.UserQueryService;
import com.cameronm.scheduleconsult.utilities.ScreenLoader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ResourceBundle;

/**
 * The LoginController class is the controller class for the login screen
 *
 * @author Cameron M
 * @since 02-19-2023
 */
public class LoginController implements Initializable, DBQueries {

    /**
     * The user logged into the program
     */
    private static User programUser;

    /**
     * The label displaying the language of the login screen
     */
    @FXML
    private Label languageLabel;

    /**
     * The label displaying the zoneID of the current location of use
     */
    @FXML
    private Label zoneIDLabel;

    /**
     * The label displaying the title of the program
     */
    @FXML
    private Label titleLabel;

    /**
     * The label displaying a translation of the title for non-English languages
     */
    @FXML
    private Label translatedTitleLabel;

    /**
     * The label displaying the version number of the program
     */
    @FXML
    private Label versionLabel;

    /**
     * The label for the username textField
     */
    @FXML
    private Label usernameLabel;

    /**
     * The text field for entry of the username
     */
    @FXML
    private TextField usernameTextField;

    /**
     * The label for the password passwordField
     */
    @FXML
    private Label passwordLabel;

    /**
     * The password field for entry of the password
     */
    @FXML
    private PasswordField passwordPasswordField;

    /**
     * The label displaying an error if credentials are invalid
     */
    @FXML
    private Label credentialErrorLabel;

    /**
     * The sign-in button
     */
    @FXML
    private Button signInButton;

    /**
     * The resource bundle for all text related to the log-in screen
     */
    private ResourceBundle resourceBundle;

    /**
     * The initialize method initializes login screen
     *
     * @param url The url passed into the initialize method
     * @param resourceBundle The resource bundle passed into the initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = ResourceBundle
                .getBundle("com.cameronm.scheduleconsult.i18n.loginscreen.login", Main.LOCALE);
        setLabels(this.resourceBundle);
    }

    /**
     * The setLabels method sets the text for the labels and buttons based on the current language setting
     */
    private void setLabels(ResourceBundle resourceBundle) {
        languageLabel.setText(resourceBundle.getString("languageLabel"));
        zoneIDLabel.setText(String.valueOf(ZoneId.systemDefault()));
        titleLabel.setText(Main.PROGRAM_TITLE);
        translatedTitleLabel.setText(resourceBundle.getString("translatedTitleLabel"));
        versionLabel.setText(Main.PROGRAM_VERSION);
        usernameLabel.setText(resourceBundle.getString("usernameLabel"));
        passwordLabel.setText(resourceBundle.getString("passwordLabel"));
        signInButton.setText(resourceBundle.getString("signInButton"));
    }

    /**
     * The logIn method logs in the user
     */
    private void logIn() {
        String username = usernameTextField.getText().toLowerCase();
        String password = passwordPasswordField.getText();
        passwordPasswordField.clear();
        if (username.isBlank() || password.isEmpty()) {
            credentialErrorLabel.setText(resourceBundle.getString("blankCredentialFieldError"));
            CredentialLogger.log(username, false);
            return;
        }
        DBModels userModel = DBModels.USERS;
        String dbUsernameColumn = userModel.getAttributes().get("name");
        String dbPasswordColumn = userModel.getAttributes().get("password");
        String whereQuery = String.format(WHERE + EQUALS_STRING + AND + EQUALS_STRING,
                dbUsernameColumn, username, dbPasswordColumn, password);
        String sqlQuery = SELECT_ALL + whereQuery;
        ObservableList<User> matchingUsers = UserQueryService.getUsers(sqlQuery);
        if (!matchingUsers.isEmpty()) {
            CredentialLogger.log(username, true);
            setProgramUser(matchingUsers.get(0));
            loadMainScreen();
        } else {
            CredentialLogger.log(username, false);
            credentialErrorLabel.setText(resourceBundle.getString("credentialError"));
        }
    }

    /**
     * The signInButtonClicked method calls the logIn method when the signInButton is clicked
     */
    @FXML
    void signInButtonClicked() { logIn(); }

    /**
     * The passwordPasswordFieldTyped method calls the keyTyped method
     *
     * @param event Event occurring when something is typed into the passwordPasswordField
     */
    @FXML
    void passwordPasswordFieldTyped(KeyEvent event) {
        keyTyped(event);
    }

    /**
     * The usernameTextFieldTyped method calls the keyTyped method
     *
     * @param event Event occurring when something is typed into the usernameTextField
     */
    @FXML
    void usernameTextFieldTyped(KeyEvent event) {
        keyTyped(event);
    }

    /**
     * The keyTyped method clears the credentialError when something is typed in any fields and
     * if the "ENTER" key is pressed, the logIn method is called
     *
     * @param event Event occurring when something is typed into any textField on the log-in screen
     */
    private void keyTyped(KeyEvent event) {
        credentialErrorLabel.setText("");
        if (event.getCharacter().equals("\r")) {
            logIn();
        }
    }

    /**
     * The getProgramUser method returns the user who is signed in to the program
     */
    public static User getProgramUser() {
        return programUser;
    }

    /**
     * The setProgramUser method sets the user of the program to the user that signed in
     *
     * @param programUser The user who signed in
     */
    public static void setProgramUser(User programUser) {
        LoginController.programUser = programUser;
    }

    /**
     * The loadMainScreen method loads the Main Screen of the application
     */
    private void loadMainScreen() {
        try {
            ScreenLoader.loadScreen(Main.VIEWS_PATH + "MainScreen.fxml",
                    Main.WINDOW_TITLE,
                    null,
                    MainController.class,
                    this,
                    null,
                    true,
                    true,
                    true);
        } catch(IOException io) {
            System.out.println("Loading Main Screen Unsuccessful");
        }
    }
}
