package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Countries {

    private int countryID;
    private String countryName;
    private static ObservableList<Countries> allCountries = FXCollections.observableArrayList();


    /** This is the constructor for Countries.
     * @param countryID the id of the country
     * @param countryName the name of the country
     */
    public Countries(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }


    /** This gets the country ID of a country.
     * @return the country ID of a country
     */
    public int getCountryID() {
        return countryID;
    }

    /** This sets the country ID of a country.
     * @param countryID the country ID of a country
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /** This gets the name of a country.
     * @return the name of a country
     */
    public String getCountryName() {
        return countryName;
    }

    /** This sets the name of a country.
     * @param countryName the name of a country
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /** This adds a new country to the Countries list.
     * @param newCountry The new country to add
     */
    public static void addCountry(Countries newCountry) {
        allCountries.add(newCountry);
    }

    /** Obtains all countries from the Countries list.
     * @return all countries from the Countries list
     */
    public static ObservableList<Countries> getAllCountries() {
        return allCountries;
    }

    /** This returns a string to a combo box.
     * @return country name
     */
    @Override
    public String toString() {
        return countryName;
    }
}
