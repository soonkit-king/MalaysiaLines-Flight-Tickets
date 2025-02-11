package my.utem.ftmk.flightticketingsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import adapter.PassengerDetailAdapter;
import fragment.AddOnsFragment;
import fragment.CustomerDetailsFragment;

public class BookingActivity extends AppCompatActivity {

    private boolean isFragmentReplaced = false; // Track button state
    private Button btnNext;
    private ImageButton btnCloseOrBack;
    private TextView tvBookingSectionName,ratePayment ;
    private static final int SEAT_SELECTION_REQUEST = 1;
    private TextView tvPax;
    private int pax = 0;
    private double totalPayment = 0;
    private CustomerDetailsFragment customerDetailsFragment;  // Fragment reference

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        customerDetailsFragment = new CustomerDetailsFragment();




        btnNext = findViewById(R.id.next_button);
        btnCloseOrBack = findViewById(R.id.close_button);
        tvBookingSectionName = findViewById(R.id.tvBookingSectionName);
        ratePayment = findViewById(R.id.totalPayment);
        tvPax = findViewById(R.id.tvPax);

        // Initial state - always start with CustomerDetailsFragment
        replaceFragment(new CustomerDetailsFragment());
        btnNext.setText("Continue to add-ons");
        isFragmentReplaced = false;

        // Retrieve values from Intent
        int pax = getIntent().getIntExtra("pax", 1); // Pax as integer
        double totalPayment = getIntent().getDoubleExtra("totalPayment", 1.0); // Total payment as double


        ratePayment.setText(String.format("RM %.2f", totalPayment));

        tvPax.setText(pax + " pax");

        btnCloseOrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentReplaced) {
                    // Goes from customer details page to main page
                    // Show a dialog and ask if they are sure to or not to exit bcs it will remove inputs
                    showConfirmDialog();
                } else {
                    // Goes from add-ons page to customer details page
                    replaceFragment(new CustomerDetailsFragment());
                    btnNext.setText("Continue to add-ons"); // Changed "Add-ons" to "Continue to add-ons" to match initial state
                    tvBookingSectionName.setText("Passenger details");
                    btnCloseOrBack.setImageDrawable(ContextCompat.getDrawable(BookingActivity.this, R.drawable.baseline_close_24));
                    isFragmentReplaced = false;
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (!isFragmentReplaced) {
                // Get Fragment instance and validate the form
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.passanger_detail_fragment_container);
                RecyclerView recyclerView = findViewById(R.id.rvPassengerDetail);
                PassengerDetailAdapter adapter = (PassengerDetailAdapter) recyclerView.getAdapter();


                if (fragment instanceof CustomerDetailsFragment) {
                    if (!((CustomerDetailsFragment) fragment).validateForm() && adapter != null && !adapter.validateAllPassengers(recyclerView)) {
                        return;  // Stop if validation fails
                    }
                }

                // Goes from customer details page to add-ons page
                replaceFragment(new AddOnsFragment());
                btnNext.setText("Proceed to Payment");
                tvBookingSectionName.setText("Add-ons");
                btnCloseOrBack.setImageDrawable(ContextCompat.getDrawable(BookingActivity.this, R.drawable.baseline_arrow_back_ios_24));
                isFragmentReplaced = true;
            } else {
                // Goes from add-ons page to main page
                Intent intent = new Intent(BookingActivity.this, PaymentValidationActivity.class);
                startActivity(intent);
                finish();
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
        transaction.addToBackStack(null); // Allow user to navigate back
        transaction.commit();
    }


@Override
    protected void onStart() {
        super.onStart();
    }

    private void clearCustomerDetails() {
        // Get the list of all SharedPreferences files in the app
        SharedPreferences sharedPreferences = getSharedPreferences(CustomerDetailsFragment.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clears all stored data
        editor.apply(); // Apply changes
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

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to clear SharedPreferences
                clearCustomerDetails();

                //After the user has confirm, the code will return to mainActivity page
                finish();
                dialog.dismiss();
            }
        });
    }
}