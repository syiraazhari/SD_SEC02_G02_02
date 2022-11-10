package com.ui.AdminStaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koolearn.klibrary.ui.android.R;
import com.ui.home.FeedbackActivity;
import com.ui.home.FeedbackForm;

import java.util.ArrayList;

public class ViewFeedback extends AppCompatActivity {


    ArrayList<FeedbackForm> list;
    RecyclerView rcv_feedback;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);

        rcv_feedback = findViewById(R.id.rcv_feedback);
        rcv_feedback.setHasFixedSize(true);
        rcv_feedback.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Feedback");


        list = new ArrayList<>();
        rcv_feedback.setLayoutManager(new LinearLayoutManager(this));
        feedbackAdapter = new FeedbackAdapter(this,list);
        rcv_feedback.setAdapter(feedbackAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FeedbackForm feedbackForm = dataSnapshot.getValue(com.ui.home.FeedbackForm.class);
                    /*Log.d("error", "onDataChange: " + voucher);*/
                    list.add(feedbackForm);
                }
                feedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}