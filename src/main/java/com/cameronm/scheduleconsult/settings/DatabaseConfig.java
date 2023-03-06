package com.cameronm.scheduleconsult.settings;

/**
 * The DatabaseConfig interface stores configuration settings for the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public interface DatabaseConfig {

    /**
     * The location of the database
     */
    String DB_LOCATION = "//localhost/";

    /**
     * The name of the database
     */
    String DB_NAME = "client_schedule";

    /**
     * The protocol of the database
     */
    String DB_PROTOCOL = "jdbc";

    /**
     * The database vendor
     */
    String DB_VENDOR = ":mysql:";

    /**
     * The driver of the database
     */
    String DB_DRIVER = "com.mysql.cj.jdbc.Driver"; // version 8.0.25

    /**
     * Specifies the timezone that the server should use when interpreting datetime values
     */
    String DB_TIMEZONE = "?serverTimezone=UTC";
}
