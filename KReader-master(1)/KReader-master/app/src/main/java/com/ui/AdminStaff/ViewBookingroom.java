package com.ui.AdminStaff;

import android.os.Bundle;
import android.util.Log;

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

public class ViewBookingroom extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<BookingRoom> bookingRooms;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    BookingRoomAdapter bookingRoomAdapter;
    ArrayList<BookingRoom> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookingroom);

        recyclerView = findViewById(R.id.rcv_bookingroom);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Room Booking Info");

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingRoomAdapter = new BookingRoomAdapter(this,list);
        recyclerView.setAdapter(bookingRoomAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        BookingRoom bookingRoom = ds.getValue(com.ui.AdminStaff.BookingRoom.class);
                        list.add(bookingRoom);
                    }

                }
                bookingRoomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
