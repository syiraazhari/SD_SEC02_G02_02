package com.ui.book;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<BookingDetail> list;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public MyAdapter(Context context) {
        this.context = context;
    }

    public MyAdapter(Context context, ArrayList<BookingDetail> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        BookingDetail bookingDetail = list.get(position);
        holder.displayname.setText(bookingDetail.getFullname());
        holder.displayic.setText(bookingDetail.getIcnumber());
        holder.displayphone.setText(bookingDetail.getPhonenumber());
        holder.displaybook.setText(bookingDetail.getNamebook());
        holder.displayquantity.setText(bookingDetail.getQuantity());
        holder.displayrentdate.setText(bookingDetail.getRentdate());
        holder.displaytotal.setText(bookingDetail.getTotal());
        String delboookingID = bookingDetail.getBookingID();
        holder.displayid.setText(bookingDetail.getBookingID());

        firebaseAuth = FirebaseAuth.getInstance();

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context.getApplicationContext());
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("Deleting this record will result in completely removing your account from the system");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, delboookingID, Toast.LENGTH_SHORT).show();
                        DatabaseReference delbookinginfo= FirebaseDatabase.getInstance().getReference("Booking Info").
                                child(firebaseAuth.getUid()).child(delboookingID);
                        DatabaseReference delPayment= FirebaseDatabase.getInstance().getReference("Payment Detail").
                                child(delboookingID);

                        delbookinginfo.removeValue();
                        delPayment.removeValue().addOnSuccessListener(suc ->{
                            Toast.makeText(context, "Record is deleted", Toast.LENGTH_SHORT).show();
                            notifyItemChanged(position);
                            notifyItemRemoved(position);

                        }).addOnFailureListener(er ->{
                            Toast.makeText(context, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                return true;

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView displayname, displayic, displayphone, displaybook, displayquantity, displayrentdate, displaytotal, displayid,txt_option;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            displayname = itemView.findViewById(R.id.tvName);
            displayic = itemView.findViewById(R.id.tvIC);
            displayphone = itemView.findViewById(R.id.tvphone);
            displaybook = itemView.findViewById(R.id.tvbook);
            displayquantity = itemView.findViewById(R.id.tvlistQuantity);
            displayrentdate = itemView.findViewById(R.id.tvrentdate);
            displaytotal = itemView.findViewById(R.id.tvlisttotal);

            displayid = itemView.findViewById(R.id.tvbookid);

        }
    }

}
