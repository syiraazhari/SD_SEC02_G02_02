package com.ui.AdminStaff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.koolearn.klibrary.ui.android.R;
import com.ui.profile.UserProfile;

public class CheckVerification extends AppCompatActivity {

    private Button approve, notapprove;
    private TextView name, ic, tel;
    private String stuid;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_verification);

        notapprove = findViewById(R.id.buttonVeriNotApprove);
        approve = findViewById(R.id.buttonVeriApprove);
        name = findViewById(R.id.tvStudentName);
        ic = findViewById(R.id.tvStudentIC);
        tel = findViewById(R.id.tvStudentTel);

        stuid = getIntent().getStringExtra("keystudentid");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        DocumentReference documentReference = firebaseFirestore.collection("StudentVeri").document(stuid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("FullName"));
                ic.setText(value.getString("IC"));
                tel.setText(value.getString("telefon"));
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = firebaseFirestore.collection("StudentVeri").document(stuid);

                AlertDialog.Builder dialog = new AlertDialog.Builder(CheckVerification.this);
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("Approving this apply will result in Changing user student role from the system" );
                dialog.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference = firebaseDatabase.getReference("User Info").child(stuid);
                        databaseReference.child("userStudent").setValue("Student");
                        finish();
                        /*databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserProfile userProfile = new UserProfile();
                                //userProfile.setUserStudent((userProfile.getUserName(), userProfile.getUserAge(), userProfile.getUserEmail(), userProfile.getUserRole(), userProfile.getUserId(), "Student");
                                databaseReference.setValue(userProfile);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });*/
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        notapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = firebaseFirestore.collection("StudentVeri").document(stuid);

                AlertDialog.Builder dialog = new AlertDialog.Builder(CheckVerification.this);
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("Deleting this apply will result in completely removing the apply from the system" );
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        documentReference.delete() ;
                    }
                });
                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                finish();

            }
        });


    }
}