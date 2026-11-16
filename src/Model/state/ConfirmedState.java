package Model.state;

import Model.Booking;

public class ConfirmedState implements BookingState{
    @Override
    public void confirm(Booking booking) {
        throw new IllegalStateException("Booking is already confirmed!");
    }

    @Override
    public void pay(Booking booking) {
        booking.setState(new PaidState());
    }

    @Override
    public void checkIn(Booking booking) {
        throw new IllegalStateException("Cannot check in: booking is not paid yet");
    }

    @Override
    public void cancel(Booking booking) {
        booking.setState(new CancelledState());
    }

    @Override
    public String getName() {
        return "CONFIRMED";
    }
}
