package lsc195.lsc195;

import helper.AppointmentsQuery;
import helper.CustomersQuery;
import helper.DBMethods;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** This class creates a scheduling app that pulls and pushes data from and to a database.
 *
 * JavaDoc is located at LSC195\javadoc
 *
 * @author Louie Sanchez
 *
 */
public class SchedulingApplication extends Application {

    /** This loads the main form as the initial scene when the program is run. */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SchedulingApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 320);
        stage.setTitle("Scheduling Database");
        stage.setScene(scene);
        stage.show();
    }

    /** This is the main method. This is the first method that gets called when the program is run.
     * @param args
     */
    public static void main(String[] args) throws SQLException {

        JDBC.openConnection();

        launch();

        JDBC.closeConnection();

    }
}