package model;

import helper.AppointmentsQuery;
import helper.CustomersQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.TimeZone;

public class Appointments {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private int contact;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int customerId;
    private int userId;
    private static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();


    /** This is the constructor for Appointments.
     * @param appointmentId the id of the appointment
     * @param title the title of the appointment
     * @param description the description of the appointment
     * @param location the location of the appointment
     * @param contact the contact of the appointment
     * @param type the type of appointment
     * @param startDateTime the start date and time of the appointment
     * @param endDateTime  the end date and time of the appointment
     * @param customerId the customerId of the appointment
     * @param userId the userId of the appointment
     */
    public Appointments(int appointmentId, String title, String description, String location, int contact, String type, LocalDateTime startDateTime, LocalDateTime endDateTime, int customerId, int userId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerId = customerId;
        this.userId = userId;
    }


    /** This gets the Appointment ID of an appointment.
     * @return the ID of a appointment
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /** This sets the Appointment ID of an appointment.
     * @param appointmentId the ID of an appointment
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /** This gets the title of an appointment.
     * @return the title of an appointment
     */
    public String getTitle() {
        return title;
    }

    /** This sets the title of an appointment.
     * @param title the title of a appointment
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** This gets the description of an appointment.
     * @return the description of an appointment
     */
    public String getDescription() {
        return description;
    }

    /** This sets the description of an appointment.
     * @param description the description of an appointment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /** This gets the location of an appointment.
     * @return the location of an appointment
     */
    public String getLocation() {
        return location;
    }

    /** This sets the location of an appointment.
     * @param location the location of an appointment
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /** This gets the contact of an appointment.
     * @return the contact of a appointment
     */
    public int getContact() {
        return contact;
    }

    /** This sets the contact of an appointment.
     * @param contact the contact of an appointment
     */
    public void setContact(int contact) {
        this.contact = contact;
    }

    /** This gets the type of appointment.
     * @return the type of appointment
     */
    public String getType() {
        return type;
    }

    /** This sets the type of appointment.
     * @param type the type of appointment
     */
    public void setType(String type) {
        this.type = type;
    }

    /** This gets the start date and time of an appointment.
     * @return the start date and time of a appointment
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /** This sets the start date and time of an appointment.
     * @param startDateTime the start date and time of an appointment
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /** This gets the end date and time of an appointment.
     * @return the end date and time of an appointment
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /** This sets the end date and time of an appointment.
     * @param endDateTime the end date and time of a appointment
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /** This gets the Customer ID of an appointment.
     * @return the Customer ID of an appointment
     */
    public int getCustomerId() {
        return customerId;
    }

    /** This sets the Customer ID of an appointment.
     * @param customerId the Customer ID of an appointment
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /** This gets the User ID of an appointment.
     * @return the User ID of an appointment
     */
    public int getUserId() {
        return userId;
    }

    /** This sets the User ID of an appointment.
     * @param userId the User ID of an appointment
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /** This searches the Appointments list by customer ID.
     * @param customerID The customer ID to search in the list
     * @return results of the customer ID search
     */
    public static ObservableList<Appointments> lookupAppointment(int customerID){

        ObservableList<Appointments> customerIdSearch = FXCollections.observableArrayList();

        for(Appointments appointments : allAppointments) {
            if (appointments.getCustomerId() == customerID) {
                customerIdSearch.add(appointments);
            }
        }

        return customerIdSearch;
    }

    /** This adds a new appointment to the Appointments list.
     * @param newAppointment The new appointment to add
     */
    public static void addAppointment(Appointments newAppointment) {
        allAppointments.add(newAppointment);
    }

    public static void updateAppointment(int id, Appointments updatedAppointment) {

        int index = -1;

        for(Appointments appointments : Appointments.getAllAppointments()) {
            index++;

            if(appointments.getAppointmentId() == id) {
                Appointments.getAllAppointments().set(index, updatedAppointment);
            }
        }
    }

    /** This deletes an appointment from the Appointments list.
     * @param appointment The appointment to be removed
     * @return the Appointments list with the appointment removed
     */
    public static boolean deleteAppointment(Appointments appointment) throws SQLException {

        if (allAppointments.contains(appointment)) {
            AppointmentsQuery.delete((appointment.getAppointmentId()));
            return Appointments.getAllAppointments().remove(appointment);
        }
        else {
            return false;
        }
    }

    /** Obtains all appointments from the Appointments list.
     * @return all appointments from the Appointments list
     */
    public static ObservableList<Appointments> getAllAppointments() {
        return allAppointments;
    }

    /** This converts UTC date and time to local.
     * @param utcLDT The UTC date and time to be converted
     * @return Local date and time
     */
    public static java.time.LocalDateTime convertUTCToLocal(java.time.LocalDateTime utcLDT) {

        java.time.ZoneId utcZoneID = ZoneId.of("UTC");
        java.time.ZonedDateTime utcZDT = ZonedDateTime.of(utcLDT, utcZoneID);

        java.time.ZoneId localZoneID = ZoneId.systemDefault();
        java.time.ZonedDateTime localZDT = ZonedDateTime.ofInstant(utcZDT.toInstant(), localZoneID);

        java.time.LocalDateTime localDateTime = localZDT.toLocalDateTime();

        return localDateTime;

    }

    /** This converts local date and time to UTC.
     * @param localDateTime The local date and time to be converted
     * @return UTC date and time
     */
    public static java.time.LocalDateTime convertLocalToUTC(java.time.LocalDateTime localDateTime) {

        java.time.LocalDate localDate = localDateTime.toLocalDate();
        java.time.LocalTime localTime = localDateTime.toLocalTime();
        java.time.ZoneId localZoneID = ZoneId.systemDefault();
        ZonedDateTime localZDT = ZonedDateTime.of(localDate, localTime, localZoneID);

        java.time.ZoneId utcZoneID = ZoneId.of("UTC");
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(localZDT.toInstant(), utcZoneID);

        java.time.LocalDateTime utcLDT = utcZDT.toLocalDateTime();

        return utcLDT;

    }

    /** This converts EST to local.
     * @param easternTime The EST time to be converted
     * @return Local time
     */
    public static java.time.LocalTime convertEasternToLocal(java.time.LocalTime easternTime) {

        java.time.LocalDate easternDate = LocalDate.now();
        java.time.ZoneId easternZoneID = ZoneId.of("US/Eastern");
        java.time.ZonedDateTime easternZDT = ZonedDateTime.of(easternDate, easternTime, easternZoneID);

        java.time.ZoneId localZoneID = ZoneId.systemDefault();
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(easternZDT.toInstant(), localZoneID);

        java.time.LocalTime localTime = utcZDT.toLocalTime();

        return localTime;

    }

    /** This converts Eastern date and time to local.
     * @param easternDateTime The Eastern date and time to be converted
     * @return Local date and time
     */
    public static java.time.LocalTime convertEasternToLocal(java.time.LocalDateTime easternDateTime) {

        java.time.LocalDate easternDate = easternDateTime.toLocalDate();
        java.time.LocalTime easternTime = easternDateTime.toLocalTime();
        java.time.ZoneId easternZoneID = ZoneId.of("US/Eastern");
        java.time.ZonedDateTime easternZDT = ZonedDateTime.of(easternDate, easternTime, easternZoneID);

        java.time.ZoneId localZoneID = ZoneId.systemDefault();
        ZonedDateTime utcZDT = ZonedDateTime.ofInstant(easternZDT.toInstant(), localZoneID);

        java.time.LocalTime localTime = utcZDT.toLocalTime();

        return localTime;

    }

}
