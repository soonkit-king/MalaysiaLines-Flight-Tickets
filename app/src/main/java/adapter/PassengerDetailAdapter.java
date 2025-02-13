package adapter;

import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;
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
        holder.bindPassenger(passenger);
    }

    @Override
    public int getItemCount() {
        return passengerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView passengerTitle, first_name_error, last_name_error,  passport_error,nationality_error,countryOfIssue_error;
        RadioGroup genderGroup;
        RadioButton radioMale, radioFemale;
        EditText firstName, lastName, dobDay, dobMonth, dobYear;
        EditText nationality, countryOfIssue, passportNumber;
        EditText expiryDay, expiryMonth, expiryYear;

        // TextWatcher instances to avoid creating new ones every bind
        MyTextWatcher firstNameWatcher, lastNameWatcher, nationalityWatcher, countryOfIssueWatcher, passportNumberWatcher, dobDayWatcher, dobMonthWatcher, dobYearWatcher, expiryDayWatcher, expiryMonthWatcher, expiryYearWatcher;

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

            // Error TextViews for Validation
            first_name_error = itemView.findViewById(R.id.first_name_error);
            last_name_error = itemView.findViewById(R.id.last_name_error);
            nationality_error = itemView.findViewById(R.id.nationality_error);
            passport_error = itemView.findViewById(R.id.passport_error);
            countryOfIssue_error= itemView.findViewById(R.id.countryOfIssue_error);
        }

        public void bindPassenger(Passenger passenger) {
            passengerTitle.setText("Passenger " + (getAdapterPosition() + 1) + ":");

            firstName.setText(passenger.getFirstName());
            lastName.setText(passenger.getLastName());
            nationality.setText(passenger.getNationality());
            countryOfIssue.setText(passenger.getCountryOfIssue());
            passportNumber.setText(passenger.getPassportNumber());

            if (passenger.getGender().equalsIgnoreCase("Male")) {
                radioMale.setChecked(true);
            } else if (passenger.getGender().equalsIgnoreCase("Female")) {
                radioFemale.setChecked(true);
            }

            String[] dobParts = passenger.getDateOfBirth().split("/");
            if (dobParts.length == 3) {
                dobDay.setText(dobParts[0]);
                dobMonth.setText(dobParts[1]);
                dobYear.setText(dobParts[2]);
            }

            String[] expiryParts = passenger.getPassportExpiry().split("/");
            if (expiryParts.length == 3) {
                expiryDay.setText(expiryParts[0]);
                expiryMonth.setText(expiryParts[1]);
                expiryYear.setText(expiryParts[2]);
            }
        }

        public boolean validatePassenger() {
            boolean isValid = true;

            if (TextUtils.isEmpty(firstName.getText().toString().trim())) {
                first_name_error.setText("*Required field");
                first_name_error.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                first_name_error.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(lastName.getText().toString().trim())) {
                last_name_error.setText("*Required field");
                last_name_error.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                last_name_error.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(nationality.getText().toString().trim())) {
                nationality_error.setText("*Required field");
                nationality_error.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                nationality_error.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(passportNumber.getText().toString().trim())) {
                passport_error.setText("*Required field");
                passport_error.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                passport_error.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(countryOfIssue_error.getText().toString().trim())) {
                countryOfIssue_error.setText("*Required field");
                countryOfIssue_error.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                countryOfIssue_error.setVisibility(View.GONE);
            }

            return isValid;
        }
    }

    // Custom TextWatcher to update passenger data directly
    private class MyTextWatcher implements TextWatcher {
        private final Passenger passenger;
        private final String field;

        public MyTextWatcher(Passenger passenger, String field) {
            this.passenger = passenger;
            this.field = field;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String value = s.toString();
            switch (field) {
                case "firstName":
                    passenger.setFirstName(value);
                    break;
                case "lastName":
                    passenger.setLastName(value);
                    break;
                case "nationality":
                    passenger.setNationality(value);
                    break;
                case "countryOfIssue":
                    passenger.setCountryOfIssue(value);
                    break;
                case "passportNumber":
                    passenger.setPassportNumber(value);
                    break;
                case "dobDay":
                    passenger.setDateOfBirth(joinDateParts(value, getFieldValue(passenger, "dobMonth"), getFieldValue(passenger, "dobYear")));
                    break;
                case "dobMonth":
                    passenger.setDateOfBirth(joinDateParts(getFieldValue(passenger, "dobDay"), value, getFieldValue(passenger, "dobYear")));
                    break;
                case "dobYear":
                    passenger.setDateOfBirth(joinDateParts(getFieldValue(passenger, "dobDay"), getFieldValue(passenger, "dobMonth"), value));
                    break;
                case "expiryDay":
                    passenger.setPassportExpiry(joinDateParts(value, getFieldValue(passenger, "expiryMonth"), getFieldValue(passenger, "expiryYear")));
                    break;
                case "expiryMonth":
                    passenger.setPassportExpiry(joinDateParts(getFieldValue(passenger, "expiryDay"), value, getFieldValue(passenger, "expiryYear")));
                    break;
                case "expiryYear":
                    passenger.setPassportExpiry(joinDateParts(getFieldValue(passenger, "expiryDay"), getFieldValue(passenger, "expiryMonth"), value));
                    break;
            }
        }

        private String joinDateParts(String day, String month, String year) {
            if (day != null && !day.isEmpty() && month != null && !month.isEmpty() && year != null && !year.isEmpty()) {
                return day + "/" + month + "/" + year;
            }
            return null;
        }

    public boolean validateAllPassengers(RecyclerView recyclerView) {
        boolean allValid = true;
        for (int i = 0; i < getItemCount(); i++) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder instanceof ViewHolder) {
                if (!((ViewHolder) viewHolder).validatePassenger()) {
                    allValid = false;
                }
            }
        }
        return allValid;
    }

}

        private String getFieldValue(Passenger passenger, String field) {
            switch (field) {
                case "dobDay":
                    return (passenger.getDateOfBirth() != null && passenger.getDateOfBirth().split("/").length == 3) ? passenger.getDateOfBirth().split("/")[0] : "";
                case "dobMonth":
                    return (passenger.getDateOfBirth() != null && passenger.getDateOfBirth().split("/").length == 3) ? passenger.getDateOfBirth().split("/")[1] : "";
                case "dobYear":
                    return (passenger.getDateOfBirth() != null && passenger.getDateOfBirth().split("/").length == 3) ? passenger.getDateOfBirth().split("/")[2] : "";
                case "expiryDay":
                    return (passenger.getPassportExpiry() != null && passenger.getPassportExpiry().split("/").length == 3) ? passenger.getPassportExpiry().split("/")[0] : "";
                case "expiryMonth":
                    return (passenger.getPassportExpiry() != null && passenger.getPassportExpiry().split("/").length == 3) ? passenger.getPassportExpiry().split("/")[1] : "";
                case "expiryYear":
                    return (passenger.getPassportExpiry() != null && passenger.getPassportExpiry().split("/").length == 3) ? passenger.getPassportExpiry().split("/")[2] : "";
                default:
                    return null;
            }
        }
    }
