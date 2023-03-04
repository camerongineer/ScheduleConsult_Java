package com.cameronm.scheduleconsult.models;

import java.sql.Timestamp;

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
     * @param divisionId    The ID of the first level division entity
     * @param division      The name of the first level division
     * @param createdDate   The date and time that the first level division entity was created
     * @param createdBy     The name of the user who created the first level division entity
     * @param lastUpdated   The date and time that the first level division entity was last updated
     * @param lastUpdatedBy The name of the user who last updated the first level division entity
     * @param countryId     The country ID foreign key for the first level division entity
     */
    public FirstLevelDivision(int divisionId,
                              String division,
                              Timestamp createdDate,
                              String createdBy,
                              Timestamp lastUpdated,
                              String lastUpdatedBy,
                              int countryId) {
        super(divisionId, division, createdDate, createdBy, lastUpdated, lastUpdatedBy);
        this.countryId = countryId;
    }

    /**
     * The getCountryId method returns the country ID foreign key of the first level division entity
     *
     * @return returns the country ID foreign key
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * The setCountryId method sets the country ID foreign key of the first level division entity
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * The equals method determines whether two objects are equal
     *
     * @param obj The object
     * @return Return boolean result determining if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FirstLevelDivision other)) {
            return false;
        }
        return super.equals(other) && this.countryId == other.countryId;
    }
}
