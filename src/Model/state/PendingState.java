package Model.state;

import Model.Booking;

public class PendingState implements BookingState{
    @Override
    public void confirm(Booking booking) {
        booking.setState(new ConfirmedState());
    }

    @Override
    public void pay(Booking booking) {
        throw new IllegalStateException("Cannot pay: booking must be confirmed first");
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
        return "PENDING";
    }
}
