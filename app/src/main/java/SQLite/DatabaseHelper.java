package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

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
                "residence_country TEXT, " +
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
        ContentValues values = new ContentValues();

        values.put("departure_airport", departureAirport);
        values.put("arrival_airport", arrivalAirport);
        values.put("departure_time", departureTime);
        values.put("arrival_time", arrivalTime);
        values.put("flight_duration", flightDuration);
        values.put("price_rate", priceRate);

        // Insert the row and return the primary key value of the new row
        long newRowId = db.insert("flight", null, values);

        // Close the database connection
        db.close();
    }

    public boolean insertCompleteBooking(
            int flightId,
            int pax,
            String departureDatetime,
            String arrivalDatetime,
            String seatNo,
            int refund,
            double payment,
            // Contact details
            String firstName,
            String lastName,
            String email,
            String residenceCountry,
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
            bookingValues.put("refund_guarantee", refund);
            bookingValues.put("total_payment", payment);
            
            int bookingId = (int) db.insert("booking", null, bookingValues);

            // 2. Insert contact information
            ContentValues contactValues = new ContentValues();
            contactValues.put("booking_id", bookingId);
            contactValues.put("first_name", firstName);
            contactValues.put("last_name", lastName);
            contactValues.put("email", email);
            contactValues.put("residence_country", residenceCountry);
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
}
