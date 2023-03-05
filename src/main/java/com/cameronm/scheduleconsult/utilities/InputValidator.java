package com.cameronm.scheduleconsult.utilities;

import com.cameronm.scheduleconsult.models.NamedEntity;
import com.cameronm.scheduleconsult.services.AppointmentQueryService;
import com.cameronm.scheduleconsult.services.TimeConversionService;
import com.cameronm.scheduleconsult.views.AlertHandler;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * The InputValidator class contains methods related to validating user input
 *
 * @author Cameron M
 * @since 02-27-2023
 */
abstract public class InputValidator implements TimeConversionService {

    /**
     * The regex for a valid ID
     */
    public static final String VALID_ID_REGEX = "^[0-9]{1,10}$";

    /**
     * The isValidEntityId method returns a boolean specifying if a string is a valid ID integer value
     *
     * @param idString The input string of the ID
     * @return Returns a boolean specifying if the input string is a valid ID integer value
     */
    public static boolean isValidEntityId(String idString) {
        if (idString.trim()
                    .matches(InputValidator.VALID_ID_REGEX)) {
            return Integer.parseInt(idString.trim()) >= 0;
        }
        return false;
    }

    /**
     * The validIdInputListener method return a listener which sets rules for what can be typed into an ID text field
     *
     * @param field     The text field
     * @param maxLength The maximum length of the text field
     * @return Returns a changeListener enforcing the rules of the text field
     */
    public static ChangeListener<String> validIdInputListener(TextField field, int maxLength) {
        return (observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            try {
                Integer.parseInt(newValue);
                if (newValue.length() > maxLength) {
                    field.setText(oldValue);
                    throw new NumberFormatException();
                } else if (newValue.matches("-\\d+")) {
                    field.setText(String.valueOf(Math.abs(Integer.parseInt(newValue))));
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException numberFormatException) {
                field.setText(oldValue);
                Toolkit.getDefaultToolkit()
                       .beep();
            }
        };
    }

    /**
     * The validStringInputListener method return a listener which sets rules for what can be typed into a string text
     * field
     *
     * @param field     The text field
     * @param maxLength The maximum length of the text field
     * @return Returns a changeListener enforcing the rules of the text field
     */
    public static ChangeListener<String> validStringInputListener(TextField field, int maxLength) {
        return (observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }
            if (newValue.length() > maxLength) {
                newValue = newValue.substring(0, maxLength);
                field.setText(newValue);
                if (oldValue.length() == newValue.length()) {
                    Toolkit.getDefaultToolkit()
                           .beep();
                }
            }
        };
    }

    /**
     * The checkValidInput method returns an exception if the input into a text field is invalid
     *
     * @param fieldName  The name of the field
     * @param input      The input typed into the field
     * @param maxSize    The maximum size of the input string
     * @param isIntInput Boolean specifying if the text field expects an integer value
     * @return Returns an exception if input is invalid
     */
    public static Exception checkValidInput(String fieldName, String input, int maxSize, boolean isIntInput) {
        if (input.isBlank() || input.length() > maxSize) {
            return new IllegalArgumentException(fieldName);
        }
        if (isIntInput) {
            try {
                int integer = Integer.parseInt(input);
                if (integer < 0) {
                    return new IllegalArgumentException(fieldName);
                }
            } catch (NumberFormatException nfe) {
                return new IllegalArgumentException(fieldName);
            }
        }
        return null;
    }

    /**
     * The checkValidDateRange returns an exception if any time values are invalid
     *
     * @param startDate             The start date that was entered
     * @param startTime             The start time that was entered
     * @param endDate               The end date that was entered
     * @param endTime               The end time that was entered
     * @param modifiedAppointmentId The ID of the appointment
     * @return Returns an exception with a message specifying the error
     */
    public static Exception checkValidDateRange(LocalDate startDate,
                                                LocalTime startTime,
                                                LocalDate endDate,
                                                LocalTime endTime,
                                                String modifiedAppointmentId) {
        if (startDate == null || startTime == null) {
            return new IllegalArgumentException("Start Date/Time");
        }
        if (endDate == null || endTime == null) {
            return new IllegalArgumentException("End Date/Time");
        }
        ZonedDateTime startInLocalTimezone = ZonedDateTime.of(startDate, startTime, LOCAL_ZONE_ID);
        ZonedDateTime endInLocalTimezone = ZonedDateTime.of(endDate, endTime, LOCAL_ZONE_ID);
        if (!startInLocalTimezone.isBefore(endInLocalTimezone)) {
            return new DateTimeException("Start Date/Time must be before End Date/Time");
        }
        ZonedDateTime startInCompanyTimezone = startInLocalTimezone.withZoneSameInstant(COMPANY_ZONE_ID);
        ZonedDateTime endInCompanyTimezone = endInLocalTimezone.withZoneSameInstant(COMPANY_ZONE_ID);
        ZonedDateTime companyOpeningStart = ZonedDateTime.of(startDate, COMPANY_OPENING_TIME, COMPANY_ZONE_ID);
        ZonedDateTime companyClosingStart = ZonedDateTime.of(startDate, COMPANY_CLOSING_TIME, COMPANY_ZONE_ID);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma(z)");
        String validStartTime;
        String validEndTime;
        if (!LOCAL_ZONE_ID.getId()
                          .equals(COMPANY_ZONE_ID.getId())) {
            ZonedDateTime companyOpeningInLocalTime = companyOpeningStart.withZoneSameInstant(LOCAL_ZONE_ID);
            ZonedDateTime companyClosingInLocalTime = companyClosingStart.withZoneSameInstant(LOCAL_ZONE_ID);
            boolean isOverNight = companyClosingInLocalTime.toLocalDate()
                                                           .isAfter(companyOpeningInLocalTime.toLocalDate());
            validStartTime =
                    (companyOpeningInLocalTime.format(formatter) + " / " + companyOpeningStart.format(formatter));
            validEndTime = (companyClosingInLocalTime.format(formatter) +
                    (isOverNight ? " Next Day " : "") + " / " + companyClosingStart.format(formatter));
        } else {
            validStartTime = companyOpeningStart.format(formatter)
                                                .substring(0,
                                                           companyOpeningStart.format(formatter)
                                                                              .length() - 5);
            validEndTime = companyClosingStart.format(formatter)
                                              .substring(0,
                                                         companyClosingStart.format(formatter)
                                                                            .length() - 5);
        }
        if (!startInCompanyTimezone.toLocalDate()
                                   .equals(endInCompanyTimezone.toLocalDate()) ||
                startInCompanyTimezone.toLocalTime()
                                      .isBefore(companyOpeningStart.toLocalTime()) ||
                endInCompanyTimezone.toLocalTime()
                                    .isAfter(companyClosingStart.toLocalTime())) {
            return new DateTimeException(String.format(
                    "The appointment start date and end date must occur on the same business day between %s to %s",
                    validStartTime, validEndTime));
        }
        if (AppointmentQueryService.appointmentExistsInRange(
                startDate,
                startTime,
                endDate,
                endTime,
                modifiedAppointmentId)) {
            return new IllegalStateException("An appointment already exist during this time");
        }
        return null;
    }

    /**
     * The checkEntitySelected method checks if the entity passed in is null
     *
     * @param fieldName The name of the field that the entity was chosen from
     * @param entity    The entity passed into the method
     * @param <T>       The type of entity which must extend NameEntity
     * @return Returns an exception if the entity passed it is null
     */
    public static <T extends NamedEntity> Exception checkEntitySelected(String fieldName, T entity) {
        if (entity == null) {
            return new IllegalArgumentException(fieldName);
        }
        return null;
    }

    /**
     * The invalidEntriesError method takes a list of exceptions and displays all the errors in a popup message
     *
     * @param exceptionList The list containing the exceptions
     * @return Return a boolean specifying if errors exist in the list
     */
    public static boolean checkInvalidEntriesError(List<Exception> exceptionList) {
        exceptionList.removeIf(Objects::isNull);
        if (exceptionList.isEmpty()) {
            return true;
        }
        List<Exception> illegalArgumentExceptions = exceptionList
                .stream()
                .filter(e -> e instanceof IllegalArgumentException)
                .toList();
        List<Exception> dateTimeExceptions = exceptionList
                .stream()
                .filter(e -> e instanceof DateTimeException)
                .toList();
        List<Exception> instantiationExceptions = exceptionList
                .stream()
                .filter(e -> e instanceof InstantiationException)
                .toList();
        List<Exception> illegalStateExceptions = exceptionList
                .stream()
                .filter(e -> e instanceof IllegalStateException)
                .toList();
        StringBuilder message = new StringBuilder();
        if (!illegalArgumentExceptions.isEmpty()) {
            message.append("Invalid inputs appear in the following fields: ");
            for (Exception exception : illegalArgumentExceptions) {
                message.append(exception.getMessage())
                       .append(", ");
            }
            message.replace(message.length() - 2, message.length(), "\n");
        }
        if (!dateTimeExceptions.isEmpty()) {
            for (Exception exception : dateTimeExceptions) {
                message.append(exception.getMessage())
                       .append("\n");
            }
        }
        if (!instantiationExceptions.isEmpty()) {
            for (Exception exception : instantiationExceptions) {
                message.append(exception.getMessage())
                       .append("\n");
            }
        }
        if (!illegalStateExceptions.isEmpty()) {
            for (Exception exception : illegalStateExceptions) {
                message.append(exception.getMessage())
                       .append("\n");
            }
        }
        AlertHandler.popupExpandablePrompt(Alert.AlertType.ERROR,
                                           "ERROR: Invalid Entries",
                                           "One or more errors occurred:",
                                           message.toString());
        return false;
    }
}
