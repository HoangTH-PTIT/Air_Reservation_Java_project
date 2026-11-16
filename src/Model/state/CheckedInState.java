package Model.state;

import Model.Booking;

public class CheckedInState implements BookingState {

    @Override
    public void confirm(Booking booking) {
        throw new IllegalStateException("Cannot confirm: booking is already checked in");
    }

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Cannot pay: booking is already checked in");
    }

    @Override
    public void checkIn(Booking booking) {
        throw new IllegalStateException("Cannot check in: booking is already checked in");
    }

    @Override
    public void cancel(Booking booking) {
        throw new IllegalStateException("Cannot cancel: booking is already checked in");
    }

    @Override
    public String getName() {
        return "CHECKED_IN";
    }
}
