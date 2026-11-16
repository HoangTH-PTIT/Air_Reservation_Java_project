package Model.strategy;

import Model.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class EarlyBirdPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Flight flight, Seat seat) {
        return seat.calculatePrice(flight.getBasePrice()) * 0.90;
    }
}
