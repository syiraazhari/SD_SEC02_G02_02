package com.ui.AdminStaff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class StafflistsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    StaffListAdapter staffListAdapter;
    ArrayList<StaffList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stafflists);

        //getSupportActionBar().hide();

        recyclerView = findViewById(R.id.StaffList);

        databaseReference = FirebaseDatabase.getInstance().getReference("User Info")    ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        staffListAdapter = new StaffListAdapter(this, list);
        recyclerView.setAdapter(staffListAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                     //StaffList staffList = dataSnapshot.getValue(com.example.laibraryadminstaff.ui.AdminStaff.StaffList.class);
                    StaffList staffList = dataSnapshot.getValue(com.ui.AdminStaff.StaffList.class);
                     list.add(staffList);
                }
                staffListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}