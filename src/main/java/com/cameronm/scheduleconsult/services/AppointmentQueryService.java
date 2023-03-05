package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.DAO.DBConnection;
import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.Appointment;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The AppointmentQueryService class is responsible for converting entries in the appointments table of the database
 * into Appointment instances
 *
 * @author Cameron M
 * @since 02-22-2023
 */
public abstract class AppointmentQueryService extends QueryService implements DBQueries {

    public static final String MODIFY_APPOINTMENT_QUERY = updateValuesQueriesSelector(DBModels.APPOINTMENTS);
    public static final String ADD_APPOINTMENT_QUERY = insertValuesQueriesSelector(DBModels.APPOINTMENTS);
    public static final Map<String, String> ATTRIBUTES = DBModels.APPOINTMENTS.getAttributes();
    public static final String TABLE_NAME = DBModels.APPOINTMENTS.getTableName();

    /**
     * The getAllAppointments method returns an ObservableList of all appointments in the database
     *
     * @return Returns an ObservableList of all appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return getAppointments(SELECT_ALL);
    }

    /**
     * The getAppointments method returns an ObservableList of specific appointments in the database
     *
     * @param sqlQuery The query specifying the appointments to return
     * @return Returns an ObservableList of appointments specified by the query
     */
    public static ObservableList<Appointment> getAppointments(String sqlQuery) {
        return QueryService.getEntities(DBModels.APPOINTMENTS, sqlQuery);
    }

    /**
     * The getAppointment method takes a result set and previously retrieved common attributes and retrieves all the
     * rest of the remaining appointment attributes and returns the appointment
     *
     * @param results          The results set passed in from an entry in the appointments table
     * @param entityAttributes Common attribute types that are shared by all audited entities
     * @return Returns an appointment
     * @throws SQLException Throws SQLException if the ResultSet is invalid
     */
    static Appointment getAppointment(ResultSet results, HashMap<String, String> entityAttributes) throws SQLException {
        entityAttributes.put("description", results.getString(ATTRIBUTES.get("description")));
        entityAttributes.put("location", results.getString(ATTRIBUTES.get("location")));
        entityAttributes.put("type", results.getString(ATTRIBUTES.get("type")));
        entityAttributes.put("start", results.getString(ATTRIBUTES.get("start")));
        entityAttributes.put("end", results.getString(ATTRIBUTES.get("end")));
        entityAttributes.put("customerId", results.getString(ATTRIBUTES.get("customerId")));
        entityAttributes.put("userId", results.getString(ATTRIBUTES.get("userId")));
        entityAttributes.put("contactId", results.getString(ATTRIBUTES.get("contactId")));
        String description = entityAttributes.get("description");
        String location = entityAttributes.get("location");
        String type = entityAttributes.get("type");
        Timestamp start = TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("start")));
        Timestamp end = TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("end")));
        int customerId = Integer.parseInt(entityAttributes.get("customerId"));
        int userId = Integer.parseInt(entityAttributes.get("userId"));
        int contactId = Integer.parseInt(entityAttributes.get("contactId"));
        return new Appointment(
                Integer.parseInt(entityAttributes.get("id")),
                entityAttributes.get("name"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("createDate"))),
                entityAttributes.get("createdBy"),
                TimeConversionService.convertFromServerTime(Timestamp.valueOf(entityAttributes.get("lastUpdate"))),
                entityAttributes.get("lastUpdatedBy"),
                description,
                location,
                type,
                start,
                end,
                customerId,
                userId,
                contactId
        );
    }

    /**
     * The getAppointmentById method returns an appointment specified by the ID
     *
     * @param appointmentId The ID of the appointment
     * @return returns an appointment entity
     */
    private static Appointment getAppointmentById(int appointmentId) {
        return QueryService.getEntityById(DBModels.APPOINTMENTS, appointmentId);
    }

    /**
     * The deleteAppointment method deletes an appointment from the appointments table in the database
     *
     * @param appointmentToBeDeleted The appointment being deleted
     */
    public static void deleteAppointment(Appointment appointmentToBeDeleted) {
        QueryService.deleteEntityById(DBModels.APPOINTMENTS, appointmentToBeDeleted.getId());
    }

    /**
     * The addAppointment method adds an appointment to the appointments table in the database
     *
     * @param appointment The appointment being added to the database
     * @return Returns the added appointment
     */
    public static Appointment addAppointment(Appointment appointment) {
        try (PreparedStatement statement = DBConnection.getConnection()
                                                       .prepareStatement(ADD_APPOINTMENT_QUERY,
                                                                         Statement.RETURN_GENERATED_KEYS)) {
            setAppointmentStatement(statement, appointment, true);
            statement.execute();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return getAppointmentById(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * The modifyAppointment method modifies an existing appointment in the appointments table of the database
     *
     * @param appointment The appointment being modified in the database
     */
    public static void modifyAppointment(Appointment appointment) {
        try (PreparedStatement statement = DBConnection.getConnection()
                                                       .prepareStatement(
                                                               String.format(MODIFY_APPOINTMENT_QUERY,
                                                                             ATTRIBUTES.get("id"),
                                                                             appointment.getId()))) {
            setAppointmentStatement(statement, appointment, false);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The setAppointmentStatement method sets the PreparedStatement with the values of the appointment
     *
     * @param statement    The statement being set
     * @param appointment  The appointment whose values are being set
     * @param setCreatedBy Boolean specifying if the created_by field is being set
     * @throws SQLException Throws SQLException in the event of an error
     */
    private static void setAppointmentStatement(PreparedStatement statement,
                                                Appointment appointment,
                                                boolean setCreatedBy) throws SQLException {
        statement.setInt(1, appointment.getContactId());
        statement.setInt(2, appointment.getCustomerId());
        statement.setString(3, appointment.getDescription());
        statement.setTimestamp(4, appointment.getEnd());
        statement.setString(5, appointment.getLocation());
        statement.setString(6, appointment.getName());
        statement.setTimestamp(7, appointment.getStart());
        statement.setString(8, appointment.getType());
        statement.setInt(9, appointment.getUserId());
        if (setCreatedBy) {
            statement.setString(10, appointment.getCreatedBy());
        }
        statement.setString(setCreatedBy ? 11 : 10, appointment.getLastUpdatedBy());
    }

    /**
     * The getAppointmentsInRange method returns a list of appointments in a specific range
     *
     * @param startDate The start date of the range
     * @param endDate   The end date of the range
     * @return Returns a list of appointments in the range
     */
    public static ObservableList<Appointment> getAppointmentsInRange(LocalDate startDate, LocalDate endDate) {
        return getAppointmentsInRange(startDate, LocalTime.MIN, endDate, LocalTime.MAX);
    }

    /**
     * The getAppointmentsInRange method returns a list of appointments in a specific range
     *
     * @param startDate The start date of the range
     * @param startTime The start time of the range
     * @param endDate   The end date of the range
     * @param endTime   The end time of the range
     * @return Returns a list of appointments in the range
     */
    public static ObservableList<Appointment> getAppointmentsInRange(LocalDate startDate, LocalTime startTime,
                                                                     LocalDate endDate, LocalTime endTime) {
        Timestamp start = Timestamp.valueOf(LocalDateTime.of(startDate, startTime));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(endDate, endTime));
        return getAppointmentsInRange(start, end);
    }

    /**
     * The getAppointmentsInRange method returns a list of appointments in a specific range
     *
     * @param start The start timestamp of the range
     * @param end   The end timestamp of the range
     * @return Returns a list of appointments in the range
     */
    public static ObservableList<Appointment> getAppointmentsInRange(Timestamp start, Timestamp end) {
        String sqlQuery = SELECT_ALL +
                WHERE +
                String.format(
                        TIME_RANGE,
                        ATTRIBUTES.get("start"), start,
                        ATTRIBUTES.get("start"), end);
        return getAppointments(sqlQuery);
    }

    /**
     * The getMinMaxAppointmentRange returns a map with the earliest and latest appointment in the database
     *
     * @return Returns a map with the earliest and latest appointment
     */
    public static HashMap<String, Timestamp> getMinMaxAppointmentRange() {
        String sqlQuery = String.format("SELECT MIN(%s) AS min, MAX(%s) AS max FROM %s;",
                                        ATTRIBUTES.get("start"),
                                        ATTRIBUTES.get("end"),
                                        TABLE_NAME);
        HashMap<String, Timestamp> minMaxAppointments = new HashMap<>();
        try (PreparedStatement statement = DBConnection.getConnection()
                                                       .prepareStatement(sqlQuery);
             ResultSet resultSet = statement.executeQuery(sqlQuery)
        ) {
            if (resultSet.next()) {
                minMaxAppointments.put("start", Timestamp.valueOf(resultSet.getString("min")));
                minMaxAppointments.put("end", Timestamp.valueOf(resultSet.getString("max")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return minMaxAppointments;
    }

    /**
     * The appointmentExistsInRange method returns a boolean specifying if there is an existing appointment within a
     * range
     *
     * @param startDate             The start date of the range
     * @param startTime             The start time of the range
     * @param endDate               The end date of the range
     * @param endTime               The end time of the range
     * @param modifiedAppointmentId The ID of the appointment that is being modified
     * @return Returns a boolean specifying if there is an appointment within the range
     */
    public static boolean appointmentExistsInRange(LocalDate startDate,
                                                   LocalTime startTime,
                                                   LocalDate endDate,
                                                   LocalTime endTime,
                                                   String modifiedAppointmentId) {
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, startTime));
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, endTime));
        Timestamp convertedStart = TimeConversionService.convertToServerTime(startTimestamp);
        Timestamp convertedEnd = TimeConversionService.convertToServerTime(endTimestamp);
        String sqlQuery = String.format(SELECT_ALL, TABLE_NAME) +
                WHERE + "(" +
                String.format(NOT_IN_TIME_RANGE,
                              ATTRIBUTES.get("start"), convertedEnd,
                              ATTRIBUTES.get("end"), convertedStart) + ")";
        if (!modifiedAppointmentId.isEmpty()) {
            sqlQuery += (AND + String.format(NOT_EQUAL_INTEGER, ATTRIBUTES.get("id"), modifiedAppointmentId));
        }
        return queryHasMatches(sqlQuery);
    }

    /**
     * The customerAppointmentReport returns a string of the total number of customer appointments by type and month
     *
     * @return Returns a string of the total number of customer appointments by type and month
     */
    public static String customerAppointmentReport() {
        String sqlQuery = "SELECT MONTHNAME(ANY_VALUE(start)) as Month," +
                " YEAR(ANY_VALUE(start)) as Year," +
                " Type, COUNT(Customer_ID) AS Total " +
                " FROM appointments GROUP BY YEAR(start), MONTH(start)," +
                " Type ORDER BY Total DESC, year(start), month(start);";
        try (ResultSet rs = QueryService.getResultsSet(sqlQuery)) {
            StringBuilder report = new StringBuilder();
            while (rs.next()) {
                String month = rs.getString("Month");
                String year = rs.getString("Year");
                String type = rs.getString("Type");
                int total = rs.getInt("Total");
                String output = String.format("%-4s %-11s %-26s %4d", year, month, type, total);
                report.append(output)
                      .append("\n");
            }
            return report.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
