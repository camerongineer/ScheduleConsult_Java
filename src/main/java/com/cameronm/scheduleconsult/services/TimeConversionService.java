package com.cameronm.scheduleconsult.services;

import com.cameronm.scheduleconsult.Main;
import com.cameronm.scheduleconsult.settings.DatabaseConfig;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

/**
 * The TimeConversionService class is responsible for all the time conversion within the program
 *
 * @author Cameron M
 * @since 02-26-2023
 */
public interface TimeConversionService {

    /**
     * The ZoneId of the local machine
     */
    ZoneId LOCAL_ZONE_ID = ZoneId.systemDefault();

    /**
     * The ZoneId of that the server utilizes
     */
    ZoneId SERVER_ZONE_ID = DatabaseConfig.DB_ZONE_ID;

    /**
     * The ZoneId of the company for appointment scheduling purposes
     */
    ZoneId COMPANY_ZONE_ID = Main.COMPANY_ZONE_ID;

    /**
     * The days of the week that company is open for appointment scheduling purposes
     */
    HashSet<DayOfWeek> COMPANY_OPEN_DAYS = Main.COMPANY_OPEN_DAYS;

    /**
     * The time when the company opens for appointment scheduling purposes
     */
    LocalTime COMPANY_OPENING_TIME = Main.COMPANY_OPENING_TIME;

    /**
     * The time when the company closes for appointment scheduling purposes
     */
    LocalTime COMPANY_CLOSING_TIME = Main.COMPANY_CLOSING_TIME;

    /**
     * The intervals of time between available appointments
     */
    int APPOINTMENT_TIME_INTERVALS = Main.APPOINTMENT_TIME_INTERVALS;

    /**
     * The earliest date that an appointment can be changed to
     */
    LocalDate APPOINTMENT_EARLIEST_DATE = Main.APPOINTMENT_EARLIEST_DATE;

    /**
     * The format commonly used for hours and minutes
     */
    DateTimeFormatter HOURS_FORMAT = DateTimeFormatter.ofPattern("h:mma");

    /**
     * The format commonly used for months
     */
    DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMMM");

    /**
     * The format commonly used for months, days, and years
     */
    DateTimeFormatter MONTH_DAY_FORMAT = DateTimeFormatter.ofPattern("MMMM d, uuuu");

    /**
     * The format commonly used for months, days, years, and time with timezone
     */
    DateTimeFormatter MONTH_DATE_HOURS_TIMEZONE_FORMAT = DateTimeFormatter.ofPattern("MMMM d, uuuu - h:mm a (z)");

    /**
     * The displayDateTime method returns a string converter used to show time in a specific selection box
     *
     * @param format The format displaying the in the box
     * @param <T>    the type of time object
     * @return Returns the string converter showing the time format
     */
    static <T> StringConverter<T> displayedDateTime(DateTimeFormatter format) {
        return new StringConverter<>() {
            @Override
            public String toString(T dateTime) {
                if (dateTime == null) {
                    return null;
                } else if (dateTime instanceof LocalTime) {
                    return ((LocalTime) dateTime).format(format);
                } else if (dateTime instanceof LocalDate) {
                    return ((LocalDate) dateTime).format(format);
                }
                throw new IllegalArgumentException("Unsupported type: " + dateTime.getClass());
            }

            @Override
            public T fromString(String string) {
                if (string == null || string.trim()
                                            .isEmpty()) {
                    return null;
                } else if (LocalTime.parse(string, format)
                                    .toString()
                                    .equals(string)) {
                    return (T) LocalTime.parse(string, format);
                } else if (LocalDate.parse(string, format)
                                    .toString()
                                    .equals(string)) {
                    return (T) LocalDate.parse(string, format);
                }
                throw new IllegalArgumentException("Unsupported type: " + string);
            }
        };
    }

    /**
     * The cellDateTimeDisplay method returns a string representing a timestamp in a specified format
     *
     * @param timestamp The timestamp being displayed
     * @param format    The format the timestamp is being displayed in
     * @return Returns a simple string property for display in a table view
     */
    static SimpleStringProperty cellDateTimeDisplay(Timestamp timestamp, DateTimeFormatter format) {
        ZonedDateTime serverTimeDate = ZonedDateTime.of(timestamp.toLocalDateTime(), LOCAL_ZONE_ID);
        return new SimpleStringProperty(serverTimeDate.format(format));
    }

    /**
     * The getCurrentTimeInServerTime method returns a timestamp of the present moment in the timezone of the server
     *
     * @return Returns a timestamp of the present moment in the timezone where the server is located
     */
    static Timestamp getCurrentTimeInServerTime() {
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        return convertTime(currentTime, LOCAL_ZONE_ID, SERVER_ZONE_ID);
    }

    /**
     * The convertFromServerTime method converts a timestamp from the server timezone to the local timezone
     *
     * @param timestamp The timestamp in the server timezone
     * @return Returns a timestamp of the server timezone converted to the local timezone
     */
    static Timestamp convertFromServerTime(Timestamp timestamp) {
        return convertTime(timestamp, SERVER_ZONE_ID, LOCAL_ZONE_ID);
    }

    /**
     * The convertToServerTime method converts a timestamp from the local timezone to the server timezone
     *
     * @param timestamp The timestamp in the local timezone
     * @return Returns a timestamp of the local timezone converted to the server timezone
     */
    static Timestamp convertToServerTime(Timestamp timestamp) {
        return convertTime(timestamp, LOCAL_ZONE_ID, SERVER_ZONE_ID);
    }

    /**
     * The getConvertedZonedDateTime method returns a ZonedDateTime object converted from one timezone to another
     *
     * @param localDateTime The LocalDateTime object being converted to another timezone
     * @param originZoneId  The timezone of the original LocalDateTime object
     * @param outputZoneId  The timezone that the LocalDateTime object is converted to
     * @return Returns a ZonedDateTime object converted from one timezone to another
     */
    static ZonedDateTime getConvertedZonedDateTime(LocalDateTime localDateTime,
                                                   ZoneId originZoneId,
                                                   ZoneId outputZoneId) {
        Timestamp timestampConverted = convertTime(Timestamp.valueOf(localDateTime), originZoneId, outputZoneId);
        return ZonedDateTime.of(timestampConverted.toLocalDateTime(), outputZoneId);
    }

    /**
     * The convertTime method returns a timestamp converted from one timezone to another
     *
     * @param timeToConvert The timestamp being converted to another timezone
     * @param originZoneId  The timezone of the original timestamp
     * @param outputZoneId  The timezone that the timestamp is converted to
     * @return Returns a converted timestamp
     */
    static Timestamp convertTime(Timestamp timeToConvert, ZoneId originZoneId, ZoneId outputZoneId) {
        LocalDateTime localDateTime = timeToConvert.toLocalDateTime();
        long offsetHours = getHoursOffset(originZoneId, outputZoneId);
        LocalDateTime convertedLocalDateTime = localDateTime.plusHours(offsetHours);
        return Timestamp.valueOf(convertedLocalDateTime);
    }

    /**
     * The getHoursOffset returns the amount of hours between 2 timezones
     *
     * @param originZoneId   The timezone of origin
     * @param comparedZoneId The timezone of being compared
     * @return Returns a converted timestamp
     */
    static long getHoursOffset(ZoneId originZoneId, ZoneId comparedZoneId) {
        LocalDateTime truncatedOrigin = LocalDateTime.now(originZoneId)
                                                     .truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime truncatedOutput = LocalDateTime.now(comparedZoneId)
                                                     .truncatedTo(ChronoUnit.SECONDS);
        return ChronoUnit.HOURS.between(truncatedOrigin, truncatedOutput);
    }
}
