package com.ui.home;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.koolearn.klibrary.ui.android.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class bookingActivity extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Spinner Roomspinner;
    private EditText Fullname, IcNum, PhoneNum, Totalstudent, Roomnumber;
    private TextView rentdate, renttime;
    private Button bookroom, choosedate;
    String []room;
    int index;
    String noRoom, roomfullname, roomicnum, roomphonenum, roomtotalstudent, roombooktime, roombookdate, roomnumber;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage);

        Fullname = findViewById(R.id.etFullName3);
        IcNum = findViewById(R.id.etIc2);
        PhoneNum = findViewById(R.id.etPnumber2);
        Totalstudent =  findViewById(R.id.TotalstudentET);
        Roomspinner = findViewById(R.id.roomspinner);
        renttime = findViewById(R.id.tvRoomRentTime);
        rentdate = findViewById(R.id.tvRoomRentDate);


        bookroom = findViewById(R.id.setBookRoombtn);
        choosedate = findViewById(R.id.setRoomDatebtn);

        firebaseAuth = firebaseAuth.getInstance();

        room = getResources().getStringArray(R.array.StudyRoom_array);

        ArrayAdapter <String>adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,room);
        Roomspinner.setAdapter(adapter);
        Roomspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index = adapterView.getSelectedItemPosition();
                noRoom = room[index];
                Toast.makeText(getBaseContext(),"" + room[index] + " selected",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bookroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                roomfullname = Fullname.getText().toString();
                roomicnum = IcNum.getText().toString();
                roomphonenum = PhoneNum.getText().toString();
                roomtotalstudent = Totalstudent.getText().toString();
                roombooktime = renttime.getText().toString();
                roombookdate = rentdate.getText().toString();


                Intent intent = new Intent(bookingActivity.this, ConfirmRoomBooking.class);
                intent.putExtra("Roomfullnamekey",roomfullname);
                intent.putExtra("Roomicnumberkey",roomicnum);
                intent.putExtra("Roomphonenumberkey",roomphonenum);
                intent.putExtra("Roomtotalstudentkey",roomtotalstudent);
                intent.putExtra("Roomnumberkey",noRoom);
                intent.putExtra("Roomrenttimekey",roombooktime);
                intent.putExtra("Roomrentdatekey",roombookdate);
                startActivity(intent);


            }
        });

        choosedate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Date DateandTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                String date = dateFormat.format(DateandTime);
                String time = timeFormat.format(DateandTime);
                rentdate.setText(date);
                renttime.setText(time);
            }
        });
    }
    private boolean validate(){
        Boolean result = false;
        roomfullname = Fullname.getText().toString();
        roomicnum = IcNum.getText().toString();
        roomphonenum = PhoneNum.getText().toString();
        roomtotalstudent = Totalstudent.getText().toString();
        roombooktime = renttime.getText().toString();
        roombookdate = rentdate.getText().toString();


        if (roomfullname.isEmpty() || roomicnum.isEmpty() || roomphonenum.isEmpty() || roomtotalstudent.isEmpty() || roombooktime.isEmpty() || roombookdate.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else  {
            result = true;
        }

        return result;
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
