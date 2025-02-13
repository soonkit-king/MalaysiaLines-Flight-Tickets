package my.utem.ftmk.flightticketingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fragment.AddOnsFragment;
import utils.PrefKey;

public class SeatSelectionActivity extends AppCompatActivity {

    private int rows = 34; // Example row count (admin-defined)
    private int seatsPerSide = 3; // Number of seats per side

    private ImageButton btnBackToAddOns, done;
    private List<String> selectedSeats = new ArrayList<>(); // Store selected seats
    private Set<String> previouslySelectedSeats = new HashSet<>(); // Store previously selected seats
    private int maxSeatsToSelect; // The maximum number of seats to select
    private int seatsAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Retrieve the pax count from the intent
        maxSeatsToSelect = getIntent().getIntExtra("pax", 1); // Default to 1 if not found
        seatsAvailable = maxSeatsToSelect;

        // Retrieve previously selected seats from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PrefKey.PREF_BOOKING, MODE_PRIVATE);
        previouslySelectedSeats = sharedPreferences.getStringSet(PrefKey.KEY_SELECTED_SEATS, new HashSet<>());

        LinearLayout seatContainer = findViewById(R.id.seatContainer);
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

        for (int i = 0; i < rows; i++) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER);
            rowLayout.setPadding(0, 16, 0, 16);

            // Seat count per row
            char seatLetter = 'A'; // Start with seat A

            // Left side seats
            for (int j = 0; j < seatsPerSide; j++) {
                String seatTag = (i + 1) + String.valueOf(seatLetter);
                ImageView seat = createSeat(seatTag); // 1A, 1B, 1C - ROW NUMBER FIRST
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
                ImageView seat = createSeat(seatTag); // 1D, 1E, 1F - ROW NUMBER FIRST
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

    private ImageView createSeat(String seatTag) {
        ImageView seat = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(88, 120); // Adjust size as needed
        params.setMargins(0, 0, 0, 0); // Space between seats
        seat.setLayoutParams(params);
        seat.setImageResource(R.drawable.seat_icon_blue); // Default unselected seat
        seat.setTag(seatTag);

        // Seat click listener
        seat.setOnClickListener(view -> {
            ImageView selectedSeat = (ImageView) view;
            if (!selectedSeats.contains(seatTag)) {
                // Select seat
                if (seatsAvailable > 0) {
                    selectedSeat.setImageResource(R.drawable.seat_icon_green);
                    selectedSeats.add(seatTag); // Add seat to list
                    seatsAvailable -= 1; //Decrement seat available
                } else {
                    //Selection limit
                    Toast.makeText(SeatSelectionActivity.this, "You can only select " + maxSeatsToSelect + " seats.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Deselect seat
                selectedSeat.setImageResource(R.drawable.seat_icon_blue);
                selectedSeats.remove(seatTag); // Remove seat from list
                seatsAvailable += 1; //Increment seat available
            }
        });

        return seat;
    }
}