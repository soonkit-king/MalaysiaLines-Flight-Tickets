package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Conversions {

    public static double calculateTotalPayment(int pax, double priceRate){
        return pax * priceRate;
    }

    public static String formatPriceRate(double value) {
        return String.format("RM%.2f", value) + " / pax";
    }

    public static String formatMoney(double value) {
        return String.format("RM%.2f", value);
    }

    public static String formatDate(int day, int month, int year) {
        return String.format("%02d %s %d", day, getMonthAbbr(month), year);
    }

    // Converts month index (0-11) to abbreviation
    public static String getMonthAbbr(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[month];
    }

    public static List<String> calculateFlightDatetimes(String departureDate, String departureTime, String duration) {
        // Define formatters
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"); // Input format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"); // Output format

        // Parse departure date and time
        LocalDateTime departureDatetime = LocalDateTime.parse(departureDate + " " + departureTime, inputFormatter);

        // Parse duration (handles both "1h 2m" and "30m" cases)
        int hours = 0, minutes = 0;
        if (duration.contains("h")) {
            String[] parts = duration.split("h");
            hours = Integer.parseInt(parts[0].trim());
            if (parts.length > 1 && parts[1].contains("m")) {
                minutes = Integer.parseInt(parts[1].replace("m", "").trim());
            }
        } else if (duration.contains("m")) {
            minutes = Integer.parseInt(duration.replace("m", "").trim());
        }
        else {
            return List.of(); // Immutable empty list (Java 9+)
        }

        // Calculate arrival time
        LocalDateTime arrivalDatetime = departureDatetime.plusHours(hours).plusMinutes(minutes);

        // Format output
        return Arrays.asList(
                departureDatetime.format(outputFormatter),
                arrivalDatetime.format(outputFormatter)
        );
    }
}
