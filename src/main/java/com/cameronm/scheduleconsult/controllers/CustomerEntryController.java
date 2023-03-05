package com.cameronm.scheduleconsult.controllers;

import com.cameronm.scheduleconsult.DAO.DBModels;
import com.cameronm.scheduleconsult.DAO.DBQueries;
import com.cameronm.scheduleconsult.models.*;
import com.cameronm.scheduleconsult.services.*;
import com.cameronm.scheduleconsult.utilities.InputValidator;
import com.cameronm.scheduleconsult.utilities.UIHelper;
import com.cameronm.scheduleconsult.views.AlertHandler;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The CustomerEntryController class is the controller class for the customer entry screen
 *
 * @author Cameron M
 * @since 03-01-2023
 */
public class CustomerEntryController implements Initializable, DBQueries {

    /**
     * The customer passed to the entry screen
     */
    private Customer customer;

    /**
     * The address TextField
     */
    @FXML
    private TextField addressTextField;

    /**
     * The ComboBox containing country entities
     */
    @FXML
    private ComboBox<Country> countryComboBox;

    /**
     * The customer ID TextField
     */
    @FXML
    private TextField customerIdTextField;

    /**
     * The ComboBox containing first-level division entities
     */
    @FXML
    private ComboBox<FirstLevelDivision> divisionComboBox;

    /**
     * The name TextField
     */
    @FXML
    private TextField nameTextField;

    /**
     * The phone TextField
     */
    @FXML
    private TextField phoneTextField;

    /**
     * The postal code TextField
     */
    @FXML
    private TextField postalCodeTextField;

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
     * The saveButtonClicked method displays a popup prompt confirming if the customer information should be saved
     */
    @FXML
    void saveButtonClicked() {
        if (InputValidator.checkInvalidEntriesError(validateAll())) {
            if (AlertHandler.confirmAction("Are you sure you want to save?", "")) {
                saveCustomer();
                Stage stage = (Stage) saveButton.getScene()
                                                .getWindow();
                stage.close();
                AlertHandler.entityModified(DBModels.CUSTOMERS, customer, false);
            }
        }
    }

    /**
     * The saveCustomer method saves the information provided in fields of the customer entry screen creating a new
     * customer entity or updating an existing one
     */
    private void saveCustomer() {
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String postalCode = postalCodeTextField.getText();
        FirstLevelDivision division = divisionComboBox.getValue();
        String phoneNumber = phoneTextField.getText();
        if (customer == null) {
            customer = CustomerQueryService.addCustomer(
                    new Customer(-1,
                                 name,
                                 null,
                                 LoginController.getProgramUser()
                                                .getName(),
                                 null,
                                 LoginController.getProgramUser()
                                                .getName(),
                                 address,
                                 postalCode,
                                 phoneNumber,
                                 division.getId()
                    ));
        } else {
            customer.setName(name);
            customer.setAddress(address);
            customer.setPostalCode(postalCode);
            customer.setDivisionId(division.getId());
            customer.setPhone(phoneNumber);
            customer.setLastUpdatedBy(LoginController.getProgramUser()
                                                     .getName());
            CustomerQueryService.modifyCustomer(customer);
        }
    }

    /**
     * The initialize method initializes customer entry screen
     *
     * @param url            The url passed into the initialize method
     * @param resourceBundle The resource bundle passed into the initialize method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.setOnAction(actionEvent -> {
            refreshDivisions();
            divisionComboBox.getSelectionModel()
                            .selectFirst();
        });
        divisionComboBox.setOnAction(actionEvent -> {
            if (countryComboBox.getValue() == null && divisionComboBox.getValue() != null) {
                countryComboBox
                        .getSelectionModel()
                        .select(CountryQueryService
                                        .getCountryById(divisionComboBox.getValue()
                                                                        .getCountryId()));
            }
        });
        UIHelper.setEntityComboBox(CountryQueryService.getAllCountries(), countryComboBox);
        UIHelper.setEntityComboBox(FirstLevelDivisionQueryService.getAllDivisions(), divisionComboBox);
        UIHelper.initIdTextField(customerIdTextField);
        UIHelper.initStringTextField(nameTextField, 50);
        UIHelper.initStringTextField(addressTextField, 100);
        UIHelper.initStringTextField(postalCodeTextField, 50);
        UIHelper.initStringTextField(phoneTextField, 50);
    }

    /**
     * The initCustomerFields method populates the fields of customer entry screen for modifications to an existing
     * customer
     */
    void initCustomerFields() {
        customerIdTextField.setText(String.valueOf(customer.getId()));
        nameTextField.setText(customer.getName());
        addressTextField.setText(customer.getAddress());
        postalCodeTextField.setText(customer.getPostalCode());
        FirstLevelDivision customerDivision = FirstLevelDivisionQueryService
                .getDivisionById(customer.getDivisionId());
        Country customerCountry = CountryQueryService
                .getCountryById(customerDivision.getCountryId());
        countryComboBox.getSelectionModel()
                       .select(customerCountry);
        UIHelper.setEntityComboBox(filterDivisions(customerDivision.getCountryId()), divisionComboBox);
        divisionComboBox.getSelectionModel()
                        .select(customerDivision);
        phoneTextField.setText(customer.getPhone());
    }

    /**
     * The refreshDivisions method refreshes the divisions ComboBox to match the corresponding country
     */
    private void refreshDivisions() {
        divisionComboBox.getItems()
                        .clear();
        if (countryComboBox.getValue() != null) {
            divisionComboBox.setItems(filterDivisions(countryComboBox.getValue()
                                                                     .getId()));
        } else {
            divisionComboBox.setItems(filterDivisions(null));
        }
    }

    /**
     * The filterDivisions method filters the divisions in the divisions ComboBox based on the country ID of the
     * division
     *
     * @param countryId The country ID of the division
     * @return Returns an ObservableList of divisions to be displayed in the ComboBox
     */
    private ObservableList<FirstLevelDivision> filterDivisions(Integer countryId) {
        if (countryId != null) {
            return FirstLevelDivisionQueryService.getAllDivisionsByCountryId(countryId);
        }
        return FirstLevelDivisionQueryService.getAllDivisions();
    }

    /**
     * The setCustomer method sets the customer
     *
     * @param customer The customer to be set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * The validateAll method validates all the fields and entities selected on the customer entry screen
     *
     * @return Returns a list of exceptions to be displayed in a popup
     */
    private List<Exception> validateAll() {
        List<Exception> allErrors = new ArrayList<>(List.copyOf(validateFields()));
        allErrors.addAll(validateEntities());
        return allErrors;
    }

    /**
     * The validateFields method validates all the fields of the customer entry screen
     *
     * @return Returns a list of exceptions to be displayed in a popup
     */
    private List<Exception> validateFields() {
        List<Exception> exceptions = new ArrayList<>();
        exceptions.add(InputValidator.checkValidInput("Name", nameTextField.getText(), 50, false));
        exceptions.add(InputValidator.checkValidInput("Address", addressTextField.getText(), 100, false));
        exceptions.add(InputValidator.checkValidInput("Postal Code", postalCodeTextField.getText(), 50, false));
        exceptions.add(InputValidator.checkEntitySelected("Division", divisionComboBox.getValue()));
        exceptions.add(InputValidator.checkEntitySelected("Country", countryComboBox.getValue()));
        exceptions.add(InputValidator.checkValidInput("Phone Number",
                                                      phoneTextField.getText(),
                                                      50,
                                                      false));
        exceptions.removeIf(Objects::isNull);
        return exceptions;
    }

    /**
     * The validateEntities method validates entities selected on the customer entry screen
     *
     * @return Returns a list of exceptions to be displayed in a popup
     */
    private List<Exception> validateEntities() {
        List<Exception> exceptions = new ArrayList<>();
        FirstLevelDivision division = divisionComboBox.getValue();
        Country country = countryComboBox.getValue();
        if (division == null) {
            exceptions.add(new InstantiationException("Division not selected"));
        }
        if (country == null) {
            exceptions.add(new InstantiationException("Country not selected"));
        }
        if (division != null && country != null && division.getCountryId() != country.getId()) {
            exceptions.add(new InstantiationException("This division is not in this country"));
        }
        return exceptions;
    }
}

