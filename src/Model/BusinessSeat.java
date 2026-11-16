package Model;

import Model.enums.*;

public class BusinessSeat extends Seat{
    public BusinessSeat(String seatNumber) {
        super(seatNumber);
    }

    @Override
    public SeatClass getSeatClass() {
        return SeatClass.BUSINESS;
    }

    @Override
    public double getPriceMultiplier() {
        return 3.0;
    }
}
