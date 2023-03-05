package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.FirstLevelDivision;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * The FirstLevelDivisionQueryService class is responsible for converting entries in the first_level_divisions table of
 * the database into FirstLevelDivision instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class FirstLevelDivisionQueryService extends QueryService {

    /**
     * The getAllDivisions method returns an ObservableList of all first-level divisions in the database
     *
     * @return Returns an ObservableList of all first-level divisions
     */
    public static ObservableList<FirstLevelDivision> getAllDivisions() {
        return getDivisions(DBQueries.SELECT_ALL);
    }

    /**
     * The getDivisions method returns an ObservableList of specific first-level divisions in the database
     *
     * @param sqlQuery The query specifying the first-level divisions to return
     * @return Returns an ObservableList of first-level divisions specified by the query
     */
    public static ObservableList<FirstLevelDivision> getDivisions(String sqlQuery) {
        return QueryService.getEntities(DBModels.DIVISIONS, sqlQuery);
    }

    /**
     * The getDivision method takes a results set and previously retrieved common attributes and retrieves all the rest
     * of the remaining first-level division attributes and returns the first-level division
     *
     * @param results          The results set passed in from an entry in the first_level_divisions table
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns a first-level division
     * @throws SQLException Throws an SQLException of the ResultSet is invalid
     */
    static FirstLevelDivision getDivision(ResultSet results, HashMap<String, String> entityAttributes)
            throws SQLException {
        entityAttributes.put("countryId",
                             results.getString(DBModels.DIVISIONS.getAttributes()
                                                                 .get("countryId")));
        int countryId = Integer.parseInt(entityAttributes.get("countryId"));
        return new FirstLevelDivision(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("createDate"))),
                entityAttributes.get("createdBy"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("lastUpdate"))),
                entityAttributes.get("lastUpdatedBy"),
                countryId
        );
    }

    /**
     * The getMatchFromDatabase method retrieves a matching entry from a first-level division if the customer's division
     * ID matches the first-level division's primary ID
     *
     * @param divisionId      The primary ID of the first-level division that must match the division ID foreign key of
     *                        the customer
     * @param requestedColumn The first-level division column type requested by the customer
     * @return Returns the requested match
     */
    public static String getMatchFromDatabase(int divisionId, String requestedColumn) {
        return getMatchFromDatabase(DBModels.DIVISIONS, DBModels.CUSTOMERS, divisionId, requestedColumn);
    }

    /**
     * The getDivisionById method returns a first-level division specified by the ID
     *
     * @param divisionId The ID of the division
     * @return returns a first-level division entity
     */
    public static FirstLevelDivision getDivisionById(int divisionId) {
        return QueryService.getEntityById(DBModels.DIVISIONS, divisionId);
    }

    /**
     * The getAllDivisionsByCountryId method returns a list of first-level divisions based on the country ID
     *
     * @param countryId The country ID of the first-level division
     * @return Returns a list of first-level divisions
     */
    public static ObservableList<FirstLevelDivision> getAllDivisionsByCountryId(int countryId) {
        String sqlQuery = SELECT_ALL +
                WHERE +
                String.format(EQUALS_INTEGER,
                              DBModels.DIVISIONS.getAttributes()
                                                .get("countryId"),
                              countryId);
        return QueryService.getEntities(DBModels.DIVISIONS, sqlQuery);
    }
}
