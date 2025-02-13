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

import sqlite.DatabaseHelper;
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
                    //saveBookingToDatabase(); // Save booking before proceeding
                }
            }
        });
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
