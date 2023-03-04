package com.cameronm.scheduleconsult.models;

import java.sql.Timestamp;

/**
 * The Customer class represents a customer entity from the database
 *
 * @author Cameron M
 * @since 02-20-2023
 */
public class Customer extends AuditedEntity {

    /**
     * The address of the customer
     */
    private String address;

    /**
     * The postal code of the customer
     */
    private String postalCode;

    /**
     * The phone number of the customer
     */
    private String phone;

    /**
     * The division ID foreign key for the customer
     */
    private int divisionId;

    /**
     * The constructor for the Customer class
     *
     * @param customerId    The ID of the customer entity
     * @param customerName  The name of the customer
     * @param createdDate   The date and time that the customer entity was created
     * @param createdBy     The name of the user who created the customer entity
     * @param lastUpdated   The date and time that the customer entity was last updated
     * @param lastUpdatedBy The name of the user who last updated the customer entity
     * @param address       The address of the customer
     * @param postalCode    The postal code of the customer
     * @param phone         The phone number of the customer
     * @param divisionId    The division ID foreign key for the customer
     */
    public Customer(int customerId,
                    String customerName,
                    Timestamp createdDate,
                    String createdBy,
                    Timestamp lastUpdated,
                    String lastUpdatedBy,
                    String address,
                    String postalCode,
                    String phone,
                    int divisionId) {
        super(customerId, customerName, createdDate, createdBy, lastUpdated, lastUpdatedBy);
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * The getAddress method returns the address of the customer
     *
     * @return returns the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * The setAddress method sets the address of the customer
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * The getPostalCode method returns the postal code of the customer
     *
     * @return returns the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * The setPostalCode method sets the postal code of the customer
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * The getPhone method returns the phone number of the customer
     *
     * @return returns the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * The setPhone method sets the phone number of the customer
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * The getDivisionId method returns the division ID foreign key of the customer
     *
     * @return returns the division ID foreign key
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * The setDivisionId method sets the division ID foreign key of the customer
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * The equals method determines whether two objects are equal
     *
     * @param obj The object
     * @return Return boolean result determining if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Customer other)) {
            return false;
        }
        return super.equals(other) &&
                this.address.equals(other.address) &&
                this.postalCode.equals(other.postalCode) &&
                this.phone.equals(other.phone) &&
                this.divisionId == other.divisionId;
    }
}
