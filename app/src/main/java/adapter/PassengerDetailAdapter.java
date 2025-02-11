package adapter;

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

        // Set passenger title
        holder.passengerTitle.setText("Passenger " + (position + 1) + ":");

        // Set passenger details

        holder.firstName.setText(passenger.getFirstName());
        holder.lastName.setText(passenger.getLastName());
        holder.nationality.setText(passenger.getNationality());
        holder.countryOfIssue.setText(passenger.getCountryOfIssue());
        holder.passportNumber.setText(passenger.getPassportNumber());
        holder.dobDay.setText(passenger.getDateOfBirth() != null && passenger.getDateOfBirth().split("/").length == 3 ? passenger.getDateOfBirth().split("/")[0] : "");
        holder.dobMonth.setText(passenger.getDateOfBirth() != null && passenger.getDateOfBirth().split("/").length == 3 ? passenger.getDateOfBirth().split("/")[1] : "");
        holder.dobYear.setText(passenger.getDateOfBirth() != null && passenger.getDateOfBirth().split("/").length == 3 ? passenger.getDateOfBirth().split("/")[2] : "");
        holder.expiryDay.setText(passenger.getPassportExpiry() != null && passenger.getPassportExpiry().split("/").length == 3 ? passenger.getPassportExpiry().split("/")[0] : "");
        holder.expiryMonth.setText(passenger.getPassportExpiry() != null && passenger.getPassportExpiry().split("/").length == 3 ? passenger.getPassportExpiry().split("/")[1] : "");
        holder.expiryYear.setText(passenger.getPassportExpiry() != null && passenger.getPassportExpiry().split("/").length == 3 ? passenger.getPassportExpiry().split("/")[2] : "");

        // Set gender selection
        if (passenger.getGender() != null) {
            if (passenger.getGender().equalsIgnoreCase("Male")) {
                holder.radioMale.setChecked(true);
            } else if (passenger.getGender().equalsIgnoreCase("Female")) {
                holder.radioFemale.setChecked(true);
            } else {
                holder.genderGroup.clearCheck(); // Clear selection if gender is not Male or Female
            }
        } else {
            holder.genderGroup.clearCheck(); // Clear selection if gender is null
        }

        // Clear previous listeners to avoid issues with RecyclerView reuse
        clearTextWatchers(holder);

        // Set up text watchers for live updates
        holder.firstNameWatcher = new MyTextWatcher(passenger, "firstName");
        holder.firstName.addTextChangedListener(holder.firstNameWatcher);

        holder.lastNameWatcher = new MyTextWatcher(passenger, "lastName");
        holder.lastName.addTextChangedListener(holder.lastNameWatcher);

        holder.nationalityWatcher = new MyTextWatcher(passenger, "nationality");
        holder.nationality.addTextChangedListener(holder.nationalityWatcher);

        holder.countryOfIssueWatcher = new MyTextWatcher(passenger, "countryOfIssue");
        holder.countryOfIssue.addTextChangedListener(holder.countryOfIssueWatcher);

        holder.passportNumberWatcher = new MyTextWatcher(passenger, "passportNumber");
        holder.passportNumber.addTextChangedListener(holder.passportNumberWatcher);

        holder.dobDayWatcher = new MyTextWatcher(passenger, "dobDay");
        holder.dobDay.addTextChangedListener(holder.dobDayWatcher);

        holder.dobMonthWatcher = new MyTextWatcher(passenger, "dobMonth");
        holder.dobMonth.addTextChangedListener(holder.dobMonthWatcher);

        holder.dobYearWatcher = new MyTextWatcher(passenger, "dobYear");
        holder.dobYear.addTextChangedListener(holder.dobYearWatcher);

        holder.expiryDayWatcher = new MyTextWatcher(passenger, "expiryDay");
        holder.expiryDay.addTextChangedListener(holder.expiryDayWatcher);

        holder.expiryMonthWatcher = new MyTextWatcher(passenger, "expiryMonth");
        holder.expiryMonth.addTextChangedListener(holder.expiryMonthWatcher);

        holder.expiryYearWatcher = new MyTextWatcher(passenger, "expiryYear");
        holder.expiryYear.addTextChangedListener(holder.expiryYearWatcher);

        // Set up radio group listener for gender selection
        holder.genderGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioMale) {
                passenger.setGender("Male");
            } else if (checkedId == R.id.radioFemale) {
                passenger.setGender("Female");
            }
        });
    }

    private void clearTextWatchers(ViewHolder holder) {
        if (holder.firstNameWatcher != null) {
            holder.firstName.removeTextChangedListener(holder.firstNameWatcher);
        }
        if (holder.lastNameWatcher != null) {
            holder.lastName.removeTextChangedListener(holder.lastNameWatcher);
        }
        if (holder.nationalityWatcher != null) {
            holder.nationality.removeTextChangedListener(holder.nationalityWatcher);
        }
        if (holder.countryOfIssueWatcher != null) {
            holder.countryOfIssue.removeTextChangedListener(holder.countryOfIssueWatcher);
        }
        if (holder.passportNumberWatcher != null) {
            holder.passportNumber.removeTextChangedListener(holder.passportNumberWatcher);
        }
        if (holder.dobDayWatcher != null) {
            holder.dobDay.removeTextChangedListener(holder.dobDayWatcher);
        }
        if (holder.dobMonthWatcher != null) {
            holder.dobMonth.removeTextChangedListener(holder.dobMonthWatcher);
        }
        if (holder.dobYearWatcher != null) {
            holder.dobYear.removeTextChangedListener(holder.dobYearWatcher);
        }
        if (holder.expiryDayWatcher != null) {
            holder.expiryDay.removeTextChangedListener(holder.expiryDayWatcher);
        }
        if (holder.expiryMonthWatcher != null) {
            holder.expiryMonth.removeTextChangedListener(holder.expiryMonthWatcher);
        }
        if (holder.expiryYearWatcher != null) {
            holder.expiryYear.removeTextChangedListener(holder.expiryYearWatcher);
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
                    passenger.setDob(joinDateParts(value, getFieldValue(passenger, "dobMonth"), getFieldValue(passenger, "dobYear")));
                    break;
                case "dobMonth":
                    passenger.setDob(joinDateParts(getFieldValue(passenger, "dobDay"), value, getFieldValue(passenger, "dobYear")));
                    break;
                case "dobYear":
                    passenger.setDob(joinDateParts(getFieldValue(passenger, "dobDay"), getFieldValue(passenger, "dobMonth"), value));
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
}