package com.cameronm.scheduleconsult.DAO;

import com.cameronm.scheduleconsult.services.QueryService;
import com.cameronm.scheduleconsult.settings.DatabaseConfig;
import com.cameronm.scheduleconsult.settings.UserCredentialConfig;

import java.sql.*;
import java.time.ZoneId;

/**
 * The DBConnection class is responsible for managing the connection to a MySQL database using the JDBC API. The
 * configuration settings are located within the settings package.
 *
 * @author Cameron M
 * @since 02-19-2023
 */
public abstract class DBConnection implements UserCredentialConfig, DatabaseConfig {

    /**
     * The full URL for accessing the database
     */
    private static final String JDBC_URL = DB_PROTOCOL + DB_VENDOR + DB_LOCATION + DB_NAME + DB_TIMEZONE;

    /**
     * The connection to the database
     */
    public static Connection connection;

    /**
     * The openConnection method opens the connection to the database
     */
    public static void openConnection() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(JDBC_URL, DB_ADMIN_USER_NAME, DB_ADMIN_PASSWORD);
            System.out.println("Database connected.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            System.out.println("ClassNotFound Exception:" + classNotFoundException.getMessage());
        }
    }

    /**
     * The getConnection method returns the connection to the database
     *
     * @return returns the connection to the database
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * The closeConnection method closes the connection to the database
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Database disconnected.");
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * The getServerTimezone method returns the timezone for the server
     *
     * @return Returns the timezone of the server
     */
    public static ZoneId getServerTimezone() {
        try {
            ResultSet resultSet = QueryService.getResultsSet("SELECT @@SESSION.time_zone as time_zone;");
            resultSet.next();
            return ZoneId.of(resultSet.getString("time_zone"));
        } catch (Exception exception) {
            return ZoneId.of("UTC");
        }
    }

    /**
     * The setServerTimezone method sets the timezone to UTC
     */
    public static void setServerTimezone() {
        QueryService.execute("SET @@SESSION.time_zone = '+00:00'");
    }
}
