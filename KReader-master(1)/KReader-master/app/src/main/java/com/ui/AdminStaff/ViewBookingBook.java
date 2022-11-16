package com.ui.AdminStaff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingBook extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Bookingbook> bookingbook;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    BookingBookAdapter bookingBookAdapter;
    ArrayList<Bookingbook> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingbook);


        recyclerView = findViewById(R.id.rcv_bookingbook);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking Info");

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingBookAdapter = new BookingBookAdapter(this,list);
        recyclerView.setAdapter(bookingBookAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Bookingbook bookingbook = ds.getValue(com.ui.AdminStaff.Bookingbook.class);
                        list.add(bookingbook);
                    }

                }
                bookingBookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
