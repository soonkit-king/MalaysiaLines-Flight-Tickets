package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import model.Booking;
import my.utem.ftmk.flightticketingsystem.R;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private Context context;

    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_booking_history, parent, false); // Replace with your booking item layout
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvBookingDepartureAirport.setText(booking.getDepartureAirport());
        holder.tvBookingArrivalAirport.setText(booking.getArrivalAirport());
        holder.tvPax.setText(String.valueOf(booking.getPax()));
        holder.tvDepartureDatetime.setText(String.valueOf(booking.getDepartureDatetime()));
        holder.tvArrivalDatetime.setText(String.valueOf(booking.getArrivalDatetime()));
        holder.tvSeatNo.setText(booking.getSeatNo());
        holder.tvRefund.setText(booking.hasRefundGuarantee() ? "With refund guarantee" : "Without refund guarantee");
        holder.tvTotalPayment.setText(Double.toString(booking.getTotalPayment())); // Format double to 2 decimal places
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }


    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingDepartureAirport, tvBookingArrivalAirport, tvPax, tvDepartureDatetime,
                tvSeatNo, tvRefund, tvTotalPayment, tvArrivalDatetime;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingDepartureAirport = itemView.findViewById(R.id.tvBookingDepartureAirport);
            tvBookingArrivalAirport = itemView.findViewById(R.id.tvBookingArrivalAirport);
            tvDepartureDatetime = itemView.findViewById(R.id.tvDepartureDatetime);
            tvArrivalDatetime = itemView.findViewById(R.id.tvArrivalDatetime);
            tvSeatNo = itemView.findViewById(R.id.tvSeatNo);
            tvRefund = itemView.findViewById(R.id.tvRefund);
            tvTotalPayment = itemView.findViewById(R.id.tvTotalPayment);
            tvPax = itemView.findViewById(R.id.tvPax);
        }
    }
}