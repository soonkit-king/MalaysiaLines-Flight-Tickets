package fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import my.utem.ftmk.flightticketingsystem.BookingActivity;
import my.utem.ftmk.flightticketingsystem.MainActivity;
import my.utem.ftmk.flightticketingsystem.R;
import my.utem.ftmk.flightticketingsystem.SeatSelectionActivity;

public class AddOnsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_ons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnChooseSeat = view.findViewById(R.id.btnChooseSeat);
        btnChooseSeat.setOnClickListener(this::goToSeatSelection);
    }

    private void goToSeatSelection(View view) {
        Intent intent = new Intent(requireActivity(), SeatSelectionActivity.class);
        startActivity(intent);
    }


}