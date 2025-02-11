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
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
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
    private SwitchCompat switchRefundGuarantee; // Declare the switch

    private static final String PREF_NAME = "AddOnsPrefs"; // Separate preference file for Addons
    private static final String CUSTOMER_PREF_NAME = "CustomerDetails"; // Use separate name for CustomerDetails SharedPreferences
    private static final String KEY_REFUND_GUARANTEE = "refundGuarantee";
    private static final String KEY_SELECTED_SEATS = "selectedSeats";  // Key for storing selected seats
    private static final int SEAT_SELECTION_REQUEST_CODE = 1; // Request code for seat selection activity
    private static final String KEY_PAX_COUNT = "paxCount"; // Key to retrieve pax count

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

        switchRefundGuarantee = view.findViewById(R.id.switch_refund_guarantee); // Initialize the switch

        // Load the switch state from SharedPreferences
        loadSwitchState();

        // Set a listener to save the switch state whenever it changes
        switchRefundGuarantee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSwitchState(isChecked);
            }
        });

        // Retrieve selected seats from SharedPreferences - Moved this *after* loading switch
        displaySelectedSeats();
    }


    private void goToSeatSelection(View view) {
        Intent intent = new Intent(requireActivity(), SeatSelectionActivity.class);

        // Get the pax count from CustomerDetails SharedPreferences
        SharedPreferences customerPrefs = requireActivity().getSharedPreferences(CUSTOMER_PREF_NAME, Context.MODE_PRIVATE);
        int pax = customerPrefs.getInt(KEY_PAX_COUNT, 1); // Default to 1 if not found

        // Pass pax to the intent
        intent.putExtra("pax", pax);

        startActivityForResult(intent, SEAT_SELECTION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEAT_SELECTION_REQUEST_CODE) {
            if (resultCode == android.app.Activity.RESULT_OK && data != null) {
                // Retrieve the selected seats from the Intent
                ArrayList<String> seats = data.getStringArrayListExtra("selectedSeats");
                if (seats != null) {
                    selectedSeats = seats;
                    displaySelectedSeats();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the selected seats when the fragment is resumed.
        displaySelectedSeats();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearSharedPreferences();
    }

    private void displaySelectedSeats() {
        // Retrieve selected seats from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); //Use addon's PREF NAME
        Set<String> selectedSeatsSet = sharedPreferences.getStringSet(KEY_SELECTED_SEATS, new HashSet<>()); // Provide a default empty set if not found, use the const

        selectedSeats = new ArrayList<>(selectedSeatsSet); // Convert Set to List

        if (!selectedSeats.isEmpty()) {
            // Update the button text with the selected seats
            btnChooseSeat.setText("Seats Selected: " + String.join(", ", selectedSeats)); //Correct way
        } else {
            btnChooseSeat.setText("Choose Seat"); // Set default text if no seats are selected
        }
    }

    //Methods to store Switch state

    private void saveSwitchState(boolean isChecked) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_REFUND_GUARANTEE, isChecked);
        editor.apply();
    }

    private void loadSwitchState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean(KEY_REFUND_GUARANTEE, false); // Default to false if not found
        switchRefundGuarantee.setChecked(isChecked);
    }

    //Method to clear all shared prefs

    public void clearSharedPreferences() {

        // Clear selected seats.
        SharedPreferences sharedPreferencesSeats = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE); // Use correct name
        SharedPreferences.Editor editorSeats = sharedPreferencesSeats.edit();
        editorSeats.remove(KEY_SELECTED_SEATS);  // Remove the specific key, not clear all
        editorSeats.apply(); // or editor.commit();  Apply is asynchronous, commit is synchronous

        // Clear refund switch prefs.
        SharedPreferences sharedPreferencesSwitch = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSwitch = sharedPreferencesSwitch.edit();
        editorSwitch.remove(KEY_REFUND_GUARANTEE);  // Remove the specific key, not clear all
        editorSwitch.apply(); // or editor.commit();  Apply is asynchronous, commit is synchronous

        Log.d("AddOnsFragment", "Add-ons Shared Preferences cleared.");
    }
}