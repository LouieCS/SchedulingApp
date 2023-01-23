package helper;

import model.Contacts;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class creates a helper to query logins in the database.
 *
 * @author Louie Sanchez
 *
 */
public abstract class LoginQuery {

    public static void select() throws SQLException {
        String sql = "SELECT * FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String username = rs.getString("User_Name");
            int userID = rs.getInt("User_ID");
            User.addUser(new User(userID, username));
        }
    }

    public static boolean correctLoginInfo(String usernameInput, String passwordInput) throws SQLException {
        String sql = "SELECT * FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String username = rs.getString("User_Name");
            String password = rs.getString("Password");
//            User.addUser(new User(userID, username));

            if(username.equals(usernameInput) && password.equals(passwordInput)) {

                return true;
            }
        }

        return false;
    }

}
