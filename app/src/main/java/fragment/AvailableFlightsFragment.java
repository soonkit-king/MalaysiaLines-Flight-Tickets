package fragment;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import adapter.FlightAdapter;
import model.Flight;
import my.utem.ftmk.flightticketingsystem.R;
import sqlite.DatabaseHelper;

public class AvailableFlightsFragment extends Fragment {

    RecyclerView recyclerView;
    FlightAdapter flightAdapter;
    private List<Flight> flightList;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_flights, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.rvFlights);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load Flights
        initializeFlightsInDatabase();
        loadFlights();

        // Set Adapter
        flightAdapter = new FlightAdapter(getContext(),flightList);
        recyclerView.setAdapter(flightAdapter);

        return view;
    }

    private void initializeFlightsInDatabase() {
        // Add some initial flights to the database if it's empty
        Cursor cursor = dbHelper.getAllFlights();
        if (cursor.getCount() == 0) {
            // Insert sample flights
            dbHelper.insertFlight(
                    "Kuala Lumpur (KUL)", "Penang (PEN)",
                    "07:30", "08:40",
                    "1h 10m", 189.00
            );

            dbHelper.insertFlight(
                    "Penang (PEN)", "Kota Kinabalu (BKI)",
                    "09:15", "11:45",
                    "2h 30m", 299.50
            );

            dbHelper.insertFlight(
                    "Kuching (KCH)", "Johor Bahru (JHB)",
                    "08:00", "10:00",
                    "2h 00m", 259.00
            );

            dbHelper.insertFlight(
                    "Kota Bharu (KBR)", "Kuala Lumpur (KUL)",
                    "10:30", "11:45",
                    "1h 15m", 199.00
            );

            dbHelper.insertFlight(
                    "Langkawi (LGK)", "Kuching (KCH)",
                    "11:00", "13:45",
                    "2h 45m", 329.00
            );

            dbHelper.insertFlight(
                    "Kuala Lumpur (KUL)", "Kuching (KCH)",
                    "14:30", "16:15",
                    "1h 45m", 279.00
            );

            dbHelper.insertFlight(
                    "Johor Bahru (JHB)", "Kota Kinabalu (BKI)",
                    "13:15", "15:45",
                    "2h 30m", 309.00
            );

            dbHelper.insertFlight(
                    "Kota Kinabalu (BKI)", "Penang (PEN)",
                    "16:00", "18:30",
                    "2h 30m", 289.00
            );

            dbHelper.insertFlight(
                    "Kuching (KCH)", "Langkawi (LGK)",
                    "15:45", "18:30",
                    "2h 45m", 319.00
            );

            dbHelper.insertFlight(
                    "Penang (PEN)", "Johor Bahru (JHB)",
                    "17:30", "19:00",
                    "1h 30m", 219.00
            );
        }
        cursor.close();
    }

    private void loadFlights() {
        flightList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllFlights();

        try {
            while (cursor.moveToNext()) {
                String departureAirport = cursor.getString(cursor.getColumnIndexOrThrow("departure_airport"));
                String arrivalAirport = cursor.getString(cursor.getColumnIndexOrThrow("arrival_airport"));
                String departureTime = cursor.getString(cursor.getColumnIndexOrThrow("departure_time"));
                String arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow("arrival_time"));
                String flightDuration = cursor.getString(cursor.getColumnIndexOrThrow("flight_duration"));
                double priceRate = cursor.getDouble(cursor.getColumnIndexOrThrow("price_rate"));

                Flight flight = new Flight(
                        departureAirport,
                        arrivalAirport,
                        departureTime,
                        arrivalTime,
                        flightDuration,
                        priceRate
                );
                flightList.add(flight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }
}
