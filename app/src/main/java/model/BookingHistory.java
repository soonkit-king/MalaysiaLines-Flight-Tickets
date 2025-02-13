package model;

public class BookingHistory {
    private int bookingId;
    private String departureAirport;
    private String arrivalAirport;
    private String departureDatetime;
    private String arrivalDatetime;
    private String seatNo;
    private boolean hasRefundGuarantee;
    private int pax;
    private double totalPayment;

    // Constructor (optional, but recommended)
    public BookingHistory(int bookingId, String departureAirport, String arrivalAirport, String departureDatetime, String arrivalDatetime, String seatNo, boolean hasRefundGuarantee, int pax, double totalPayment) {
        this.bookingId = bookingId;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDatetime = departureDatetime;
        this.arrivalDatetime = arrivalDatetime;
        this.seatNo = seatNo;
        this.hasRefundGuarantee = hasRefundGuarantee;
        this.pax = pax;
        this.totalPayment = totalPayment;
    }

    // Getters
    public int getBookingId() {
        return bookingId;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureDatetime() {
        return departureDatetime;
    }

    public String getArrivalDatetime() {
        return arrivalDatetime;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public boolean hasRefundGuarantee() {
        return hasRefundGuarantee;
    }

    public int getPax() {
        return pax;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    // Setters
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public void setDepartureDatetime(String departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public void setArrivalDatetime(String arrivalDatetime) {
        this.arrivalDatetime = arrivalDatetime;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public void setHasRefundGuarantee(boolean hasRefundGuarantee) {
        this.hasRefundGuarantee = hasRefundGuarantee;
    }

    public void setPax(int pax) {
        this.pax = pax;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }
}