package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public long insertBooking(int flightId, int pax, String depDatetime, String arrDatetime, String seatNo, int refund, double payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("flight_id", flightId);
        values.put("pax", pax);
        values.put("departure_datetime", depDatetime);
        values.put("arrival_datetime", arrDatetime);
        values.put("seat_no", seatNo);
        values.put("refund_guarantee", refund);
        values.put("total_payment", payment);

        return db.insert("booking", null, values);
    }

    public long insertContact(int bookingId, String firstName, String lastName, String email, String residenceCountry, String countryCode, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("booking_id", bookingId);
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("residence_country", residenceCountry);
        values.put("country_code", countryCode);
        values.put("phone_number", phoneNumber);

        return db.insert("contact", null, values);
    }

    public long insertPassenger(Passenger passenger) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("booking_id", passenger.getBookingId());
        values.put("first_name", passenger.getFirstName());
        values.put("last_name", passenger.getLastName());
        values.put("gender", passenger.getGender());
        values.put("date_of_birth", passenger.getDateOfBirth());
        values.put("nationality", passenger.getNationality());
        values.put("issue_country", passenger.getCountryOfIssue());
        values.put("passport_number", passenger.getPassportNumber());
        values.put("passport_expiry_date", passenger.getPassportExpiry());

        return db.insert("passenger", null, values);
    }

    public Cursor getBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM booking", null);
    }

    public Cursor getPassengers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM passenger", null);
    }
}
