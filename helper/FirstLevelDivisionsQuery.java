package helper;

import model.Countries;
import model.FirstLevelDivisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class creates a helper to query first level divisions in the database.
 *
 * @author Louie Sanchez
 *
 */
public abstract class FirstLevelDivisionsQuery {

    public static void select() throws SQLException {
        String sql = "SELECT * FROM first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");
            String countryName = null;

            for (Countries country : Countries.getAllCountries()) {
                if (country.getCountryID() == countryID) {
                    countryName = country.getCountryName();
                }
            }

            FirstLevelDivisions.addFirstLevelDivision(new FirstLevelDivisions(countryID, countryName, divisionID, divisionName));
        }
    }

}
