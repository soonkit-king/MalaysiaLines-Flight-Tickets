package my.utem.ftmk.flightticketingsystem;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Passenger;

public class PaymentValidationActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    private SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "CustomerDetails";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COUNTRY_RESIDENCE = "countryResidence";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_COUNTRY_CODE = "countryCode";
    private static final String KEY_PAX_COUNT = "paxCount"; // Store pax count
    private static final String KEY_PASSENGER_DATA = "passengerData"; // Store passenger details (JSON)
    private static final String KEY_COUNTRY_CODES = "countryCodes"; // Key for storing country codes
    private static String fullPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validation);

        ImageView dot1 = findViewById(R.id.dot1);
        ImageView dot2 = findViewById(R.id.dot2);
        ImageView dot3 = findViewById(R.id.dot3);

        // Function to animate dot scaling smoothly
        Runnable animateDots = new Runnable() {
            int currentDot = 1;

            @Override
            public void run() {
                // Animate the current dot smoothly
                animateDot(getDotByIndex(currentDot));

                // Move to the next dot
                currentDot = (currentDot % 3) + 1;

                // Repeat every 400ms for smoother effect
                handler.postDelayed(this, 400);
            }

            private ImageView getDotByIndex(int index) {
                if (index == 1) return dot1;
                else if (index == 2) return dot2;
                else return dot3;
            }
        };

        // Start the animation loop
        handler.post(animateDots);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to BookingSuccessActivity after 3 seconds

                Intent intent = new Intent(PaymentValidationActivity.this, BookingSuccessActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Ensure PaymentValidationActivity is removed from the stack


            }
        }, 3000); // 3000ms = 3 seconds
        loadSharedPrefsData();
    }

    private void animateDot(ImageView dot) {
        ObjectAnimator scaleUp = ObjectAnimator.ofFloat(dot, "scaleX", 1.0f, 1.5f);
        scaleUp.setDuration(400);
        scaleUp.setRepeatMode(ValueAnimator.REVERSE);
        scaleUp.setRepeatCount(1);
        scaleUp.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(dot, "scaleY", 1.0f, 1.5f);
        scaleUpY.setDuration(400);
        scaleUpY.setRepeatMode(ValueAnimator.REVERSE);
        scaleUpY.setRepeatCount(1);
        scaleUpY.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleUp.start();
        scaleUpY.start();
    }

    @Override
    public void onBackPressed() {
        // Do nothing when back button is pressed
        // This disables the back button
        super.onBackPressed();
    }


    private void loadSharedPrefsData() {
        // Retrieve SharedPreferences data and store in variables
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Retrieve individual values
        String firstName = sharedPreferences.getString(KEY_FIRST_NAME, "");
        String lastName = sharedPreferences.getString(KEY_LAST_NAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String countryResidence = sharedPreferences.getString(KEY_COUNTRY_RESIDENCE, "");
        String phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, "");
        String countryCode = sharedPreferences.getString(KEY_COUNTRY_CODE, "");
        int paxCount = sharedPreferences.getInt(KEY_PAX_COUNT, 0); // Default to 0 if not found
        String passengerJsonString = sharedPreferences.getString(KEY_PASSENGER_DATA, null);

        // Combine country code and phone number
        String fullPhoneNumber = countryCode + phoneNumber; // You can change the separator if needed

        // If you need the country codes from SharedPreferences
        Set<String> countryCodes = sharedPreferences.getStringSet(KEY_COUNTRY_CODES, new HashSet<>());

        // If passenger data exists in SharedPreferences, convert it to a list of Passenger objects
        List<Passenger> passengerList = new ArrayList<>();
        if (passengerJsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(passengerJsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject passengerJson = jsonArray.getJSONObject(i);
                    Passenger passenger = new Passenger(
                            passengerJson.getInt("bookingId"),
                            passengerJson.getString("firstName"),
                            passengerJson.getString("lastName"),
                            passengerJson.getString("nationality"),
                            passengerJson.getString("countryOfIssue"),
                            passengerJson.getString("passportNumber"),
                            passengerJson.getString("gender"),
                            passengerJson.getString("dateOfBirth"),
                            passengerJson.getString("passportExpiry")
                    );
                    passengerList.add(passenger);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Here, you can now use these variables (like `firstName`, `lastName`, etc.) to insert into your local database
        // Example usage for inserting into a local database (assuming you have a method like `insertToDatabase`):
        // insertToDatabase(firstName, lastName, email, countryResidence, phoneNumber, countryCode, paxCount, passengerList);
    }



}
