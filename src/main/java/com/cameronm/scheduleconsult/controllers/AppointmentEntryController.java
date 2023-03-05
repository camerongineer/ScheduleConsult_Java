package com.cameronm.scheduleconsult.controllers;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.Appointment;
import com.cameronm.scheduleconsult.models.Contact;
import com.cameronm.scheduleconsult.services.*;

import com.cameronm.scheduleconsult.utilities.InputValidator;
import com.cameronm.scheduleconsult.utilities.UIHelper;
import com.cameronm.scheduleconsult.views.AlertHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

/**
 * The AppointmentEntryController class is the controller class for the appointment entry screen
 *
 * @author Cameron M
 * @since 02-25-2023
 */
public class AppointmentEntryController implements Initializable, TimeConversionService, DBQueries {

    /**
     * The appointment passed to the entry screen
     */
    private Appointment appointment = null;

    /**
     * The appointment ID TextField
     */
    @FXML
    private TextField appointmentIdTextField;

    /**
     * The contact ComboBox
     */
    @FXML
    private ComboBox<Contact> contactComboBox;

    /**
     * The customer ID TextField
     */
    @FXML
    private TextField customerIdTextField;

    /**
     * The descriptionTextField
     */
    @FXML
    private TextField descriptionTextField;

    /**
     * The DatePicker to select the end date of the appointment
     */
    @FXML
    private DatePicker endDatePicker;

    /**
     * The ComboBox to select the end time of the appointment
     */
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;

    /**
     * The location TextField
     */
    @FXML
    private TextField locationTextField;

    /**
     * The DatePicker to select the start date of the appointment
     */
    @FXML
    private DatePicker startDatePicker;

    /**
     * The ComboBox to select the start time of the appointment
     */
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;

    /**
     * The title TextField
     */
    @FXML
    private TextField titleTextField;

    /**
     * The type TextField
     */
    @FXML
    private TextField typeTextField;

    /**
     * The user ID TextField
     */
    @FXML
    private TextField userIdTextField;

    /**
     * The cancel button
     */
    @FXML
    private Button cancelButton;

    /**
     * The save button
     */
    @FXML
    private Button saveButton;

    /**
     * The cancelButtonClicked method displays a popup prompt confirming if the window should be closed without saving
     * any changes
     */
    @FXML
    void cancelButtonClicked() {
        if (AlertHandler.confirmAction("Are you sure you want to cancel?", "")) {
            Stage stage = (Stage) cancelButton.getScene()
                                              .getWindow();
            stage.close();
        }
    }

    /**
     * The saveButtonClicked method displays a popup prompt confirming if the appointment information should be saved
     */
    @FXML
    void saveButtonClicked() {
        if (InputValidator.checkInvalidEntriesError(validateAll())) {
            if (AlertHandler.confirmAction("Are you sure you want to save?", "")) {
                saveAppointment();
                Stage stage = (Stage) saveButton.getScene()
                                                .getWindow();
                stage.close();
                AlertHandler.entityModified(DBModels.APPOINTMENTS, appointment, false);
            }
        }
    }

    /**
     * The saveAppointment method saves the information provided in fields of the appointment entry screen creating a
     * new appointment entity or updating an existing one
     */
    private void saveAppointment() {
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        Contact contact = contactComboBox.getValue();
        int contactId = contact.getId();
        String type = typeTextField.getText();
        int customerId = Integer.parseInt(customerIdTextField.getText());
        int userId = Integer.parseInt(userIdTextField.getText());
        LocalDate startDate = startDatePicker.getValue();
        LocalTime startTime = startTimeComboBox.getSelectionModel()
                                               .getSelectedItem();
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDate, startTime));
        startTimestamp = TimeConversionService.convertToServerTime(startTimestamp);
        LocalDate endDate = endDatePicker.getValue();
        LocalTime endTime = endTimeComboBox.getSelectionModel()
                                           .getSelectedItem();
        Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(endDate, endTime));
        endTimestamp = TimeConversionService.convertToServerTime(endTimestamp);
        if (appointment == null) {
            appointment = AppointmentQueryService.addAppointment(
                    new Appointment(-1,
                                    title,
                                    null,
                                    LoginController.getProgramUser()
                                                   .getName(),
                                    null,
                                    LoginController.getProgramUser()
                                                   .getName(),
                                    description,
                                    location,
                                    type,
                                    startTimestamp,
                                    endTimestamp,
                                    customerId,
                                    userId,
                                    contactId
                    ));
        } else {
            appointment.setName(title);
            appointment.setDescription(description);
            appointment.setLocation(location);
            appointment.setContactId(contactId);
            appointment.setType(type);
            appointment.setStart(startTimestamp);
            appointment.setEnd(endTimestamp);
            appointment.setCustomerId(customerId);
            appointment.setUserId(userId);
            appointment.setLastUpdatedBy(LoginController.getProgramUser()
                                                        .getName());
            AppointmentQueryService.modifyAppointment(appointment);
        }
    }

    /**
     * The initialize method initializes appointment entry screen
     *
     * @param url            The url passed into the initialize method
     * @param resourceBundle The resource bundle passed into the initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UIHelper.initStringTextField(titleTextField, 50);
        UIHelper.initStringTextField(descriptionTextField, 50);
        UIHelper.initStringTextField(locationTextField, 50);
        setContactComboBox();
        UIHelper.initStringTextField(typeTextField, 50);
        initDateTimePicker(startDatePicker, startTimeComboBox);
        initDateTimePicker(endDatePicker, endTimeComboBox);
        UIHelper.initIdTextField(customerIdTextField);
        UIHelper.initIdTextField(userIdTextField);
    }

    /**
     * The initAppointmentFields method populates the fields of appointment entry screen for modifications to an
     * existing appointment
     */
    protected void initAppointmentFields() {
        appointmentIdTextField.setText(String.valueOf(appointment.getId()));
        titleTextField.setText(appointment.getName());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        contactComboBox.getSelectionModel()
                       .select(ContactQueryService.getContacts(String.format(SELECT_ALL + WHERE + EQUALS_INTEGER,
                                                                             DBModels.CONTACTS.getTableName(),
                                                                             DBModels.CONTACTS.getAttributes()
                                                                                              .get("id"),
                                                                             appointment.getContactId()))
                                                  .get(0));
        typeTextField.setText(appointment.getType());
        startDatePicker.setValue(appointment.getStart()
                                            .toLocalDateTime()
                                            .toLocalDate());
        setValidTimes(startDatePicker, startTimeComboBox);
        startTimeComboBox.getSelectionModel()
                         .select(appointment.getStart()
                                            .toLocalDateTime()
                                            .toLocalTime());
        endDatePicker.setValue(appointment.getEnd()
                                          .toLocalDateTime()
                                          .toLocalDate());
        setValidTimes(endDatePicker, endTimeComboBox);
        endTimeComboBox.getSelectionModel()
                       .select(appointment.getEnd()
                                          .toLocalDateTime()
                                          .toLocalTime());
        customerIdTextField.setText(String.valueOf(appointment.getCustomerId()));
        userIdTextField.setText(String.valueOf(appointment.getUserId()));
    }

    /**
     * The setContactComboBox method populates the combo box with contacts the sets the combo box to display the name of
     * the contacts
     */
    private void setContactComboBox() {
        contactComboBox.setItems(ContactQueryService.getAllContacts());
        contactComboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        contactComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Contact contact) {
                if (contact == null) {
                    return null;
                } else {
                    return contact.getName();
                }
            }

            @Override
            public Contact fromString(String string) {
                return null;
            }
        });
    }

    /**
     * The initDateTimePicker method initializes the dates and times for the appointment
     *
     * @param datePicker   The date picker
     * @param timeComboBox The time combo box
     */
    private void initDateTimePicker(DatePicker datePicker, ComboBox<LocalTime> timeComboBox) {
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setOnAction(actionEvent -> setValidTimes(datePicker, timeComboBox));
        timeComboBox.setOnAction(actionEvent -> {
            Platform.runLater(() -> {
                LocalTime selectedTime = timeComboBox.getValue();
                if (selectedTime != null) {
                    timeComboBox.getSelectionModel()
                                .select(selectedTime);
                }
            });
        });
    }

    /**
     * The dayCellFactory method sets the rules for the days displayed in the appointment date pickers
     */
    Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
        @Override
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item.isBefore(APPOINTMENT_EARLIEST_DATE) || !COMPANY_OPEN_DAYS.contains(item.getDayOfWeek())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #d7758d;");
                    }
                }
            };
        }
    };

    /**
     * The setValidTimes method sets times displayed in the time combo boxes
     *
     * @param datePicker   The date picker displaying the dates
     * @param timeComboBox The combo box displaying the times
     */
    private void setValidTimes(DatePicker datePicker, ComboBox<LocalTime> timeComboBox) {
        if (datePicker.getValue() == null) {
            return;
        }
        LocalTime previousTime = timeComboBox.getValue();
        ZonedDateTime validLocalTimeZoneStart = TimeConversionService
                .getConvertedZonedDateTime(
                        LocalDateTime.of(
                                datePicker.getValue(),
                                TimeConversionService.COMPANY_OPENING_TIME),
                        COMPANY_ZONE_ID,
                        LOCAL_ZONE_ID);
        ZonedDateTime validLocalTimeZoneEnd = TimeConversionService
                .getConvertedZonedDateTime(
                        LocalDateTime.of(
                                datePicker.getValue(),
                                TimeConversionService.COMPANY_CLOSING_TIME),
                        COMPANY_ZONE_ID,
                        LOCAL_ZONE_ID);
        ZonedDateTime displayTime = validLocalTimeZoneStart;
        timeComboBox.getItems()
                    .clear();

        while (displayTime.isBefore(validLocalTimeZoneEnd)) {
            timeComboBox.getItems()
                        .add(displayTime.toLocalTime());
            displayTime = displayTime.plusMinutes(APPOINTMENT_TIME_INTERVALS);
        }
        if (previousTime != null) {
            timeComboBox.getSelectionModel()
                        .select(previousTime);
        } else {
            timeComboBox.getSelectionModel()
                        .selectFirst();
        }
        datePicker.setConverter(TimeConversionService.displayedDateTime(MONTH_DAY_FORMAT));
        timeComboBox.setConverter(TimeConversionService.displayedDateTime(HOURS_FORMAT));
    }

    /**
     * The startDatePickerSelected method sets the times of the start time combo box when the start date picker is
     * selected
     */
    @FXML
    void startDatePickerSelected() {
        setValidTimes(startDatePicker, startTimeComboBox);
    }

    /**
     * The startTimeComboBoxSelected method shifts the day of the start or end date when a start date later than the end
     * date is selected
     *
     * @param actionEvent The action event
     */
    @FXML
    void startTimeComboBoxSelected(ActionEvent actionEvent) {
        setStartDateTimeBeforeEnd(actionEvent);
    }

    /**
     * The endDatePickerSelected method sets the times of the end time combo box when the end date picker is selected
     */
    @FXML
    void endDatePickerSelected() {
        setValidTimes(endDatePicker, endTimeComboBox);
    }

    /**
     * The endTimeComboBoxSelected method shifts the day of the start or end date when a start date later than the end
     * date is selected
     *
     * @param actionEvent The action event
     */
    @FXML
    void endTimeComboBoxSelected(ActionEvent actionEvent) {
        setStartDateTimeBeforeEnd(actionEvent);
    }

    /**
     * The setStartDateTimeBeforeEnd method shifts the day of the start or end date when a start date later than the end
     * date is selected
     *
     * @param actionEvent The action event
     */
    private void setStartDateTimeBeforeEnd(ActionEvent actionEvent) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            return;
        }

        if (endDate.isBefore(startDate)) {
            if (actionEvent.getSource()
                           .equals(endTimeComboBox)) {
                startDatePicker.setValue(endDate);
            } else {
                endDatePicker.setValue(startDate);
            }
        }

        LocalTime startTime = startTimeComboBox.getSelectionModel()
                                               .getSelectedItem();
        LocalTime endTime = endTimeComboBox.getSelectionModel()
                                           .getSelectedItem();

        if (startTime == null || endTime == null) {
            return;
        }

        if (!startTimeComboBox.getItems()
                              .isEmpty() && startDatePicker.getValue()
                                                           .equals(endDatePicker.getValue())) {
            if (LocalDateTime.of(endDate, endTime)
                             .isBefore(LocalDateTime.of(startDate, startTime))) {
                if (actionEvent.getSource()
                               .equals(endTimeComboBox)) {
                    startTimeComboBox.getSelectionModel()
                                     .select(endTimeComboBox.getSelectionModel()
                                                            .getSelectedIndex());
                } else {
                    endTimeComboBox.getSelectionModel()
                                   .select(startTimeComboBox.getSelectionModel()
                                                            .getSelectedIndex());
                }
            }
        }
    }

    /**
     * The setAppointment method sets the appointment
     *
     * @param appointment The appointment to be set
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * The validateAll method validates all the fields and entities selected on the appointment entry screen
     *
     * @return Returns a list of exceptions to be displayed in a popup
     */
    private List<Exception> validateAll() {
        List<Exception> allErrors = new ArrayList<>(List.copyOf(validateFields()));
        allErrors.addAll(validateEntities());
        return allErrors;
    }

    /**
     * The validateFields method validates all the fields of the appointment entry screen
     *
     * @return Returns a list of exceptions to be displayed in a popup
     */
    private List<Exception> validateFields() {
        List<Exception> exceptions = new ArrayList<>();
        exceptions.add(InputValidator.checkValidInput("Title",
                                                      titleTextField.getText(),
                                                      50,
                                                      false));
        exceptions.add(InputValidator.checkValidInput("Description",
                                                      descriptionTextField.getText(),
                                                      50,
                                                      false));
        exceptions.add(InputValidator.checkValidInput("Location",
                                                      locationTextField.getText(),
                                                      50,
                                                      false));
        exceptions.add(InputValidator.checkEntitySelected("Contact",
                                                          contactComboBox.getValue()));
        exceptions.add(InputValidator.checkValidInput("Type",
                                                      typeTextField.getText(),
                                                      50,
                                                      false));
        exceptions.add(InputValidator.checkValidDateRange(
                startDatePicker.getValue(),
                startTimeComboBox.getValue(),
                endDatePicker.getValue(),
                endTimeComboBox.getValue(),
                appointmentIdTextField.getText()));
        exceptions.add(InputValidator.checkValidInput("Customer ID",
                                                      customerIdTextField.getText(),
                                                      10, true));
        exceptions.add(InputValidator.checkValidInput("User ID",
                                                      userIdTextField.getText(),
                                                      10, true));
        exceptions.removeIf(Objects::isNull);
        return exceptions;
    }

    /**
     * The validateEntities method validates entities selected on the appointment entry screen
     *
     * @return Returns a list of exceptions to be displayed in a popup
     */
    private List<Exception> validateEntities() {
        List<Exception> exceptions = new ArrayList<>();
        if (InputValidator.isValidEntityId(customerIdTextField.getText())) {
            int customerId = Integer.parseInt(customerIdTextField.getText()
                                                                 .trim());
            if (CustomerQueryService.retrieveCustomerById(customerId) == null) {
                exceptions.add(new InstantiationException("No customer with this ID exists in the database"));
            }
        }
        if (InputValidator.isValidEntityId(userIdTextField.getText())) {
            int userId = Integer.parseInt(userIdTextField.getText()
                                                         .trim());
            if (UserQueryService.retrieveUserById(userId) == null) {
                exceptions.add(new InstantiationException("No user with this ID exists in the database"));
            }
        }
        return exceptions;
    }
}
