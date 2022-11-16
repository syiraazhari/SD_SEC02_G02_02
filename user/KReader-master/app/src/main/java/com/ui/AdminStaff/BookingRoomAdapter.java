package com.ui.AdminStaff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koolearn.klibrary.ui.android.R;
import com.ui.home.FeedbackForm;

import java.util.ArrayList;

public class BookingRoomAdapter extends RecyclerView.Adapter<BookingRoomAdapter.BookingRoomViewHolder> {

    Context context;
    ArrayList<BookingRoom> list;

    public BookingRoomAdapter() {
    }

    public BookingRoomAdapter(Context context, ArrayList<BookingRoom> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookingRoomAdapter.BookingRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_bookingroom, parent, false);
        return new BookingRoomAdapter.BookingRoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingRoomAdapter.BookingRoomViewHolder holder, int position) {
        BookingRoom bookingRoom = list.get(position);
        holder.name.setText(bookingRoom.getRoomfullname());
        holder.ic.setText(bookingRoom.getRoomicnumber());
        holder.phone.setText(bookingRoom.getRoomphonenumber());
        holder.room.setText(bookingRoom.getRoomnumber());
        holder.head.setText(bookingRoom.getRoomtotalstudent());
        holder.date.setText(bookingRoom.getRoomrentdate());
        holder.time.setText(bookingRoom.getRoomrentime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class BookingRoomViewHolder extends RecyclerView.ViewHolder {
        TextView name, ic, phone, room, head, date, time;

        public BookingRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textbr12);
            ic = itemView.findViewById(R.id.textbr22);
            phone = itemView.findViewById(R.id.textbr32);
            room = itemView.findViewById(R.id.textbr42);
            head = itemView.findViewById(R.id.textbr52);
            date = itemView.findViewById(R.id.textbr62);
            time = itemView.findViewById(R.id.textbr72);
        }

    }

}
