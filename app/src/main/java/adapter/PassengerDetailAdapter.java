package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.Passenger;
import my.utem.ftmk.flightticketingsystem.R;

public class PassengerDetailAdapter extends RecyclerView.Adapter<PassengerDetailAdapter.ViewHolder> {

    private final List<Passenger> passengerList;

    public PassengerDetailAdapter(List<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_passenger_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Passenger passenger = passengerList.get(position);

        // Set passenger title
        holder.passengerTitle.setText("Passenger " + (position + 1) + ":");

        // Set passenger details
        holder.firstName.setText(passenger.getFirstName());
        holder.lastName.setText(passenger.getLastName());
        holder.nationality.setText(passenger.getNationality());
        holder.countryOfIssue.setText(passenger.getCountryOfIssue());
        holder.passportNumber.setText(passenger.getPassportNumber());

        // Set gender selection
        if (passenger.getGender().equalsIgnoreCase("Male")) {
            holder.radioMale.setChecked(true);
        } else if (passenger.getGender().equalsIgnoreCase("Female")) {
            holder.radioFemale.setChecked(true);
        }

        // Set date of birth
        String[] dobParts = passenger.getDateOfBirth().split("/");
        if (dobParts.length == 3) {
            holder.dobDay.setText(dobParts[0]);
            holder.dobMonth.setText(dobParts[1]);
            holder.dobYear.setText(dobParts[2]);
        }

        // Set passport expiry date
        String[] expiryParts = passenger.getPassportExpiry().split("/");
        if (expiryParts.length == 3) {
            holder.expiryDay.setText(expiryParts[0]);
            holder.expiryMonth.setText(expiryParts[1]);
            holder.expiryYear.setText(expiryParts[2]);
        }
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView passengerTitle;
        RadioGroup genderGroup;
        RadioButton radioMale, radioFemale;
        EditText firstName, lastName, dobDay, dobMonth, dobYear;
        EditText nationality, countryOfIssue, passportNumber;
        EditText expiryDay, expiryMonth, expiryYear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            passengerTitle = itemView.findViewById(R.id.passengerTitle);
            genderGroup = itemView.findViewById(R.id.genderGroup);
            radioMale = itemView.findViewById(R.id.radioMale);
            radioFemale = itemView.findViewById(R.id.radioFemale);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            dobDay = itemView.findViewById(R.id.dobDay);
            dobMonth = itemView.findViewById(R.id.dobMonth);
            dobYear = itemView.findViewById(R.id.dobYear);
            nationality = itemView.findViewById(R.id.nationality);
            countryOfIssue = itemView.findViewById(R.id.countryOfIssue);
            passportNumber = itemView.findViewById(R.id.passportNumber);
            expiryDay = itemView.findViewById(R.id.expiryDay);
            expiryMonth = itemView.findViewById(R.id.expiryMonth);
            expiryYear = itemView.findViewById(R.id.expiryYear);
        }
    }
}
