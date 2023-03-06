package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBConnection;
import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.NamedEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.*;

/**
 * The QueryService class is responsible for converting entries in the database into Entity instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class QueryService implements DBQueries {

    /**
     * The execute method executes a query statement in the database
     *
     * @param sqlQuery The query executed
     * @return Returns boolean confirming success of query execution
     */
    public static boolean execute(String sqlQuery) {
        try (
                Statement statement = DBConnection.getConnection()
                                                  .prepareStatement(sqlQuery)
        ) {
            statement.execute(sqlQuery);
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }

    /**
     * The getResultsSet method returns the result set of a query
     *
     * @param sqlQuery The query executed
     * @return Returns the result set of the query
     * @throws SQLException Throws SQLException if unsuccessful
     */
    public static ResultSet getResultsSet(String sqlQuery) throws SQLException {
        Statement statement = DBConnection.getConnection()
                                          .prepareStatement(sqlQuery);
        return statement.executeQuery(sqlQuery);
    }

    /**
     * The getEntities method returns a list of entities specified in by a model and query
     *
     * @param dbModel  The database model used
     * @param sqlQuery The query executed
     * @param <T>      The type of NamedEntity returned in the list
     * @return Returns an observable list of entities
     */
    static <T extends NamedEntity> ObservableList<T> getEntities(DBModels dbModel, String sqlQuery) {
        sqlQuery = String.format(sqlQuery, dbModel.getTableName());
        ObservableList<T> entities = FXCollections.observableArrayList();
        try (
                Statement statement = DBConnection.getConnection()
                                                  .prepareStatement(sqlQuery);
                ResultSet results = statement.executeQuery(sqlQuery)
        ) {
            HashMap<String, String> entityAttributes = new HashMap<>();
            while (results.next()) {
                entityAttributes.put("id",
                                     results.getString(dbModel.getAttributes()
                                                              .get("id")));
                entityAttributes.put("name",
                                     results.getString(dbModel.getAttributes()
                                                              .get("name")));
                T entity = null;
                if (dbModel == DBModels.CONTACTS) {
                    entity = (T) ContactQueryService.getContact(results, entityAttributes);
                } else {
                    entityAttributes.put("createDate",
                                         results.getString(dbModel.getAttributes()
                                                                  .get("createDate")));
                    entityAttributes.put("createdBy",
                                         results.getString(dbModel.getAttributes()
                                                                  .get("createdBy")));
                    entityAttributes.put("lastUpdate",
                                         results.getString(dbModel.getAttributes()
                                                                  .get("lastUpdate")));
                    entityAttributes.put("lastUpdatedBy",
                                         results.getString(dbModel.getAttributes()
                                                                  .get("lastUpdatedBy")));
                    switch (dbModel.getTableName()) {
                        case "appointments" -> {
                            entity = (T) AppointmentQueryService.getAppointment(results, entityAttributes);
                        }
                        case "countries" -> {
                            entity = (T) CountryQueryService.getCountry(entityAttributes);
                        }
                        case "customers" -> {
                            entity = (T) CustomerQueryService.getCustomer(results, entityAttributes);
                        }
                        case "first_level_divisions" -> {
                            entity = (T) FirstLevelDivisionQueryService.getDivision(results, entityAttributes);
                        }
                        case "users" -> {
                            entity = (T) UserQueryService.getUser(results, entityAttributes);
                        }
                    }
                }
                entities.add(entity);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return entities;
    }

    /**
     * The getEntityById method returns an entity specified in by a model the ID of the entity
     *
     * @param dbModel The database model used
     * @param id      The ID of the entity
     * @param <T>     The type of NamedEntity returned
     * @return returns an entity
     */
    static <T extends NamedEntity> T getEntityById(DBModels dbModel, int id) {
        String sqlQuery = SELECT_ALL + WHERE + String.format(EQUALS_INTEGER,
                                                             dbModel.getAttributes()
                                                                    .get("id"),
                                                             id);
        ObservableList<T> entity = getEntities(dbModel, sqlQuery);
        if (!entity.isEmpty()) {
            return entity.get(0);
        } else {
            return null;
        }
    }

    /**
     * The deleteEntityById method deletes an entity specified in by a model the ID of the entity
     *
     * @param dbModel The database model used
     * @param id      The ID of the entity
     * @return Returns boolean confirming success of query execution
     */
    static boolean deleteEntityById(DBModels dbModel, int id) {
        String sqlQuery = String.format(DELETE, dbModel.getTableName()) +
                WHERE +
                String.format(EQUALS_INTEGER,
                              dbModel.getAttributes()
                                     .get("id"),
                              id);
        return execute(sqlQuery);
    }

    /**
     * The getMatchFromDatabase method returns a string of a requested column item of matching entity primary and
     * foreign keys
     *
     * @param primaryEntity   The model whose ID matches its primary key
     * @param foreignEntity   The model whose ID matches its foreign key
     * @param primaryId       The ID of the primaryEntity
     * @param requestedColumn The requested column
     * @return returns a string of the requested column
     */
    static String getMatchFromDatabase(DBModels primaryEntity,
                                       DBModels foreignEntity,
                                       int primaryId,
                                       String requestedColumn) {
        String primaryTable = primaryEntity.getTableName();
        String foreignTable = foreignEntity.getTableName();
        String matchingColumnName = primaryEntity.getAttributes()
                                                 .get("id");
        String primaryColumn = primaryTable + "." + matchingColumnName;
        String foreignColumn = foreignTable + "." + matchingColumnName;
        String query = String.format(SELECT_ALL, primaryTable) +
                String.format(JOIN, foreignTable, primaryColumn, foreignColumn) +
                String.format(WHERE + EQUALS_INTEGER, primaryColumn, primaryId);
        String name = null;
        try (
                Statement statement = DBConnection.getConnection()
                                                  .prepareStatement(query);
                ResultSet resultSet = statement.executeQuery(query)
        ) {
            if (resultSet.next()) {
                name = resultSet.getString(requestedColumn);
            }
            return name;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return name;
    }

    /**
     * The queryHasMatches method returns a boolean specifying if a query has any matches
     *
     * @param sqlQuery The query executed
     * @return Returns a boolean specifying if any results were found
     */
    public static boolean queryHasMatches(String sqlQuery) {
        try (
                Statement statement = DBConnection.getConnection()
                                                  .createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            return resultSet.next();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    /**
     * The insertValuesQueriesSelector builds a query string for inserting a new value in the database
     *
     * @param dbModel The model specifying the table being inserted to and the attributes needed
     * @return Returns the query string for inserting a new value
     */
    protected static String insertValuesQueriesSelector(DBModels dbModel) {
        Map<String, String> values = neededValuesMap(dbModel);
        StringBuilder valueColumns = new StringBuilder();
        StringBuilder valueTemplate = new StringBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String value = entry.getValue();
            valueColumns.append(value)
                        .append(", ");
            valueTemplate.append("?, ");
        }
        if (dbModel != DBModels.CONTACTS) {
            valueColumns.append(String.format("%s, %s, %s, %s",
                                              dbModel.getAttributes()
                                                     .get("createDate"),
                                              dbModel.getAttributes()
                                                     .get("createdBy"),
                                              dbModel.getAttributes()
                                                     .get("lastUpdate"),
                                              dbModel.getAttributes()
                                                     .get("lastUpdatedBy")));
            valueTemplate.append("NOW(), ?, NOW(), ?");
        } else {
            valueColumns.setLength(valueColumns.length() - 2);
            valueTemplate.setLength(valueTemplate.length() - 2);
        }
        return String.format(
                INSERT,
                dbModel.getTableName(),
                valueColumns) +
                String.format(VALUES, valueTemplate);
    }

    /**
     * The updateValuesQueriesSelector builds a query string for updating an existing value in the database
     *
     * @param dbModel The model specifying the table being updated and the attributes needed
     * @return Returns the query string for updating a value
     */
    protected static String updateValuesQueriesSelector(DBModels dbModel) {
        Map<String, String> values = neededValuesMap(dbModel);
        StringBuilder setValues = new StringBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String value = entry.getValue();
            setValues.append(value)
                     .append(" = ?, ");
        }
        setValues.append(String.format("%s = NOW(), %s = ?",
                                       dbModel.getAttributes()
                                              .get("lastUpdate"),
                                       dbModel.getAttributes()
                                              .get("lastUpdatedBy")));
        return String.format(
                UPDATE, dbModel.getTableName()) +
                String.format(SET, setValues) +
                WHERE + EQUALS_INTEGER;
    }

    /**
     * The neededValueMap removes unneeded attributes from the model when creating a query
     *
     * @param dbModel The model of the entity
     * @return Returns a map with unneeded attributes removed
     */
    private static Map<String, String> neededValuesMap(DBModels dbModel) {
        Map<String, String> values = new TreeMap<>(dbModel.getAttributes());
        values.remove("id");
        values.remove("createDate");
        values.remove("createdBy");
        values.remove("lastUpdate");
        values.remove("lastUpdatedBy");
        return values;
    }
}
