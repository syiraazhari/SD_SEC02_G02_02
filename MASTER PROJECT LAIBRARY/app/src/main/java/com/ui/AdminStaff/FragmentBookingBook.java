package com.ui.AdminStaff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class FragmentBookingBook extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<Bookingbook> bookingbooks;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    BookingBookAdapter bookingBookAdapter;
    ArrayList<Bookingbook> list;

    public FragmentBookingBook() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bookingbook, container, false);

        recyclerView = v.findViewById(R.id.rcv_bookingbook);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking Info");

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingBookAdapter = new BookingBookAdapter(getContext(),list);
        recyclerView.setAdapter(bookingBookAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Bookingbook bookingbook = dataSnapshot.getValue(com.ui.AdminStaff.Bookingbook.class);
                    list.add(bookingbook);
                }
                bookingBookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}
