package com.cameronm.scheduleconsult.models;

import java.sql.Timestamp;

/**
 * The AuditedEntity adds common attributes to database entities that keep track
 * of dates and times of when they were created/updated
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public abstract class AuditedEntity extends NamedEntity {

    /**
     * The date and time of when the audited entity was created
     */
    private Timestamp createdDate;

    /**
     * The name of the user who created the audited entity
     */
    private String createdBy;

    /**
     * The date and time of when the audited entity was last updated
     */
    private Timestamp lastUpdated;

    /**
     * The name of the user who last updated the audited entity
     */
    private String lastUpdatedBy;

    /**
     * The constructor for the AuditedEntity class
     *
     * @param id The ID of the audited entity
     * @param name The name of the audited entity
     * @param createdDate The date and time that the audited entity was created
     * @param createdBy The name of the user who created the audited entity
     * @param lastUpdated The date and time that the audited entity was last updated
     * @param lastUpdatedBy The name of the user who last updated the audited entity
     */
    AuditedEntity(int id,
                  String name,
                  Timestamp createdDate,
                  String createdBy,
                  Timestamp lastUpdated,
                  String lastUpdatedBy) {
        super(id, name);
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * The getCreatedDate method returns the date and time of when the audited entity was created
     *
     * @return returns the time and date of creation
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /**
     * The setCreatedDate method sets the date and time of when the audited entity was created
     */
    public void setCreatedDate(Timestamp datetime) {
        this.createdDate = datetime;
    }

    /**
     * The getCreatedBy method returns the name of the user who created the audited entity
     *
     * @return returns the name of the user
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * The setCreatedBy method sets the name of the user who created the audited entity
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * The getLastUpdated method returns the date and time of when the audited entity was last updated
     *
     * @return returns the time and date of last update
     */
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    /**
     * The setLastUpdated method sets the date and time of when the audited entity was last updated
     */
    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * The getCreatedBy method returns the name of the user who last updated the audited entity
     *
     * @return returns the name of the user
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * The setLastUpdatedBy method sets the name of the user who last updated the audited entity
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * The equals method determines whether two objects are equal
     *
     * @param obj The object
     * @return Return boolean result determining if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AuditedEntity other)) {
            return false;
        }
        return super.equals(other) &&
                this.getCreatedBy().equals(other.createdBy) &&
                this.getCreatedDate().toString().equals(other.createdDate.toString());
    }
}
