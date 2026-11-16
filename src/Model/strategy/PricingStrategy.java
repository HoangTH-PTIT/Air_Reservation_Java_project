package Model.strategy;

import Model.*;

public interface PricingStrategy {
    double calculatePrice(Flight flight, Seat seat);
}
