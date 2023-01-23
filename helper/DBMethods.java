package helper;

import model.*;

import java.sql.SQLException;

/**
 * This class creates a helper to query all tables in the database.
 *
 * @author Louie Sanchez
 *
 */
public abstract class DBMethods {

    public static void retrieveAllDatabaseTables() throws SQLException {
        Countries.getAllCountries().clear();
        FirstLevelDivisions.getAllCountries().clear();
        Customers.getAllCustomers().clear();
        Appointments.getAllAppointments().clear();
        Contacts.getAllContacts().clear();
        User.getAllUsers().clear();

        CountriesQuery.select();
        FirstLevelDivisionsQuery.select();
        CustomersQuery.select();
        AppointmentsQuery.select();
        ContactsQuery.select();
        LoginQuery.select();
    }

}
