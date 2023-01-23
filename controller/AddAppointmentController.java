package controller;


import helper.AppointmentsQuery;
import helper.CustomersQuery;
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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Appointments.*;

/**
 * This class creates a form to add an appointment.
 *
 * @author Louie Sanchez
 *
 */
public class AddAppointmentController implements Initializable {

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

    /** This creates a new appointment. After creating a new appointment, a user is redirected back to the main form. */
    @FXML
    void onActionSaveAppointment(ActionEvent event) throws IOException, SQLException {

        try {
            Boolean appointmentRequirementsMet = false;

            String title = appointmentTitleTXT.getText();
            String description = appointmentDescriptionTXT.getText();
            String location = appointmentLocationTXT.getText();
            int contact = contactCOMBO.getSelectionModel().getSelectedItem().getContactID();
            String type = appointmentTypeTXT.getText();

            java.time.LocalDate localStartDate = startDATEPICKER.getValue();
            java.time.LocalTime localStartTime = (LocalTime) startTimeCOMBO.getSelectionModel().getSelectedItem();
            java.time.LocalDateTime localStartDateTime = LocalDateTime.of(localStartDate, localStartTime);
            java.time.LocalDateTime utcStartDateTime = convertLocalToUTC(localStartDateTime);
            java.sql.Timestamp utcStartTimestamp = Timestamp.valueOf(utcStartDateTime);

            java.time.LocalDate localEndDate = endDATEPICKER.getValue();
            java.time.LocalTime localEndTime = (LocalTime) endTimeCOMBO.getSelectionModel().getSelectedItem();
            java.time.LocalDateTime localEndDateTime = LocalDateTime.of(localEndDate, localEndTime);
            java.time.LocalDateTime utcEndDateTime = convertLocalToUTC(localEndDateTime);
            java.sql.Timestamp utcEndTimestamp = Timestamp.valueOf(utcEndDateTime);

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
                    if ((localStartDateTime.isAfter(appointment.getStartDateTime()) && localStartDateTime.isBefore(appointment.getEndDateTime())) || (localEndDateTime.isAfter(appointment.getStartDateTime()) && localEndDateTime.isBefore(appointment.getEndDateTime())) || (appointment.getStartDateTime().isAfter(localStartDateTime) && appointment.getStartDateTime().isBefore(localEndDateTime)) || (appointment.getStartDateTime().isAfter(localStartDateTime) && appointment.getStartDateTime().isBefore(localEndDateTime)) || (localStartDateTime.equals(appointment.getStartDateTime())) || (localEndDateTime.equals(appointment.getEndDateTime()))) {
                        appointmentRequirementsMet = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Overlapping appointment schedule.");
                        alert.showAndWait();
                        break;
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
                AppointmentsQuery.insert(title, description, location, contact, type, utcStartTimestamp, utcEndTimestamp, customerId, userId);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(SchedulingApplication.class.getResource("main-view.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (NullPointerException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select values from the combo boxes.");
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

        java.time.LocalTime easternStart = LocalTime.of(8, 0);
        java.time.LocalTime easternEnd = LocalTime.of(22, 0);

        java.time.LocalDateTime easternDateTimeStart = LocalDateTime.of(selectedDate, easternStart);
        java.time.LocalDateTime easternDateTimeEnd = LocalDateTime.of(selectedDate, easternEnd);

        java.time.LocalTime localStart = convertEasternToLocal(easternDateTimeStart);
        java.time.LocalTime localEnd = convertEasternToLocal(easternDateTimeEnd);

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

        java.time.LocalTime easternStart = LocalTime.of(8, 0);
        java.time.LocalTime easternEnd = LocalTime.of(22, 0);

        java.time.LocalDateTime easternDateTimeStart = LocalDateTime.of(selectedDate, easternStart);
        java.time.LocalDateTime easternDateTimeEnd = LocalDateTime.of(selectedDate, easternEnd);

        java.time.LocalTime localStart = convertEasternToLocal(easternDateTimeStart);
        java.time.LocalTime localEnd = convertEasternToLocal(easternDateTimeEnd);

        while (localStart.isBefore(localEnd.plusSeconds(1))) {
            endTimeCOMBO.getItems().add(localStart);
            localStart = localStart.plusMinutes(15);
        }

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

        java.time.LocalTime easternStart = LocalTime.of(8, 0);
        java.time.LocalTime easternEnd = LocalTime.of(22, 0);
        java.time.LocalTime localStart = convertEasternToLocal(easternStart);
        java.time.LocalTime localEnd = convertEasternToLocal(easternEnd);

        while (localStart.isBefore(localEnd.plusSeconds(1))) {
            startTimeCOMBO.getItems().add(localStart);
            endTimeCOMBO.getItems().add(localStart);
            localStart = localStart.plusMinutes(15);
        }

    }

}