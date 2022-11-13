package com.ui.AdminStaff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    Context context;
    ArrayList<Voucher> list;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public VoucherAdapter(Context context, ArrayList<Voucher> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = list.get(position);
        holder.code.setText(voucher.getVoucherCode());
        holder.dis.setText(voucher.getVoucherDis());
        holder.num.setText(voucher.getVoucherNum());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context , ViewVoucher.class);
                intent.putExtra("keyvouchercode", voucher.getVoucherCode());
                intent.putExtra("keyvoucherdis", voucher.getVoucherDis());
                intent.putExtra("keyvouchernum", voucher.getVoucherNum());
                intent.putExtra("keyvoucherid", voucher.getVoucherID());
                context.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        TextView code, dis, num;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.text12);
            dis = itemView.findViewById(R.id.text22);
            num = itemView.findViewById(R.id.text32);

        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
