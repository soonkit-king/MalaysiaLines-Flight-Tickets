package adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import model.Flight;
import fragment.CustomerDetailsFragment;
import my.utem.ftmk.flightticketingsystem.BookingActivity;
import my.utem.ftmk.flightticketingsystem.R;
import utils.Conversions;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_available_flight, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);
        holder.tvDepartureAirport.setText(flight.getDepartureAirport());
        holder.tvArrivalAirport.setText(flight.getArrivalAirport());
        holder.tvArrivalTime.setText(flight.getArrivalTime());
        holder.tvDepartureTime.setText(flight.getDepartureTime());
        holder.tvPriceRate.setText(Conversions.formatPriceRate(flight.getPriceRate()));
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
        TextView flightInfo = dialogView.findViewById(R.id.flightInfo);
        Button dateButton = dialogView.findViewById(R.id.dateButton);
        NumberPicker paxPicker = dialogView.findViewById(R.id.paxPicker);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);

        // Format Flight Info
        String formattedInfo = String.format("%s → %s\n%s → %s\n%s",
                flight.getDepartureAirport(),
                flight.getArrivalAirport(),
                flight.getDepartureTime(),  // Already in HH:mm
                flight.getArrivalTime(),    // Already in HH:mm
                Conversions.formatPriceRate(flight.getPriceRate()));

        flightInfo.setText(formattedInfo);

        // Set Number Picker (1 - 10 passengers)
        paxPicker.setMinValue(1);
        paxPicker.setMaxValue(10);

        // Handle Date Picker
        final String[] selectedDate = {""}; // Store selected date
        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate[0] = Conversions.formatDate(dayOfMonth, month, year); // Convert to DD MMM YYYY
                        dateButton.setText(selectedDate[0]);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            // Prevent past dates
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            datePickerDialog.show();
        });


        // Handle Confirm Button
        AlertDialog dialog = builder.create();
        confirmButton.setOnClickListener(v -> {
            int selectedPax = paxPicker.getValue();
            if (selectedDate[0].isEmpty()) {
                Toast.makeText(context, "Please select a date!", Toast.LENGTH_SHORT).show();
            } else {
                // Convert date + time + duration into datetime values
                List<String> datetimes = Conversions.compileBoardingDatetimes(selectedDate[0], flight.getDepartureTime(), flight.getDuration());
                Intent intent = getIntent(flight, selectedPax, datetimes);

                context.startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private @NonNull Intent getIntent(Flight flight, int selectedPax, List<String> datetimes) {
        double totalPayment = Conversions.calculateTotalPayment(selectedPax, flight.getPriceRate());

        // Create Intent to navigate to AddOnActivity
        Intent intent = new Intent(context, BookingActivity.class);
        intent.putExtra("departureAirport", flight.getDepartureAirport());
        intent.putExtra("arrivalAirport", flight.getArrivalAirport());
        intent.putExtra("departureDatetime", datetimes.get(0));
        intent.putExtra("arrivalDatetime", datetimes.get(1));
        intent.putExtra("pax", selectedPax);
        intent.putExtra("totalPayment", totalPayment);
        return intent;
    }
}
