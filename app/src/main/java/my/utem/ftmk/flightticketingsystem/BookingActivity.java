package my.utem.ftmk.flightticketingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fragment.AvailableFlightsFragment;
import fragment.CustomerDetailsFragment;

public class BookingActivity extends AppCompatActivity {

    private boolean isFragmentReplaced = false; // Track button state
    private Button btnAdd_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Load the default fragment (CustomerDetailsFragment)
        replaceFragment(new CustomerDetailsFragment());

        // Find the button
        btnAdd_on = findViewById(R.id.btnAdd_on); // Ensure your button has this ID in XML

        // Set click listener for button
        btnAdd_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFragmentReplaced) {
                    // First click: Replace fragment and change button text
                    replaceFragment(new AvailableFlightsFragment());
                    btnAdd_on.setText("Proceed to Payment");
                    isFragmentReplaced = true;
                } else {
                    // Second click: Start another activity
                    Intent intent = new Intent(BookingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.passanger_detail_fragment_container, fragment);
        transaction.addToBackStack(null); // Allow user to navigate back
        transaction.commit();
    }
}
