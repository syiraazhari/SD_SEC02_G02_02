package com.ui.AdminStaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koolearn.android.kooreader.KooReader;
import com.koolearn.android.kooreader.RecyclerItemClickListener;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class ChangeVoucher extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FloatingActionButton btnFloating;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout refreshLayout1;
    ArrayList<Voucher> list;
    VoucherAdapter voucherAdapter;
    RecyclerView rcv_voucher;
    CardView cvvoucher;

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChangeVoucher.this, AdminStaffFramgnet.class));
        finish();
    }*/

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshLayout1.setRefreshing(false);
        }
    };
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_voucher);

        //onRefresh();

        btnFloating = findViewById(R.id.fab_addVoucher);
        rcv_voucher = findViewById(R.id.rcv_voucher);
        /*refreshLayout1 = (SwipeRefreshLayout) findViewById(R.id.refresh1);
        refreshLayout1.setOnRefreshListener(this);
        refreshLayout1.setColorSchemeResources(R.color.progressBara, R.color.progressBarb);
        refreshLayout1.setProgressBackgroundColor(R.color.progressBarBg);*/
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("voucher");
        rcv_voucher.setHasFixedSize(true);
        rcv_voucher.setLayoutManager(new LinearLayoutManager(this));

        btnFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangeVoucher.this, AddVoucher.class));
            }
        });

        list = new ArrayList<>();
        rcv_voucher.setLayoutManager(new LinearLayoutManager(this));
        voucherAdapter = new VoucherAdapter(this,list);
        rcv_voucher.setAdapter(voucherAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Voucher voucher = dataSnapshot.getValue(com.ui.AdminStaff.Voucher.class);
                    Log.d("error", "onDataChange: " + voucher);
                    list.add(voucher);
                }
                voucherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(1, 2000);

    }
}