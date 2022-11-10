package com.ui.book;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koolearn.klibrary.ui.android.R;
import com.ui.AdminStaff.AdminBook;
import com.ui.AdminStaff.Voucher;
import com.ui.profile.UserProfile;

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
    String userRoleStudent, voucherID, voucherCode, stringdis, booksprice;
    Integer num= 1, tempnum;
    Double dis=0.00, bookspricedouble;



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

        String tempid = SelectBookAdapter.tempselectbookid;

        databaseReference = FirebaseDatabase.getInstance().getReference("Books").child(tempid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AdminBook adminBook = snapshot.getValue(AdminBook.class);
                booksprice = adminBook.getAdminbookPrice();
                Log.i(TAG,"price is" +booksprice);

                bookspricedouble = Double.valueOf(booksprice);
                Log.i(TAG,"price is" +bookspricedouble);

                nquantity = Integer.parseInt(iquantity);
                price = nquantity * bookspricedouble;
                total.setText(String.valueOf(price));

                firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("voucher").child(ioffer);



                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Voucher voucher = snapshot.getValue(Voucher.class);

                        if(snapshot.exists()){
                            num = Integer.parseInt(voucher.getVoucherNum());
                            Log.i(TAG,"num5 is" +num);
                            tempnum = num;
                            dis = Double.parseDouble(voucher.getVoucherDis());
                            stringdis = voucher.getVoucherDis();
                            voucherID = voucher.getVoucherID();
                            voucherCode = voucher.getVoucherCode();



                        }else{
                    /*num = 0;
                    dis = 0.0;*/
                        }

                        if (num > 0) {
                            Log.i(TAG,"num4 is" +num);
                            Log.i(TAG,"num6 is" +tempnum);

                            if (price > 10) {
                                afterprice = price * (100 - dis) / 100;
                                Log.i(TAG,"num7 is" +price);
                                Log.i(TAG,"num8 is" +dis);

                                percentoffer = price - afterprice;
                                if (percentoffer > 4) {
                                    price = price - 4;
                                    Log.i(TAG,"num9 is" +price);

                                } else {
                                    price = afterprice;
                                    Log.i(TAG,"num10 is" +price);

                                }
                            } else {
                                price = price;
                            }


                        }else{
                            price = price;

                        }

                        iprice = String.format("%.2f",price);
                        Log.i(TAG,"num11 is" +iprice);
                        Log.i(TAG,"num12 is" +price);


                        totalafter.setText(iprice);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }



        });






        /*if (inbook.equals("Rainforest Creatures of Malaysia")){
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

        }*/

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




        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = databaseReference.push().getKey();
                istatus = "1";
                DatabaseReference databasedate = firebaseDatabase2.getReference("Date").child(firebaseAuth.getUid());
                DateDatabase dateDatabase = new DateDatabase(irentday, irentmonth, irentyear, istatus);
                databasedate.setValue(dateDatabase);

                DatabaseReference databaseReference1 = firebaseDatabase.getReference("voucher").child(ioffer);
                Voucher voucher = new Voucher();
                Log.i(TAG,"num3 " +  voucher.getVoucherNum());


                num = num-1;
                String stringnum = num.toString();
                String vid, vdis, vcode;
                vid = voucher.getVoucherID();
                vdis = voucher.getVoucherDis();
                vcode = voucher.getVoucherCode();
                Log.i(TAG,"num2 " +  vid+ " " + vdis + " " + vcode + " " + num);

                voucher.setVoucherID(voucherID);
                voucher.setVoucherCode(voucherCode);
                voucher.setVoucherDis(stringdis);
                voucher.setVoucherNum(stringnum);
                databaseReference1.setValue(voucher);

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