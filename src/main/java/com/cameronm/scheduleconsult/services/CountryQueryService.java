package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.Country;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * The CountryQueryService class is responsible for converting entries
 * in the countries table of the database into Country instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class CountryQueryService extends QueryService {

    /**
     * The getAllCountry method returns an ObservableList of all contacts in the database
     *
     * @return Returns an ObservableList of all contacts
     */
    public static ObservableList<Country> getAllCountries() {
        return getCountries(DBQueries.SELECT_ALL);
    }

    /**
     * The getCountries method returns an ObservableList of specific countries in the database
     *
     * @param sqlQuery The query specifying the countries to return
     * @return Returns an ObservableList of countries specified by the query
     */
    public static ObservableList<Country> getCountries(String sqlQuery) {
        return QueryService.getEntities(DBModels.COUNTRIES, sqlQuery);
    }

    /**
     * The getCountry method takes previously retrieved common attributes and returns the country
     *
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns a country
     */
    static Country getCountry(HashMap<String, String> entityAttributes) {
        return new Country(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("createDate"))),
                entityAttributes.get("createdBy"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("lastUpdate"))),
                entityAttributes.get("lastUpdatedBy")
        );
    }

    /**
     * The getCountryById method returns a country specified by the ID
     *
     * @param countryId The ID of the country
     * @return returns a country entity
     */
    public static Country getCountryById(int countryId) {
        return QueryService.getEntityById(DBModels.COUNTRIES, countryId);
    }
}
