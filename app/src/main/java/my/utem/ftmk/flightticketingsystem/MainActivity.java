package my.utem.ftmk.flightticketingsystem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragment.AvailableFlightsFragment;
import fragment.BookingHistoryFragment;
import sqlite.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // *****USE ONLY FOR CLEAR BOOKING DATABASE ONLY*****
        //DatabaseHelper dbHelper = new DatabaseHelper(this);
        //dbHelper.clearbookingdatabase();

        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, new AvailableFlightsFragment())
                .commit();
        
        // Handle navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_book_flight) {
                selectedFragment = new AvailableFlightsFragment();
            } else if (item.getItemId() == R.id.nav_booking_history) {
                selectedFragment = new BookingHistoryFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}
