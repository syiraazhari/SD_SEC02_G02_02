package com.ui.home;

import android.app.SearchableInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;


import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.koolearn.klibrary.ui.android.R;
import com.ui.AdminStaff.AdminBook;

import java.util.ArrayList;


public class ListOfBookActivity extends AppCompatActivity {


    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout refreshLayout1;
    ArrayList<AdminBook> list;
    CardView cvbook;
    private FirebaseStorage firebaseStorage;
    Toolbar toolbar;


    RecyclerView rcv_userlistbook;
    userbookAdapter userbookAdapters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_book);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");


        rcv_userlistbook = findViewById(R.id.rcv_userlistbook);
        rcv_userlistbook.setHasFixedSize(true);
        rcv_userlistbook.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AdminBook> options =
                new FirebaseRecyclerOptions.Builder<AdminBook>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Books"),AdminBook.class)
                        .build();

        userbookAdapters = new userbookAdapter(options);
        rcv_userlistbook.setAdapter(userbookAdapters);

        /*firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");

        list = new ArrayList<>();
        rcv_userlistbook.setLayoutManager(new LinearLayoutManager(this));
        userbookAdapters = new userbookAdapter(this,list);
        rcv_userlistbook.setAdapter(userbookAdapters);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AdminBook adminBook = dataSnapshot.getValue(com.ui.AdminStaff.AdminBook.class);
                    list.add(adminBook);
                }
                userbookAdapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        userbookAdapters.startListening();;
    }

    @Override
    protected void onStart() {
        super.onStart();
        userbookAdapters.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem menuItem = menu.findItem(R.id.search_listofbook);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processsearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processsearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    private void processsearch(String s){
        FirebaseRecyclerOptions<AdminBook> options =
                new FirebaseRecyclerOptions.Builder<AdminBook>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Books").orderByChild("adminBookName").startAt(s).endAt(s+"\uf8ff"), AdminBook.class)
                        .build();


        userbookAdapters = new userbookAdapter(options);
        userbookAdapters.startListening();
        rcv_userlistbook.setAdapter(userbookAdapters);
    }



}