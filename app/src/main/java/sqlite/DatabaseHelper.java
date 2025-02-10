package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public Cursor getFlights() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM flight", null);
    }

    public boolean deleteFlight(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("flight", "flight_id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // CRUD operations for booking
    public boolean insertBooking(int flightId, int pax, String depDatetime, String arrDatetime, String seatNo, int refund, double payment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("flight_id", flightId);
        values.put("pax", pax);
        values.put("departure_datetime", depDatetime);
        values.put("arrival_datetime", arrDatetime);
        values.put("seat_no", seatNo);
        values.put("refund_guarantee", refund);
        values.put("total_payment", payment);
        long result = db.insert("booking", null, values);
        return result != -1;
    }

    public Cursor getBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM booking", null);
    }

    // CRUD operations for contact and passenger follow the same pattern
}
