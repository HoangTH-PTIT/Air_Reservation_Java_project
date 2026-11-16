package Model;

import java.time.LocalDateTime;

public class Ticket {
    private String ticketId;
    private Booking booking;
    private LocalDateTime issuedDate;

    public Ticket(String ticketId, Booking booking) {
        if (!booking.getStatusName().equals("PAID")
                && !booking.getStatusName().equals("CHECKED_IN")) {
            throw new IllegalStateException("Cannot issue ticket: booking is not paid yet");
        }
        this.ticketId = ticketId;
        this.booking = booking;
        this.issuedDate = LocalDateTime.now();
    }

    public boolean isValid() {
        return !booking.getStatusName().equals("CANCELLED");
    }

    public String getTicketId() {
        return ticketId;
    }

    public Booking getBooking() {
        return booking;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    @Override
    public String toString() {
        return ticketId + " - " + booking.getPNR() + " - Passenger: " + booking.getPassenger() + " - Flight: " + booking.getFlight() + " - Seat: " + booking.getSeat() + " - " + booking.getStatusName();
    }
}
