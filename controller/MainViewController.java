package controller;

import helper.AppointmentsQuery;
import helper.DBMethods;
import helper.CustomersQuery;
import helper.DBMethods;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lsc195.lsc195.AlertInterface;
import lsc195.lsc195.ReportInterface;
import lsc195.lsc195.SchedulingApplication;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.*;
import static model.Appointments.getAllAppointments;
import static model.Appointments.lookupAppointment;
import static model.Contacts.getAllContacts;
import static model.Customers.getAllCustomers;
import static model.Customers.lookupCustomer;


/** This class creates the main view of the application where a user can view, modify, and delete appointments and customers.
 *
 * @author Louie Sanchez
 *
 */
public class MainViewController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Tab appointmentsTab;
    @FXML
    private Tab customersTab;
    @FXML
    private TextField searchTableTXT;
    @FXML
    private TableView<Appointments> appointmentsTBL;
    @FXML
    private TableColumn<Appointments, Integer> appointmentIdCOL;
    @FXML
    private TableColumn<Appointments, String> appointmentTitleCOL;
    @FXML
    private TableColumn<Appointments, String> appointmentDescriptionCOL;
    @FXML
    private TableColumn<Appointments, String> appointmentLocationCOL;
    @FXML
    private TableColumn<Appointments, String> appointmentContactCOL;
    @FXML
    private TableColumn<Appointments, String> appointmentTypeCOL;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentStartCOL;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> appointmentEndCOL;
    @FXML
    private TableColumn<Appointments, Integer> appointmentCustomerIdCOL;
    @FXML
    private TableColumn<Appointments, Integer> appointmentUserIdCOL;
    @FXML
    private TableView<Customers> customerTBL;
    @FXML
    private TableColumn<Customers, Integer> customerIdCOL;
    @FXML
    private TableColumn<Customers, String> customerNameCOL;
    @FXML
    private TableColumn<Customers, String> customerAddressCOL;
    @FXML
    private TableColumn<Customers, String> customerPostalCodeCOL;
    @FXML
    private TableColumn<Customers, String> customerPhoneNumCOL;
    @FXML
    private TableColumn<Customers, Integer> customerDivisionIdCOL;
    @FXML
    private Label filterLBL;
    @FXML
    private RadioButton filterWeekRADIO;
    @FXML
    private ToggleGroup filterAppointmentsRADIOGROUP;
    @FXML
    private RadioButton filterMonthRADIO;
    @FXML
    private RadioButton filterNoneRADIO;
    @FXML
    private Label numberOfAppointmentLBL;
    @FXML
    private Label interfaceApptMessageLBL;
    @FXML
    private Tab scheduleTab;
    @FXML
    private ComboBox<Contacts> contactCOMBO;
    @FXML
    private TextFlow scheduleTXTFLOW;
    @FXML
    private Text scheduleTXT;
    @FXML
    private Tab contactsTab;
    @FXML
    private TableView<Contacts> contactTBL;
    @FXML
    private TableColumn<Contacts, Integer> contactIdCOL;
    @FXML
    private TableColumn<Contacts, String> contactNameCOL;
    @FXML
    private TableColumn<Contacts, String> contactEmailCOL;
    @FXML
    private Button addBTN;
    @FXML
    private Button updateBTN;
    @FXML
    private Button deleteBTN;
    @FXML
    private ToggleGroup scheduleToggleGroup;
    @FXML
    private ToggleGroup scheduleToggleGroup1;
    @FXML
    private ComboBox<Customers> customerCOMBO;

    /** This lets a user search the Appointments or Customer tables. */
    @FXML
    void onActionSearchTable(ActionEvent event) {
        if (customersTab.isSelected()) {
            try {
                String query = searchTableTXT.getText();
                ObservableList<Customers> customers = lookupCustomer(query);

                if (customers.size() == 0) {
                    int idQuery = Integer.parseInt(query);
                    customers = lookupCustomer(idQuery);
                }

                customerTBL.setItems(customers);
                searchTableTXT.setText("");

                if (customers.size() == 0) {
                    customerTBL.setItems(getAllCustomers());
                    searchTableTXT.setText("");
                }
            } catch (NumberFormatException exception) {
                customerTBL.setItems(getAllCustomers());
                searchTableTXT.setText("");
            }
        }
        else {
            try {
                String query = searchTableTXT.getText();
                int idQuery = Integer.parseInt(query);
                ObservableList<Appointments> appointments = lookupAppointment(idQuery);

                appointmentsTBL.setItems(appointments);
                numberOfAppointmentLBL.setText("Number of appointments: " + appointments.size());
                searchTableTXT.setText("");

                if (appointments.size() == 0) {
                    appointmentsTBL.setItems(getAllAppointments());
                    numberOfAppointmentLBL.setText("Number of appointments: " + getAllAppointments().size());
                    searchTableTXT.setText("");
                }
            } catch (NumberFormatException exception) {
                appointmentsTBL.setItems(getAllAppointments());
                numberOfAppointmentLBL.setText("Number of appointments: " + getAllAppointments().size());
                searchTableTXT.setText("");
            }
        }
    }

    /** This changes the main screen when a user selects the Appointments tab. */
    @FXML
    void onSelectionAppointments() {
        try {
            searchTableTXT.setPromptText("Search by Customer ID");
            searchTableTXT.setVisible(true);
            filterLBL.setVisible(true);
            filterMonthRADIO.setVisible(true);
            filterWeekRADIO.setVisible(true);
            filterNoneRADIO.setVisible(true);
            numberOfAppointmentLBL.setVisible(true);
            interfaceApptMessageLBL.setVisible(true);
            addBTN.setVisible(true);
            updateBTN.setVisible(true);
            deleteBTN.setVisible(true);
        } catch (NullPointerException exception) {}
    }

    /** This changes the main screen when a user selects the Customers tab. */
    @FXML
    void onSelectionCustomers() {
        searchTableTXT.setPromptText("Search by ID or name");
        searchTableTXT.setVisible(true);
        filterLBL.setVisible(false);
        filterMonthRADIO.setVisible(false);
        filterWeekRADIO.setVisible(false);
        filterNoneRADIO.setVisible(false);
        numberOfAppointmentLBL.setVisible(false);
        interfaceApptMessageLBL.setVisible(false);
        addBTN.setVisible(true);
        updateBTN.setVisible(true);
        deleteBTN.setVisible(true);
    }

    /** This changes the main screen when a user selects the Schedule tab. */
    @FXML
    void onSelectionSchedule() {
        searchTableTXT.setVisible(false);
        filterLBL.setVisible(false);
        filterMonthRADIO.setVisible(false);
        filterWeekRADIO.setVisible(false);
        filterNoneRADIO.setVisible(false);
        numberOfAppointmentLBL.setVisible(false);
        interfaceApptMessageLBL.setVisible(false);
        addBTN.setVisible(false);
        updateBTN.setVisible(false);
        deleteBTN.setVisible(false);
    }

    /** This changes the main screen when a user selects the Contacts tab. */
    @FXML
    void onSelectionContacts() {
        searchTableTXT.setVisible(false);
        filterLBL.setVisible(false);
        filterMonthRADIO.setVisible(false);
        filterWeekRADIO.setVisible(false);
        filterNoneRADIO.setVisible(false);
        numberOfAppointmentLBL.setVisible(false);
        interfaceApptMessageLBL.setVisible(false);
        addBTN.setVisible(false);
        updateBTN.setVisible(false);
        deleteBTN.setVisible(false);
    }

    /** This filters the Appointments table and reports the number of appointments in the current week. */
    @FXML
    public void onActionFilterByWeek(ActionEvent actionEvent) {
        java.time.LocalDateTime localDateTime = LocalDateTime.now();

        ObservableList<Appointments> appointmentsFilter = FXCollections.observableArrayList();
        ObservableList<Appointments> allAppointments = Appointments.getAllAppointments();

        for(Appointments appointment : allAppointments) {
            if((localDateTime.getYear() == appointment.getStartDateTime().getYear()) && (localDateTime.getMonth() == appointment.getStartDateTime().getMonth())) {

                if(localDateTime.getDayOfWeek() == SUNDAY) {
                    if(((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) < 7) && ((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) >= 0)){
                        appointmentsFilter.add(appointment);
                    }
                }
                else if (localDateTime.getDayOfWeek() == MONDAY) {
                    if(((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) < 6) && ((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) >= -1)){
                        appointmentsFilter.add(appointment);
                    }
                }
                else if (localDateTime.getDayOfWeek() == TUESDAY) {
                    if(((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) < 5) && ((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) >= -2)){
                        appointmentsFilter.add(appointment);
                    }
                }
                else if (localDateTime.getDayOfWeek() == WEDNESDAY) {
                    if(((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) < 4) && ((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) >= -3)){
                        appointmentsFilter.add(appointment);
                    }
                }
                else if (localDateTime.getDayOfWeek() == THURSDAY) {
                    if(((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) < 3) && ((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) >= -4)){
                        appointmentsFilter.add(appointment);
                    }
                }
                else if (localDateTime.getDayOfWeek() == FRIDAY) {
                    if(((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) < 2) && ((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) >= -5)){
                        appointmentsFilter.add(appointment);
                    }
                }
                else if (localDateTime.getDayOfWeek() == SATURDAY) {
                    if(((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) < 1) && ((appointment.getStartDateTime().getDayOfMonth() - localDateTime.getDayOfMonth()) >= -6)){
                        appointmentsFilter.add(appointment);
                    }
                }
            }
        }

        appointmentsTBL.setItems(appointmentsFilter);
        numberOfAppointmentLBL.setText("Number of appointments: " + appointmentsFilter.size());
    }

    /** This filters the Appointments table and reports the number of appointments in the current month. */
    @FXML
    public void onActionFilterByMonth(ActionEvent actionEvent) {
        java.time.LocalDateTime localDateTime = LocalDateTime.now();

        ObservableList<Appointments> appointmentsFilter = FXCollections.observableArrayList();
        ObservableList<Appointments> allAppointments = Appointments.getAllAppointments();

        for(Appointments appointment : allAppointments) {
            if(localDateTime.getMonth() == appointment.getStartDateTime().getMonth()) {
                appointmentsFilter.add(appointment);
            }
        }

        appointmentsTBL.setItems(appointmentsFilter);
        numberOfAppointmentLBL.setText("Number of appointments: " + appointmentsFilter.size());
    }

    /** This resets the Appointments table and reports the number of appointments in the table. */
    @FXML
    public void onActionFilterByNone(ActionEvent actionEvent) {
        appointmentsTBL.setItems(getAllAppointments());
        numberOfAppointmentLBL.setText("Number of appointments: " + getAllAppointments().size());
    }

    /** This allows a user to select an item from the Contact combo box in the Schedule tab. */
    @FXML
    public void onActionContactComboEnable(ActionEvent event){
        contactCOMBO.setDisable(false);
        customerCOMBO.setDisable(true);
        customerCOMBO.getSelectionModel().clearSelection();
        scheduleTXT.setText("");
    }

    /** This allows a user to select an item from the Customer combo box in the Schedule tab. */
    @FXML
    public void onActionCustomerComboEnable(ActionEvent event){
        customerCOMBO.setDisable(false);
        contactCOMBO.setDisable(true);
        contactCOMBO.getSelectionModel().clearSelection();
        scheduleTXT.setText("");
    }

    /** This reports the number of appointments in each month and the number of appointments by type. */
    @FXML
    public  void onActionScheduleMonthType(ActionEvent event){
        contactCOMBO.setDisable(true);
        contactCOMBO.getSelectionModel().clearSelection();
        customerCOMBO.setDisable(true);
        customerCOMBO.getSelectionModel().clearSelection();
        scheduleTXT.setText("");

        StringBuilder stringBuilder = new StringBuilder();
        String finalSchedule;

        int apptsJanuary = 0;
        int apptsFebruary = 0;
        int apptsMarch = 0;
        int apptsApril = 0;
        int apptsMay = 0;
        int apptsJune = 0;
        int apptsJuly = 0;
        int apptsAugust = 0;
        int apptsSeptember = 0;
        int apptsOctober = 0;
        int apptsNovember = 0;
        int apptsDecember = 0;

        for (Appointments appointments : Appointments.getAllAppointments()) {
            if (appointments.getStartDateTime().getMonth().equals(Month.JANUARY)){
                apptsJanuary +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.FEBRUARY)){
                apptsFebruary +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.MARCH)){
                apptsMarch +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.APRIL)){
                apptsApril +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.MAY)){
                apptsMay +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.JUNE)){
                apptsJune +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.JULY)){
                apptsJuly +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.AUGUST)){
                apptsAugust +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.SEPTEMBER)){
                apptsSeptember +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.OCTOBER)){
                apptsOctober +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.NOVEMBER)){
                apptsNovember +=1;
            }
            if (appointments.getStartDateTime().getMonth().equals(Month.DECEMBER)){
                apptsDecember +=1;
            }
        }

        stringBuilder.append("# Of Appointments Per Month" + "\n" +
                "January:\t\t" + apptsJanuary + "\n" +
                "February:\t\t" + apptsFebruary + "\n" +
                "March:\t\t" + apptsMarch + "\n" +
                "April:\t\t" + apptsApril + "\n" +
                "May:\t\t\t" + apptsMay + "\n" +
                "June:\t\t" + apptsJune + "\n" +
                "July:\t\t\t" + apptsJuly + "\n" +
                "August:\t\t" + apptsAugust + "\n" +
                "September:\t" + apptsSeptember + "\n" +
                "October:\t\t" + apptsOctober + "\n" +
                "November:\t" + apptsNovember + "\n" +
                "December:\t" + apptsDecember + "\n\n");

        stringBuilder.append("# Of Appointments Per Type\n");
        ObservableList appointmentType = FXCollections.observableArrayList();
        for (Appointments appointment : Appointments.getAllAppointments()) {
            int typeCounter = 0;
            if (!appointmentType.contains(appointment.getType())) {
                appointmentType.add(appointment.getType());
                for (Appointments checkType: Appointments.getAllAppointments()) {
                    if (appointment.getType().equals(checkType.getType())) {
                        typeCounter += 1;
                    }
                }
                stringBuilder.append(appointment.getType() + ":\t" + typeCounter + "\n");
            }
        }

        finalSchedule = stringBuilder.toString();
        scheduleTXT.setText(finalSchedule);
    }

    /** This reports the all appointments associated with the selected contact. */
    @FXML
    public void onActionScheduleContactSelect(ActionEvent actionEvent) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String finalSchedule;
            int contactID = contactCOMBO.getSelectionModel().getSelectedItem().getContactID();
            int numberOfApptsCounter = 0;
            ReportInterface totalMessage = n -> "Total # of Appointments is:\t" + n + "\n";

            for (Appointments appointment : Appointments.getAllAppointments()) {
                if (appointment.getContact() == contactID) {
                    String apptID = String.valueOf(appointment.getAppointmentId());
                    String apptTitle = appointment.getTitle();
                    String apptType = appointment.getType();
                    String apptDescription = appointment.getDescription();
                    String apptStartTime = String.valueOf(appointment.getStartDateTime());
                    String apptEndTime = String.valueOf(appointment.getEndDateTime());
                    String apptCustomerID = String.valueOf(appointment.getCustomerId());

                    stringBuilder.append("Appointment ID:\t" + apptID + "\n" +
                            "Title:\t\t\t\t" + apptTitle + "\n" +
                            "Type:\t\t\t" + apptType + "\n" +
                            "Description:\t\t" + apptDescription + "\n" +
                            "Time:\t\t\t" + apptStartTime + " to " + apptEndTime + "\n" +
                            "Customer ID:\t\t" + apptCustomerID + "\n\n");

                    numberOfApptsCounter += 1;
                }
            }

            stringBuilder.append(totalMessage.totalAppointment(numberOfApptsCounter));
            finalSchedule = stringBuilder.toString();
            scheduleTXT.setText(finalSchedule);

        } catch (NullPointerException exception) {}
    }

    /** This reports the all appointments associated with the selected customer. */
    @FXML
    public void onActionScheduleCustomerSelect(ActionEvent actionEvent) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String finalSchedule;
            int customerID = customerCOMBO.getSelectionModel().getSelectedItem().getId();
            int numberOfApptsCounter = 0;
            ReportInterface totalMessage = n -> "Total # of Appointments is:\t" + n + "\n";

            for (Appointments appointment : Appointments.getAllAppointments()) {
                if (appointment.getCustomerId() == customerID) {
                    String apptID = String.valueOf(appointment.getAppointmentId());
                    String apptTitle = appointment.getTitle();
                    String apptType = appointment.getType();
                    String apptDescription = appointment.getDescription();
                    String apptStartTime = String.valueOf(appointment.getStartDateTime());
                    String apptEndTime = String.valueOf(appointment.getEndDateTime());
                    String apptContactID = String.valueOf(appointment.getContact());

                    stringBuilder.append("Appointment ID:\t" + apptID + "\n" +
                            "Title:\t\t\t\t" + apptTitle + "\n" +
                            "Type:\t\t\t" + apptType + "\n" +
                            "Description:\t\t" + apptDescription + "\n" +
                            "Time:\t\t\t" + apptStartTime + " to " + apptEndTime + "\n" +
                            "Contact ID:\t\t" + apptContactID + "\n\n");

                    numberOfApptsCounter += 1;
                }
            }

            stringBuilder.append(totalMessage.totalAppointment(numberOfApptsCounter));
            finalSchedule = stringBuilder.toString();
            scheduleTXT.setText(finalSchedule);

        } catch (NullPointerException exception) {}
    }

    /** This directs a user to the add form. */
    @FXML
    void onActionAdd(ActionEvent event) throws IOException{
        if (customersTab.isSelected()) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(SchedulingApplication.class.getResource("addcustomer-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(SchedulingApplication.class.getResource("addappointment-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This directs a user to the modify form. */
    @FXML
    void onActionUpdate(ActionEvent event) throws IOException{
        if (customersTab.isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(SchedulingApplication.class.getResource("modifycustomer-view.fxml"));
                loader.load();

                ModifyCustomerController MCController = loader.getController();
                MCController.sendCustomer(customerTBL.getSelectionModel().getSelectedItem());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            } catch(NullPointerException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select an item in the table.");
                alert.showAndWait();
            }
        }
        else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(SchedulingApplication.class.getResource("modifyappointment-view.fxml"));
                loader.load();

                ModifyAppointmentController MPMController = loader.getController();
                MPMController.sendAppointment(appointmentsTBL.getSelectionModel().getSelectedItem());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            } catch(NullPointerException exception) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select an item in the table.");
                alert.showAndWait();
            }
        }
    }

    /** This deletes a user selected record from a table. */
    @FXML
    void onActionDelete(ActionEvent event) {
        AlertInterface message = (i, r) -> "ID " + i + " (" + r + ") was successfully deleted.";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Proceeding will delete this record. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            if (customersTab.isSelected()) {
                try {
                    if (customerTBL.getSelectionModel().getSelectedItem() == null) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Customer record was not deleted because a record was not selected.");
                        alert.showAndWait();

                    } else {
                        Customers selectedCustomer = customerTBL.getSelectionModel().getSelectedItem();
                        int selectedCustomerNumOfAppointments = 0;

                        for (Appointments appointments : getAllAppointments()) {
                            if (appointments.getCustomerId() == selectedCustomer.getId()) {
                                selectedCustomerNumOfAppointments++;
                            }
                        }

                        if (selectedCustomerNumOfAppointments == 0) {
                            String customerName = selectedCustomer.getName();
                            int customerID = selectedCustomer.getId();

                            Customers.deleteCustomer(selectedCustomer);
                            customerTBL.setItems(getAllCustomers());

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Deleted");
                            alert.setContentText(message.deleteMessage(customerID, customerName));
                            alert.show();
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("The selected customer record was not deleted. Please delete any associated appointments with the customer first.");
                            alert.showAndWait();
                        }
                    }
                } catch (NullPointerException exception) {
                    System.out.println(exception);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    if (appointmentsTBL.getSelectionModel().getSelectedItem() == null) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Appointment was not deleted because a row was not selected.");
                        alert.showAndWait();
                    } else {
                        Appointments selectedAppointment = appointmentsTBL.getSelectionModel().getSelectedItem();
                        String appointmentTitle = selectedAppointment.getTitle();
                        int appointmentId = selectedAppointment.getAppointmentId();
                        Appointments.deleteAppointment(selectedAppointment);
                        appointmentsTBL.setItems(getAllAppointments());

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Deleted");
                        alert.setContentText(message.deleteMessage(appointmentId, appointmentTitle));
                        alert.show();
                    }
                } catch (NullPointerException exception) {
                    System.out.println(exception);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /** This directs a user to the login form. */
    @FXML
    void onActionSignOut(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(SchedulingApplication.class.getResource("login-view.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** This exits the application. */
    @FXML
    void onActionExitApplication(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will exit the application. Do you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /** This initializes tables and combo boxes. This also checks to see if there are any appointments scheduled within the next 15 minutes a user logs in. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            DBMethods.retrieveAllDatabaseTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        interfaceApptMessageLBL.setText("# of Appointments in the next 15 minutes: 0");
        ObservableList<Appointments> appointmentsInFifteenMins = FXCollections.observableArrayList();
        ObservableList<Appointments> allAppointments = Appointments.getAllAppointments();
        for(Appointments appointment : allAppointments) {
            if((appointment.getStartDateTime().toLocalDate().equals(LocalDate.now())) && (appointment.getStartDateTime().toLocalTime().isBefore(LocalTime.now().plusMinutes(15))) && (appointment.getStartDateTime().toLocalTime().isAfter(LocalTime.now().minusMinutes(1)))) {
                appointmentsInFifteenMins.add(appointment);
                interfaceApptMessageLBL.setText("# of Appointments in the next 15 minutes: " + appointmentsInFifteenMins.size());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment");
                alert.setContentText("Appointment (ID: " + appointment.getAppointmentId() + ") scheduled for " + appointment.getStartDateTime().toLocalDate() + " at " + appointment.getStartDateTime().toLocalTime() + " is within 15 minutes of now.");
                alert.show();
            }
        }

        appointmentsTBL.setItems(getAllAppointments());
        numberOfAppointmentLBL.setText("Total # of appointments: " + getAllAppointments().size());
        appointmentIdCOL.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleCOL.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionCOL.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationCOL.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactCOL.setCellValueFactory(new PropertyValueFactory<>("contact"));
        appointmentTypeCOL.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartCOL.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        appointmentEndCOL.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        appointmentCustomerIdCOL.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentUserIdCOL.setCellValueFactory(new PropertyValueFactory<>("userId"));

        customerTBL.setItems(getAllCustomers());
        customerIdCOL.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCOL.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCOL.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeCOL.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneNumCOL.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerDivisionIdCOL.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        contactTBL.setItems(getAllContacts());
        contactIdCOL.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        contactNameCOL.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        contactEmailCOL.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));

        ObservableList<Contacts> contacts = Contacts.getAllContacts();
        contactCOMBO.setItems(contacts);

        ObservableList<Customers> customers = Customers.getAllCustomers();
        customerCOMBO.setItems(customers);

    }

}