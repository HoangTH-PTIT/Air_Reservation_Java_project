package Model.state;

import Model.Booking;

public class CancelledState implements BookingState {

    @Override
    public void confirm(Booking booking) {
        throw new IllegalStateException("Cannot confirm: booking is cancelled");
    }

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Cannot pay: booking is cancelled");
    }

    @Override
    public void checkIn(Booking booking) {
        throw new IllegalStateException("Cannot check in: booking is cancelled");
    }

    @Override
    public void cancel(Booking booking) {
        throw new IllegalStateException("Cannot cancel: booking is already cancelled");
    }

    @Override
    public String getName() {
        return "CANCELLED";
    }
}
