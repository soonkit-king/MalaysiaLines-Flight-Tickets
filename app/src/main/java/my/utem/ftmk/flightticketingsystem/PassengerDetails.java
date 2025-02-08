package my.utem.ftmk.flightticketingsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PassengerDetails extends AppCompatActivity {
    TextView tv;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_passenger_details);
        Intent intent = getIntent();

        tv = findViewById(R.id.tvEndAirport);

        String msg = intent.getStringExtra("flight_depart");
        tv.setText(msg);
    }
}