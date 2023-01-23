package helper;

import model.Contacts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class creates a helper to query contacts in the database.
 *
 * @author Louie Sanchez
 *
 */
public abstract class ContactsQuery {

    public static void select() throws SQLException {
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String contactEmail = rs.getString("Email");

            Contacts.addContact(new Contacts(contactID, contactName, contactEmail));
        }
    }

}
