package com.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.FirebaseDatabase;
import com.koolearn.klibrary.ui.android.R;

public class BookingReceipt extends AppCompatActivity {

    Button backtohome;
    private FirebaseDatabase firebaseDatabase;
    private TextView tvbookingid, tvpaymentid,tvamount;
    private String bookingid, paymentid, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_receipt);

        backtohome = findViewById(R.id.btnbacktohome);
        tvbookingid = findViewById(R.id.tvBookingid);
        tvpaymentid = findViewById(R.id.tvPaymentID);
        tvamount = findViewById(R.id.tvAmount);

        amount = getIntent().getStringExtra("keyamount");
        bookingid = getIntent().getStringExtra("keyid");
        paymentid = getIntent().getStringExtra("keypaymentid");

        tvamount.setText(amount);
        tvbookingid.setText(bookingid);
        tvpaymentid.setText(paymentid);

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingReceipt.this, com.koolearn.android.kooreader.MainActivity.class));
            }
        });

    }
}