package my.utem.ftmk.flightticketingsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fragment.AddOnFragment;

public class PassengerDetailsCard extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, countryResidenceEditText, phoneNumberEditText;
    private TextView firstNameErrorTextView, lastNameErrorTextView, emailErrorTextView, phoneNumberErrorTextView;
    private Spinner countryCodeSpinner;
    private ImageView closeButton;
    private Button nextButton;
    private int pax = 0; // Variable to hold the number of passengers
    private TextView paxTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_contact_details);

        // Get pax from the intent that opened this activity
        pax = getIntent().getIntExtra("pax", 1);


        // Initialize UI elements
        firstNameEditText = findViewById(R.id.first_name);
        lastNameEditText = findViewById(R.id.last_name);
        emailEditText = findViewById(R.id.email);
        countryResidenceEditText = findViewById(R.id.country_residence);
        phoneNumberEditText = findViewById(R.id.phone_number);
        firstNameErrorTextView = findViewById(R.id.first_name_error);
        lastNameErrorTextView = findViewById(R.id.last_name_error);
        emailErrorTextView = findViewById(R.id.email_error);
        phoneNumberErrorTextView = findViewById(R.id.phone_number_error);
        countryCodeSpinner = findViewById(R.id.country_code);
        setupCountryCodeSpinner();
       /* closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> showConfirmationDialog());

        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> validateAndMoveToNextScreen());
        //paxTextView = findViewById(R.id.text_pax);
       // paxTextView.setText(pax + " pax");*/



    }


    private void setupCountryCodeSpinner() {
        // You'll likely want to load these from a resource or an API call
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add("+1");
        countryCodes.add("+44");
        countryCodes.add("+60");
        countryCodes.add("+81");
        countryCodes.add("+86");
        // Setup Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCodeSpinner.setAdapter(adapter);
    }


    //Validation methods
    private boolean validateForm() {
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
    }

    private void showConfirmationDialog() {
        if (!isFormFilled()) {
            //If the form is empty, just close the current activity
            finish();
            return;
        }
        //else:
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to go back?");
        builder.setMessage("The information you've entered will be lost if you proceed.");
        builder.setCancelable(false);
        builder.setNegativeButton("No", (dialog, which) -> {
            // Do nothing and close the dialog
            dialog.dismiss();
        });

        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Go back if the user confirms
            finish();
        });


        AlertDialog dialog = builder.create();
        dialog.show();

        Button yesButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button noButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);


        if(yesButton!= null && noButton !=null){
            yesButton.setTextColor(Color.RED);
            noButton.setTextColor(Color.BLACK);
        }
    }


    //Helper method to check if the form is filled
    private boolean isFormFilled() {
        return !TextUtils.isEmpty(firstNameEditText.getText().toString()) ||
                !TextUtils.isEmpty(lastNameEditText.getText().toString()) ||
                !TextUtils.isEmpty(emailEditText.getText().toString()) ||
                !TextUtils.isEmpty(countryResidenceEditText.getText().toString()) ||
                !TextUtils.isEmpty(phoneNumberEditText.getText().toString());

    }

    private void validateAndMoveToNextScreen() {
        if (validateForm() && pax > 0) {
            // Form is valid, move to the next screen
            // Implement moving to the add-on screen or any other logic here
            //Example: Replace the current activity with the next activity

            Intent intent = new Intent(this, AddOnFragment.class);
            startActivity(intent);
        }
        else {
            // Form validation failed, error messages will be displayed
        }
    }
}