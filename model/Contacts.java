package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Contacts {

    private int contactID;
    private String contactName;
    private String contactEmail;
    private static ObservableList<Contacts> allContacts = FXCollections.observableArrayList();


    /** This is the constructor for Contacts.
     * @param contactID the id of the contact
     * @param contactName the name of the contact
     * @param contactEmail the email of the contact
     */
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }


    /** This gets the contact ID of a contact.
     * @return the contact ID of a contact
     */
    public int getContactID() {
        return contactID;
    }

    /** This sets the contact ID of a contact.
     * @param contactID the contact ID of a contact
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /** This gets the name of a contact.
     * @return the name of a contact
     */
    public String getContactName() {
        return contactName;
    }

    /** This sets the name of a contact.
     * @param contactName the name of a contact
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** This gets the email of a contact.
     * @return the email of a contact
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /** This sets the email of a contact.
     * @param contactEmail the email of a contact
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /** This adds a new contact to the Contacts list.
     * @param newContact The new contact to add
     */
    public static void addContact(Contacts newContact) {
        allContacts.add(newContact);
    }

    /** Obtains all contacts from the Contacts list.
     * @return all contacts from the Contacts list
     */
    public static ObservableList<Contacts> getAllContacts() {
        return allContacts;
    }

    /** This returns a string to a combo box.
     * @return contact name
     */
    @Override
    public String toString() {
        return contactName;
    }
}
