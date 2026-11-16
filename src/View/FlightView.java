package View;

import Model.Flight;
import Model.Seat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightView {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void showFlights(List<Flight> flights) {
        if (flights.isEmpty()) {
            System.out.println("No flights available.");
            return;
        }
        for (Flight f : flights) {
            System.out.println(f.getFlightNo() + " | " + f.getFrom() + " -> " + f.getTo() + " | " + f.getDepartureTime().format(FMT) + " -> " + f.getArrivalTime().format(FMT) + " | Base price: " + f.getBasePrice());
        }
    }

    public void showSeats(Flight flight) {
        System.out.println("Seats of flight " + flight.getFlightNo()
                + " (AVAILABLE / RESERVED / BOOKED):");
        int count = 0;
        for (Seat s : flight.getSeatList()) {
            System.out.print(s.getSeatNumber() + ":" + s.getStatus() + "  ");
            if (++count % 6 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
}
