package my.utem.ftmk.flightticketingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fragment.AddOnsFragment;
import fragment.CustomerDetailsFragment;

public class BookingActivity extends AppCompatActivity {

    private boolean isFragmentReplaced = false; // Track button state
    private Button btnNext;
    private ImageButton btnCloseOrBack;
    private TextView tvBookingSectionName;
    private static final int SEAT_SELECTION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        btnNext = findViewById(R.id.next_button);
        btnCloseOrBack = findViewById(R.id.close_or_back_button);
        tvBookingSectionName = findViewById(R.id.tvBookingSectionName);

        // Initial state - always start with CustomerDetailsFragment
        replaceFragment(new CustomerDetailsFragment());
        btnNext.setText("Continue to add-ons");
        isFragmentReplaced = false;

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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentReplaced) {
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
        SharedPreferences sharedPreferences = getSharedPreferences("CustomerDetails", MODE_PRIVATE); // Replace "CustomerDetails" with your actual SharedPreferences name
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear ALL shared preferences related to customer details!
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

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCustomerDetails();
                finish();
                dialog.dismiss();
            }
        });
    }
}