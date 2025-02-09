package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ConversionUtil {

    public double calculateTotalPayment(int pax, double priceRate){
        return pax * priceRate;
    }

    public static List<String> compileBoardingDatetimes(String departureDate, String departureTime, String duration) {
        // Define formatters for input and output
        DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter inputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM HH:mm");

        // Parse departure date and time
        LocalDateTime departureDatetime = LocalDateTime.parse(
                departureDate.replace("/", "-") + "T" + departureTime,
                DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm")
        );

        // Parse duration
        int hours = Integer.parseInt(duration.split("h")[0].trim());
        int minutes = Integer.parseInt(duration.split("h")[1].replace("m", "").trim());

        // Calculate arrival time
        LocalDateTime arrivalDatetime = departureDatetime.plusHours(hours).plusMinutes(minutes);

        // Format output
        return Arrays.asList(
                departureDatetime.format(outputFormatter),
                arrivalDatetime.format(outputFormatter)
        );
    }
}
