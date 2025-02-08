package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MalaysiaLines.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_PASSENGERS = "passengers";
    private static final String TABLE_REFUND = "refund_guarantee";

    // Common Columns
    private static final String COLUMN_ID = "id";

    // Contacts Table Columns
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_PHONE = "phone";

    // Passengers Table Columns
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_NATIONALITY = "nationality";
    private static final String COLUMN_COUNTRY_ISSUE = "country_issue";
    private static final String COLUMN_PASSPORT_NUMBER = "passport_number";
    private static final String COLUMN_PASSPORT_EXPIRY = "passport_expiry";

    // Refund Guarantee Table Columns
    private static final String COLUMN_REFUND_STATUS = "refund_status"; // True/False

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createContactsTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_COUNTRY + " TEXT, " +
                COLUMN_PHONE + " TEXT)";

        String createPassengersTable = "CREATE TABLE " + TABLE_PASSENGERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_DOB + " TEXT, " +
                COLUMN_NATIONALITY + " TEXT, " +
                COLUMN_COUNTRY_ISSUE + " TEXT, " +
                COLUMN_PASSPORT_NUMBER + " TEXT, " +
                COLUMN_PASSPORT_EXPIRY + " TEXT);";

        String createRefundTable = "CREATE TABLE " + TABLE_REFUND + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REFUND_STATUS + " BOOLEAN)";

        db.execSQL(createContactsTable);
        db.execSQL(createPassengersTable);
        db.execSQL(createRefundTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSENGERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REFUND);
        onCreate(db);
    }

    public boolean insertRefundGuarantee(boolean refundStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REFUND_STATUS, refundStatus);
        long result = db.insert(TABLE_REFUND, null, values);
        return result != -1;
    }

    public boolean getRefundStatus(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REFUND, new String[]{COLUMN_REFUND_STATUS}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            boolean status = cursor.getInt(0) > 0;
            cursor.close();
            return status;
        }
        return false;
    }
}
