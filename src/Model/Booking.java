package Model;

import Model.state.*;

import java.time.LocalDateTime;

public class Booking {
    private String PNR;
    private Passenger passenger;
    private Flight flight;
    private Seat seat;
    private LocalDateTime bookingDate;
    private double totalPrice;
    private BookingState state;

    public Booking(String PNR, Passenger passenger, Flight flight, Seat seat, double totalPrice) {
        this.PNR = PNR;
        this.passenger = passenger;
        this.flight = flight;
        this.seat = seat;
        this.bookingDate = LocalDateTime.now();
        this.totalPrice = totalPrice;
        this.state = new PendingState();
    }

    public void confirm() {
        state.confirm(this);
    }

    public void pay() {
        state.pay(this);
    }

    public void checkIn() {
        state.checkIn(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public BookingState getState() {
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
    }

    public String getStatusName() {
        return state.getName();
    }

    public String getPNR() {
        return PNR;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    public Seat getSeat() {
        return seat;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return PNR + " - " + passenger + " - " + flight + " - Seat " + seat + " - " + state.getName() + " - " + totalPrice;
    }
}
