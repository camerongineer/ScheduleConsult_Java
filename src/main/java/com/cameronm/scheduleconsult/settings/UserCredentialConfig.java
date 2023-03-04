package com.cameronm.scheduleconsult.settings;

/**
 * The UserCredentialConfig interface stores credential settings for accessing the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public interface UserCredentialConfig {

    /**
     * The userName for admin access to the database
     */
    String DB_ADMIN_USER_NAME = "sqlUser";

    /**
     * The password for admin access to the database
     */
    String DB_ADMIN_PASSWORD = "Passw0rd!";
}
