import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FlightManager extends FlightDistance {

    //        ************************************************************ Fields ************************************************************
    private final Flight flight = new Flight();
    private static int nextFlightDay = 0;
    private static final List<Flight> FLIGHTS = new ArrayList<>();

    //        ************************************************************ Behaviours/Methods ************************************************************

    FlightManager() {
        this.flight.flightSchedule = null;
        this.flight.flightNumber = null;
        this.flight.numOfSeatsInTheFlight = 0;
        this.flight.toWhichCity = null;
        this.flight.fromWhichCity = null;
        this.flight.gate= null;
    }

    /**
     * Creates new random flight from the specified arguments.
     *
     * @param flightSchedule           includes departure date and time of flight
     * @param flightNumber             unique identifier of each flight
     * @param numOfSeatsInTheFlight    available seats in the flight
     * @param chosenDestinations       consists of origin and destination airports(cities)
     * @param distanceBetweenTheCities gives the distance between the airports both in miles and kilometers
     * @param gate
     efrom where passengers will board to the aircraft
     */

    /**
     * Creates Flight Schedule. All methods of this class are collaborating with each other
     * to create flight schedule of the said length in this method.
     */
    public void flightScheduler() {
        int numOfFlights = 15;              // decides how many unique flights to be included/display in scheduler
        RandomGenerator r1 = new RandomGenerator();
        for (int i = 0; i < numOfFlights; i++) {
            String[][] chosenDestinations = r1.randomDestinations();
            String[] distanceBetweenTheCities = calculateDistance(Double.parseDouble(chosenDestinations[0][1]), Double.parseDouble(chosenDestinations[0][2]), Double.parseDouble(chosenDestinations[1][1]), Double.parseDouble(chosenDestinations[1][2]));
            String flightSchedule = createNewFlightsAndTime();
            String flightNumber = r1.randomFlightNumbGen(2, 1).toUpperCase();
            int numOfSeatsInTheFlight = r1.randomNumOfSeats();
            String flightTime = calculateFlightTime(Double.parseDouble(distanceBetweenTheCities[0]));
            String gate= r1.randomFlightNumbGen(1, 30);
            FLIGHTS.add(new Flight(flightSchedule, flightNumber, numOfSeatsInTheFlight, chosenDestinations, distanceBetweenTheCities, flightTime, gate.toUpperCase()));
        }
    }

    /**
     * Registers new Customer in this Flight.
     *
     * @param customer customer to be registered
     */
    void addNewCustomerToFlight(Customer customer) {
        this.flight.listOfRegisteredCustomersInAFlight.add(customer);
    }

    /**
     * Adds numOfTickets to existing customer's tickets for this flight.
     *
     * @param customer     customer in which tickets are to be added
     * @param numOfTickets number of tickets to add
     */
//    void addTicketsToExistingCustomer(Customer customer, int numOfTickets) {
//        customer.addExistingFlightToCustomerList(customerIndex, numOfTickets);
//    }

    /***
     * Checks if the specified customer is already registered in the FLight's array list
     * @param customersList of the flight
     * @param customer specified customer to be checked
     * @return true if the customer is already registered in the said flight, false otherwise
     */
//    boolean isCustomerAlreadyAdded(List<Customer> customersList, Customer customer) {
//        boolean isAdded = false;
//        for (Customer customer1 : customersList) {
//            if (customer1.getUserID().equals(customer.getUserID())) {
//                isAdded = true;
//                customerIndex = customersList.indexOf(customer1);
//                break;
//            }
//        }
//        return isAdded;
//    }

    /**
     * Calculates the flight time, using avg. ground speed of 450 knots.
     *
     * @param distanceBetweenTheCities distance between the cities/airports in miles
     * @return formatted flight time
     */
    public String calculateFlightTime(double distanceBetweenTheCities) {
        double groundSpeed = 450;
        double time = (distanceBetweenTheCities / groundSpeed);
        String timeInString = String.format("%.4s", time);
        String[] timeArray = timeInString.replace('.', ':').split(":");
        int hours = Integer.parseInt(timeArray[0]);
        int minutes = Integer.parseInt(timeArray[1]);
        int modulus = minutes % 5;
        // Changing flight time to make minutes near/divisible to 5.
        if (modulus < 3) {
            minutes -= modulus;
        } else {
            minutes += 5 - modulus;
        }
        if (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        if (hours <= 9 && Integer.toString(minutes).length() == 1) {
            return String.format("0%s:%s0", hours, minutes);
        } else if (hours <= 9 && Integer.toString(minutes).length() > 1) {
            return String.format("0%s:%s", hours, minutes);
        } else if (hours > 9 && Integer.toString(minutes).length() == 1) {
            return String.format("%s:%s0", hours, minutes);
        } else {
            return String.format("%s:%s", hours, minutes);
        }
    }

    /**
     * Creates flight arrival time by adding flight time to flight departure time
     *
     * @return flight arrival time
     */


    void deleteFlight(String flightNumber) {
        boolean isFound = false;
        Iterator<Flight> list = FLIGHTS.iterator();
        while (list.hasNext()) {
            Flight flightManager = list.next();
            if (flightManager.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            list.remove();
        } else {
            System.out.println("Flight with given Number not found...");
        }
        displayFlightSchedule();
    }

    /**
     * Calculates the distance between the cities/airports based on their lat longs.
     *
     * @param lat1 origin city/airport latitude
     * @param lon1 origin city/airport longitude
     * @param lat2 destination city/airport latitude
     * @param lon2 destination city/airport longitude
     * @return distance both in miles and km between the cities/airports
     */
    @Override
    public String[] calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double distance = Math.sin(degreeToRadian(lat1)) * Math.sin(degreeToRadian(lat2)) + Math.cos(degreeToRadian(lat1)) * Math.cos(degreeToRadian(lat2)) * Math.cos(degreeToRadian(theta));
        distance = Math.acos(distance);
        distance = radianToDegree(distance);
        distance = distance * 60 * 1.1515;
        /* On the Zero-Index, distance will be in Miles, on 1st-index, distance will be in KM and on the 2nd index distance will be in KNOTS*/
        String[] distanceString = new String[3];
        distanceString[0] = String.format("%.2f", distance * 0.8684);
        distanceString[1] = String.format("%.2f", distance * 1.609344);
        distanceString[2] = Double.toString(Math.round(distance * 100.0) / 100.0);
        return distanceString;
    }

    private double degreeToRadian(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double radianToDegree(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void displayFlightSchedule() {

        Iterator<Flight> flightIterator = FLIGHTS.iterator();
        System.out.println();
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+------------------------+\n");
        System.out.printf("| Num  | FLIGHT SCHEDULE\t\t\t\t\t   | FLIGHT NO | Available Seats  | \tFROM ====>>       | \t====>> TO\t   | \t    ARRIVAL TIME       | FLIGHT TIME |  GATE  |   DISTANCE(MILES/KMS)  |%n");
        System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+------------------------+\n");
        int i = 0;
        while (flightIterator.hasNext()) {
            i++;
            Flight f1 = flightIterator.next();
            System.out.println(f1.toString(i));
             System.out.print("+------+-------------------------------------------+-----------+------------------+-----------------------+------------------------+---------------------------+-------------+--------+------------------------+\n");
        }
    }

    @Override
    public String toString(int i) {
        return String.format("| %-5d| %-41s | %-9s | \t%-9s | %-21s | %-22s | %-10s  |   %-6sHrs |  %-4s  |  %-8s / %-11s|", i, flight.flightSchedule, flight.flightNumber, flight.numOfSeatsInTheFlight, flight.fromWhichCity, flight.toWhichCity, flight.flightTime, flight.gate, flight.distanceInMiles, flight.distanceInKm);
    }

    /**
     * Creates new random flight schedule
     *
     * @return newly created flight schedule
     */
    public String createNewFlightsAndTime() {

        Calendar c = Calendar.getInstance();
        // Incrementing nextFlightDay, so that next scheduled flight would be in the future, not in the present
        nextFlightDay += Math.random() * 7;
        c.add(Calendar.DATE, nextFlightDay);
        c.add(Calendar.HOUR, nextFlightDay);
        c.set(Calendar.MINUTE, ((c.get(Calendar.MINUTE) * 3) - (int) (Math.random() * 45)));
        Date myDateObj = c.getTime();
        LocalDateTime date = Instant.ofEpochMilli(myDateObj.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        date = getNearestHourQuarter(date);
        return date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy, HH:mm a "));
    }

    /**
     * Formats flight schedule, so that the minutes would be to the nearest quarter.
     *
     * @param datetime to be formatting
     * @return formatted LocalDateTime with minutes close to the nearest hour quarter
     */
    public LocalDateTime getNearestHourQuarter(LocalDateTime datetime) {
        int minutes = datetime.getMinute();
        int mod = minutes % 15;
        LocalDateTime newDatetime;
        if (mod < 8) {
            newDatetime = datetime.minusMinutes(mod);
        } else {
            newDatetime = datetime.plusMinutes(15 - mod);
        }
        newDatetime = newDatetime.truncatedTo(ChronoUnit.MINUTES);
        return newDatetime;
    }


    //        ************************************************************ Setters & Getters ************************************************************

    public int getNoOfSeats() {
        return flight.numOfSeatsInTheFlight;
    }

    public String getFlightNumber() {
        return flight.flightNumber;
    }

    public void setNoOfSeatsInTheFlight(int numOfSeatsInTheFlight) {
        this.flight.numOfSeatsInTheFlight = numOfSeatsInTheFlight;
    }

    public String getFlightTime() {
        return flight.flightTime;
    }

    public List<Flight> getFlightList() {
        return FLIGHTS;
    }

    public List<Customer> getListOfRegisteredCustomersInAFlight() {
        return flight.listOfRegisteredCustomersInAFlight;
    }

    public String getFlightSchedule() {
        return flight.flightSchedule;
    }

    public String getFromWhichCity() {
        return flight.fromWhichCity;
    }

    public String getGate() {
        return flight.gate;
    }

    public String getToWhichCity() {
        return flight.toWhichCity;
    }

}