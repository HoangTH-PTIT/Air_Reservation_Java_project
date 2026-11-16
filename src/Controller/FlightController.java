package Controller;

import Model.*;
import Model.enums.*;
import java.time.*;
import java.util.*;
import java.time.temporal.TemporalAdjusters;
import Model.factory.*;

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

    public void seedSampleData() {
        Aircraft a1 = new Aircraft("AC001", "Airbus A320", "Airbus", 18);
        Aircraft a2 = new Aircraft("AC002", "Boeing 787", "Boeing", 18);
        Aircraft a3 = new Aircraft("AC003", "Airbus A321", "Airbus", 18);

        LocalDateTime dep1 = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dep2 = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dep3 = LocalDateTime.now().plusDays(2).withHour(9).withMinute(30).withSecond(0).withNano(0);
        LocalDateTime dep4 = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime dep5 = LocalDateTime.now().plusDays(35).withHour(7).withMinute(0).withSecond(0).withNano(0);

        addFlight(new Flight("VN123", "HAN", "SGN", dep1, dep1.plusHours(2), a1, 1500000, createSeats(18)));
        addFlight(new Flight("VN456", "SGN", "DAD", dep2, dep2.plusHours(1).plusMinutes(30), a2, 1200000, createSeats(18)));
        addFlight(new Flight("VN789", "HAN", "DAD", dep3, dep3.plusHours(1).plusMinutes(30), a3, 900000, createSeats(18)));
        addFlight(new Flight("VN101", "HAN", "CXR", dep4, dep4.plusHours(1).plusMinutes(15), a1, 1000000, createSeats(18)));
        addFlight(new Flight("VN202", "SGN", "HAN", dep5, dep5.plusHours(2).plusMinutes(10), a2, 1300000, createSeats(18)));
    }

    private List<Seat> createSeats(int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        String letters = "ABCDEF";
        for(int i=0; i<totalSeats; i++) {
            String seatNumber = (i/6 + 1) + "" + letters.charAt(i%6);
            SeatClass seatClass;
            if(i<6) {
                seatClass = SeatClass.FIRST;
            } else if(i<12) {
                seatClass = SeatClass.BUSINESS;
            } else {
                seatClass = SeatClass.ECONOMY;
            }
            seats.add(SeatFactory.createSeat(seatClass, seatNumber));
        }
        return seats;
    }

}
