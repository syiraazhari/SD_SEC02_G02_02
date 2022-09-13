package com.example.laibrary.ui.AdminStaff;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.laibrary.R;
import com.example.laibrary.ui.profile.UpdateProfile;
import com.example.laibrary.ui.profile.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateAllUserProfile extends AppCompatActivity {

    private EditText newallUserName, newallUserEmail, newallUserAge, newallUserRole;
    private Button allsave;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ImageView updateallProfilePic;
    private static int PICK_IMAGE2 = 123;
    Uri imagePath2;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    String userid;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE2 && resultCode == RESULT_OK && data.getData() != null ){
            imagePath2 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath2);
                updateallProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_all_user_profile);

        newallUserName = (EditText) findViewById(R.id.etNameUpdate2);
        newallUserEmail = (EditText) findViewById(R.id.etEmailUpdate2);
        newallUserAge = (EditText) findViewById(R.id.etAgeUpdate2);
        newallUserRole = (EditText) findViewById(R.id.etRoleUpdate2);
        allsave = (Button) findViewById(R.id.btnSave2);
        updateallProfilePic = (ImageView)findViewById(R.id.ivProileUpdate2);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userid = getIntent().getStringExtra("keyeuserid");
        Log.i(TAG,"user id4 is " + userid);

        DatabaseReference databaseReference = firebaseDatabase.getReference("User Info").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                newallUserName.setText(userProfile.getUserName());
                newallUserAge.setText(userProfile.getUserAge());
                newallUserEmail.setText(userProfile.getUserEmail());
                newallUserRole.setText(userProfile.getUserRole());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateAllUserProfile.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(getIntent().getStringExtra("keyeuserid")).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(updateallProfilePic);
            }
        });

        allsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = newallUserName.getText().toString();
                String age =  newallUserAge.getText().toString();
                String email = newallUserEmail.getText().toString();
                String role = newallUserRole.getText().toString();

                UserProfile userProfile = new UserProfile(name, age, email, role, getIntent().getStringExtra("keyeuserid"));

                databaseReference.setValue(userProfile);

                StorageReference imageReference = storageReference.child(getIntent().getStringExtra("keyeuserid")).child("Images").child("Profile Pic");   //User id/Images/Profile Pic
                UploadTask uploadTask = imageReference.putFile(imagePath2);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateAllUserProfile.this, "Upload failed.",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UpdateAllUserProfile.this, "Upload successfully.",Toast.LENGTH_SHORT).show();

                    }
                });

                finish();
            }
        });

        updateallProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),PICK_IMAGE2);
            }
        });

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