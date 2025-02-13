package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Passenger;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "flight_booking.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE flight (" +
                "flight_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "departure_airport TEXT, " +
                "arrival_airport TEXT, " +
                "departure_time TEXT, " +
                "arrival_time TEXT, " +
                "flight_duration TEXT, " +
                "price_rate REAL)");

        db.execSQL("CREATE TABLE booking (" +
                "booking_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "flight_id INTEGER, " +
                "pax INTEGER, " +
                "departure_datetime TEXT, " +
                "arrival_datetime TEXT, " +
                "seat_no TEXT, " +
                "refund_guarantee INTEGER, " +
                "total_payment REAL, " +
                "FOREIGN KEY(flight_id) REFERENCES flight(flight_id))");

        db.execSQL("CREATE TABLE contact (" +
                "contact_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "booking_id INTEGER, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "email TEXT, " +
                "country_residence TEXT, " +
                "country_code TEXT, " +
                "phone_number TEXT, " +
                "FOREIGN KEY(booking_id) REFERENCES booking(booking_id))");

        db.execSQL("CREATE TABLE passenger (" +
                "passenger_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "booking_id INTEGER, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "gender TEXT, " +
                "date_of_birth TEXT, " +
                "nationality TEXT, " +
                "issue_country TEXT, " +
                "passport_number TEXT, " +
                "passport_expiry_date TEXT, " +
                "FOREIGN KEY(booking_id) REFERENCES booking(booking_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS flight");
        db.execSQL("DROP TABLE IF EXISTS booking");
        db.execSQL("DROP TABLE IF EXISTS contact");
        db.execSQL("DROP TABLE IF EXISTS passenger");
        onCreate(db);
    }

    public Cursor getAllFlights() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM flight", null);
    }

    public Cursor getAllBookingHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT b.*, f.departure_airport, f.arrival_airport FROM booking b LEFT JOIN flight f ON b.flight_id = f.flight_id", null);
    }

    public void insertFlight(String departureAirport, String arrivalAirport,
                             String departureTime, String arrivalTime,
                             String flightDuration, double priceRate) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the flight already exists
        Cursor cursor = db.rawQuery("SELECT * FROM flight WHERE departure_airport = ? AND arrival_airport = ? AND departure_time = ?",
                new String[]{departureAirport, arrivalAirport, departureTime});

        if (cursor.getCount() == 0) { // If no existing flight, insert new one
            ContentValues values = new ContentValues();
            values.put("departure_airport", departureAirport);
            values.put("arrival_airport", arrivalAirport);
            values.put("departure_time", departureTime);
            values.put("arrival_time", arrivalTime);
            values.put("flight_duration", flightDuration);
            values.put("price_rate", priceRate);

            db.insert("flight", null, values);
        }

        cursor.close();
        db.close();
    }


    public boolean insertCompleteBooking(
            int flightId,
            int pax,
            String departureDatetime,
            String arrivalDatetime,
            String seatNo,
            boolean refundGuarantee,
            double totalPayment,
            // Contact details
            String firstName,
            String lastName,
            String email,
            String countryResidence,
            String countryCode,
            String phoneNumber,
            // Passenger list
            List<Passenger> passengers
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.beginTransaction();

            // 1. Insert booking first
            ContentValues bookingValues = new ContentValues();
            bookingValues.put("flight_id", flightId);
            bookingValues.put("pax", pax);
            bookingValues.put("departure_datetime", departureDatetime);
            bookingValues.put("arrival_datetime", arrivalDatetime);
            bookingValues.put("seat_no", seatNo);
            bookingValues.put("refund_guarantee", refundGuarantee);
            bookingValues.put("total_payment", totalPayment);

            int bookingId = (int) db.insert("booking", null, bookingValues);

            // 2. Insert contact information
            ContentValues contactValues = new ContentValues();
            contactValues.put("booking_id", bookingId);
            contactValues.put("first_name", firstName);
            contactValues.put("last_name", lastName);
            contactValues.put("email", email);
            contactValues.put("country_residence", countryResidence);
            contactValues.put("country_code", countryCode);
            contactValues.put("phone_number", phoneNumber);

            db.insert("contact", null, contactValues);

            // 3. Insert all passengers
            for (Passenger passenger : passengers) {
                passenger.setBookingId(bookingId);
                ContentValues passengerValues = new ContentValues();
                passengerValues.put("booking_id", passenger.getBookingId());
                passengerValues.put("first_name", passenger.getFirstName());
                passengerValues.put("last_name", passenger.getLastName());
                passengerValues.put("gender", passenger.getGender());
                passengerValues.put("date_of_birth", passenger.getDateOfBirth());
                passengerValues.put("nationality", passenger.getNationality());
                passengerValues.put("issue_country", passenger.getCountryOfIssue());
                passengerValues.put("passport_number", passenger.getPassportNumber());
                passengerValues.put("passport_expiry_date", passenger.getPassportExpiry());

                db.insert("passenger", null, passengerValues);
            }

            db.setTransactionSuccessful();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public void clearbookingdatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("booking", null, null);
        db.close();
    }

    public Set<String> getBookedSeats(int flightId, String departureDatetime) {
        Set<String> bookedSeats = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT seat_no FROM booking WHERE flight_id = ? AND departure_datetime = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(flightId), departureDatetime});

        if (cursor != null) {
            Log.d("DB_FETCH", "Query executed. Checking data...");

            while (cursor.moveToNext()) {
                String seatNoString = cursor.getString(0); // Get the comma-separated string

                if (seatNoString != null && !seatNoString.isEmpty()) {
                    String[] seats = seatNoString.split(",\\s*"); // Split by comma and optional whitespace

                    for (String seat : seats) {
                        bookedSeats.add(seat.trim()); // Add each individual seat after trimming whitespace
                        Log.d("DB_FETCH", "Fetched seat: " + seat.trim());
                    }
                }

            }
            cursor.close();
        } else {
            Log.e("DB_FETCH", "Cursor is null. Query failed.");
        }

        db.close();
        return bookedSeats;
    }


}
