package model;

public class Flight {
    private String departureAirport;
    private String arrivalAirport;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private double priceRate;

    // Constructor (optional, but recommended)
    public Flight(String departureAirport, String arrivalAirport, String departureTime, String arrivalTime, String duration, double priceRate) {
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.priceRate = priceRate;
    }

    // Getters
    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public double getPriceRate() {
        return priceRate;
    }

    // Setters
    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setPriceRate(double priceRate) {
        this.priceRate = priceRate;
    }
}