package controller;


import helper.CustomersQuery;
import javafx.collections.FXCollections;
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
import model.Countries;
import model.Customers;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class creates a form to modify an existing customer.
 *
 * @author Louie Sanchez
 *
 */
public class ModifyCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField customerIdTXT;
    @FXML
    private TextField customerNameTXT;
    @FXML
    private TextField customerAddressTXT;
    @FXML
    private TextField customerPostalCodeTXT;
    @FXML
    private TextField customerPhoneNumberTXT;
    @FXML
    private ComboBox<FirstLevelDivisions> customer1stDivisionCOMBO;
    @FXML
    private ComboBox<Countries> customerCountryCOMBO;


    /** This updates an existing customer. After updating the customer record, a user is redirected back to the main form. */
    @FXML
    void onActionSaveCustomer(ActionEvent event) throws IOException, SQLException {
        try {
            Boolean appointmentRequirementsMet;

            int id = Integer.parseInt(customerIdTXT.getText());
            String name = customerNameTXT.getText();
            String address = customerAddressTXT.getText();
            String postalCode = customerPostalCodeTXT.getText();
            String phoneNumber = customerPhoneNumberTXT.getText();
            int divisionId = customer1stDivisionCOMBO.getSelectionModel().getSelectedItem().getDivisionID();

            if (name.isBlank() || address.isBlank() || postalCode.isBlank() || phoneNumber.isBlank()) {
                appointmentRequirementsMet = false;

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter valid values into the text fields. The text fields cannot be blank.");
                alert.showAndWait();
            }
            else {
                appointmentRequirementsMet = true;
            }

            if (appointmentRequirementsMet.equals(true)) {
                CustomersQuery.update(id, name, address, postalCode, phoneNumber, divisionId);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(SchedulingApplication.class.getResource("main-view.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch(NullPointerException exception) {
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

        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(SchedulingApplication.class.getResource("main-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This filters the Country combo box based on the user's selection of the 1st Division combo box. */
    @FXML
    void onActionFirstLvlDivSelectionFilter(ActionEvent event) {
        try {
//            ObservableList<FirstLevelDivisions> filterFirstLevelDivisions = FXCollections.observableArrayList();
            ObservableList<Countries> filterCountries = FXCollections.observableArrayList();
            for (Countries country : Countries.getAllCountries()) {
                if (country.getCountryID() == customer1stDivisionCOMBO.getSelectionModel().getSelectedItem().getCountryID()) {
                    customerCountryCOMBO.setValue(country);
                }
            }
        } catch (NullPointerException e){}
    }

    /** This filters the 1st Division combo box based on the user's selection of the Country combo box. */
    @FXML
    void onActionCountrySelectionFilter(ActionEvent event) {
        try {
            ObservableList<FirstLevelDivisions> filterFirstLevelDivisions = FXCollections.observableArrayList();
//        ObservableList<Countries> filterCountries = null;
            for (Countries country : Countries.getAllCountries()) {
                if (country.getCountryID() == customerCountryCOMBO.getSelectionModel().getSelectedItem().getCountryID()) {
                    for (FirstLevelDivisions fld : FirstLevelDivisions.getAllFirstLevelDivisions()) {
                        if (fld.getCountryID() == country.getCountryID()) {
                            filterFirstLevelDivisions.add(fld);
                        }
                    }
                    customer1stDivisionCOMBO.setItems(filterFirstLevelDivisions);
                }
            }
        } catch (NullPointerException e){}
    }

    /** This obtains the current information of an existing customer and displays it in the form.
     * @param selectedCustomer the customer to obtain information about
     */
    public void sendCustomer(Customers selectedCustomer) {

        customerIdTXT.setText(String.valueOf(selectedCustomer.getId()));
        customerNameTXT.setText(String.valueOf(selectedCustomer.getName()));
        customerAddressTXT.setText(String.valueOf(selectedCustomer.getAddress()));
        customerPostalCodeTXT.setText(String.valueOf(selectedCustomer.getPostalCode()));
        customerPhoneNumberTXT.setText(String.valueOf(selectedCustomer.getPhoneNumber()));


        FirstLevelDivisions selectedCustomerFLD = null;
        for (FirstLevelDivisions fld : FirstLevelDivisions.getAllFirstLevelDivisions()) {
            if (fld.getDivisionID() == selectedCustomer.getDivisionId()) {
                selectedCustomerFLD = fld;
                break;
            }
        }
        customer1stDivisionCOMBO.setValue(selectedCustomerFLD);


        Countries selectedCustomerCountry = null;
        for (FirstLevelDivisions fld : FirstLevelDivisions.getAllFirstLevelDivisions()) {
            if (fld.getDivisionID() == selectedCustomer.getDivisionId()) {
                for (Countries country : Countries.getAllCountries()) {
                    if (fld.getCountryID() == country.getCountryID()) {
                        selectedCustomerCountry = country;
                        break;
                    }
                }
            }
        }
        customerCountryCOMBO.setValue(selectedCustomerCountry);

    }

    /** This initializes the 1st Division and Country combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<FirstLevelDivisions> firstLevelDivisions = FirstLevelDivisions.getAllFirstLevelDivisions();
        ObservableList<Countries> countries = Countries.getAllCountries();

        customer1stDivisionCOMBO.setItems(firstLevelDivisions);
        customerCountryCOMBO.setItems(countries);

    }

}