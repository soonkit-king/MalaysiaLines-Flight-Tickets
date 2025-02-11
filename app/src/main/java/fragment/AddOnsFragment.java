package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.utem.ftmk.flightticketingsystem.R;
import my.utem.ftmk.flightticketingsystem.SeatSelectionActivity;

public class AddOnsFragment extends Fragment {

    private Button btnChooseSeat; // Declare btnChooseSeat as a member variable
    private List<String> selectedSeats = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_ons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnChooseSeat = view.findViewById(R.id.btnChooseSeat); // Initialize btnChooseSeat
        btnChooseSeat.setOnClickListener(this::goToSeatSelection);

        // CLEAR SHARED PREFS HERE ON FRAGMENT CREATION - consider keeping this out.
        clearSharedPreferences();

        // Retrieve selected seats from SharedPreferences - Moved this *after* clearing
        displaySelectedSeats();
    }


    private void goToSeatSelection(View view) {
        Intent intent = new Intent(requireActivity(), SeatSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the selected seats when the fragment is resumed.
        displaySelectedSeats();
    }

    private void displaySelectedSeats() {
        // Retrieve selected seats from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Set<String> selectedSeatsSet = sharedPreferences.getStringSet("selectedSeats", new HashSet<>()); // Provide a default empty set if not found

        selectedSeats = new ArrayList<>(selectedSeatsSet); // Convert Set to List

        if (!selectedSeats.isEmpty()) {
            // Do something with the selected seats

            // Update the button text with the selected seats
            btnChooseSeat.setText("Seats Selected: " + String.join(", ", selectedSeats)); //Correct way
        } else {
            btnChooseSeat.setText("Choose Seat"); // Set default text if no seats are selected
            // Handle the case where no seats were selected (optional)
        }
    }


    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("selectedSeats");  // Remove the specific key
        //editor.clear(); // Clears all stored data in shared preferences. Use with caution.

        editor.apply(); // or editor.commit();  Apply is asynchronous, commit is synchronous
        Log.d("SeatSelection", "Shared Preferences cleared.");
    }
}