package com.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koolearn.klibrary.ui.android.R;

public class ConfirmRoomBooking extends AppCompatActivity {
    private TextView roomfullname, roomicnum, roomphonenum, roomtotalstudent, roomrenttime, roomrentdate, roomnumber;
    private Button roomconfirmbtn, changebtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    String Roomfullname, Roomicnum, Roomphonenum, Roomtotalstudent, Roomrenttime, Roomrentdate, Roomnumber;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomconfirmbooking);

        roomconfirmbtn = findViewById(R.id.btnRoomComfirm);
        changebtn = findViewById(R.id.btnroomChange);

        roomfullname = findViewById(R.id.tvroomFullName);
        roomicnum = findViewById(R.id.tvroomIcnum);
        roomphonenum = findViewById(R.id.tvroomphonenumber3);
        roomtotalstudent = findViewById(R.id.tvRoomtotalstudent);
        roomnumber = findViewById(R.id.tvRoomnumber);
        roomrenttime = findViewById(R.id.tvRenttime4);
        roomrentdate = findViewById(R.id.tvRoomrentdate);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        Roomfullname = getIntent().getStringExtra("Roomfullnamekey");
        Roomicnum = getIntent().getStringExtra("Roomicnumberkey");
        Roomphonenum = getIntent().getStringExtra("Roomphonenumberkey");
        Roomtotalstudent = getIntent().getStringExtra("Roomtotalstudentkey");
        Roomnumber = getIntent().getStringExtra("Roomnumberkey");
        Roomrenttime = getIntent().getStringExtra("Roomrenttimekey");
        Roomrentdate = getIntent().getStringExtra("Roomrentdatekey");

        Log.i("Roomfullname", Roomfullname);
        Log.i("Roomicnum", Roomicnum);
        Log.i("Roomphonenum", Roomphonenum);
        Log.i("Roomtotalstudent", Roomtotalstudent);
        Log.i("Roomtotalstudent", Roomnumber);
        Log.i("Roomrenttime", Roomrenttime);
        Log.i("Roomrentdate", Roomrentdate);






        roomfullname.setText(Roomfullname.toString() );
        roomicnum.setText(Roomicnum);
        roomphonenum.setText(Roomphonenum);
        roomtotalstudent.setText(Roomtotalstudent);
        roomnumber.setText(Roomnumber);
        roomrenttime.setText(Roomrenttime);
        roomrentdate.setText(Roomrentdate);

        Intent intent = getIntent();
        Log.d("tag", Roomfullname);
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String uid = FirebaseDatabase.getInstance().getReference().push().getKey();
        databaseReference = FirebaseDatabase.getInstance().getReference("Room Booking").child(id);

        roomconfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = firebaseDatabase.getReference("Room Booking Info").
                        child(firebaseAuth.getUid()).child(uid);
                RoomBookingDetail roomBookingDetail = new RoomBookingDetail();
                roomBookingDetail.setRoomfullname(Roomfullname);
                roomBookingDetail.setRoomicnumber(Roomicnum);
                roomBookingDetail.setRoomphonenumber(Roomphonenum);
                roomBookingDetail.setRoomtotalstudent(Roomtotalstudent);
                roomBookingDetail.setRoomnumber(Roomnumber);
                roomBookingDetail.setRoomrentime(Roomrenttime);
                roomBookingDetail.setRoomrentdate(Roomrentdate);

                databaseReference.setValue(roomBookingDetail);

                Toast.makeText(getBaseContext(),"Room successfully booked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConfirmRoomBooking.this, HomeFragment.class);

                startActivity(intent);
            }
        });

        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmRoomBooking.this, bookingActivity.class));
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
