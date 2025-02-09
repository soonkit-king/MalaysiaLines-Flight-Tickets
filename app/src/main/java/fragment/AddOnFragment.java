package fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import my.utem.ftmk.flightticketingsystem.R;

public class AddOnFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ConstraintLayout mainLayout = view.findViewById(R.id.main);

        // Inflate the card view layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.card_choose_seat, mainLayout, false);
        Button btnChooseSeat = cardView.findViewById(R.id.btnChooseSeat);



        // Add the card view to the main layout
        mainLayout.addView(cardView);
    }

    private void goToSeatSelection(View view) {

    }

}