package helper;

import model.Appointments;
import model.Countries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class creates a helper to query countries in the database.
 *
 * @author Louie Sanchez
 *
 */
public abstract class CountriesQuery {

    public static void select() throws SQLException {
        String sql = "SELECT * FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");

            Countries.addCountry(new Countries(countryID, countryName));
        }
    }

}
