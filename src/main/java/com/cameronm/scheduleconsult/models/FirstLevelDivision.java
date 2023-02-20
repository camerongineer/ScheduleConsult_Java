package com.cameronm.scheduleconsult.models;

import java.time.LocalDateTime;

/**
 * The FirstLevelDivision class represents a first level division entity from the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public class FirstLevelDivision extends AuditedEntity {

    /**
     * The country ID is a foreign key for the first level division
     */
    private int countryId;

    /**
     * The constructor for the FirstLevelDivision class
     *
     * @param id The ID of the first level division entity
     * @param name The name of the first level division
     * @param createdDate The date and time that the first level division entity was created
     * @param createdBy The name of the user who created the first level division entity
     * @param lastUpdated The date and time that the first level division entity was last updated
     * @param lastUpdatedBy The name of the user who last updated the first level division entity
     * @param countryId The country ID foreign key for the first level division
     */
    public FirstLevelDivision(int id,
                              String name,
                              LocalDateTime createdDate,
                              String createdBy, LocalDateTime lastUpdated,
                              String lastUpdatedBy,
                              int countryId) {
        super(id, name, createdDate, createdBy, lastUpdated, lastUpdatedBy);
        this.countryId = countryId;
    }

    /**
     * The getCountryId method returns the country ID foreign key of the first level division
     *
     * @return returns the country ID foreign key
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * The setCountryId method sets the country ID foreign key of the first level division
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
