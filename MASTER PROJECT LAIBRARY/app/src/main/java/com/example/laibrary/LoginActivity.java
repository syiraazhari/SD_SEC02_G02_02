package com.example.laibrary;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laibrary.ui.home.HomeFragment;
import com.example.laibrary.ui.profile.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button login;
    private int counter =5;
    private TextView userRegistration ;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;
    private FirebaseDatabase firebaseDatabase;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.tvInfo);
        login = (Button) findViewById(R.id.btnLogin);
        forgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        userRegistration = (TextView)findViewById(R.id.tvRegister);

        email = Name.getText().toString();

        Info.setText("No of attemps remaining: 5");

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user !=null){
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Name.getText().toString().isEmpty() || !Password.getText().toString().isEmpty() ){
                    validate(Name.getText().toString(), Password.getText().toString());
                }else {
                    Toast.makeText(LoginActivity.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
            }
        });



    }

    private void validate (String userName, String userPassword){

        progressDialog.setMessage("Please wait patiently.");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                }else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No of reamaining attemps: "+ counter);
                    progressDialog.dismiss();
                    if( counter == 0){
                        login.setEnabled(false);
                    }
                }
            }
        });


    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();

        if (emailFlag){
            Toast.makeText(LoginActivity.this, "verified", Toast.LENGTH_SHORT).show();
            finish();

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();


            DatabaseReference databaseReference = firebaseDatabase.getReference("User Info").child(firebaseAuth.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserProfile userProfile = snapshot.getValue(UserProfile.class);
                    Log.i(TAG,"user role is " + userProfile.getUserRole());
                    if (!userProfile.getUserRole().equals("Admin")  && !userProfile.getUserRole().equals("Staff")){
                        Toast.makeText(LoginActivity.this, "Sorry, You are not a staff or an admin", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                    }else {
                        Log.i(TAG,"user role1 is user " + userProfile.getUserRole());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, error.getCode(), Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(this, "Verify your email.", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }



}