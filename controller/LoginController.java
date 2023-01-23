package controller;

import helper.LoginQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lsc195.lsc195.SchedulingApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static model.Appointments.convertLocalToUTC;


/** This class creates a login form for a user to log in into the application.
 *
 * @author Louie Sanchez
 *
 */
public class LoginController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField usernameTXT;
    @FXML
    private TextField passwordTXT;
    @FXML
    private Button loginBTN;
    @FXML
    private Button exitBTN;
    @FXML
    private Label loginTitleLBL;
    @FXML
    private Label instructionLBL;
    @FXML
    private Label usernameLBL;
    @FXML
    private Label passwordLBL;
    @FXML
    private Label locationLBL;

    /** This exits the application. */
    @FXML
    void onActionExitApplication(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will exit the application. Do you want to continue?");

        try {
            ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                alert.setContentText(rb.getString("This") + " " + rb.getString("will") + " " + rb.getString("exit") + " " + rb.getString("the") + " " + rb.getString("application") + ". " + rb.getString("Do") + " " + rb.getString("you") + " " + rb.getString("want") + " " + rb.getString("to") + " " + rb.getString("continue") + "?");
            }
        }
        catch (Exception e) {}

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /** This directs a user to the main view after successfully entering both username and password. */
    @FXML
    void onActionLogIn(ActionEvent event) throws IOException, SQLException {
        String filename = "login_activity.txt", item;
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);

        String usernameInput = usernameTXT.getText();
        String passwordInput = passwordTXT.getText();
        if (LoginQuery.correctLoginInfo(usernameInput, passwordInput) == true) {
            LocalDateTime utc = convertLocalToUTC(LocalDateTime.now());
            outputFile.println("SUCCESSFUL LOGIN\t" + utc.toLocalDate() + "\t" + utc.toLocalTime() + "\n");
            outputFile.close();

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(SchedulingApplication.class.getResource("main-view.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else {
            LocalDateTime utc = convertLocalToUTC(LocalDateTime.now());
            outputFile.println("UNSUCCESSFUL LOGIN\t" + utc.toLocalDate() + "\t" + utc.toLocalTime() + "\n");
            outputFile.close();

                Alert alert = new Alert(Alert.AlertType.ERROR, "Incorrect username and/or password.");

                try {
                    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
                    if (Locale.getDefault().getLanguage().equals("fr")) {
                        alert.setContentText(rb.getString("Incorrect") + " " + rb.getString("username") + " " + rb.getString("and") + "/" + rb.getString("or") + " " + rb.getString("password") + ".");
                    }
                } catch (Exception e) {}

                Optional<ButtonType> result = alert.showAndWait();
        }

    }

    /** This translates the application if the locale default is French. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        java.time.ZoneId localZoneID = ZoneId.systemDefault();
        locationLBL.setText("(" + String.valueOf(localZoneID) + ")");
        try {
            rb = ResourceBundle.getBundle("Nat", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) {
                loginTitleLBL.setText(rb.getString("Scheduling"));
                instructionLBL.setText(rb.getString("Please") + " " + rb.getString("enter") + " " + rb.getString("your") + " " + rb.getString("username") + " " + rb.getString("and") + " " + rb.getString("password") + ".");
                usernameLBL.setText(rb.getString("username"));
                passwordLBL.setText(rb.getString("password"));
                loginBTN.setText(rb.getString("Log") + " " + rb.getString("In"));
                exitBTN.setText(rb.getString("Exit"));
            }
        } catch(MissingResourceException e){
            System.out.println(e);
        }

    }

}