package my.utem.ftmk.flightticketingsystem;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SeatSelectionActivity extends AppCompatActivity {

    private int rows = 34; // Example row count (admin-defined)
    private int seatsPerSide = 3; // Number of seats per side

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        
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

            // Left side seats
            for (int j = 0; j < seatsPerSide; j++) {
                ImageView seat = createSeat("Row " + (i + 1) + " Left " + (j + 1));
                rowLayout.addView(seat);
            }

            // Spacer
            TextView textView = createTextView(""+(i+1));
            View spacer = new View(this);
            View spacer2 = new View(this);
            LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.MATCH_PARENT);
            rowLayout.addView(spacer, spacerParams);
            rowLayout.addView(textView);
            rowLayout.addView(spacer2, spacerParams);

            // Right side seats
            for (int j = 0; j < seatsPerSide; j++) {
                ImageView seat = createSeat("Row " + (i + 1) + " Right " + (j + 1));
                rowLayout.addView(seat);
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

        // State variable for the seat selection
        final boolean[] isSelected = {false};

        // Seat click listener
        seat.setOnClickListener(view -> {
            ImageView selectedSeat = (ImageView) view;
            if (!isSelected[0]) {
                // Select seat
                selectedSeat.setImageResource(R.drawable.seat_icon_green);
                isSelected[0] = true;
                Toast.makeText(this, "Selected: " + seatTag, Toast.LENGTH_SHORT).show();
            } else {
                // Deselect seat
                selectedSeat.setImageResource(R.drawable.seat_icon_blue);
                isSelected[0] = false;
                Toast.makeText(this, "Deselected: " + seatTag, Toast.LENGTH_SHORT).show();
            }
        });

        return seat;
    }

}
