package Controller;

import Model.*;
import java.time.*;
import java.util.*;

public class FlightController {
    private List<Flight> flights = new ArrayList<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public List<Flight> getAllFlights() {
        return flights;
    }

    public Flight findFlight(String flightNo) {
        for (Flight f:flights) {
            if(f.getFlightNo().equalsIgnoreCase(flightNo)) {
                return f;
            }
        }
        return null;
    }

    public List<Flight> searchFlights(String from, String to, LocalDate date) {
        List<Flight> result = new ArrayList<>();
        for(Flight f:flights) {
            if(f.getFrom().equalsIgnoreCase(from) && f.getTo().equalsIgnoreCase(to) && f.getDepartureTime().toLocalDate().equals(date)) {
                result.add(f);
            }
        }
        return result;
    }

    public Seat findSeat(Flight flight, String seatNumber) {
        for (Seat s:flight.getSeatList()) {
            if(s.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                return s;
            }
        }
        return null;
    }

    public List<Seat> getAvailableSeats(Flight flight) {
        List<Seat> result = new ArrayList<>();
        for(Seat s:flight.getSeatList()) {
            if(s.isAvailable()) {
                result.add(s);
            }
        }
        return result;
    }
}
