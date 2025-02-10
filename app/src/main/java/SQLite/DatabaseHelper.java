package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "flight_booking.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS flight");
        db.execSQL("DROP TABLE IF EXISTS booking");
        onCreate(db);
    }

    // Method to insert flight data from XML
    public void importFlightsFromXML(File xmlFile) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new FileReader(xmlFile));

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            int eventType = parser.getEventType();
            String text = null;
            String departure = null, arrival = null, depTime = null, arrTime = null, duration = null;
            double price = 0.0;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equals("flight")) {
                            departure = arrival = depTime = arrTime = duration = null;
                            price = 0.0;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equals("departure_airport")) departure = text;
                        else if (tagName.equals("arrival_airport")) arrival = text;
                        else if (tagName.equals("departure_time")) depTime = text;
                        else if (tagName.equals("arrival_time")) arrTime = text;
                        else if (tagName.equals("flight_duration")) duration = text;
                        else if (tagName.equals("price_rate")) price = Double.parseDouble(text);
                        else if (tagName.equals("flight")) {
                            values.put("departure_airport", departure);
                            values.put("arrival_airport", arrival);
                            values.put("departure_time", depTime);
                            values.put("arrival_time", arrTime);
                            values.put("flight_duration", duration);
                            values.put("price_rate", price);
                            db.insert("flight", null, values);
                        }
                        break;
                }
                eventType = parser.next();
            }
            db.close();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    // Method to export flight data to XML
    public void exportFlightsToXML(File xmlFile) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM flight", null);

        try {
            XmlSerializer serializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            serializer.setOutput(writer);

            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "flights");

            while (cursor.moveToNext()) {
                serializer.startTag("", "flight");

                serializer.startTag("", "flight_id");
                serializer.text(cursor.getString(0));
                serializer.endTag("", "flight_id");

                serializer.startTag("", "departure_airport");
                serializer.text(cursor.getString(1));
                serializer.endTag("", "departure_airport");

                serializer.startTag("", "arrival_airport");
                serializer.text(cursor.getString(2));
                serializer.endTag("", "arrival_airport");

                serializer.startTag("", "departure_time");
                serializer.text(cursor.getString(3));
                serializer.endTag("", "departure_time");

                serializer.startTag("", "arrival_time");
                serializer.text(cursor.getString(4));
                serializer.endTag("", "arrival_time");

                serializer.startTag("", "flight_duration");
                serializer.text(cursor.getString(5));
                serializer.endTag("", "flight_duration");

                serializer.startTag("", "price_rate");
                serializer.text(cursor.getString(6));
                serializer.endTag("", "price_rate");

                serializer.endTag("", "flight");
            }
            serializer.endTag("", "flights");
            serializer.endDocument();

            FileOutputStream fos = new FileOutputStream(xmlFile);
            fos.write(writer.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            db.close();
        }
    }
}
