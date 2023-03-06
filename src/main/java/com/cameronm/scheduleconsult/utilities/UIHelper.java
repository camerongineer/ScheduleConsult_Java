package com.cameronm.scheduleconsult.utilities;

import com.cameronm.scheduleconsult.Main;
import com.cameronm.scheduleconsult.models.Appointment;
import com.cameronm.scheduleconsult.models.NamedEntity;
import com.cameronm.scheduleconsult.services.AppointmentQueryService;
import com.cameronm.scheduleconsult.views.AlertHandler;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * The UIHelper class contain methods used by UI elements of the program
 *
 * @author Cameron M
 * @since 03-04-2023
 */
public abstract class UIHelper {

    /**
     * The addDoubleClickHandler method adds a handler to a tableview that performs an action when it is double-clicked
     *
     * @param tableView The tableview
     * @param action    The action that gets performed
     */
    public static void addDoubleClickHandler(TableView<?> tableView, Runnable action) {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                action.run();
            }
        });
    }

    /**
     * The closeProgram method displays a confirmation popup before exiting the program
     */
    public static void closeProgram() {
        if (AlertHandler.closePrompt()) {
            Main.safeExit();
        }
    }

    /**
     * The refreshEntity table method refreshes a table view with a list of entities
     *
     * @param entityList The list of entities
     * @param tableView  The table view displaying the entities
     * @param <T>        The type of entity which must extend NamedEntity
     */
    public static <T extends NamedEntity> void refreshEntityTable(ObservableList<T> entityList,
                                                                  TableView<T> tableView) {
        tableView.setItems(entityList);
        tableView.refresh();
    }

    /**
     * The setEntityComboBox method sets a combobox with a list of entities
     *
     * @param entityList The list of entities
     * @param comboBox   The combo box containing the entities
     * @param <T>        The type of entity which must extend NamedEntity
     */
    public static <T extends NamedEntity> void setEntityComboBox(ObservableList<T> entityList, ComboBox<T> comboBox) {
        comboBox.setItems(entityList);
        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
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
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T t) {
                if (t == null) {
                    return null;
                } else {
                    return t.getName();
                }
            }

            @Override
            public T fromString(String string) {
                return null;
            }
        });
    }

    /**
     * The initStringTextField method adds a listener which sets rules for what can be typed into a string text field
     *
     * @param field     The text field
     * @param maxLength The maximum length of the text field
     */
    public static void initStringTextField(TextField field, int maxLength) {
        field.textProperty()
             .addListener(InputValidator.validStringInputListener(field, maxLength));
    }

    /**
     * The initIdTextField method adds a listener which sets rules for what can be typed into an ID text field
     *
     * @param field The text field
     */
    public static void initIdTextField(TextField field) {
        field.textProperty()
             .addListener(InputValidator.validIdInputListener(field, 10));
    }

    /**
     * The filterIntervals method filters the combo boxes based on the week/month and year selected
     *
     * @param yearComboBox     The combo box displaying years
     * @param intervalComboBox The combo box displaying weeks or months
     * @param weekRadioButton  The week radio button
     * @param monthRadioButton The month radio button
     * @return Returns a list of appointments selected within the range of the combo box selections
     */
    public static ObservableList<Appointment> filterIntervals(ComboBox<Year> yearComboBox,
                                                              ComboBox<LocalDate> intervalComboBox,
                                                              RadioButton weekRadioButton,
                                                              RadioButton monthRadioButton) {
        Year year = yearComboBox.getValue();
        LocalDate interval = intervalComboBox.getValue();
        if (year != null) {
            LocalDateTime start = null;
            LocalDateTime end = null;
            if (interval != null) {
                if (weekRadioButton.isSelected()) {
                    start = interval.atStartOfDay();
                    LocalDate nextSaturday = interval.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
                    end = LocalDateTime.of(nextSaturday, LocalTime.MAX);
                } else if (monthRadioButton.isSelected()) {
                    start = LocalDateTime.of(year.getValue(), interval.getMonth(), 1, 0, 0, 0);
                    end = LocalDateTime.of(year.getValue(), interval.getMonth(), interval.lengthOfMonth(), 23, 59, 59);
                }
                assert start != null;
            } else {
                start = year.atDay(1).atStartOfDay();
                end = start.plusYears(1).minusSeconds(1);
            }
            return AppointmentQueryService.getAppointmentsInRange(Timestamp.valueOf(start), Timestamp.valueOf(end));
        }
        return AppointmentQueryService.getAllAppointments();
    }
}