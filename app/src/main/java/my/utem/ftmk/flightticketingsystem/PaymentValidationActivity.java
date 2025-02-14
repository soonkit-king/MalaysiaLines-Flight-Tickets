package my.utem.ftmk.flightticketingsystem;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Passenger;
import sqlite.DatabaseHelper;
import utils.PrefKey;

public class PaymentValidationActivity extends AppCompatActivity {
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private HandlerThread animationThread;
    private Handler animationHandler;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_validation);

        dbHelper = new DatabaseHelper(this);

        ImageView dot1 = findViewById(R.id.dot1);
        ImageView dot2 = findViewById(R.id.dot2);
        ImageView dot3 = findViewById(R.id.dot3);

        // Initialize HandlerThread for animation
        animationThread = new HandlerThread("AnimationThread");
        animationThread.start();
        animationHandler = new Handler(animationThread.getLooper());

        // Runnable to animate dots smoothly on a separate thread
        Runnable animateDots = new Runnable() {
            int currentDot = 1;

            @Override
            public void run() {
                // Switch to UI thread for animation
                runOnUiThread(() -> animateDot(getDotByIndex(currentDot)));

                // Move to the next dot
                currentDot = (currentDot % 3) + 1;

                // Repeat every 400ms
                animationHandler.postDelayed(this, 400);
            }

            private ImageView getDotByIndex(int index) {
                if (index == 1) return dot1;
                else if (index == 2) return dot2;
                else return dot3;
            }
        };

        // Start animation on a separate thread
        animationHandler.post(animateDots);

        // Navigate to BookingSuccessActivity after 3 seconds
        mainHandler.postDelayed(() -> {
            Intent intent = new Intent(PaymentValidationActivity.this, BookingSuccessActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Remove PaymentValidationActivity from the stack
        }, 3000);

        loadSharedPrefsData();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Disable back button
            }
        });
    }

    private void animateDot(ImageView dot) {
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(dot, "scaleX", 1.0f, 1.5f);
        scaleUpX.setDuration(400);
        scaleUpX.setRepeatMode(ValueAnimator.REVERSE);
        scaleUpX.setRepeatCount(1);
        scaleUpX.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(dot, "scaleY", 1.0f, 1.5f);
        scaleUpY.setDuration(400);
        scaleUpY.setRepeatMode(ValueAnimator.REVERSE);
        scaleUpY.setRepeatCount(1);
        scaleUpY.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleUpX.start();
        scaleUpY.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the animation thread to prevent memory leaks
        if (animationThread != null) {
            animationThread.quitSafely();
        }
    }

    private void loadSharedPrefsData() {
        sharedPreferences = getSharedPreferences(PrefKey.PREF_BOOKING, MODE_PRIVATE);

        int flightId = sharedPreferences.getInt(PrefKey.KEY_FLIGHT_ID, -1);
        String departureDatetime = sharedPreferences.getString(PrefKey.KEY_DEPARTURE_DATETIME, "");
        String arrivalDatetime = sharedPreferences.getString(PrefKey.KEY_ARRIVAL_DATETIME, "");
        Set<String> seatNo = sharedPreferences.getStringSet(PrefKey.KEY_SELECTED_SEATS, new HashSet<>());
        String seatNoString = TextUtils.join(", ", seatNo);
        boolean refund = sharedPreferences.getBoolean(PrefKey.KEY_REFUND_GUARANTEE, false);
        double totalPayment = sharedPreferences.getFloat(PrefKey.KEY_TOTAL_PAYMENT, -1);

        String firstName = sharedPreferences.getString(PrefKey.KEY_FIRST_NAME, "");
        String lastName = sharedPreferences.getString(PrefKey.KEY_LAST_NAME, "");
        String email = sharedPreferences.getString(PrefKey.KEY_EMAIL, "");
        String countryResidence = sharedPreferences.getString(PrefKey.KEY_COUNTRY_RESIDENCE, "");
        String phoneNumber = sharedPreferences.getString(PrefKey.KEY_PHONE_NUMBER, "");
        String countryCode = sharedPreferences.getString(PrefKey.KEY_COUNTRY_CODE, "");
        int pax = sharedPreferences.getInt(PrefKey.KEY_PAX_COUNT, 0);
        String passengerJsonString = sharedPreferences.getString(PrefKey.KEY_PASSENGER_DATA, null);

        String fullPhoneNumber = countryCode + phoneNumber;

        List<Passenger> passengerList = decodeJsonPassenger(passengerJsonString);

        dbHelper.insertCompleteBooking(
                flightId, pax, departureDatetime, arrivalDatetime, seatNoString, refund, totalPayment,
                firstName, lastName, email, countryResidence, countryCode, fullPhoneNumber, passengerList
        );
    }

    private List<Passenger> decodeJsonPassenger(String passengerJsonString) {
        List<Passenger> list = Collections.emptyList();
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
                    list.add(passenger);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
