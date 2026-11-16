package Model.state;

import Model.Booking;

public class PaidState implements BookingState {

    @Override
    public void confirm(Booking booking) {
        throw new IllegalStateException("Cannot confirm: booking is already confirmed");
    }

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Cannot pay: booking is already paid");
    }

    @Override
    public void checkIn(Booking booking) {
        booking.setState(new CheckedInState());
    }

    @Override
    public void cancel(Booking booking) {
        booking.setState(new CancelledState());
    }

    @Override
    public String getName() {
        return "PAID";
    }
}
