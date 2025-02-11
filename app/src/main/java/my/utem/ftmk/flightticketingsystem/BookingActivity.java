package my.utem.ftmk.flightticketingsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

       pax = getIntent().getIntExtra("pax", 1);
        totalPayment = getIntent().getDoubleExtra("totalPayment", 1.0);
        double ratePerPax = pax*totalPayment;
        ratePayment.setText(String.format("Rate per pax: %.2f", ratePerPax));

        tvPax.setText(pax + " pax");

       // ratePayment.setText(totalPayment*pax);

        btnCloseOrBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentReplaced) {
                    // Goes from customer details page to main page
                    finish();
                    // TODO: Show a dialog and ask if they are sure to or not to exit bcs it will remove inputs
                    // TODO: Clear/remove all the inputs from sharedpreferences
                } else {
                    // Goes from add-ons page to customer details page
                    replaceFragment(new CustomerDetailsFragment());
                    btnNext.setText("Add-ons");
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


}
