package Model;

import Model.enums.*;
import java.math.*;

public abstract class Seat {
    private String seatNumber;
    private SeatStatus status;

    protected Seat(String seatNumber) {
        this.seatNumber = seatNumber;
        this.status = SeatStatus.AVAILABLE;
    }

    public abstract SeatClass getSeatClass();
    public abstract double getPriceMultiplier();

    public double calculatePrice(double basePrice) {
        return basePrice * getPriceMultiplier();
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return seatNumber + " - " + getSeatClass() + " - " + status;
    }
}
