package model;

public class Flight {
    private String departureAirport;
    private String arrivalAirport;
    private String timeDepart;

    private String timeArrive;
    private String duration;
    private String price;

    public Flight(String departureAirport,String arrivalAirport,  String timeDepart, String timeArrive, String duration, String price) {
        this.departureAirport = departureAirport;
        this.arrivalAirport= arrivalAirport;
        this.timeArrive = timeArrive;
        this.timeDepart = timeDepart;
        this.duration = duration;
        this.price = price;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getTimeArrive() {
        return timeArrive;
    }

    public String getTimeDepart() {
        return timeDepart;
    }

    public String getDuration() {
        return duration;
    }

    public String getPrice() {
        return price;
    }
}
