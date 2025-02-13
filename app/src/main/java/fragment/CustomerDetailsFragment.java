package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adapter.PassengerDetailAdapter;
import model.Passenger;
import my.utem.ftmk.flightticketingsystem.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import utils.PrefKey;

public class CustomerDetailsFragment extends Fragment {

    private RecyclerView rvPassengerDetail;
    private PassengerDetailAdapter passengerDetailAdapter;
    private Spinner countryCodeSpinner;
    private final List<String> countryCodes = new ArrayList<>();

    private final List<Passenger> passengerList = new ArrayList<>();
    private TextView first_name, last_name, email, country_residence, phone_number;

    private SharedPreferences sharedPreferences;

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
        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email);
        country_residence = view.findViewById(R.id.country_residence);
        phone_number = view.findViewById(R.id.phone_number);
        rvPassengerDetail.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedPreferences = getActivity().getSharedPreferences(PrefKey.PREF_BOOKING, Context.MODE_PRIVATE);

        // Clear the spinner adapter initially (Optional, but can help ensure a clean state)
        countryCodeSpinner.setAdapter(null);

        // Check if country codes are already saved in SharedPreferences
        if (loadCountryCodesFromSharedPreferences()) {
            // If loaded from SharedPreferences, update spinner
            updateSpinner();
        } else {
            // If not saved, fetch from API
            fetchCountryCodes();
        }

        // Default value
        int pax = 1;
        if (getActivity() != null && getActivity().getIntent().hasExtra("pax")) {
            pax = getActivity().getIntent().getIntExtra("pax", 1);
            savePaxCount(pax); // Save the pax count
        } else {
            // Retrieve pax count from SharedPreferences if not passed through intent
            pax = getPaxCount();
        }

        generatePassengers(pax);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Load data from SharedPreferences in onStart
        loadSavedData();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveDataToSharedPreferences(); // Save data when fragment is paused
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearSharedPreferences();
    }

    private void saveDataToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save text field data
        editor.putString(PrefKey.KEY_FIRST_NAME, first_name.getText().toString());
        editor.putString(PrefKey.KEY_LAST_NAME, last_name.getText().toString());
        editor.putString(PrefKey.KEY_EMAIL, email.getText().toString());
        editor.putString(PrefKey.KEY_COUNTRY_RESIDENCE, country_residence.getText().toString());

        // Save the phone number (without country code)
        editor.putString(PrefKey.KEY_PHONE_NUMBER, phone_number.getText().toString());

        // Save the selected country code.
        String selectedCountryCode = (String) countryCodeSpinner.getSelectedItem();
        editor.putString(PrefKey.KEY_COUNTRY_CODE, selectedCountryCode);

        // Convert passenger list to JSON and save it
        try {
            JSONArray jsonArray = new JSONArray();
            for (Passenger passenger : passengerList) {
                JSONObject passengerJson = new JSONObject();
                passengerJson.put("firstName", passenger.getFirstName());
                passengerJson.put("lastName", passenger.getLastName());
                passengerJson.put("nationality", passenger.getNationality());
                passengerJson.put("countryOfIssue", passenger.getCountryOfIssue());
                passengerJson.put("passportNumber", passenger.getPassportNumber());
                passengerJson.put("gender", passenger.getGender());
                passengerJson.put("dateOfBirth", passenger.getDateOfBirth());
                passengerJson.put("passportExpiry", passenger.getPassportExpiry());

                jsonArray.put(passengerJson);
            }
            editor.putString(PrefKey.KEY_PASSENGER_DATA, jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.apply();
    }

    private void loadSavedData() {
        first_name.setText(sharedPreferences.getString(PrefKey.KEY_FIRST_NAME, ""));
        last_name.setText(sharedPreferences.getString(PrefKey.KEY_LAST_NAME, ""));
        email.setText(sharedPreferences.getString(PrefKey.KEY_EMAIL, ""));
        country_residence.setText(sharedPreferences.getString(PrefKey.KEY_COUNTRY_RESIDENCE, ""));

        // Load the phone number
        phone_number.setText(sharedPreferences.getString(PrefKey.KEY_PHONE_NUMBER, ""));

        // Load the selected country code
        String savedCountryCode = sharedPreferences.getString(PrefKey.KEY_COUNTRY_CODE, null);
        if (savedCountryCode != null) {
            int spinnerPosition = countryCodes.indexOf(savedCountryCode);
            if (spinnerPosition >= 0) {
                countryCodeSpinner.setSelection(spinnerPosition);
            }
        }


        // Load passenger data from JSON
        String passengerJsonString = sharedPreferences.getString(PrefKey.KEY_PASSENGER_DATA, null);
        if (passengerJsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(passengerJsonString);
                passengerList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject passengerJson = jsonArray.getJSONObject(i);
                    Passenger passenger = new Passenger(
                            passengerJson.optInt("bookingId", -1),
                            passengerJson.optString("firstName", ""),
                            passengerJson.optString("lastName", ""),
                            passengerJson.optString("nationality", ""),
                            passengerJson.optString("countryOfIssue", ""),
                            passengerJson.optString("passportNumber", ""),
                            passengerJson.optString("gender", ""),
                            passengerJson.optString("dateOfBirth", ""),
                            passengerJson.optString("passportExpiry", "")
                    );
                    passengerList.add(passenger);
                }
                passengerDetailAdapter = new PassengerDetailAdapter(passengerList);
                rvPassengerDetail.setAdapter(passengerDetailAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void generatePassengers(int count) {
        passengerList.clear();
        //Load passenger data from SharedPreferences
        String passengerJsonString = sharedPreferences.getString(PrefKey.KEY_PASSENGER_DATA, null);

        if (passengerJsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(passengerJsonString);
                passengerList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject passengerJson = jsonArray.getJSONObject(i);
                    Passenger passenger = new Passenger(
                            passengerJson.optInt("bookingId", -1),
                            passengerJson.optString("firstName", ""),
                            passengerJson.optString("lastName", ""),
                            passengerJson.optString("nationality", ""),
                            passengerJson.optString("countryOfIssue", ""),
                            passengerJson.optString("passportNumber", ""),
                            passengerJson.optString("gender", ""),
                            passengerJson.optString("dateOfBirth", ""),
                            passengerJson.optString("passportExpiry", "")
                    );
                    passengerList.add(passenger);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            passengerDetailAdapter = new PassengerDetailAdapter(passengerList);
            rvPassengerDetail.setAdapter(passengerDetailAdapter);

        } else {
            for (int i = 0; i < count; i++) {
                passengerList.add(new Passenger(-1, "", "", "", "", "", "", "", ""));
            }
            passengerDetailAdapter = new PassengerDetailAdapter(passengerList);
            rvPassengerDetail.setAdapter(passengerDetailAdapter);
        }
    }

    private void fetchCountryCodes() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://restcountries.com/v3.1/all"; // API Endpoint

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        countryCodes.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject country = jsonArray.getJSONObject(i);
                            if (country.has("idd") && country.getJSONObject("idd").has("root")) {
                                String root = country.getJSONObject("idd").getString("root");
                                JSONArray suffixes = country.getJSONObject("idd").optJSONArray("suffixes");

                                if (suffixes != null) {
                                    for (int j = 0; j < suffixes.length(); j++) {
                                        countryCodes.add(root + suffixes.getString(j));
                                    }
                                } else {
                                    countryCodes.add(root);
                                }
                            }
                        }

                        // Sort country codes numerically
                        countryCodes.sort((a, b) -> {
                            try {
                                return Integer.compare(Integer.parseInt(a.replace("+", "")), Integer.parseInt(b.replace("+", "")));
                            } catch (NumberFormatException e) {
                                return a.compareTo(b);
                            }
                        });

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                updateSpinner();
                                saveCountryCodesToSharedPreferences(); // Save country codes after fetching
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, countryCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCodeSpinner.setAdapter(adapter);
    }

    // Save country codes to SharedPreferences
    private void saveCountryCodesToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> countryCodeSet = new HashSet<>(countryCodes);
        editor.putStringSet(PrefKey.KEY_COUNTRY_CODES, countryCodeSet);
        editor.apply();
    }


    // Load country codes from SharedPreferences
    private boolean loadCountryCodesFromSharedPreferences() {
        Set<String> countryCodeSet = sharedPreferences.getStringSet(PrefKey.KEY_COUNTRY_CODES, null);
        if (countryCodeSet != null) {
            countryCodes.clear();
            countryCodes.addAll(countryCodeSet);

            // Sort country codes numerically AFTER loading from SharedPreferences
            countryCodes.sort((a, b) -> {
                try {
                    return Integer.compare(Integer.parseInt(a.replace("+", "")), Integer.parseInt(b.replace("+", "")));
                } catch (NumberFormatException e) {
                    return a.compareTo(b);
                }
            });

            return true; // Codes loaded successfully
        }
        return false; // Codes not found
    }

    private void clearSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all SharedPreferences
        editor.apply();
    }


    // Pax count management in Shared Preferences
    private void savePaxCount(int count) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PrefKey.KEY_PAX_COUNT, count);
        editor.apply();
    }

    private int getPaxCount() {
        return sharedPreferences.getInt(PrefKey.KEY_PAX_COUNT, 1); // Default to 1 if not found
    }
}