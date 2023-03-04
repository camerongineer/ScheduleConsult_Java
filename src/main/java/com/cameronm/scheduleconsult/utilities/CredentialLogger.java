package com.cameronm.scheduleconsult.utilities;

import java.io.*;
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
            System.out.println("Login " + (isValidCredential ? "Successful" : "Failed"));
        } catch (IOException io) {
            System.out.println("File Error: " + io.getMessage());
        }
    }

    /**
     * The report method returns the log to a string
     *
     * @return Returns the contents of the log
     */
    public static String report() {
        try (BufferedReader br = new BufferedReader(new FileReader(LOGIN_ACTIVITY_FILE))) {
            StringBuilder report = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                report.append(line).append("\n");
            }
            return report.toString();
        } catch (IOException io) {
            System.out.println("File Error: " + io.getMessage());
            return "";
        }
    }
}