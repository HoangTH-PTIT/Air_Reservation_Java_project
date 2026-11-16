package Model.factory;

import Model.*;
import Model.enums.SeatClass;

public class SeatFactory {
    public static Seat createSeat(SeatClass seatClass, String seatNumber) {
        switch (seatClass) {
            case ECONOMY:
                return new EconomySeat(seatNumber);
            case BUSINESS:
                return new BusinessSeat(seatNumber);
            case FIRST:
                return new FirstClassSeat(seatNumber);
            default:
                throw new IllegalArgumentException("Unknown seat class: " + seatClass);
        }
    }
}
