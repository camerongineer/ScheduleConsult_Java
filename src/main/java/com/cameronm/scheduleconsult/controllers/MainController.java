package com.cameronm.scheduleconsult.controllers;

import com.cameronm.scheduleconsult.DAO.DBConnection;
import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.Main;
import com.cameronm.scheduleconsult.models.Appointment;
import com.cameronm.scheduleconsult.models.Customer;
import com.cameronm.scheduleconsult.services.*;
import com.cameronm.scheduleconsult.utilities.ScreenLoader;
import com.cameronm.scheduleconsult.utilities.UIHelper;
import com.cameronm.scheduleconsult.views.AlertHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The MainController class is the controller class for the main screen
 *
 * @author Cameron M
 * @since 02-23-2023
 */
public class MainController implements Initializable, DBQueries, TimeConversionService {

    /**
     * The main tab pane
     */
    @FXML
    private TabPane mainTabPane;

    /**
     * The reports menu item
     */
    @FXML
    private MenuItem reportsMenuItem;

    /**
     * The quit menu item
     */
    @FXML
    private MenuItem quitMenuItem;

    /**
     * The log-out menu item
     */
    @FXML
    private MenuItem logOutMenuItem;

    /**
     * The about menu item
     */
    @FXML
    private MenuItem aboutMenuItem;

    /**
     * The label for the appointment filter intervals
     */
    @FXML
    private Label appointmentFilterIntervalLabel;

    /**
     * The label for the appointment filter years
     */
    @FXML
    private Label appointmentFilterYearLabel;

    /**
     * The appointment filter interval combo box
     */
    @FXML
    private ComboBox<LocalDate> appointmentFilterIntervalComboBox;

    /**
     * The appointment filter year combo box
     */
    @FXML
    private ComboBox<Year> appointmentFilterYearComboBox;

    /**
     * The appointment filter month radio button
     */
    @FXML
    private RadioButton appointmentFilterMonthRadioButton;

    /**
     * The appointment filter week radio button
     */
    @FXML
    private RadioButton appointmentFilterWeekRadioButton;

    /**
     * The appointment show-all radio button
     */
    @FXML
    private RadioButton appointmentFilterShowAllRadioButton;

    /**
     * The appointment filter toggle group
     */
    @FXML
    private ToggleGroup appointmentFilterToggleGroup;

    /**
     * The appointment table view
     */
    @FXML
    private TableView<Appointment> appointmentTableView;

    /**
     * The column for the appointment table displaying the appointment ID
     */
    @FXML
    private TableColumn<Appointment, Integer> idAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the title
     */
    @FXML
    private TableColumn<Appointment, String> titleAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the description
     */
    @FXML
    private TableColumn<Appointment, String> descriptionAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the location
     */
    @FXML
    private TableColumn<Appointment, String> locationAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the contact name
     */
    @FXML
    private TableColumn<Appointment, String> contactAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the type
     */
    @FXML
    private TableColumn<Appointment, String> typeAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the start date and time
     */
    @FXML
    private TableColumn<Appointment, String> startAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the end date and time
     */
    @FXML
    private TableColumn<Appointment, String> endAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the customer ID
     */
    @FXML
    private TableColumn<Customer, Integer> customerIdAppointmentTableColumn;

    /**
     * The column for the appointment table displaying the user ID
     */
    @FXML
    private TableColumn<Appointment, Integer> userIdAppointmentTableColumn;

    /**
     * The button for creating a new appointment
     */
    @FXML
    private Button createNewAppointmentButton;

    /**
     * The button for modifying an existing appointment
     */
    @FXML
    private Button modifyAppointmentButton;

    /**
     * The button for deleting an existing appointment
     */
    @FXML
    private Button deleteAppointmentButton;

    /**
     * The customer table view
     */
    @FXML
    private TableView<Customer> customerTableView;

    /**
     * The column for the customer table displaying the customer ID
     */
    @FXML
    private TableColumn<Customer, Integer> idCustomerTableColumn;

    /**
     * The column for the customer table displaying the name
     */
    @FXML
    private TableColumn<Customer, String> nameCustomerTableColumn;

    /**
     * The column for the customer table displaying the address
     */
    @FXML
    private TableColumn<Customer, String> addressCustomerTableColumn;

    /**
     * The column for the customer table displaying the name of the first-level division
     */
    @FXML
    private TableColumn<Customer, String> divisionCustomerTableColumn;

    /**
     * The column for the customer table displaying the postal code
     */
    @FXML
    private TableColumn<Customer, String> postalCodeCustomerTableColumn;

    /**
     * The column for the customer table displaying the phone number
     */
    @FXML
    private TableColumn<Customer, String> phoneCustomerTableColumn;

    /**
     * The button for adding a new customer
     */
    @FXML
    private Button addCustomerButton;

    /**
     * The button for modifying an existing customer
     */
    @FXML
    private Button modifyCustomerButton;

    /**
     * The button for deleting an existing customer
     */
    @FXML
    private Button deleteCustomerButton;

    /**
     * The label displaying the name of the user that is logged in
     */
    @FXML
    private Label loggedInUserLabel;

    /**
     * The label displaying the status of the server connection
     */
    @FXML
    private Label serverConnectionStatusLabel;

    /**
     * The initialize method initializes main screen
     *
     * @param url            The url passed into the initialize method
     * @param resourceBundle The resource bundle passed into the initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLabels();
        setAppointmentTable(filterAppointments());
        mainTabPane.getSelectionModel()
                   .selectedItemProperty()
                   .addListener((observable, oldTab, newTab) -> {
                       if (newTab.getText()
                                 .equals("Appointments")) {
                           setAppointmentTable(filterAppointments());
                       } else if (newTab.getText()
                                        .equals("Customers")) {
                           setCustomerTable(CustomerQueryService.getAllCustomers());
                       }
                   });
        createNewAppointmentButton.setOnAction(actionEvent -> loadAppointmentScreen(false));
        modifyAppointmentButton.setOnAction(actionEvent -> loadAppointmentScreen(true));
        deleteAppointmentButton.setOnAction(actionEvent -> deleteAppointment());
        appointmentFilterToggleGroup.selectedToggleProperty()
                                    .addListener((observable, oldToggle, newToggle) -> {
                                        if (newToggle != null) {
                                            setAppointmentFilterComboBoxVisibility();
                                            appointmentFilterIntervalComboBox.getItems()
                                                                             .clear();
                                            setAppointmentTable(filterAppointments());
                                        }
                                    });
        appointmentFilterYearComboBox.setOnAction(actionEvent -> {
            appointmentFilterIntervalComboBox.getItems()
                                             .clear();
            setAppointmentTable(filterAppointments());
        });
        appointmentFilterIntervalComboBox.setOnAction(actionEvent -> setAppointmentTable(filterAppointments()));
        reportsMenuItem.setOnAction(actionEvent -> loadReportsScreen());
        logOutMenuItem.setOnAction(actionEvent -> logOut());
        quitMenuItem.setOnAction(actionEvent -> UIHelper.closeProgram());
        aboutMenuItem.setOnAction(actionEvent -> AlertHandler.aboutPopup());
        addCustomerButton.setOnAction(actionEvent -> loadCustomerScreen(false));
        modifyCustomerButton.setOnAction(actionEvent -> loadCustomerScreen(true));
        deleteCustomerButton.setOnAction(actionEvent -> deleteCustomer());
        UIHelper.addDoubleClickHandler(appointmentTableView, () -> loadAppointmentScreen(true));
        UIHelper.addDoubleClickHandler(customerTableView, () -> loadCustomerScreen(true));
        appointmentReminder();
    }

    /**
     * The setLabels method sets the label in for the main screen
     */
    private void setLabels() {
        boolean isConnected = false;
        String username = LoginController.getProgramUser()
                                         .getName();
        try {
            isConnected = !DBConnection.getConnection()
                                       .isClosed();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        serverConnectionStatusLabel.setText("DATABASE: " + (isConnected ? "connected" : "disconnected"));
        serverConnectionStatusLabel.setTextFill(Color.valueOf(isConnected ? "#3cdd33" : "#f61a1a"));
        loggedInUserLabel.setText("USER: " + username);
        loggedInUserLabel.setTextFill(Color.valueOf("#3cdd33"));
    }

    /**
     * The setAppointmentTable sets the appointment table view a list of appointments
     *
     * @param appointmentList The list of appointments displayed in the table view
     */
    private void setAppointmentTable(ObservableList<Appointment> appointmentList) {
        idAppointmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleAppointmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionAppointmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationAppointmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactAppointmentTableColumn.setCellValueFactory(column -> {
            int contactId = column.getValue()
                                  .getContactId();
            String requestedColumn = DBModels.CONTACTS.getAttributes()
                                                      .get("name");
            String contactName = ContactQueryService.retrieveMatchFromDatabase(contactId, requestedColumn);
            return new SimpleStringProperty(contactName);
        });
        typeAppointmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startAppointmentTableColumn.setCellValueFactory(column -> TimeConversionService
                .cellDateTimeDisplay(column.getValue()
                                           .getStart(),
                                     MONTH_DATE_HOURS_TIMEZONE_FORMAT));
        endAppointmentTableColumn.setCellValueFactory(column -> TimeConversionService
                .cellDateTimeDisplay(column.getValue()
                                           .getEnd(),
                                     MONTH_DATE_HOURS_TIMEZONE_FORMAT));
        customerIdAppointmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdAppointmentTableColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        UIHelper.refreshEntityTable(appointmentList, appointmentTableView);
    }

    /**
     * The populateAppointmentFilterYearComboBox method populates with values within 2 timestamps
     *
     * @param start The start of the range
     * @param end   The end of the range
     */
    private void populateAppointmentFilterYearComboBox(Timestamp start, Timestamp end) {
        if (start == null || end == null) {
            return;
        }
        appointmentFilterYearComboBox.getItems()
                                     .clear();
        LocalDate startDate = start.toLocalDateTime()
                                   .toLocalDate();
        LocalDate endDate = end.toLocalDateTime()
                               .toLocalDate();

        int startYear = startDate.getYear();
        int endYear = endDate.getYear();

        for (int year = startYear; year <= endYear; year++) {
            appointmentFilterYearComboBox.getItems()
                                         .add(Year.of(year));
        }
    }

    /**
     * The setAppointmentFilterComboBoxVisibility method changes the visibility of the labels and combo boxes when the
     * different radio buttons are selected
     */
    private void setAppointmentFilterComboBoxVisibility() {
        boolean showAllAppointmentNotSelected = !appointmentFilterShowAllRadioButton.isSelected();
        boolean showWeekFilterSelected = appointmentFilterWeekRadioButton.isSelected();
        boolean showMonthFilterSelected = appointmentFilterMonthRadioButton.isSelected();
        if (showAllAppointmentNotSelected) {
            HashMap<String, Timestamp> range = AppointmentQueryService.getMinMaxAppointmentRange();
            populateAppointmentFilterYearComboBox(range.get("start"), range.get("end"));
        }
        if (showWeekFilterSelected) {
            appointmentFilterIntervalLabel.setText("Week");
        } else if (showMonthFilterSelected) {
            appointmentFilterIntervalLabel.setText("Month");
            populateMonthsComboBox();
        }
        appointmentFilterIntervalLabel.setVisible(showAllAppointmentNotSelected);
        appointmentFilterIntervalComboBox.setVisible(showAllAppointmentNotSelected);
        appointmentFilterYearLabel.setVisible(showAllAppointmentNotSelected);
        appointmentFilterYearComboBox.setVisible(showAllAppointmentNotSelected);
    }

    /**
     * The filterAppointment method filters the appointments in the appointment table view
     *
     * @return Returns a list of appointment to be displayed in the table view
     */
    private ObservableList<Appointment> filterAppointments() {
        if (!appointmentFilterShowAllRadioButton.isSelected() && appointmentFilterYearComboBox.getValue() != null) {
            if (appointmentFilterToggleGroup.getSelectedToggle()
                                            .equals(appointmentFilterWeekRadioButton)) {
                populateWeeksComboBox();
            } else if (appointmentFilterToggleGroup.getSelectedToggle()
                                                   .equals(appointmentFilterMonthRadioButton)) {
                populateMonthsComboBox();
            }
            return UIHelper.filterIntervals(appointmentFilterYearComboBox,
                                            appointmentFilterIntervalComboBox,
                                            appointmentFilterWeekRadioButton,
                                            appointmentFilterMonthRadioButton);
        }
        return AppointmentQueryService.getAllAppointments();
    }

    /**
     * The populateWeeksComboBox populated the interval comboBox with weeks if the week radio button is selected
     */
    private void populateWeeksComboBox() {
        Year selectedYear = appointmentFilterYearComboBox.getValue();
        if (selectedYear == null) {
            return;
        }
        DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("'Week of' EE, M/dd 'thru Sat'");
        ObservableList<LocalDate> weeks = FXCollections.observableArrayList();
        LocalDate firstDayOfYear = selectedYear.atDay(1);
        LocalDate lastDayOfYear = LocalDate.ofYearDay(selectedYear.getValue(), selectedYear.length());
        LocalDate currentWeekStart = firstDayOfYear;
        while (currentWeekStart.isBefore(lastDayOfYear)) {
            LocalDate currentWeekEnd = currentWeekStart.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            List<Appointment> appointmentsInWeek = AppointmentQueryService.getAppointmentsInRange(
                    currentWeekStart,
                    currentWeekEnd.atTime(LocalTime.MAX)
                                  .toLocalDate());
            if (!appointmentsInWeek.isEmpty()) {
                weeks.add(currentWeekStart);
            }
            currentWeekStart = currentWeekEnd.plusDays(1);
        }
        appointmentFilterIntervalComboBox.setConverter(TimeConversionService.displayedDateTime(weekFormatter));
        appointmentFilterIntervalComboBox.setItems(weeks);
    }

    /**
     * The populateMonthsComboBox populated the interval comboBox with months if the month radio button is selected
     */
    private void populateMonthsComboBox() {
        Year selectedYear = appointmentFilterYearComboBox.getValue();
        if (selectedYear == null) {
            return;
        }
        ObservableList<LocalDate> months = FXCollections.observableArrayList();
        ObservableList<Appointment> appointmentsInYear = AppointmentQueryService
                .getAppointmentsInRange(
                        selectedYear.atDay(1),
                        LocalDate.ofYearDay(selectedYear.getValue(), selectedYear.length()));
        for (int i = 1; i <= 12; i++) {
            LocalDate month = LocalDate.of(selectedYear.getValue(), i, 1);
            int finalI = i;
            List<Appointment> appointmentsInMonth = appointmentsInYear.filtered(appointment ->
                                                                                        appointment.getStart()
                                                                                                   .toLocalDateTime()
                                                                                                   .getMonthValue() ==
                                                                                                finalI);
            if (!appointmentsInMonth.isEmpty()) {
                months.add(month);
            }
        }
        appointmentFilterIntervalComboBox.setConverter(TimeConversionService.displayedDateTime(MONTH_FORMAT));
        appointmentFilterIntervalComboBox.setItems(months);
    }

    /**
     * The appointmentReminder method displays a popup specifying all appointment within a certain number of minutes
     */
    private void appointmentReminder() {
        Timestamp now = TimeConversionService.getCurrentTimeInDatabaseTime();
        Timestamp fifteenMinutesFromNow = Timestamp.valueOf(now.toLocalDateTime()
                                                               .plusMinutes(Main.APPOINTMENT_REMINDER_MINUTES));
        ObservableList<Appointment> appointments =
                AppointmentQueryService.getAppointmentsInRange(now, fifteenMinutesFromNow);
        if (!appointments.isEmpty()) {
            StringBuilder message = new StringBuilder(
                    "You have appointments within the next " + Main.APPOINTMENT_REMINDER_MINUTES + " minutes:");
            for (Appointment appointment : appointments) {
                LocalDateTime localTime =
                        TimeConversionService.convertFromServerTime(appointment.getStart())
                                             .toLocalDateTime();
                message.append("\n")
                       .append(appointment.getName())
                       .append(" at ")
                       .append(localTime.format(HOURS_FORMAT))
                       .append(".");
            }
            AlertHandler.notification(message.toString(), Color.ROSYBROWN);
        } else {
            AlertHandler.notification(String.format("There are no appointments within the next %d minutes",
                                                    Main.APPOINTMENT_REMINDER_MINUTES),
                                      Color.LIGHTSKYBLUE);
        }
    }

    /**
     * The loadAppointmentScreen method loads the screen display fields for adding or modifying an appointment
     *
     * @param isModifyAppointment Boolean specifying if an existing appointment is being modified
     */
    private void loadAppointmentScreen(boolean isModifyAppointment) {
        Appointment appointmentToBeModified = null;
        String appointmentScreenTitle;
        if (isModifyAppointment) {
            appointmentToBeModified = appointmentTableView.getSelectionModel()
                                                          .getSelectedItem();
            if (appointmentToBeModified == null) {
                return;
            }
            appointmentScreenTitle = Main.WINDOW_TITLE + " - Modify Appointment: " + appointmentToBeModified.getName();
        } else {
            appointmentScreenTitle = Main.WINDOW_TITLE + " - Create New Appointment";
        }
        try {
            AppointmentEntryController appointmentEntryController =
                    ScreenLoader.loadScreen(Main.VIEWS_PATH + "AppointmentEntryScreen.fxml",
                                            appointmentScreenTitle,
                                            mainTabPane,
                                            AppointmentEntryController.class,
                                            this,
                                            () -> setAppointmentTable(filterAppointments()),
                                            false,
                                            false,
                                            true);
            if (isModifyAppointment) {
                appointmentEntryController.setAppointment(appointmentToBeModified);
                appointmentEntryController.initAppointmentFields(appointmentToBeModified);
            }
        } catch (IOException io) {
            System.out.println("Loading Appointment Screen Unsuccessful");
        }
        setAppointmentTable(filterAppointments());
    }

    /**
     * The deleteAppointment method displays a prompt confirming if an appointment should be deleted
     */
    private void deleteAppointment() {
        Appointment appointmentToBeDeleted = appointmentTableView.getSelectionModel()
                                                                 .getSelectedItem();
        if (appointmentToBeDeleted != null) {
            if (AlertHandler.deleteEntityPrompt(DBModels.APPOINTMENTS)) {
                AppointmentQueryService.deleteAppointment(appointmentToBeDeleted);
                AlertHandler.entityModified(DBModels.APPOINTMENTS, appointmentToBeDeleted, true);
                setAppointmentTable(filterAppointments());
            }
        }
    }

    /**
     * The setCustomerTable sets the customer table view a list of customers
     *
     * @param customerList The list of customers displayed in the table view
     */
    private void setCustomerTable(ObservableList<Customer> customerList) {
        idCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        divisionCustomerTableColumn.setCellValueFactory(cellData -> {
            int divisionId = cellData.getValue()
                                     .getDivisionId();
            String requestedColumn = DBModels.DIVISIONS.getAttributes()
                                                       .get("name");
            String division = FirstLevelDivisionQueryService.retrieveMatchFromDatabase(divisionId, requestedColumn);
            return new SimpleStringProperty(division);
        });
        phoneCustomerTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        UIHelper.refreshEntityTable(customerList, customerTableView);
    }

    /**
     * The loadCustomerScreen method loads the screen display fields for adding or modifying a customer
     *
     * @param isModifyCustomer Boolean specifying if an existing customer is being modified
     */
    private void loadCustomerScreen(boolean isModifyCustomer) {
        Customer customerToBeModified = null;
        String customerScreenTitle;
        if (isModifyCustomer) {
            customerToBeModified = customerTableView.getSelectionModel()
                                                    .getSelectedItem();
            if (customerToBeModified == null) {
                return;
            }
            customerScreenTitle = Main.WINDOW_TITLE + " - Modify Customer: " + customerToBeModified.getName();
        } else {
            customerScreenTitle = Main.WINDOW_TITLE + " - Add Customer";
        }
        try {
            CustomerEntryController customerEntryController =
                    ScreenLoader.loadScreen(Main.VIEWS_PATH + "CustomerEntryScreen.fxml",
                                            customerScreenTitle,
                                            mainTabPane,
                                            CustomerEntryController.class,
                                            this,
                                            () -> UIHelper.refreshEntityTable(CustomerQueryService.getAllCustomers(),
                                                                              customerTableView),
                                            false,
                                            false,
                                            true);
            if (isModifyCustomer) {
                customerEntryController.setCustomer(customerToBeModified);
                customerEntryController.initCustomerFields();
            }
        } catch (IOException io) {
            System.out.println("Loading Customer Screen Unsuccessful");
        }
        setCustomerTable(CustomerQueryService.getAllCustomers());
    }

    /**
     * The deleteCustomer method displays a prompt confirming if a customer should be deleted, and the customer is
     * deleted if the customer has no appointments scheduled
     */
    private void deleteCustomer() {
        Customer customerToBeDeleted = customerTableView.getSelectionModel()
                                                        .getSelectedItem();
        if (customerToBeDeleted != null) {
            if (AlertHandler.deleteEntityPrompt(DBModels.CUSTOMERS)) {
                if (!CustomerQueryService.deleteCustomer(customerToBeDeleted)) {
                    AlertHandler.customErrorPopup(
                            "ERROR: Appointments exist",
                            "Unable to delete this customer.",
                            "Please delete all appointments that this customer has");
                } else {
                    AlertHandler.entityModified(DBModels.CUSTOMERS, customerToBeDeleted, true);
                }
                UIHelper.refreshEntityTable(CustomerQueryService.getAllCustomers(), customerTableView);
            }
        }
    }

    /**
     * The loadReportsScreen method loads the reports screen
     */
    private void loadReportsScreen() {
        try {
            ScreenLoader.loadScreen(Main.VIEWS_PATH + "ReportsScreen.fxml",
                                    Main.WINDOW_TITLE + " - REPORTS",
                                    mainTabPane,
                                    ReportsController.class,
                                    this,
                                    null,
                                    true,
                                    false,
                                    false);
        } catch (IOException io) {
            System.out.println("Loading Reports Screen Unsuccessful");
        }
    }

    /**
     * The logOut method logs out of the program loads the log-in screen
     */
    private void logOut() {
        if (AlertHandler.logOutPrompt()) {
            LoginController.setProgramUser(null);
            Stage stage = (Stage) mainTabPane.getScene()
                                             .getWindow();
            stage.close();
            try {
                ScreenLoader.loadScreen(Main.VIEWS_PATH + "LoginScreen.fxml",
                                        Main.WINDOW_TITLE,
                                        mainTabPane,
                                        LoginController.class,
                                        this,
                                        null,
                                        false,
                                        true,
                                        false);
                System.out.println("Logout Successful");
            } catch (IOException io) {
                System.out.println("Loading Log-in Screen Unsuccessful");
            }
        }
    }
}
