package Model.strategy;

import Model.*;

public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Flight flight, Seat seat) {
        return seat.calculatePrice(flight.getBasePrice());
    }
}
