package my.utem.ftmk.flightticketingsystem;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import fragment.AddOnsFragment;
import fragment.CustomerDetailsFragment;
import utils.Conversions;
import utils.PrefKey;

public class BookingActivity extends AppCompatActivity {

    private boolean isFragmentReplaced = false;
    private Button btnNext;
    private ImageButton btnCloseOrBack;
    private TextView tvBookingSectionName, tvDepartureAirport, tvArrivalAirport, tvDepartureDatetime, tvArrivalDatetime, tvTotalPayment, tvPax;

    private FragmentManager fragmentManager;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        fragmentManager = getSupportFragmentManager();
        btnNext = findViewById(R.id.next_button);
        btnCloseOrBack = findViewById(R.id.close_or_back_button);
        tvBookingSectionName = findViewById(R.id.tvBookingSectionName);
        tvDepartureAirport = findViewById(R.id.tvStartAirport);
        tvArrivalAirport = findViewById(R.id.tvEndAirport);
        tvDepartureDatetime = findViewById(R.id.tvDepartureDatetime);
        tvArrivalDatetime = findViewById(R.id.tvArrivalDatetime);
        tvTotalPayment = findViewById(R.id.tvTotal);
        tvPax = findViewById(R.id.tvPax);

        int pax = getIntent().getIntExtra("pax", 1);
        tvPax.setText(pax + " pax");

        // Load the initial fragment (CustomerDetailsFragment)
        if (savedInstanceState == null) {
            loadInitialFragment();
        }

        btnCloseOrBack.setOnClickListener(v -> {
            if (!isFragmentReplaced) {
                showConfirmDialog(); // Show exit confirmation
            } else {
                switchToCustomerDetails();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (!isFragmentReplaced) {
                switchToAddOns();
            } else {
                proceedToPayment();
            }
        });

        sharedPreferences = getSharedPreferences(PrefKey.PREF_BOOKING, Context.MODE_PRIVATE);
        assignFlightDetails();

        handleBackButton();
    }

    private void assignFlightDetails() {
        Intent intent = getIntent();
        if (intent != null) {
            // Get intent data from FlightAdapter dialog
            int flightId = intent.getIntExtra("flightId", -1);
            String departureAirport = intent.getStringExtra("departureAirport");
            String arrivalAirport = intent.getStringExtra("arrivalAirport");
            String duration = intent.getStringExtra("duration");
            String departureDate = intent.getStringExtra("departureDate");
            String departureTime = intent.getStringExtra("departureTime");
            int pax = intent.getIntExtra("pax", 0);
            double priceRate = intent.getDoubleExtra("priceRate", -1);

            // Process the data
            assert duration != null;
            List<String> datetimes = Conversions.calculateFlightDatetimes(departureDate, departureTime, duration);
            String departureDatetime = datetimes.get(0);
            String arrivalDatetime = datetimes.get(1);
            double totalPayment = Conversions.calculateTotalPayment(pax, priceRate);

            // Add into textviews
            tvDepartureAirport.setText(departureAirport);
            tvArrivalAirport.setText(arrivalAirport);
            tvDepartureDatetime.setText(departureDatetime);
            tvArrivalDatetime.setText(arrivalDatetime);
            tvTotalPayment.setText(String.format("RM %.2f", totalPayment)); // Format to 2 decimal places
            tvPax.setText(String.valueOf(pax));

            // Put into sharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(PrefKey.KEY_FLIGHT_ID, flightId);
            editor.putString(PrefKey.KEY_DEPARTURE_AIRPORT, departureAirport);
            editor.putString(PrefKey.KEY_ARRIVAL_AIRPORT, arrivalAirport);
            editor.putString(PrefKey.KEY_DEPARTURE_DATETIME, departureDatetime);
            editor.putString(PrefKey.KEY_ARRIVAL_DATETIME, arrivalDatetime);
            editor.putFloat(PrefKey.KEY_TOTAL_PAYMENT, (float) totalPayment); // Use putFloat since there's no putDouble
            editor.putInt(PrefKey.KEY_PAX, pax);
            editor.apply(); // Apply changes asynchronously
        }
    }

    private void handleBackButton() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isFragmentReplaced) {
                    showConfirmDialog();
                } else {
                    switchToCustomerDetails();
                }
            }
        });
    }

    private void loadInitialFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        CustomerDetailsFragment customerDetailsFragment = new CustomerDetailsFragment();
        transaction.add(R.id.passanger_detail_fragment_container, customerDetailsFragment, "CustomerDetails");
        transaction.commit();
    }

    private void switchToAddOns() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment customerFragment = fragmentManager.findFragmentByTag("CustomerDetails");
        if (customerFragment != null) {
            transaction.hide(customerFragment);
        }

        AddOnsFragment addOnsFragment = (AddOnsFragment) fragmentManager.findFragmentByTag("AddOns");
        if (addOnsFragment == null) {
            addOnsFragment = new AddOnsFragment();
            transaction.add(R.id.passanger_detail_fragment_container, addOnsFragment, "AddOns");
        } else {
            transaction.show(addOnsFragment);
        }

        transaction.addToBackStack(null);
        transaction.commit();

        btnNext.setText("Proceed to Payment");
        tvBookingSectionName.setText("Add-ons");
        btnCloseOrBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_ios_24));
        isFragmentReplaced = true;
    }

    private void switchToCustomerDetails() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment addOnsFragment = fragmentManager.findFragmentByTag("AddOns");
        if (addOnsFragment != null) {
            transaction.hide(addOnsFragment);
        }

        Fragment customerFragment = fragmentManager.findFragmentByTag("CustomerDetails");
        if (customerFragment != null) {
            transaction.show(customerFragment);
        }

        transaction.commit();

        btnNext.setText("Continue to add-ons");
        tvBookingSectionName.setText("Passenger details");
        btnCloseOrBack.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_close_24));
        isFragmentReplaced = false;
    }

    private void proceedToPayment() {
        Intent intent = new Intent(BookingActivity.this, PaymentValidationActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearCustomerDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences(PrefKey.PREF_BOOKING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void clearFlightDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences(PrefKey.PREF_BOOKING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_confirm, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button noButton = dialogView.findViewById(R.id.noButton);
        Button yesButton = dialogView.findViewById(R.id.yesButton);

        noButton.setOnClickListener(v -> dialog.dismiss());

        yesButton.setOnClickListener(v -> {
            clearCustomerDetails();
            clearFlightDetails();
            finish();
            dialog.dismiss();
        });
    }
}
