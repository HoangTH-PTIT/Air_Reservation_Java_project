package Model.strategy;

import Model.*;
import java.time.DayOfWeek;

public class WeekendPricingStrategy implements PricingStrategy{
    @Override
    public double calculatePrice(Flight flight, Seat seat) {
        return seat.calculatePrice(flight.getBasePrice()) * 1.15;
    }
}
