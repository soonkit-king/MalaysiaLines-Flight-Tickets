package fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList; // Import ArrayList
import java.util.List;

import sqlite.DatabaseHelper;
import adapter.BookingHistoryAdapter;
import model.BookingHistory;
import my.utem.ftmk.flightticketingsystem.R;

public class BookingHistoryFragment extends Fragment {

    RecyclerView recyclerView;
    BookingHistoryAdapter bookingHistoryAdapter;
    private List<BookingHistory> bookingHistoryList;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_history, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadAllBookingHistory();

        // Set Adapter
        bookingHistoryAdapter = new BookingHistoryAdapter(getContext(), bookingHistoryList);
        recyclerView.setAdapter(bookingHistoryAdapter);

        return view;
    }

    private void loadAllBookingHistory() {
        bookingHistoryList = new ArrayList<>();
        dbHelper = new DatabaseHelper(getContext());
        Cursor cursor = dbHelper.getAllBookingHistory();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow("booking_id"));
                String departureAirport = cursor.getString(cursor.getColumnIndexOrThrow("departure_airport"));
                String arrivalAirport = cursor.getString(cursor.getColumnIndexOrThrow("arrival_airport"));
                String departureDatetime = cursor.getString(cursor.getColumnIndexOrThrow("departure_datetime"));
                String arrivalDatetime = cursor.getString(cursor.getColumnIndexOrThrow("arrival_datetime"));
                String seatNo = cursor.getString(cursor.getColumnIndexOrThrow("seat_no"));
                boolean hasRefundGuarantee = cursor.getInt(cursor.getColumnIndexOrThrow("refund_guarantee")) == 1;
                int pax = cursor.getInt(cursor.getColumnIndexOrThrow("pax"));
                double totalPayment = cursor.getDouble(cursor.getColumnIndexOrThrow("total_payment"));

                // Create BookingHistory object
                BookingHistory booking = new BookingHistory(bookingId, departureAirport, arrivalAirport, departureDatetime, arrivalDatetime, seatNo, hasRefundGuarantee, pax, totalPayment);
                bookingHistoryList.add(booking);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Notify adapter if initialized
        if (bookingHistoryAdapter != null) {
            bookingHistoryAdapter.notifyDataSetChanged();
        }
    }

}