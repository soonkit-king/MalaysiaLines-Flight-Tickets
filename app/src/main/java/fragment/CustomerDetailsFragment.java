package fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.PassengerDetailAdapter;
import model.Passenger;
import my.utem.ftmk.flightticketingsystem.R;

public class CustomerDetailsFragment extends Fragment {

    private RecyclerView rvPassengerDetail;
    private PassengerDetailAdapter passengerDetailAdapter;
    private Spinner countryCodeSpinner;

    private List<Passenger> passengerList = new ArrayList<>();
    private int pax = 1; // Default value

    public CustomerDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPassengerDetail = view.findViewById(R.id.rvPassengerDetail);
        countryCodeSpinner = view.findViewById(R.id.country_code);


        rvPassengerDetail.setLayoutManager(new LinearLayoutManager(getContext()));

        setupCountryCodeSpinner();
        // Get the passenger count from the intent or arguments
        if (getActivity() != null && getActivity().getIntent().hasExtra("pax")) {
            pax = getActivity().getIntent().getIntExtra("pax", 1);
        }

        generatePassengers(pax);
    }

    private void generatePassengers(int count) {
        passengerList.clear(); // Clear previous data
        for (int i = 0; i < count; i++) {
            passengerList.add(new Passenger("", "", "", "", "", "", "", ""));
        }
        passengerDetailAdapter = new PassengerDetailAdapter(passengerList);
        rvPassengerDetail.setAdapter(passengerDetailAdapter);
    }

    private void setupCountryCodeSpinner() {
        // You'll likely want to load these from a resource or an API call
        List<String> countryCodes = new ArrayList<>();
        countryCodes.add("+1");
        countryCodes.add("+44");
        countryCodes.add("+60");
        countryCodes.add("+81");
        countryCodes.add("+86");
        // Setup Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, countryCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCodeSpinner.setAdapter(adapter);
    }




}
