package model;

public class Flight {
    private String fromTo;
    private String goTo;
    private String timeDepart;

    private String timeArrive;
    private String duration;
    private String price;

    public Flight(String fromTo,String goTo,  String timeDepart, String timeArrive, String duration, String price) {
        this.fromTo = fromTo;
        this.goTo = goTo;
        this.timeArrive = timeArrive;
        this.timeDepart = timeDepart;
        this.duration = duration;
        this.price = price;
    }

    public String getFromTo() {
        return fromTo;
    }

    public String getGoTo() {
        return goTo;
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
