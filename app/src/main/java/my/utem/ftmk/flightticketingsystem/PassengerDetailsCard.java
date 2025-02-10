package my.utem.ftmk.flightticketingsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // For logging API responses and debugging
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException; // Needed for handling API response errors
import java.util.ArrayList;
import java.util.List;
import fragment.AddOnsFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PassengerDetailsCard extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, countryResidenceEditText, phoneNumberEditText;
    private TextView firstNameErrorTextView, lastNameErrorTextView, emailErrorTextView, phoneNumberErrorTextView;
    private Spinner countryCodeSpinner;
    private ImageView closeButton;
    private Button nextButton;
    private int pax = 0; // Variable to hold the number of passengers
    private TextView paxTextView;
    private OkHttpClient client = new OkHttpClient();

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
        fetchCountryCodes();
       /* closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> showConfirmationDialog());

        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> validateAndMoveToNextScreen());
        //paxTextView = findViewById(R.id.text_pax);
       // paxTextView.setText(pax + " pax");*/



    }

    private void fetchCountryCodes() {
        String url = "https://restcountries.com/v3.1/all"; // API URL

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Failed to fetch country codes", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("API_ERROR", "Unexpected response: " + response);
                    return;
                }

                String jsonData = response.body().string();
                Log.d("API_RESPONSE", jsonData); // ✅ Log API data for debugging
                List<String> countryCodes = parseCountryCodes(jsonData);

                runOnUiThread(() -> setupCountryCodeSpinner(countryCodes));
            }
        });
    }

    private List<String> parseCountryCodes(String jsonData) {
        List<String> countryCodes = new ArrayList<>();

        JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject country = jsonArray.get(i).getAsJsonObject();
            if (country.has("idd")) {
                JsonObject idd = country.getAsJsonObject("idd");
                if (idd.has("root")) {
                    String root = idd.get("root").getAsString();
                    String suffix = idd.has("suffixes") ? idd.getAsJsonArray("suffixes").get(0).getAsString() : "";
                    countryCodes.add(root + suffix);
                }
            }
        }
        return countryCodes;
    }

    private void setupCountryCodeSpinner(List<String> countryCodes) {

        if (countryCodes.isEmpty()) {
            countryCodes.add("No Data"); // Default fallback
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryCodes);
        adapter.setDropDownViewResource(android.R.layout.expandable_list_content);
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

            Intent intent = new Intent(this, AddOnsFragment.class);
            startActivity(intent);
        }
        else {
            // Form validation failed, error messages will be displayed
        }
    }
}