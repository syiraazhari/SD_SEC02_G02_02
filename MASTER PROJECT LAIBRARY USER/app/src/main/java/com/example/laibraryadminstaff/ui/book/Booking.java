package com.example.laibraryadminstaff.ui.book;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Booking extends AppCompatActivity {

    private EditText fullName,ic,pNumber,quantity, offer;
    private Spinner BukuSpinner;
    private Button Bk, chooseDate;
    private TextView Rentdate;
    String []Book;
    int index;
    String  bookName,ifullname,iic,ipnumber,iquantity,irentdate, irentday, irentmonth, irentyear, ioffer="", roleStudent;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        fullName = findViewById(R.id.etFullName);
        ic = findViewById(R.id.etIc);
        pNumber = findViewById(R.id.etPnumber);
        quantity = findViewById(R.id.etQuantity);
        BukuSpinner = findViewById(R.id.namaBuku);
        Bk = findViewById(R.id.btnBook);
        Rentdate = findViewById(R.id.tvRentDate);
        chooseDate = findViewById(R.id.btnSetDate);
        offer = findViewById(R.id.etOffer);



        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference("User Info").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                roleStudent = userProfile.getUserStudent();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Booking.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
        Log.i(TAG,"userStudent is" +roleStudent);

        Book = getResources().getStringArray(R.array.Buku_array);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter <String>adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,Book);
        BukuSpinner.setAdapter(adapter);
        BukuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 index=adapterView.getSelectedItemPosition();
                bookName = Book[index];
                Toast.makeText(getBaseContext(),"book= "+Book[index],Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        irentdate = getIntent().getStringExtra("keyrentdate");
        irentday = getIntent().getStringExtra("keyrentday");
        irentmonth = getIntent().getStringExtra("keyrentmonth");
        irentyear = getIntent().getStringExtra("keyrentyear");
        Rentdate.setText(irentdate);




        Bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ifullname = fullName.getText().toString();
                iic = ic.getText().toString();
                ipnumber = pNumber.getText().toString();
                iquantity = quantity.getText().toString();
                ioffer = offer.getText().toString();

                Intent intent = new Intent(Booking.this,ComfirmBooking.class);
                intent.putExtra("keyfullname",ifullname);
                intent.putExtra("keyic",iic);
                intent.putExtra("keypnumber",ipnumber);
                intent.putExtra("keyquantity",iquantity);
                intent.putExtra("keyrentdate",irentdate);
                intent.putExtra("keybookname",bookName);
                intent.putExtra("keyrentday",irentday);
                intent.putExtra("keyrentmonth",irentmonth);
                intent.putExtra("keyrentyear",irentyear);
                intent.putExtra("keyoffer", ioffer);
                startActivity(intent);

            }
        });

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Booking.this,BookSetDate.class));
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