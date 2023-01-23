package controller;


import helper.AppointmentsQuery;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lsc195.lsc195.SchedulingApplication;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Appointments.*;

/**
 * This class creates a form to modify an appointment.
 *
 * @author Louie Sanchez
 *
 */
public class ModifyAppointmentController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField appointmentIDTXT;
    @FXML
    private TextField appointmentTitleTXT;
    @FXML
    private TextField appointmentDescriptionTXT;
    @FXML
    private TextField appointmentLocationTXT;
    @FXML
    private TextField appointmentTypeTXT;
    @FXML
    private ComboBox userIdCOMBO;
    @FXML
    private DatePicker startDATEPICKER;
    @FXML
    private DatePicker endDATEPICKER;
    @FXML
    private ComboBox startTimeCOMBO;
    @FXML
    private ComboBox endTimeCOMBO;
    @FXML
    private ComboBox<Contacts> contactCOMBO;
    @FXML
    private ComboBox<Customers> customerCOMBO;

    /** This creates updates the existing appointment. After updating the appointment, a user is redirected back to the main form. */
    @FXML
    void onActionSaveAppointment(ActionEvent event) throws IOException, SQLException {

        try {
            Boolean appointmentRequirementsMet = false;

            int appointmentID = Integer.parseInt(appointmentIDTXT.getText());
            String title = appointmentTitleTXT.getText();
            String description = appointmentDescriptionTXT.getText();
            String location = appointmentLocationTXT.getText();
            int contact = contactCOMBO.getSelectionModel().getSelectedItem().getContactID();
            String type = appointmentTypeTXT.getText();

            java.time.LocalDate localStartDate = startDATEPICKER.getValue();
            LocalTime localStartTime = (LocalTime) startTimeCOMBO.getSelectionModel().getSelectedItem();
            LocalDateTime localStartDateTime = LocalDateTime.of(localStartDate, localStartTime);
            LocalDateTime utcStartDateTime = convertLocalToUTC(localStartDateTime);
            Timestamp utcStartTimestamp = Timestamp.valueOf(utcStartDateTime);

            java.time.LocalDate localEndDate = endDATEPICKER.getValue();
            LocalTime localEndTime = (LocalTime) endTimeCOMBO.getSelectionModel().getSelectedItem();
            LocalDateTime localEndDateTime = LocalDateTime.of(localEndDate, localEndTime);
            LocalDateTime utcEndDateTime = convertLocalToUTC(localEndDateTime);
            Timestamp utcEndTimestamp = Timestamp.valueOf(utcEndDateTime);

            int customerId = customerCOMBO.getSelectionModel().getSelectedItem().getId();
            int userId = Integer.parseInt(userIdCOMBO.getSelectionModel().getSelectedItem().toString());

            if (utcEndTimestamp.before(utcStartTimestamp)) {
                appointmentRequirementsMet = false;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter a valid date and time. The start date and time must occur before the end date and time.");
                alert.showAndWait();
            }
            else {
                for (Appointments appointment : Appointments.getAllAppointments()) {
                    if (appointmentID != appointment.getAppointmentId()) {
                        if ((localStartDateTime.isAfter(appointment.getStartDateTime()) && localStartDateTime.isBefore(appointment.getEndDateTime())) || (localEndDateTime.isAfter(appointment.getStartDateTime()) && localEndDateTime.isBefore(appointment.getEndDateTime())) || (appointment.getStartDateTime().isAfter(localStartDateTime) && appointment.getStartDateTime().isBefore(localEndDateTime)) || (appointment.getStartDateTime().isAfter(localStartDateTime) && appointment.getStartDateTime().isBefore(localEndDateTime)) || (localStartDateTime.equals(appointment.getStartDateTime())) || (localEndDateTime.equals(appointment.getEndDateTime()))) {
                            appointmentRequirementsMet = false;
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("Overlapping appointment schedule.");
                            alert.showAndWait();
                            break;
                        }
                    }
                    appointmentRequirementsMet = true;
                }
            }

            if (title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank()) {
                appointmentRequirementsMet = false;

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter valid values into the text fields. The text fields cannot be blank.");
                alert.showAndWait();
            }

            if (appointmentRequirementsMet.equals(true)) {

                AppointmentsQuery.update(appointmentID, title, description, location, contact, type, utcStartTimestamp, utcEndTimestamp, customerId, userId);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(SchedulingApplication.class.getResource("main-view.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (NullPointerException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter valid values into the text fields and select values from the combo boxes.");
            alert.showAndWait();
        }

    }

    /** This redirects a user back to the main form. */
    @FXML
    void onActionToMain(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Proceeding will cause any input to be unsaved. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(SchedulingApplication.class.getResource("main-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This takes the business hours from EST to a user's local time and adds it to the Start Time combo box. */
    @FXML
    void onActionStartDateSelected(ActionEvent event) {

        startTimeCOMBO.getItems().clear();
        java.time.LocalDate selectedDate = startDATEPICKER.getValue();

        LocalTime easternStart = LocalTime.of(8, 0);
        LocalTime easternEnd = LocalTime.of(22, 0);

        LocalDateTime easternDateTimeStart = LocalDateTime.of(selectedDate, easternStart);
        LocalDateTime easternDateTimeEnd = LocalDateTime.of(selectedDate, easternEnd);

        LocalTime localStart = convertEasternToLocal(easternDateTimeStart);
        LocalTime localEnd = convertEasternToLocal(easternDateTimeEnd);

        while (localStart.isBefore(localEnd.plusSeconds(1))) {
            startTimeCOMBO.getItems().add(localStart);
            localStart = localStart.plusMinutes(15);
        }

    }

    /** This takes the business hours from EST to a user's local time and adds it to the End Time combo box. */
    @FXML
    void onActionEndDateSelected(ActionEvent event) {

        endTimeCOMBO.getItems().clear();
        java.time.LocalDate selectedDate = endDATEPICKER.getValue();

        LocalTime easternStart = LocalTime.of(8, 0);
        LocalTime easternEnd = LocalTime.of(22, 0);

        LocalDateTime easternDateTimeStart = LocalDateTime.of(selectedDate, easternStart);
        LocalDateTime easternDateTimeEnd = LocalDateTime.of(selectedDate, easternEnd);

        LocalTime localStart = convertEasternToLocal(easternDateTimeStart);
        LocalTime localEnd = convertEasternToLocal(easternDateTimeEnd);

        while (localStart.isBefore(localEnd.plusSeconds(1))) {
            endTimeCOMBO.getItems().add(localStart);
            localStart = localStart.plusMinutes(15);
        }

    }

    /** This obtains the current information of an existing appointment and displays it in the form.
     * @param selectedAppointment the appointment to obtain information about
     */
    public void sendAppointment(Appointments selectedAppointment) {

        appointmentIDTXT.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        appointmentTitleTXT.setText(String.valueOf(selectedAppointment.getTitle()));
        appointmentDescriptionTXT.setText(String.valueOf(selectedAppointment.getDescription()));
        appointmentLocationTXT.setText(String.valueOf(selectedAppointment.getLocation()));
        appointmentTypeTXT.setText(String.valueOf(selectedAppointment.getType()));

        for(Customers customer : Customers.getAllCustomers()) {
            if (selectedAppointment.getCustomerId() == customer.getId()) {
                customerCOMBO.getSelectionModel().select(customer);
            }
        }

        for(Contacts contact : Contacts.getAllContacts()) {
            if (selectedAppointment.getContact() == contact.getContactID()) {
                contactCOMBO.getSelectionModel().select(contact);
            }
        }

        for(User user : User.getAllUsers()) {
            if (selectedAppointment.getUserId() == user.getUserID()) {
                userIdCOMBO.getSelectionModel().select(user);
            }
        }

        java.time.LocalDateTime localStartDateTime = selectedAppointment.getStartDateTime();
        startDATEPICKER.setValue(localStartDateTime.toLocalDate());
        startTimeCOMBO.getSelectionModel().select(localStartDateTime.toLocalTime());

        java.time.LocalDateTime localEndDateTime = selectedAppointment.getEndDateTime();
        endDATEPICKER.setValue(localEndDateTime.toLocalDate());
        endTimeCOMBO.getSelectionModel().select(localEndDateTime.toLocalTime());

    }

    /** This initializes the start and end time combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<Customers> customers = Customers.getAllCustomers();
        ObservableList<Contacts> contacts = Contacts.getAllContacts();
        ObservableList<User> users = User.getAllUsers();

        customerCOMBO.setItems(customers);
        contactCOMBO.setItems(contacts);
        userIdCOMBO.setItems(users);

        LocalTime easternStart = LocalTime.of(8, 0);
        LocalTime easternEnd = LocalTime.of(22, 0);
        LocalTime localStart = convertEasternToLocal(easternStart);
        LocalTime localEnd = convertEasternToLocal(easternEnd);

        while (localStart.isBefore(localEnd.plusSeconds(1))) {
            startTimeCOMBO.getItems().add(localStart);
            endTimeCOMBO.getItems().add(localStart);
            localStart = localStart.plusMinutes(15);
        }

    }

}