package com.cameronm.scheduleconsult.models;

import java.time.LocalDateTime;

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
    private LocalDateTime createdDate;

    /**
     * The name of the user who created the audited entity
     */
    private String createdBy;

    /**
     * The date and time of when the audited entity was last updated
     */
    private LocalDateTime lastUpdated;

    /**
     * The name of the user who last updated the audited entity
     */
    private String lastUpdatedBy;

    /**
     * The getCreatedDate method returns the date and time of when the audited entity was created
     *
     * @return returns the time and date of creation
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * The setCreatedDate method sets the date and time of when the audited entity was created
     */
    public void setCreatedDate(LocalDateTime datetime) {
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
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * The setLastUpdated method sets the date and time of when the audited entity was last updated
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
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
}
