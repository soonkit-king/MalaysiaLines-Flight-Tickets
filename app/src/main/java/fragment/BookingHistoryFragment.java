package fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList; // Import ArrayList
import java.util.List;

import sqlite.DatabaseHelper;
import adapter.BookingAdapter;
import model.Booking;
import my.utem.ftmk.flightticketingsystem.R;

public class BookingHistoryFragment extends Fragment {

    private DatabaseHelper db;
    private RecyclerView rvBooking;
    private List<Booking> bookingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_history, container, false);

        rvBooking = view.findViewById(R.id.rvBookings);
        rvBooking.setLayoutManager(new LinearLayoutManager(getContext()));

        loadBookingHistory(); // Load booking history

        // Set up the adapter after loading the booking history
        BookingAdapter bookingAdapter = new BookingAdapter(getActivity(), bookingList);
        rvBooking.setAdapter(bookingAdapter); // Use rvBooking instead of recyclerView

        return view;
    }

    private void loadBookingHistory() {
        Cursor cursor = db.getBookings();
        bookingList = new ArrayList<>(); // Initialize the bookingList

        if (cursor != null) {
            // Print out the column names for debugging
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                Log.d("BookingHistory", "Column: " + columnName);
            }

            if (cursor.moveToFirst()) {
                do {
                    // Extract data from the cursor
                    @SuppressLint("Range") String departureDatetime = cursor.getString(cursor.getColumnIndex("departure_datetime")); // Correct column name
                    @SuppressLint("Range") String arrivalDatetime = cursor.getString(cursor.getColumnIndex("arrival_datetime")); // Correct column name
                    @SuppressLint("Range") String seatNo = cursor.getString(cursor.getColumnIndex("seat_no")); // Correct column name
                    @SuppressLint("Range") boolean hasRefundGuarantee = cursor.getInt(cursor.getColumnIndex("refund_guarantee")) > 0; // Correct column name
                    @SuppressLint("Range") int pax = cursor.getInt(cursor.getColumnIndex("pax")); // Correct column name
                    @SuppressLint("Range") double totalPayment = cursor.getDouble(cursor.getColumnIndex("total_payment")); // Correct column name

                    // Check if any column index is -1
                    if (departureDatetime == null || arrivalDatetime == null || seatNo == null) {
                        Log.e("BookingHistory", "One of the required fields is null");
                        continue; // Skip this entry if any required field is null
                    }

                    // Create a new Booking object
                    @SuppressLint("Range") Booking booking = new Booking(
                            cursor.getString(cursor.getColumnIndex("departure_airport")), // You may need to join with flight table to get this
                            cursor.getString(cursor.getColumnIndex("arrival_airport")), // You may need to join with flight table to get this
                            departureDatetime,
                            arrivalDatetime,
                            seatNo,
                            hasRefundGuarantee,
                            pax,
                            totalPayment
                    );
                    bookingList.add(booking);
                } while (cursor.moveToNext());

                cursor.close(); // Close the cursor after use
            }
        }
    }
}