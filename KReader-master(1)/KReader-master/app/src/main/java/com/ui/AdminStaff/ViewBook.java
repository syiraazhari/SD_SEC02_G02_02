package com.ui.AdminStaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class ViewBook extends AppCompatActivity {

    private FloatingActionButton btnfaddbook;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout refreshLayout1;
    ArrayList<AdminBook> list;
    BookAdapter bookAdapter;
    RecyclerView rcv_book;
    CardView cvbook;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        btnfaddbook = findViewById(R.id.fab_addBook);
        rcv_book = findViewById(R.id.rcv_book);
        rcv_book.setHasFixedSize(true);
        rcv_book.setLayoutManager(new LinearLayoutManager(this));

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");

        btnfaddbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewBook.this, AdminAddBook.class));

            }
        });

        list = new ArrayList<>();
        rcv_book.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this,list);
        rcv_book.setAdapter(bookAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AdminBook adminBook = dataSnapshot.getValue(com.ui.AdminStaff.AdminBook.class);
                    list.add(adminBook);
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}