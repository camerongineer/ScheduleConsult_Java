package com.cameronm.scheduleconsult.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The DBConnection class is responsible for managing the connection to a MySQL database using the JDBC API.
 *
 * @author Cameron M
 * @since 02-19-2023
 */
public abstract class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference - version 8.0.25
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     * The openConnection method closes the connection to the database
     */
    public static void openConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Database connected.");
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        } catch(ClassNotFoundException classNotFoundException) {
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
        } catch(SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
