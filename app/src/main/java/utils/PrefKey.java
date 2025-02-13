package utils;

public class PrefKey {

    // Flight Details Preferences
    public static final String PREF_BOOKING = "BookingDetails";
    public static final String KEY_FLIGHT_ID = "flightId";
    public static final String KEY_DEPARTURE_AIRPORT = "departureAirport";
    public static final String KEY_ARRIVAL_AIRPORT = "arrivalAirport";
    public static final String KEY_DEPARTURE_DATETIME = "departureDatetime";
    public static final String KEY_ARRIVAL_DATETIME = "arrivalDatetime";
    public static final String KEY_TOTAL_PAYMENT = "totalPayment";
    public static final String KEY_PAX = "pax";

    // Customer Details Preferences
    public static final String KEY_PASSENGER_DATA = "passengerData"; // Store passenger details (JSON)
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_COUNTRY_RESIDENCE = "countryResidence";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_COUNTRY_CODES = "countryCodes"; // Store country codes
    public static final String KEY_COUNTRY_CODE = "countryCode";
    public static final String KEY_PAX_COUNT = "paxCount"; // Store pax count

    // Add-ons Preferences
    public static final String KEY_SELECTED_SEATS = "selectedSeats"; // Store selected seats
    public static final String KEY_REFUND_GUARANTEE = "refundGuarantee";

    // Constants
    public static final int SEAT_SELECTION_REQUEST_CODE = 1; // Request code for seat selection activity
}
