package my.utem.ftmk.flightticketingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private EditText firstNameEditText, lastNameEditText, emailEditText, countryResidenceEditText, phoneNumberEditText;
    private TextView firstNameErrorTextView, lastNameErrorTextView, emailErrorTextView, phoneNumberErrorTextView;
    private ImageButton btnCloseOrBack;
    private TextView tvBookingSectionName;
    private TextView tvPax;
    private int pax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
      Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.passanger_detail_fragment_container);
        if (fragment != null) {
            View fragmentView = fragment.getView();  // Get the fragment's root view
            if (fragmentView != null) {
                firstNameEditText = fragmentView.findViewById(R.id.first_name);
                lastNameEditText = fragmentView.findViewById(R.id.last_name);
                emailEditText = fragmentView.findViewById(R.id.email);
                countryResidenceEditText = fragmentView.findViewById(R.id.country_residence);
                phoneNumberEditText = fragmentView.findViewById(R.id.phone_number);
                firstNameErrorTextView = fragmentView.findViewById(R.id.first_name_error);
                lastNameErrorTextView = fragmentView.findViewById(R.id.last_name_error);
                emailErrorTextView = fragmentView.findViewById(R.id.email_error);
                phoneNumberErrorTextView = fragmentView.findViewById(R.id.phone_number_error);
            }
        }
        //View fragmentView = fragment.getView();
        /*  firstNameEditText = fragmentView.findViewById(R.id.first_name);
        lastNameEditText = fragmentView.findViewById(R.id.last_name);
        emailEditText = fragmentView.findViewById(R.id.email);
        countryResidenceEditText = fragmentView.findViewById(R.id.country_residence);
        phoneNumberEditText = fragmentView.findViewById(R.id.phone_number);
        firstNameErrorTextView = fragmentView.findViewById(R.id.first_name_error);
        lastNameErrorTextView = fragmentView.findViewById(R.id.last_name_error);
        emailErrorTextView = fragmentView.findViewById(R.id.email_error);
        phoneNumberErrorTextView = fragmentView.findViewById(R.id.phone_number_error);*/


        btnNext = findViewById(R.id.next_button);
        btnCloseOrBack = findViewById(R.id.close_button);
        tvBookingSectionName = findViewById(R.id.tvBookingSectionName);
        tvPax = findViewById(R.id.tvPax);

        // Initial state - always start with CustomerDetailsFragment
        replaceFragment(new CustomerDetailsFragment());
        btnNext.setText("Continue to add-ons");
        isFragmentReplaced = false;

        pax = getIntent().getIntExtra("pax", 1);

        tvPax.setText(pax + " pax");

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

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  validateForm();
                if (!isFragmentReplaced) {
                    // Goes from customer details page to add-ons page
                   // validateForm();
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
   /*private boolean validateForm() {
        boolean isValid = true;

        if (TextUtils.isEmpty(firstNameEditText.getText().toString())) {
            firstNameErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            firstNameErrorTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(lastNameEditText.getText().toString())) {
            lastNameErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            lastNameErrorTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailErrorTextView.setText("*Required field");
            emailErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            emailErrorTextView.setText("Invalid email format");
            emailErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            emailErrorTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(phoneNumberEditText.getText().toString())) {
            phoneNumberErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            phoneNumberErrorTextView.setVisibility(View.GONE);
        }

        return isValid;
    }*/

}
