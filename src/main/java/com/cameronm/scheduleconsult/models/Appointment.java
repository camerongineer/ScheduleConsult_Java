package com.cameronm.scheduleconsult.models;

import java.sql.Timestamp;

/**
 * The Appointment class represents an appointment entity from the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public class Appointment extends AuditedEntity {

    /**
     * The description of the appointment
     */
    private String description;

    /**
     * The location of the appointment
     */
    private String location;

    /**
     * The type of the appointment
     */
    private String type;

    /**
     * The date and time that the appointment is scheduled to start
     */
    private Timestamp start;

    /**
     * The date and time that the appointment is scheduled to end
     */
    private Timestamp end;

    /**
     * The customer ID foreign key for the appointment entity
     */
    private int customerId;

    /**
     * The user ID foreign key for the appointment entity
     */
    private int userId;

    /**
     * The contact ID foreign key for the appointment entity
     */
    private int contactId;

    /**
     * The constructor for the Appointment class
     *
     * @param appointmentId The ID of the appointment entity
     * @param title The title of the appointment
     * @param createdDate The date and time that the appointment entity was created
     * @param createdBy The name of the user who created the appointment entity
     * @param lastUpdated The date and time that the appointment entity was last updated
     * @param lastUpdatedBy The name of the user who last updated the appointment entity
     * @param description The description of the appointment
     * @param location The location of the appointment
     * @param type The type of appointment
     * @param start The date and time that the appointment is scheduled to start
     * @param end The date and time that the appointment is scheduled to end
     * @param customerId The customer ID foreign key for the appointment entity
     * @param userId The user ID foreign key for the appointment entity
     * @param contactId The contact ID foreign key for the appointment entity
     */
    public Appointment(int appointmentId,
                       String title,
                       Timestamp createdDate,
                       String createdBy,
                       Timestamp lastUpdated,
                       String lastUpdatedBy,
                       String description,
                       String location,
                       String type,
                       Timestamp start,
                       Timestamp end,
                       int customerId,
                       int userId,
                       int contactId) {
        super(appointmentId, title, createdDate, createdBy, lastUpdated, lastUpdatedBy);
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * The getDescription method returns the description of the appointment
     *
     * @return returns the description of the appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * The setDescription method sets the description of the appointment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The getLocation method returns the location of the appointment
     *
     * @return returns the location of the appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * The setLocation method sets the location of the appointment
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * The getType method returns the type of appointment
     *
     * @return returns the type of appointment
     */
    public String getType() {
        return type;
    }

    /**
     * The setType method sets the type of appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * The getStart method returns the date and time of when the appointment will start
     *
     * @return returns the time and date of the start of the appointment
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * The setStart method sets the date and time of when the appointment will start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * The getEnd method returns the date and time of when the appointment will end
     *
     * @return returns the time and date of the end of the appointment
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * The setEnd method sets the date and time of when the appointment will end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * The getCustomerId method returns the customer ID foreign key of the appointment entity
     *
     * @return returns the customer ID foreign key
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * The setCustomerId method sets the customer ID foreign key of the appointment entity
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * The getUserId method returns the user ID foreign key of the appointment entity
     *
     * @return returns the user ID foreign key
     */
    public int getUserId() {
        return userId;
    }

    /**
     * The setUserId method sets the user ID foreign key of the appointment entity
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * The getContactId method returns the contact ID foreign key of the appointment entity
     *
     * @return returns the contact ID foreign key
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * The setContactId method sets the contact ID foreign key of the appointment entity
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
}
