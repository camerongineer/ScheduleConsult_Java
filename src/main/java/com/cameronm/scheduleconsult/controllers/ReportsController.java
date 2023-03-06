package com.cameronm.scheduleconsult.controllers;

import com.cameronm.scheduleconsult.models.Appointment;
import com.cameronm.scheduleconsult.models.Contact;
import com.cameronm.scheduleconsult.models.User;
import com.cameronm.scheduleconsult.services.AppointmentQueryService;
import com.cameronm.scheduleconsult.services.ContactQueryService;
import com.cameronm.scheduleconsult.services.UserQueryService;
import com.cameronm.scheduleconsult.utilities.CredentialLogger;
import com.cameronm.scheduleconsult.utilities.UIHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


/**
 * The ReportsController class is the controller class for the reports screen
 *
 * @author Cameron M
 * @since 03-03-2023
 */
public class ReportsController implements Initializable {

    /**
     * The TextArea displaying the content of the customer appointments tab
     */
    @FXML
    private TextArea customerAppointmentsTextArea;

    /**
     * The ComboBox displaying contacts in the contact schedules tab
     */
    @FXML
    private ComboBox<Contact> contactSchedulesComboBox;

    /**
     * The TextArea displaying the content of the contact schedules tab
     */
    @FXML
    private TextArea contactSchedulesTextArea;

    /**
     * The ComboBox displaying users in the modified appointments tab
     */
    @FXML
    private ComboBox<User> userModifiedAppointmentsComboBox;

    /**
     * The TextArea displaying the content of the modified appointments tab
     */
    @FXML
    private TextArea userModifiedAppointmentsTextArea;

    /**
     * The TextArea displaying the content of the log-in reports tab
     */
    @FXML
    private TextArea logInReportTextArea;

    /**
     * The initialize method initializes reports screen
     *
     * @param url            The url passed into the initialize method
     * @param resourceBundle The resource bundle passed into the initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerAppointmentsTextArea.setText(AppointmentQueryService.customerAppointmentReport());
        contactSchedulesComboBox.setOnAction(actionEvent ->
                                                     contactSchedulesReport(contactSchedulesComboBox.getValue()));
        logInReportTextArea.setText(CredentialLogger.report());
        userModifiedAppointmentsComboBox.setOnAction(actionEvent ->
                                                             userModifiedAppointmentsReport(
                                                                     userModifiedAppointmentsComboBox.getValue()));
        UIHelper.setEntityComboBox(ContactQueryService.getAllContacts(), contactSchedulesComboBox);
        UIHelper.setEntityComboBox(UserQueryService.getAllUsers(), userModifiedAppointmentsComboBox);
    }

    /**
     * The contactSchedulesReport method displays a report showing the schedule of a given contact
     *
     * @param contact The contact whose schedule is displayed
     */
    private void contactSchedulesReport(Contact contact) {
        if (contact != null) {
            List<Appointment> contactAppointments = AppointmentQueryService
                    .getAllAppointments()
                    .stream()
                    .filter(appointment -> appointment.getContactId() == contact.getId())
                    .toList();
            StringBuilder report = new StringBuilder();
            contactAppointments.forEach(report::append);
            contactSchedulesTextArea.setText(report.toString());
        }
    }

    /**
     * The userModifiedAppointmentsReport displays a report showing the date of the appointments were modified from
     * their original state
     *
     * @param user The user who modified the appointments in the report
     */
    private void userModifiedAppointmentsReport(User user) {
        if (user != null) {
            List<Appointment> modifiedUpdates = AppointmentQueryService
                    .getAllAppointments()
                    .stream()
                    .filter(appointment ->
                                    appointment.getCreatedDate()
                                               .getTime() != appointment.getLastUpdated()
                                                                        .getTime() &&
                                            appointment.getUserId() == user.getId())
                    .toList();
            StringBuilder report = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' h:mm a");
            modifiedUpdates.forEach(appointment -> {
                Timestamp lastUpdated = appointment.getLastUpdated();
                report
                        .append("Appointment with ID: ")
                        .append(appointment.getId())
                        .append(" was last updated by '")
                        .append(user.getName())
                        .append("' on ")
                        .append(lastUpdated.toLocalDateTime()
                                           .format(formatter))
                        .append("\n\n");
            });
            userModifiedAppointmentsTextArea.setText(report.toString());
        }
    }
}
