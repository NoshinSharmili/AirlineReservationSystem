import java.util.List;

public interface DisplayClass {

    void displayRegisteredUsersForAllFlight();

    void displayRegisteredUsersForASpecificFlight(String flightNum);

    void displayHeaderForUsers(FlightManager flightManager, List<Customer> c);

    void displayFlightsRegisteredByOneUser(String userID);

}
