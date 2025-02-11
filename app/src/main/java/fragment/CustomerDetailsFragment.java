package fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    private EditText firstNameEditText, lastNameEditText, emailEditText, countryResidenceEditText, phoneNumberEditText;
    private TextView firstNameErrorTextView, lastNameErrorTextView, emailErrorTextView, phoneNumberErrorTextView;

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
        firstNameEditText = view.findViewById(R.id.first_name);
        lastNameEditText = view.findViewById(R.id.last_name);
        emailEditText = view.findViewById(R.id.email);
        countryResidenceEditText = view.findViewById(R.id.country_residence);
        phoneNumberEditText = view.findViewById(R.id.phone_number);
        firstNameErrorTextView = view.findViewById(R.id.first_name_error);
        lastNameErrorTextView = view.findViewById(R.id.last_name_error);
        emailErrorTextView =view.findViewById(R.id.email_error);
        phoneNumberErrorTextView = view.findViewById(R.id.phone_number_error);


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

    public boolean validateForm() {
        boolean isValid = true;

        if (TextUtils.isEmpty(firstNameEditText.getText().toString())) {
            firstNameErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            firstNameErrorTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(lastNameEditText.getText().toString())) {
            lastNameErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            lastNameErrorTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailErrorTextView.setText("*Required field");
            emailErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            emailErrorTextView.setText("Invalid email format");
            emailErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            emailErrorTextView.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(phoneNumberEditText.getText().toString())) {
            phoneNumberErrorTextView.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            phoneNumberErrorTextView.setVisibility(View.GONE);
        }

        return isValid;
    }



}
