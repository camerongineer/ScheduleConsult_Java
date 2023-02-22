package com.cameronm.scheduleconsult.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The CredentialLogger records all log-in attempts and saves them to a file
 *
 * @author Cameron M
 * @since 02-21-2023
 */
public abstract class CredentialLogger {

    /**
     * The name of the file that records the log-in attempts
     */
    private static final String LOGIN_ACTIVITY_FILE = "login_activity.txt";

    /**
     * The log method saves every log-in attempt to a file in the root directory
     *
     * @param username The username that was attempting to log in
     * @param isValidCredential Boolean value confirming whether the log-in was successful
     */
    public static void log(String username, boolean isValidCredential) {
        try (
                FileWriter fw = new FileWriter(LOGIN_ACTIVITY_FILE, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw)
        ) {
            LocalDateTime timestamp = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            pw.println("TIME:" + timestamp.format(format) +
                    " USER:" + username +
                    " SUCCESS:" + (isValidCredential ? "yes" : "no"));
        } catch (IOException io) {
            System.out.println("File Error: " + io.getMessage());
        }
    }
}