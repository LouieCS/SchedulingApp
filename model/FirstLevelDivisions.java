package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FirstLevelDivisions extends Countries {

    private int divisionID;
    private String divisionName;
    private static ObservableList<FirstLevelDivisions> allFirstLevelDivisions = FXCollections.observableArrayList();


    /** This is the constructor for First Level Divisions.
     * @param divisionID the division id of the first level division
     * @param divisionName the name of the first level division
     * @param countryID the country id of the first level division
     * @param countryName the country name of the first level division
     */
    public FirstLevelDivisions(int countryID, String countryName, int divisionID, String divisionName) {
        super(countryID, countryName);
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }


    /** This gets the ID of a First Level Division.
     * @return the ID of a First Level Division
     */
    public int getDivisionID() {
        return divisionID;
    }

    /** This sets the ID of a First Level Division.
     * @param divisionID the ID of a First Level Division
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /** This gets the name of a First Level Division.
     * @return the name of a First Level Division
     */
    public String getDivisionName() {
        return divisionName;
    }

    /** This sets the name of a First Level Division.
     * @param divisionName the name of a First Level Division
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /** This adds a new First Level Division to the First Level Divisions list.
     * @param newFirstLevelDivision The new First Level Division to add
     */
    public static void addFirstLevelDivision(FirstLevelDivisions newFirstLevelDivision) {
        allFirstLevelDivisions.add(newFirstLevelDivision);
    }

    /** Obtains all First Level Divisions from the First Level Divisions list.
     * @return all First Level Divisions from the First Level Divisions list
     */
    public static ObservableList<FirstLevelDivisions> getAllFirstLevelDivisions() {
        return allFirstLevelDivisions;
    }

    /** This returns a string to a combo box.
     * @return division name
     */
    @Override
    public String toString() {
        return divisionName;
    }

}
