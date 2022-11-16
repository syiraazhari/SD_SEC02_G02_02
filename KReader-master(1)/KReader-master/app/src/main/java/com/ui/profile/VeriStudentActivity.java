package com.ui.profile;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koolearn.klibrary.ui.android.R;

import java.util.HashMap;
import java.util.Map;

public class VeriStudentActivity extends AppCompatActivity {

    Button uploadFile, selectFile;
    TextView showFile;
    FirebaseStorage firebaseStorage, downurl;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    Uri pdfUri;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    EditText etVeriTel, etVeriIC, etVeriFullName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veri_student);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        downurl = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        selectFile = findViewById(R.id.btnSelectFile);
        showFile = findViewById(R.id.tvShowFile);
        uploadFile = findViewById(R.id.btnUploadFile);
        etVeriTel = findViewById(R.id.etVeriTel);
        etVeriIC = findViewById(R.id.etVeriIC);
        etVeriFullName = findViewById(R.id.etVeriFullName);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(VeriStudentActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                }else{
                    ActivityCompat.requestPermissions(VeriStudentActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }

            }
        });

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pdfUri != null){
                    upload(pdfUri);
                }else{
                    Toast.makeText(VeriStudentActivity.this, "Select a file", Toast.LENGTH_SHORT).show();
                }

            }
        });






    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
            Toast.makeText(VeriStudentActivity.this, "Please provide Permission", Toast.LENGTH_SHORT).show();
        }

    }


    private void selectPdf(){
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 86);
            //startActivityForResult(Intent.createChooser(intent, "Select File"),86);
    }



    private void upload(Uri pdfUri){

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file");
        progressDialog.setProgress(0);
        progressDialog.show();

        String name;
        String ic;
        String tel;
        final String[] link = new String[1];
        name = etVeriFullName.getText().toString();
        ic = etVeriIC.getText().toString();
        tel = etVeriTel.getText().toString();

        String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Verify Student").child(firebaseAuth.getUid()).child(fileName).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                DatabaseReference databaseReference = firebaseDatabase.getReference("FileVeriStudent");

                databaseReference.child(fileName).setValue(url ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(VeriStudentActivity.this, "File successfully uploaded", Toast.LENGTH_SHORT).show();

                            /*Map<String, Object> student = new HashMap<>();
                            student.put("FullName", name);
                            student.put("IC", ic);
                            student.put("telefon", tel);*/




                            StorageReference storageReference = firebaseStorage.getReference();
                            storageReference.child("Verify Student").child(firebaseAuth.getUid()).child(fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> student = new HashMap<>();
                                    student.put("FullName", name);
                                    student.put("IC", ic);
                                    student.put("telefon", tel);
                                    student.put("UserUid", firebaseAuth.getUid());


                                    Uri downloadUrl = uri;
                                    student.put("link", downloadUrl.toString());
                                    student.put("name", name+"Verification");



                                    firebaseFirestore.collection("StudentVeri").document(firebaseAuth.getUid())
                                            .set(student)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

                                }
                            });




                        }else{
                            Toast.makeText(VeriStudentActivity.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VeriStudentActivity.this, "File not successfully uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                int currentProgress = (int) (100 *snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) ;
                progressDialog.setProgress(currentProgress);
            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            //showFile.setText("A file is selected: "+  data.getData().getLastPathSegment());
            showFile.setText("A file is selected: "+  data.getData().getLastPathSegment());
        } else {
            Toast.makeText(VeriStudentActivity.this, "Please select the file", Toast.LENGTH_SHORT).show();
        }
    }
}