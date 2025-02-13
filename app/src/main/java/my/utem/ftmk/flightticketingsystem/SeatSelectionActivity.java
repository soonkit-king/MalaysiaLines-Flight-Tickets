package my.utem.ftmk.flightticketingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sqlite.DatabaseHelper;
import utils.PrefKey;

public class SeatSelectionActivity extends AppCompatActivity {

    private int rows = 34; // Example row count (admin-defined)
    private int seatsPerSide = 3; // Number of seats per side

    private ImageButton btnBackToAddOns, done;
    private List<String> selectedSeats = new ArrayList<>(); // Store selected seats
    private Set<String> previouslySelectedSeats = new HashSet<>(); // Store previously selected seats
    private int maxSeatsToSelect; // The maximum number of seats to select
    private int seatsAvailable;
    private LinearLayout seatContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Retrieve the pax count from the intent
        maxSeatsToSelect = getIntent().getIntExtra("pax", 1); // Default to 1 if not found
        seatsAvailable = maxSeatsToSelect;

        // Retrieve previously selected seats from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PrefKey.PREF_BOOKING, MODE_PRIVATE);
        int flightId = sharedPreferences.getInt(PrefKey.KEY_FLIGHT_ID, -1);
        String departureDatetime = sharedPreferences.getString(PrefKey.KEY_DEPARTURE_DATETIME, "Unknown Time");
        previouslySelectedSeats = sharedPreferences.getStringSet(PrefKey.KEY_SELECTED_SEATS, new HashSet<>());

        // Retrieve booked seats from the database
        // Retrieve booked seats from the database using flightId & departureDatetime
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Set<String> bookedSeats = dbHelper.getBookedSeats(flightId, departureDatetime);

        seatContainer = findViewById(R.id.seatContainer);
        // Add column headers (A, B, C, D, E, F) above the first row
        LinearLayout headerRowTop = new LinearLayout(this);
        headerRowTop.setOrientation(LinearLayout.HORIZONTAL);
        headerRowTop.setGravity(Gravity.CENTER);
        headerRowTop.setPadding(0, 0, 20, 0); // Adjust padding as needed

        // Create header labels
        headerRowTop.addView(createHeaderTextView("A "));
        headerRowTop.addView(createHeaderTextView("B "));
        headerRowTop.addView(createHeaderTextView("C "));

        View spacerHeader = new View(this);
        LinearLayout.LayoutParams spacerParamsHeader = new LinearLayout.LayoutParams(165, LinearLayout.LayoutParams.MATCH_PARENT);
        headerRowTop.addView(spacerHeader, spacerParamsHeader);

        headerRowTop.addView(createHeaderTextView(" D"));
        headerRowTop.addView(createHeaderTextView(" E"));
        headerRowTop.addView(createHeaderTextView(" F"));



        LinearLayout rowLayouta = new LinearLayout(this);
        rowLayouta.setOrientation(LinearLayout.HORIZONTAL);
        rowLayouta.setGravity(Gravity.CENTER);
        TextView exitL = createTextView("<  Exit");
        TextView exitR = createTextView("Exit  >");
        View space = new View(this);
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.MATCH_PARENT);
        rowLayouta.addView(exitL);
        rowLayouta.addView(space, spaceParams);
        rowLayouta.addView(exitR);
        seatContainer.addView(rowLayouta);

        seatContainer.addView(headerRowTop);

        for (int i = 0; i < rows; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER);
            rowLayout.setPadding(0, 12, 20, 16);

            // Seat count per row
            char seatLetter = 'A'; // Start with seat A

            // Left side seats
            for (int j = 0; j < seatsPerSide; j++) {
                String seatTag = (i + 1) + String.valueOf(seatLetter);
                ImageView seat = createSeat(seatTag, bookedSeats); // Pass bookedSeats as argument
                if (previouslySelectedSeats.contains(seatTag)) {
                    seat.setImageResource(R.drawable.seat_icon_green); // Set to green if previously selected
                    selectedSeats.add(seatTag); // Also update selectedSeats list to match the display
                    seatsAvailable -= 1;
                }
                rowLayout.addView(seat);
                seatLetter++; // Increment to the next seat letter
            }


            // Spacer
            TextView textView = createTextView(String.valueOf(i + 1)); // Display the row number
            View spacer = new View(this);
            View spacer2 = new View(this);
            LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT);
            rowLayout.addView(spacer, spacerParams);
            rowLayout.addView(textView);
            rowLayout.addView(spacer2, spacerParams);

            seatLetter = 'D'; // Reset seat letter for the right side

            // Right side seats
            for (int j = 0; j < seatsPerSide; j++) {
                String seatTag = (i + 1) + String.valueOf(seatLetter);
                ImageView seat = createSeat(seatTag, bookedSeats); // 1D, 1E, 1F - ROW NUMBER FIRST
                if (previouslySelectedSeats.contains(seatTag)) {
                    seat.setImageResource(R.drawable.seat_icon_green); // Set to green if previously selected
                    selectedSeats.add(seatTag); // Also update selectedSeats list to match the display
                    seatsAvailable -= 1;
                }
                rowLayout.addView(seat);
                seatLetter++; // Increment to the next seat letter
            }

            seatContainer.addView(rowLayout);
        }

        rowLayouta = new LinearLayout(this);
        rowLayouta.setOrientation(LinearLayout.HORIZONTAL);
        rowLayouta.setGravity(Gravity.CENTER);
        exitL = createTextView("<  Exit");
        exitR = createTextView("Exit  >");
        space = new View(this);
        spaceParams = new LinearLayout.LayoutParams(450, LinearLayout.LayoutParams.MATCH_PARENT);
        rowLayouta.addView(exitL);
        rowLayouta.addView(space, spaceParams);
        rowLayouta.addView(exitR);

        // Add column headers (A, B, C, D, E, F) below the last row
        LinearLayout headerRowBottom = new LinearLayout(this);
        headerRowBottom.setOrientation(LinearLayout.HORIZONTAL);
        headerRowBottom.setGravity(Gravity.CENTER);
        headerRowBottom.setPadding(0, 0, 18, 0); // Adjust padding as needed

        // Create header labels
        headerRowBottom.addView(createHeaderTextView("A "));
        headerRowBottom.addView(createHeaderTextView("B "));
        headerRowBottom.addView(createHeaderTextView("C "));

        spacerHeader = new View(this);
        spacerParamsHeader = new LinearLayout.LayoutParams(185, LinearLayout.LayoutParams.MATCH_PARENT);
        headerRowBottom.addView(spacerHeader, spacerParamsHeader);

        headerRowBottom.addView(createHeaderTextView(" D"));
        headerRowBottom.addView(createHeaderTextView(" E"));
        headerRowBottom.addView(createHeaderTextView(" F"));

        seatContainer.addView(headerRowBottom);
        seatContainer.addView(rowLayouta);

        btnBackToAddOns = findViewById(R.id.btnBackToAddOns);
        btnBackToAddOns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store selected seats in SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(PrefKey.PREF_BOOKING, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putStringSet(PrefKey.KEY_SELECTED_SEATS, new HashSet<>(selectedSeats)); // Store as a Set
                editor.apply();

                Log.d("SeatSelection", "Saving seats: " + selectedSeats);
                Intent intent = new Intent();
                intent.putExtra("selectedSeats", new ArrayList<>(selectedSeats));
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    private TextView createTextView(String s) {
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextSize(20);
        textView.setPadding(0, 16, 0, 16);
        return textView;
    }

    private ImageView createSeat(String seatTag, Set<String> bookedSeats) {
        ImageView seat = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(88, 120);
        params.setMargins(0, 0, 0, 0);
        seat.setLayoutParams(params);

        boolean isBooked = bookedSeats.contains(seatTag);

        if (isBooked) {
            seat.setImageResource(R.drawable.seat_icon_red); // Mark booked seats as RED
            seat.setEnabled(false); // Disable clicks
            seat.setClickable(false); // Ensure no clicks
        } else if (previouslySelectedSeats.contains(seatTag)) {
            seat.setImageResource(R.drawable.seat_icon_green); // Previously selected seats (GREEN)
            selectedSeats.add(seatTag);
            seatsAvailable -= 1;
        } else {
            seat.setImageResource(R.drawable.seat_icon_blue); // Default (BLUE)
        }

        seat.setTag(seatTag);

        // Add click listener only if the seat is not booked
        if (!isBooked) { // This condition is redundant but included for clarity
            seat.setOnClickListener(view -> {
                ImageView selectedSeat = (ImageView) view;
                if (!selectedSeats.contains(seatTag)) {
                    if (seatsAvailable > 0) {
                        selectedSeat.setImageResource(R.drawable.seat_icon_green);
                        selectedSeats.add(seatTag);
                        seatsAvailable -= 1;
                    } else {
                        Toast.makeText(SeatSelectionActivity.this, "You can only select " + maxSeatsToSelect + " seats.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectedSeat.setImageResource(R.drawable.seat_icon_blue);
                    selectedSeats.remove(seatTag);
                    seatsAvailable += 1;
                }
            });
        }

        return seat;
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER); // Center the text
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(88, LinearLayout.LayoutParams.WRAP_CONTENT); // Match seat width
        params.setMargins(0, 0, 0, 0);
        textView.setLayoutParams(params);
        return textView;
    }
}