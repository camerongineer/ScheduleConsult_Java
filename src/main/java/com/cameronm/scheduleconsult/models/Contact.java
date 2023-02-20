package com.cameronm.scheduleconsult.models;

/**
 * The Contact class represents a contact entity from the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public class Contact extends NamedEntity {

    /**
     * The email address of the contact
     */
    private String email;

    /**
     * The constructor of the Contact class
     *
     * @param contactId The id of the contact
     * @param contactName The name of the contact
     * @param email The email address of the contact
     */
    public Contact(int contactId, String contactName, String email) {
        super(contactId, contactName);
        this.email = email;
    }

    /**
     * The getEmail method returns the email address of the contact
     *
     * @return returns the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * The setEmail method sets the email address of the contact
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
