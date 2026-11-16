package Model;

public class Aircraft {
    private String aircraftId;
    private String model;
    private String manufacturer;
    private int totalSeats;

    public Aircraft(String aircraftId, String model, String manufacturer, int totalSeats) {
        this.aircraftId = aircraftId;
        this.model = model;
        this.manufacturer = manufacturer;
        this.totalSeats = totalSeats;
    }

    public String getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(String aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}
