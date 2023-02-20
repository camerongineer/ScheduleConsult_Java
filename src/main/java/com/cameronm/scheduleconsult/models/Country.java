package com.cameronm.scheduleconsult.models;

import java.sql.Timestamp;

/**
 * The Country class represents a country entity from the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public class Country extends AuditedEntity {

    /**
     * The constructor for the Country class
     *
     * @param countryId The ID of the country entity
     * @param country The name of the country
     * @param createdDate The date and time that the country entity was created
     * @param createdBy The name of the user who created the country entity
     * @param lastUpdated The date and time that the country entity was last updated
     * @param lastUpdatedBy The name of the user who last updated the country entity
     */
    public Country(int countryId,
                   String country,
                   Timestamp createdDate,
                   String createdBy,
                   Timestamp lastUpdated,
                   String lastUpdatedBy) {
        super(countryId, country, createdDate, createdBy, lastUpdated, lastUpdatedBy);
    }
}
