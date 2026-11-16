package Model;

import Model.enums.*;

public class FirstClassSeat extends Seat{
    public FirstClassSeat(String seatNumber) {
        super(seatNumber);
    }

    @Override
    public SeatClass getSeatClass() {
        return SeatClass.FIRST;
    }

    @Override
    public double getPriceMultiplier() {
        return 6.0;
    }
}
