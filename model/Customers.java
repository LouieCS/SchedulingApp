package model;

import helper.CustomersQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Customers {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionId;
    private static ObservableList<Customers> allCustomers = FXCollections.observableArrayList();


    /** This is the constructor for Customers.
     * @param id the id of the customer
     * @param name the name of the customer
     * @param address the address of the customer
     * @param postalCode the postal code of the customer
     * @param phoneNumber the phone number of the customer
     * @param divisionId the division Id of the customer
     */
    public Customers(int id, String name, String address, String postalCode, String phoneNumber, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
    }


    /** This gets the customer ID of a customer.
     * @return the customer ID of a customer
     */
    public int getId() {
        return id;
    }

    /** This sets the customer ID of a customer.
     * @param id the customer ID of a customer
     */
    public void setId(int id) {
        this.id = id;
    }

    /** This gets the name of a customer.
     * @return the name of a customer
     */
    public String getName() {
        return name;
    }

    /** This sets the name of a customer.
     * @param name the name of a customer
     */
    public void setName(String name) {
        this.name = name;
    }

    /** This gets the address of a customer.
     * @return the address of a customer
     */
    public String getAddress() {
        return address;
    }

    /** This sets the address of a customer.
     * @param address the address of a customer
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /** This gets the postal code of a customer.
     * @return the postal code of a customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /** This sets the postal code of a customer.
     * @param postalCode the postal code of a customer
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /** This gets the phone number of a customer.
     * @return the phone number of a customer
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** This sets the phone number of a customer.
     * @param phoneNumber the phone number of a customer
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** This gets the division ID of a customer.
     * @return the division ID of a customer
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** This sets the division ID of a customer.
     * @param divisionId the division ID of a customer
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** This searches the Customers list by customer name.
     * @param customerName The customer name to search in the list
     * @return results of the customer name search
     */
    public static ObservableList<Customers> lookupCustomer(String customerName){
        ObservableList<Customers> customerNameSearch = FXCollections.observableArrayList();

        customerName = customerName.toLowerCase();

        for(Customers customer : allCustomers) {
            if (customer.getName().toLowerCase().contains(customerName)) {
                customerNameSearch.add(customer);
            }
        }

        return customerNameSearch;
    }

    /** This searches the Customers list by customer ID.
     * @param customerID The customer ID to search in the list
     * @return results of the customer ID search
     */
    public static ObservableList<Customers> lookupCustomer(int customerID){
        ObservableList<Customers> customerIdSearch = FXCollections.observableArrayList();

        for(Customers customer : allCustomers) {
            if (customer.getId() == customerID) {
                customerIdSearch.add(customer);
            }
        }

        return customerIdSearch;
    }

    /** This adds a new customer to the Customers list.
     * @param newCustomer The new customer to add
     */
    public static void addCustomer(Customers newCustomer) {
        allCustomers.add(newCustomer);
    }

    /** This deletes a customer from the Customers list.
     * @param customer The customer to be removed
     * @return the Customers list with the customer removed
     */
    public static boolean deleteCustomer(Customers customer) throws SQLException {
        if (allCustomers.contains(customer)) {
            CustomersQuery.delete((customer.getId()));
            return Customers.getAllCustomers().remove(customer);
        }
        else {
            return false;
        }
    }

    /** Obtains all customers from the Customers list.
     * @return all customers from the Customers list
     */
    public static ObservableList<Customers> getAllCustomers() {
        return allCustomers;
    }

    /** This returns a string to a combo box.
     * @return customer name
     */
    @Override
    public String toString() {
        return name;
    }

}
