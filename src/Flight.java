import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    String flightSchedule;
    String flightNumber;
    String fromWhichCity;
    String gate;
    String toWhichCity;
    double distanceInMiles;
    double distanceInKm;
    String flightTime;
    String arrivalTime;
    int numOfSeatsInTheFlight;
    List<Customer> listOfRegisteredCustomersInAFlight;
    Flight(){

    }
    Flight(String flightSchedule, String flightNumber, int numOfSeatsInTheFlight, String[][] chosenDestinations, String[] distanceBetweenTheCities, String flightTime, String gate) {
        this.flightSchedule = flightSchedule;
        this.flightNumber = flightNumber;
        this.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
        this.fromWhichCity = chosenDestinations[0][0];
        this.toWhichCity = chosenDestinations[1][0];
        this.distanceInMiles = Double.parseDouble(distanceBetweenTheCities[0]);
        this.distanceInKm = Double.parseDouble(distanceBetweenTheCities[1]);
        this.flightTime = flightTime;
        this.arrivalTime = fetchArrivalTime();
        this.listOfRegisteredCustomersInAFlight = new ArrayList<>();
        this.gate= gate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String toString(int i) {
        return String.format("| %-5d| %-41s | %-9s | \t%-9s | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  |  %-8s / %-11s|", i, flightSchedule, flightNumber, numOfSeatsInTheFlight, fromWhichCity, toWhichCity, arrivalTime, flightTime, gate, distanceInMiles, distanceInKm);
    }

    public int getNumOfSeatsInTheFlight() {
        return numOfSeatsInTheFlight;
    }

    public void setNumOfSeatsInTheFlight(int numOfSeatsInTheFlight) {
        this.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
    }

    public List<Customer> getListOfRegisteredCustomersInAFlight() {
        return listOfRegisteredCustomersInAFlight;
    }

    public String getFlightSchedule() {
        return flightSchedule;
    }

    public String getFromWhichCity() {
        return fromWhichCity;
    }

    public String getToWhichCity() {
        return toWhichCity;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public String getGate() {
        return gate;
    }
    public String fetchArrivalTime() {
        /*These lines convert the String of flightSchedule to LocalDateTIme and add the arrivalTime to it....*/
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a ");
        LocalDateTime departureDateTime = LocalDateTime.parse(flightSchedule, formatter);

        /*Getting the Flight Time, plane was in air*/
        String[] flightTime = getFlightTime().split(":");
        int hours = Integer.parseInt(flightTime[0]);
        int minutes = Integer.parseInt(flightTime[1]);


        LocalDateTime arrivalTime;

        arrivalTime = departureDateTime.plusHours(hours).plusMinutes(minutes);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("EE, dd-MM-yyyy HH:mm a");
        return arrivalTime.format(formatter1);

    }

    /**
     * Registers new Customer in this Flight.
     *
     * @param customer customer to be registered
     */
    void addNewCustomerToFlight(Customer customer) {
        this.listOfRegisteredCustomersInAFlight.add(customer);
    }

    /**
     * Adds numOfTickets to existing customer's tickets for this flight.
     *
     * @param customer     customer in which tickets are to be added
     */
    boolean isCustomerAlreadyAdded(List<Customer> customersList, Customer customer) {
        boolean isAdded = false;
        for (Customer customer1 : customersList) {
            if (customer1.getUserID().equals(customer.getUserID())) {
                isAdded = true;
                int customerIndex = customersList.indexOf(customer1);
                break;
            }
        }
        return isAdded;
    }
}
