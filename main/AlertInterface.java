package lsc195.lsc195;

/**
 * This Lambda Expression is used when an alert shows after an Appointment or Customer has been deleted.
 * This makes it easier to display a message with the customer or appointment information confirming the deletion.
 * This also reduces the redundant code of deleting an Appointment or Customer.
 *
 * @author Louie Sanchez
 *
 */
public interface AlertInterface {

    // Value returning Lambda Expression.
    String deleteMessage(int i, String r);

}
