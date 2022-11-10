package com.ui.AdminStaff;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.Change;
import com.koolearn.klibrary.ui.android.R;
import com.ui.home.FeedbackActivity;
import com.ui.home.FeedbackForm;

import java.util.HashMap;

public class AddVoucher extends AppCompatActivity {

    private EditText voucherNum, voucherDis, voucherName;
    private Button btnCancel, btnConfirm;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);

        voucherDis = findViewById(R.id.et_VoucherDis);
        voucherName = findViewById(R.id.et_VoucherName);
        btnConfirm = findViewById(R.id.btn_confirm);
        voucherNum = findViewById(R.id.et_VoucherNum);
        btnCancel = findViewById(R.id.btn_cancel);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseUsers =  FirebaseDatabase.getInstance().getReference();


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = voucherName.getText().toString();
                String dis = voucherDis.getText().toString();
                String num = voucherNum.getText().toString();
                String id = databaseUsers.push().getKey();

                Voucher voucher  = new Voucher(name, dis, num, id);
                databaseUsers.child("voucher").child(name).setValue(voucher).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddVoucher.this, "Update successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*databaseReference = firebaseDatabase.getReference().child("Voucher");

                HashMap<String, String> vouchermap =  new HashMap<>();
                vouchermap.put("voucherCode" , name);
                vouchermap.put("voucherDis" , dis);
                vouchermap.put("voucherNum" , num);

                databaseReference.child(name).setValue(vouchermap);

                finish();*/

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}