package adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import model.Flight;
import my.utem.ftmk.flightticketingsystem.R;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightViewHolder> {
    private List<Flight> flightList;
    private Context context;

    public FlightAdapter(Context context, List<Flight> flightList) {
        this.context = context;
        this.flightList = flightList;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_flight_tile, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.tvDepartureAirport.setText(flight.getFromTo());
        holder.tvArrivalAirport.setText(flight.getGoTo());
        holder.tvArrivalTime.setText(flight.getTimeArrive());
        holder.tvDepartureTime.setText(flight.getTimeDepart());
        holder.tvPriceRate.setText(flight.getPrice());
        holder.tvFlightDuration.setText(flight.getDuration());

        // Handle Select Button Click
        holder.btnSelect.setOnClickListener(v -> showBookingDialog(flight));
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartureAirport, tvDepartureTime, tvPriceRate,tvFlightDuration , tvArrivalTime , tvArrivalAirport;
        Button btnSelect;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDepartureAirport = itemView.findViewById(R.id.tvDepartureAirport);
            tvArrivalTime = itemView.findViewById(R.id.tvArrivalTime);
            tvArrivalAirport = itemView.findViewById(R.id.tvArrivalAirport);
            tvDepartureTime = itemView.findViewById(R.id.tvDepartureTime);
            tvPriceRate = itemView.findViewById(R.id.tvPriceRate);
            tvFlightDuration = itemView.findViewById(R.id.tvFlightDuration);
            btnSelect = itemView.findViewById(R.id.btnSelect);
        }
    }

    // Show Dialog for Date & Passenger Selection
    private void showBookingDialog(Flight flight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_booking, null);
        builder.setView(dialogView);

        // Initialize UI Elements
        TextView flightInfo = dialogView.findViewById(R.id.flightInf0);
        Button dateButton = dialogView.findViewById(R.id.dateButton);
        NumberPicker passengerPicker = dialogView.findViewById(R.id.passengerPicker);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        // Set Flight Info
        flightInfo.setText(flight.getFromTo() + "→" + flight.getGoTo() + "\n" + flight.getTimeDepart() + "→" + flight.getTimeArrive() + "\n" + flight.getPrice());

        // Set Number Picker (1 - 10 passengers)
        passengerPicker.setMinValue(1);
        passengerPicker.setMaxValue(10);

        // Handle Date Picker
        final String[] selectedDate = {""}; // Store selected date
        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate[0] = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateButton.setText(selectedDate[0]);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Handle Confirm Button
        AlertDialog dialog = builder.create();
        confirmButton.setOnClickListener(v -> {
            int selectedPassengers = passengerPicker.getValue();
            if (selectedDate[0].isEmpty()) {
                Toast.makeText(context, "Please select a date!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,
                        "Flight booked!\n" + flight.getFromTo() +
                                "\nDate: " + selectedDate[0] +
                                "\nPassengers: " + selectedPassengers,
                        Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
