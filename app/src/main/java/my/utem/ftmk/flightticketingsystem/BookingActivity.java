package my.utem.ftmk.flightticketingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import SQLite.DatabaseHelper;
import fragment.AddOnsFragment;
import fragment.CustomerDetailsFragment;
import model.Passenger;

public class BookingActivity extends AppCompatActivity {

    private boolean isFragmentReplaced = false;
    private Button btnNext;
    private ImageButton btnCloseOrBack;
    private TextView tvBookingSectionName;
    private DatabaseHelper databaseHelper; // Database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        databaseHelper = new DatabaseHelper(this); // Initialize database helper
        btnNext = findViewById(R.id.next_button);
        btnCloseOrBack = findViewById(R.id.close_or_back_button);
        tvBookingSectionName = findViewById(R.id.tvBookingSectionName);

        replaceFragment(new CustomerDetailsFragment());
        btnNext.setText("Continue to add-ons");
        isFragmentReplaced = false;

        btnCloseOrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentReplaced) {
                    finish();
                } else {
                    replaceFragment(new CustomerDetailsFragment());
                    btnNext.setText("Continue to add-ons");
                    tvBookingSectionName.setText("Passenger details");
                    btnCloseOrBack.setImageDrawable(ContextCompat.getDrawable(BookingActivity.this, R.drawable.baseline_close_24));
                    isFragmentReplaced = false;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentReplaced) {
                    replaceFragment(new AddOnsFragment());
                    btnNext.setText("Proceed to Payment");
                    tvBookingSectionName.setText("Add-ons");
                    btnCloseOrBack.setImageDrawable(ContextCompat.getDrawable(BookingActivity.this, R.drawable.baseline_arrow_back_ios_24));
                    isFragmentReplaced = true;
                } else {
                    saveBookingToDatabase(); // Save booking before proceeding
                }
            }
        });
    }

    private void saveBookingToDatabase() {
        int flightId = 1; // Retrieve actual flightId
        int pax = 2;
        String depDatetime = "2025-02-15 10:00";
        String arrDatetime = "2025-02-15 14:00";
        String seatNo = "A1, A2";
        int refundGuarantee = 1;
        double totalPayment = 500.0;

        long bookingId = databaseHelper.insertBooking(flightId, pax, depDatetime, arrDatetime, seatNo, refundGuarantee, totalPayment);
        Log.d("DB_INSERT", "Insert Booking Result: " + bookingId);

        if (bookingId != -1) {
            long contactId = databaseHelper.insertContact((int) bookingId, "John", "Doe", "john.doe@example.com", "Malaysia", "+60", "123456789");
            Log.d("DB_INSERT", "Insert Contact Result: " + contactId);

            insertPassengers((int) bookingId);

            Toast.makeText(this, "Booking saved!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BookingActivity.this, PaymentValidationActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to save booking!", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertPassengers(int bookingId) {
        Passenger passenger1 = new Passenger("Alice", "Smith", "Female", "1990-05-10", "Malaysia", "Malaysia", "A1234567", "2030-05-10");
        Passenger passenger2 = new Passenger("Bob", "Johnson", "Male", "1992-07-20", "Malaysia", "Malaysia", "B7654321", "2032-07-20");

        long passenger1Id = databaseHelper.insertPassenger(booking_id, passenger1.getFirstName(), passenger1.getLastName(), passenger1.getDateOfBirth(),
                passenger1.getNationality(), passenger1.getCountryOfIssue(), passenger1.getPassportNumber(), passenger1.getPassportExpiry());
        Log.d("DB_INSERT", "Insert Passenger 1 Result: " + passenger1Id);

        long passenger2Id = databaseHelper.insertPassenger(booking_id, passenger2.getFirstName(), passenger2.getLastName(), passenger2.getDateOfBirth(),
                passenger2.getNationality(), passenger2.getCountryOfIssue(), passenger2.getPassportNumber(), passenger2.getPassportExpiry());
        Log.d("DB_INSERT", "Insert Passenger 2 Result: " + passenger2Id);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFragmentReplaced", isFragmentReplaced);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.passanger_detail_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
