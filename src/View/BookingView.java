package View;

import Model.Booking;
import Model.Payment;
import Model.Ticket;
import java.util.List;

public class BookingView {

    public void showBooking(Booking booking) {
        System.out.println("Booking: " + booking);
    }

    public void showBookings(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        for (Booking b : bookings) {
            showBooking(b);
        }
    }

    public void showPayment(Payment payment) {
        System.out.println("Payment: " + payment);
    }

    public void showTicket(Ticket ticket) {
        System.out.println("=== TICKET ===");
        System.out.println(ticket);
        System.out.println("==============");
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String message) {
        System.out.println("Error: " + message);
    }
}
