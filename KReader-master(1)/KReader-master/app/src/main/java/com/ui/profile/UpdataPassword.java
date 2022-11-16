package com.ui.profile;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koolearn.klibrary.ui.android.R;

public class UpdataPassword extends AppCompatActivity {

    private Button update;
    private EditText newPassword, currentpassword;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_password);

        update = (Button) findViewById(R.id.btnUpdatePassword);
        currentpassword = (EditText) findViewById(R.id.etCurrentPassword);
        newPassword = (EditText)findViewById(R.id.etNewRePassword);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentpass = currentpassword.getText().toString();
                String newpass = newPassword.getText().toString();


                AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), currentpass);
                firebaseUser.reauthenticate(authCredential)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firebaseUser.updatePassword(newpass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(UpdataPassword.this, "Password changed", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdataPassword.this, "Password update failed", Toast.LENGTH_SHORT).show();

                            }
                        });

                /*if (updateRePassword.equals(updatedPassword)){
                    if (newPassword.length() >7 && repassword.length()>7){
                        firebaseUser.updatePassword(updatedPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(UpdataPassword.this, "Password changed", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(UpdataPassword.this, "Password update failed", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(UpdataPassword.this, "You need to wait for one day to be able to change your password.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }else{
                        Toast.makeText(UpdataPassword.this, "Please make sure password more or equals to 8 characters or numbers", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UpdataPassword.this, "Please make sure enter same password two tome", Toast.LENGTH_SHORT).show();
                }*/


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