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
    String dbLocation = "//localhost/";

    /**
     * The name of the database
     */
    String dbName = "client_schedule";

    /**
     * The protocol of the database
     */
    String dbProtocol = "jdbc";

    /**
     * The database vendor
     */
    String dbVendor = ":mysql:";

    /**
     * The driver of the database
     */
    String dbDriver = "com.mysql.cj.jdbc.Driver"; // version 8.0.25

    /**
     * The timezone used when connecting to the database
     */
    String dbTimezone = "?connectionTimeZone = SERVER";
}
