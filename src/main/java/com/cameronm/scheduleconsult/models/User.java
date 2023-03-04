package com.cameronm.scheduleconsult.models;

import java.sql.Timestamp;

/**
 * The User class represents a user entity from the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public class User extends AuditedEntity {

    /**
     * The password of the user
     */
    private String password;

    /**
     * The constructor for the FirstLevelDivision class
     *
     * @param userId The ID of the user
     * @param userName The username of the user
     * @param createdDate The date and time that the user entity was created
     * @param createdBy The name of the user who created the user entity
     * @param lastUpdated The date and time that the user entity was last updated
     * @param lastUpdatedBy The name of the user who last updated the user entity
     * @param password The password of the user
     */
    public User(int userId,
                String userName,
                Timestamp createdDate,
                String createdBy,
                Timestamp lastUpdated,
                String lastUpdatedBy,
                String password) {
        super(userId, userName, createdDate, createdBy, lastUpdated, lastUpdatedBy);
        this.password = password;
    }

    /**
     * The getPassword method returns the password of the user
     *
     * @return returns the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * The setPassword method sets the password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The equals method determines whether two objects are equal
     *
     * @param obj The object
     * @return Return boolean result determining if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User other)) {
            return false;
        }
        return super.equals(other) && this.password.equals(other.password);
    }
}
