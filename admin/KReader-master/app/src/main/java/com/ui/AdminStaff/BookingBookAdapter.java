package com.ui.AdminStaff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class BookingBookAdapter extends RecyclerView.Adapter<BookingBookAdapter.BookingBookViewHolder>{

    Context context;
    ArrayList<Bookingbook> list;

    public BookingBookAdapter() {
    }

    public BookingBookAdapter(Context context, ArrayList<Bookingbook> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookingBookAdapter.BookingBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_bookingbook, parent, false);
        return new BookingBookAdapter.BookingBookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingBookAdapter.BookingBookViewHolder holder, int position) {
        Bookingbook bookingbook = list.get(position);
        holder.name.setText(bookingbook.getFullname());
        holder.ic.setText(bookingbook.getIcnumber());
        holder.phone.setText(bookingbook.getPhonenumber());
        holder.book.setText(bookingbook.getNamebook());
        holder.quantity.setText(bookingbook.getQuantity());
        holder.date.setText(bookingbook.getRentdate());
        holder.prcie.setText(bookingbook.getTotal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BookingBookViewHolder extends RecyclerView.ViewHolder {
        TextView name, ic, phone, book, quantity, date, prcie;

        public BookingBookViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textbb12);
            ic = itemView.findViewById(R.id.textbb22);
            phone = itemView.findViewById(R.id.textbb32);
            book = itemView.findViewById(R.id.textbb42);
            quantity = itemView.findViewById(R.id.textbb52);
            date = itemView.findViewById(R.id.textbb62);
            prcie = itemView.findViewById(R.id.textbb72);
        }

    }
}
