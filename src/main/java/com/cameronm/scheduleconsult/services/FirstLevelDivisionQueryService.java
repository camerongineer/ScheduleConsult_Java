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
 * The FirstLevelDivisionQueryService class is responsible for converting entries
 * in the first_level_divisions table of the database into FirstLevelDivision instances
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
        String sqlQuery = DBQueries.SELECT_ALL;
        return getDivisions(sqlQuery);
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
     * The getDivision method takes a results set and previously retrieved common attributes and
     * retrieves all the rest of the remaining first-level division attributes and returns the first-level division
     *
     * @param results The results set passed in from an entry in the first_level_divisions table
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns a first-level division
     * @throws SQLException Throws an SQLException of the ResultSet is invalid
     */
    static FirstLevelDivision getDivision(ResultSet results, HashMap<String, String> entityAttributes) throws SQLException {
        entityAttributes.put("countryId", results.getString(DBModels.DIVISIONS.getAttributes().get("countryId")));
        int countryId = Integer.parseInt(entityAttributes.get("countryId"));
        return new FirstLevelDivision(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                Timestamp.valueOf(entityAttributes.get("createDate")),
                entityAttributes.get("createdBy"),
                Timestamp.valueOf(entityAttributes.get("lastUpdate")),
                entityAttributes.get("lastUpdatedBy"),
                countryId
        );
    }
}
