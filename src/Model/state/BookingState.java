package Model.state;

import Model.Booking;

public interface BookingState {
    void confirm(Booking booking);
    void pay(Booking booking);
    void checkIn(Booking booking);
    void cancel(Booking booking);
    String getName();
}
