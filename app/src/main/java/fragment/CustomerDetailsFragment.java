package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<Passenger> passengerList = new ArrayList<>();
    private int passengerCount = 1; // Default value

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
        rvPassengerDetail.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the passenger count from the intent or arguments
        if (getActivity() != null && getActivity().getIntent().hasExtra("passengers")) {
            passengerCount = getActivity().getIntent().getIntExtra("passengers", 1);
        }

        generatePassengers(passengerCount);
    }

    private void generatePassengers(int count) {
        passengerList.clear(); // Clear previous data
        for (int i = 0; i < count; i++) {
            passengerList.add(new Passenger("", "", "", "", "", "", "", ""));
        }
        passengerDetailAdapter = new PassengerDetailAdapter(passengerList);
        rvPassengerDetail.setAdapter(passengerDetailAdapter);
    }
}
