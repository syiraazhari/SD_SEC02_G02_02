package com.example.laibraryadminstaff.ui.book;

import static android.content.ContentValues.TAG;

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

import com.example.laibraryadminstaff.R;
import com.example.laibraryadminstaff.ui.profile.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComfirmBooking extends AppCompatActivity {

    private TextView fullname, icnumber,pnumber,nbook,quantity,rentdate,turndate,total, totalafter;
    private Button comfirm, change;
    int nquantity;
    double price, afterprice, percentoffer;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    String ifullname,iicnumber,ipnumber,inbook,iquantity,irentdate,iprice, irentday, irentmonth, irentyear, istatus, ioffer;
    DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase2;
    String userRoleStudent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_booking);

        fullname = findViewById(R.id.tvFullName);
        icnumber = findViewById(R.id.tvIc);
        pnumber = findViewById(R.id.tvPnumber);
        nbook = findViewById(R.id.tvbname);
        quantity = findViewById(R.id.tvQuantity);
        total = findViewById(R.id.tvTotal);
        comfirm = findViewById(R.id.btnComfirm);
        change = findViewById(R.id.btnChange);
        rentdate = findViewById(R.id.tvRent);
        totalafter = findViewById(R.id.tvTotalAfter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase2 = FirebaseDatabase.getInstance();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ifullname = getIntent().getStringExtra("keyfullname");
        iicnumber = getIntent().getStringExtra("keyic");
        ipnumber =  getIntent().getStringExtra("keypnumber");
        inbook = getIntent().getStringExtra("keybookname");
        iquantity = getIntent().getStringExtra("keyquantity");
        irentdate = getIntent().getStringExtra("keyrentdate");
        irentyear = getIntent().getStringExtra("keyrentyear");
        irentday = getIntent().getStringExtra("keyrentday");
        irentmonth = getIntent().getStringExtra("keyrentmonth");
        ioffer = getIntent().getStringExtra("keyoffer");

        fullname.setText(ifullname);
        icnumber.setText(iicnumber);
        pnumber.setText(ipnumber);
        nbook.setText(inbook);
        quantity.setText(iquantity);
        rentdate.setText(irentdate);

        Log.i(TAG,"offer is" +ioffer);

        Intent intent = getIntent();

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG,"userid6 is" +id);

        databaseReference = FirebaseDatabase.getInstance().getReference("Booking").child(id);


        nquantity = Integer.parseInt(iquantity);

        if (inbook.equals("Rainforest Creatures of Malaysia")){
            price = nquantity*2;

        }else if (inbook.equals("Pergi Covid-19! Jangan Datang Lagi!")){
            price = nquantity*3;

        }else if (inbook.equals("Kelembai: Ceritera Tidak Terhikayat")){
            price = nquantity*4;

        }else if (inbook.equals("Hendak Ke Mana Gagak?")){
            price = nquantity*2.2;

        }else if (inbook.equals("Damia dan Kuda Kepang Ajaib")){
            price = nquantity*3.2;

        }else if (inbook.equals("Music and My Friends")){
            price = nquantity*4;

        }else if (inbook.equals("Gaja Loves The Sea")){
            price = nquantity*5;

        }else if (inbook.equals("Misi Melur")){
            price = nquantity*1;

        }else if (inbook.equals("Keris: Warisan Melayu")){
            price = nquantity*2;

        }else if (inbook.equals("63 Tumbuhan Ubatan")){
            price = nquantity*6;

        }

        total.setText(String.valueOf(price));

        DatabaseReference databaseReference2 = firebaseDatabase.getReference("User Info").child(firebaseAuth.getUid());
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                userRoleStudent = userProfile.getUserStudent();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ComfirmBooking.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(TAG,"userStudent3 is" +userRoleStudent);


        if (ioffer.equals("offer1") ){
            if (price > 10){
                afterprice = price  * 80 /100 ;
                percentoffer = price - afterprice;
                if (percentoffer > 4){
                    price = price - 4;
                }else{
                    price = afterprice;
                }
            }else {
                price = price;
            }
        }

        /*if (userRoleStudent.equals("Student")){
            price = price * 80 / 100;
        }*/


        iprice = String.format("%.2f",price);

        totalafter.setText(iprice);

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = databaseReference.push().getKey();
                istatus = "1";
                DatabaseReference databasedate = firebaseDatabase2.getReference("Date").child(firebaseAuth.getUid());
                DateDatabase dateDatabase = new DateDatabase(irentday, irentmonth, irentyear, istatus);
                databasedate.setValue(dateDatabase);

                DatabaseReference databaseReference = firebaseDatabase.getReference("Booking Info").
                        child(firebaseAuth.getUid()).child(id);
                BookingDetail bookingDetail = new BookingDetail();
                bookingDetail.setRentday(irentday);
                bookingDetail.setRentmonth(irentmonth);
                bookingDetail.setRentyear(irentyear);
                bookingDetail.setBookingID(id);
                bookingDetail.setFullname(ifullname);
                bookingDetail.setIcnumber(iicnumber);
                bookingDetail.setPhonenumber(ipnumber);
                bookingDetail.setNamebook(inbook);
                bookingDetail.setQuantity(iquantity);
                bookingDetail.setRentdate(irentdate);
                bookingDetail.setTotal(iprice);

                databaseReference.setValue(bookingDetail);

                Intent intent = new Intent(ComfirmBooking.this,BookPayment.class);
                intent.putExtra("keybookingid",id);
                intent.putExtra("keyTotalPrice",iprice);


                //finish();
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ComfirmBooking.this, Booking.class));
            }
        });

    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }*/

}