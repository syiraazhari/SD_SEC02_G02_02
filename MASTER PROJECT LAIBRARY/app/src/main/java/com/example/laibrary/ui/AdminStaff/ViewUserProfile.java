package com.example.laibrary.ui.AdminStaff;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laibrary.LoginActivity;
import com.example.laibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;



import java.util.ArrayList;

public class ViewUserProfile extends AppCompatActivity {

    private ImageView allprofilePic;
    private TextView allprofileName, allprofileAge, allprofileEmail, allprofilerole;
    private Button allprofileUpdate, alldeleteAcc;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    FirebaseUser firebaseUser;
    ArrayList<StaffList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user_profile);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        String currentid = firebaseAuth.getUid();

        allprofilePic = findViewById(R.id.ivallProfilePic);
        allprofileAge = findViewById(R.id.tvallProfileAge) ;
        allprofileName = findViewById(R.id.tvallProfileName);
        allprofileEmail = findViewById(R.id.tvallProfileEmail);
        allprofileUpdate = findViewById(R.id.btnallProfileUpdate);
        alldeleteAcc = findViewById(R.id.btnDeleteallAcc);
        allprofilerole = findViewById(R.id.tvallProfileRole);

        allprofileName.setText("Name: " + getIntent().getStringExtra("keyusername"));
        allprofileAge.setText("Age: " + getIntent().getStringExtra("keyuserage"));
        allprofileEmail.setText("Email: " + getIntent().getStringExtra("keyuseremail"));
        allprofilerole.setText("Role: " + getIntent().getStringExtra("keyuserrole"));

/*
        DatabaseReference databaseReference = firebaseDatabase.getReference("User Info").child(getIntent().getStringExtra("keyuserid"));
*/

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(getIntent().getStringExtra("keyuserid")).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(allprofilePic);
            }
        });


        allprofileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewUserProfile.this, UpdateAllUserProfile.class);
                i.putExtra("keyeuserid", getIntent().getStringExtra("keyuserid"));
                startActivity(i);
            }
        });

        //hide delete account button
        alldeleteAcc.setVisibility(View.GONE);

    }
}