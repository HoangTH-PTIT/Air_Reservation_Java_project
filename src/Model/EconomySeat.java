package Model;

import Model.enums.*;

public class EconomySeat extends Seat{
    public EconomySeat(String seatNumber) {
        super(seatNumber);
    }

    @Override
    public SeatClass getSeatClass() {
        return SeatClass.ECONOMY;
    }

    @Override
    public double getPriceMultiplier() {
        return 1.0;
    }
}
