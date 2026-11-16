package Controller;

import Model.*;
import Model.enums.*;
import Model.strategy.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BookingController {
    private List<Booking> bookings = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();
    private PricingStrategy pricingStrategy = new StandardPricingStrategy();
    private int bookingCounter = 0;
    private int paymentCounter = 0;
    private int ticketCounter = 0;

    public Booking createBooking(Passenger passenger, Flight flight, String seatNumber) {
        Seat seat = null;
        for(Seat s: flight.getSeatList()) {
            if(s.getSeatNumber().equalsIgnoreCase(seatNumber)) {
                seat = s;
                break;
            }
        }
        if(seat == null) {
            throw new IllegalArgumentException("Seat not found: " + seatNumber);
        }
        if(!seat.isAvailable()) {
            throw new IllegalArgumentException("Seat is not available: " + seatNumber);
        }
        setPricingStrategy(selectStrategy(flight));
        double totalPrice = pricingStrategy.calculatePrice(flight, seat);
        Booking booking = new Booking("BK" + (++bookingCounter), passenger, flight, seat, totalPrice);
        seat.setStatus(SeatStatus.RESERVED);
        bookings.add(booking);
        return booking;
    }

    public Booking findBooking(String PRN) {
        for(Booking b:bookings) {
            if(b.getPNR().equalsIgnoreCase(PRN)) {
                return b;
            }
        }
        return null;
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    private Booking requireBooking(String PRN) {
        Booking booking = findBooking(PRN);
        if(booking == null) {
            throw new IllegalArgumentException("Booking not found: " + PRN);
        }
        return booking;
    }

    public void confirmBooking(String PRN) {
        Booking booking = requireBooking(PRN);
        booking.confirm();
    }

    public Ticket payBooking(String PRN, PaymentMethod method) {
        Booking booking = requireBooking(PRN);
        booking.pay();

        Payment payment = new Payment("PAY" + (++paymentCounter), booking, method);
        payment.markSuccess();
        payments.add(payment);

        booking.getSeat().setStatus(SeatStatus.BOOKED);
        Ticket ticket = new Ticket("TKT" + (++ticketCounter), booking);
        tickets.add(ticket);
        return ticket;
    }

    public void checkInBooking(String PRN) {
        Booking booking = requireBooking(PRN);
        booking.checkIn();
    }

    public void cancelBooking(String PRN) {
        Booking booking = requireBooking(PRN);
        booking.cancel();
        booking.getSeat().setStatus(SeatStatus.AVAILABLE);

        for(Payment p:payments) {
            if(p.getBooking() == booking && p.isSuccess()) {
                p.refund();
                break;
            }
        }
    }

    private PricingStrategy selectStrategy(Flight flight) {
        DayOfWeek day = flight.getDepartureTime().getDayOfWeek();
        if(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return new WeekendPricingStrategy();
        }
        long daysBefore = ChronoUnit.DAYS.between(LocalDateTime.now(), flight.getDepartureTime());
        if(daysBefore >= 30) {
            return new EarlyBirdPricingStrategy();
        }
        return new StandardPricingStrategy();
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }
}
