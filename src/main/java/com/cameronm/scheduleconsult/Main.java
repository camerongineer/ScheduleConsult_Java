package com.cameronm.scheduleconsult;

import com.cameronm.scheduleconsult.DAO.DBConnection;
import com.cameronm.scheduleconsult.controllers.LoginController;
import com.cameronm.scheduleconsult.utilities.ScreenLoader;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

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
    public static final String VIEWS_PATH = "/com/cameronm/scheduleconsult/fxml/";

    /**
     * The locale of the system for adjusting language within the program
     */
    public static final Locale LOCALE = Locale.getDefault();

    /**
     * The ZoneId of the company for appointment scheduling purposes
     */
    public static final ZoneId COMPANY_ZONE_ID = ZoneId.of("America/New_York");

    /**
     * The days of the week that company is open for appointment scheduling purposes
     */
    public static final HashSet<DayOfWeek> COMPANY_OPEN_DAYS = new HashSet<>(Set.of(DayOfWeek.SUNDAY,
                                                                                    DayOfWeek.MONDAY,
                                                                                    DayOfWeek.TUESDAY,
                                                                                    DayOfWeek.WEDNESDAY,
                                                                                    DayOfWeek.THURSDAY,
                                                                                    DayOfWeek.FRIDAY,
                                                                                    DayOfWeek.SATURDAY)
    );

    /**
     * The time when the company opens for appointment scheduling purposes
     */
    public static final LocalTime COMPANY_OPENING_TIME = LocalTime.of(8, 0);

    /**
     * The time when the company closes for appointment scheduling purposes
     */
    public static final LocalTime COMPANY_CLOSING_TIME = LocalTime.of(22, 0);

    /**
     * The intervals of time between available appointments
     */
    public static final int APPOINTMENT_TIME_INTERVALS = 15;

    /**
     * The earliest date that an appointment can be changed to
     */
    public static final LocalDate APPOINTMENT_EARLIEST_DATE = LocalDate.of(2000, 1, 1);

    /**
     * The user is reminded any appointments within this number of minutes at log-in
     */
    public static final int APPOINTMENT_REMINDER_MINUTES = 15;

    /**
     * The title of the program
     */
    public static final String PROGRAM_TITLE = "ScheduleConsult";

    /**
     * The version number of the program
     */
    public static final String PROGRAM_VERSION = "v1.0";

    /**
     * The title of the program
     */
    public static final String WINDOW_TITLE = PROGRAM_TITLE + " " + PROGRAM_VERSION;

    /**
     * The main method is the entry point for the application
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        DBConnection.openConnection();
        launch(args);
    }

    /**
     * The start method sets up the log-in screen and displays it to the user
     *
     * @param logInStage the log-in stage for the JavaFX application
     */
    @Override
    public void start(Stage logInStage) throws IOException {
        ScreenLoader.loadScreen(Main.VIEWS_PATH + "LoginScreen.fxml",
                                Main.WINDOW_TITLE,
                                null,
                                LoginController.class,
                                null,
                                null,
                                false,
                                true,
                                false);
    }

    /**
     * The safeExit method closes the database connection before exiting the program
     */
    public static void safeExit() {
        DBConnection.closeConnection();
        System.exit(0);
    }
}
