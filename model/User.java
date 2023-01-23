package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {

    private int userID;
    private String username;
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();


    /** This is the constructor for Users.
     * @param userID the user id of the user
     * @param username the username of the user
     */
    public User(int userID, String username) {
        this.userID = userID;
        this.username = username;
    }


    /** This gets the user ID of a user.
     * @return the user ID of a user
     */
    public int getUserID() {
        return userID;
    }

    /** This sets the user ID of a user.
     * @param userID the user ID ID of a user
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /** This gets the username of a user.
     * @return the username of a user
     */
    public String getUsername() {
        return username;
    }

    /** This sets the username of a user.
     * @param username the username of a user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** This adds a new user to the Users list.
     * @param user The new user to add
     */
    public static void addUser(User user) {
        allUsers.add(user);
    }

    /** Obtains all users from the Users list.
     * @return all user from the Users list
     */
    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }

    /** This returns a string to a combo box.
     * @return user ID
     */
    @Override
    public String toString() {
        return String.valueOf(userID);
    }
}
