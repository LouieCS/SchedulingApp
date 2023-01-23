package helper;

import model.Appointments;

import java.sql.*;
import java.time.LocalDateTime;

import static model.Appointments.*;

/**
 * This class creates a helper to query appointments in the database.
 *
 * @author Louie Sanchez
 *
 */
public abstract class AppointmentsQuery {

    public static void select() throws SQLException {
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contact = rs.getInt("Contact_ID");
            String type = rs.getString("Type");

            java.sql.Date utcStartDate = rs.getDate("Start");
            java.sql.Time utcStartTime = rs.getTime("Start");
            java.time.LocalDateTime utcStartDateTimeLDT = LocalDateTime.of(utcStartDate.toLocalDate(), utcStartTime.toLocalTime());
            java.time.LocalDateTime localStartLDT = convertUTCToLocal(utcStartDateTimeLDT);

            java.sql.Date utcEndDate = rs.getDate("End");
            java.sql.Time utcEndTime = rs.getTime("End");
            java.time.LocalDateTime utcEndDateTimeLDT = LocalDateTime.of(utcEndDate.toLocalDate(), utcEndTime.toLocalTime());
            java.time.LocalDateTime localEndLDT = convertUTCToLocal(utcEndDateTimeLDT);

            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");

            Appointments.addAppointment(new Appointments(appointmentId, title, description, location, contact, type, localStartLDT, localEndLDT, customerId, userId));
        }
    }

    public static void insert(String title, String description, String location, int contactID, String type, java.sql.Timestamp startDateTime, java.sql.Timestamp endDateTime, int customerID, int userID) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setInt(4, contactID);
        ps.setString(5, type);
        ps.setTimestamp(6, startDateTime);
        ps.setTimestamp(7, endDateTime);
        ps.setInt(8, customerID);
        ps.setInt(9, userID);
        ps.executeUpdate();
    }

    public static void update(int appointmentID, String title, String description, String location, int contactID, String type, java.sql.Timestamp startDateTime, java.sql.Timestamp endDateTime, int customerID, int userID) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Contact_ID = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setInt(4, contactID);
        ps.setString(5, type);
        ps.setTimestamp(6, startDateTime);
        ps.setTimestamp(7, endDateTime);
        ps.setInt(8, customerID);
        ps.setInt(9, userID);
        ps.setInt(10, appointmentID);
        ps.executeUpdate();
    }

    public static void delete(int appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ps.executeUpdate();
    }

}
