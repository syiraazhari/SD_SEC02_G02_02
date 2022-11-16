package com.ui.AdminStaff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koolearn.klibrary.ui.android.R;
import com.squareup.picasso.Picasso;
import com.ui.profile.UpdateProfile;

import java.io.IOException;

public class ChangeBook extends AppCompatActivity {

    private EditText et_BookViewDesciption, et_BookViewPrcie, et_BookViewName;
    private Button btn_cancel1, btn_confirm1, btn_delete1;
    private String name, price, description, id;
    private FirebaseDatabase firebaseDatabase;
    private ImageView bookpic;
    private static int PICK_IMAGE = 123;
    private Uri bookimagePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null ){
            bookimagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),bookimagePath);
                bookpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_book);

        et_BookViewDesciption = findViewById(R.id.et_BookViewDesciption);
        et_BookViewPrcie = findViewById(R.id.et_BookViewPrcie);
        et_BookViewName = findViewById(R.id.et_BookViewName);
        btn_cancel1 = findViewById(R.id.btn_bookViewcancel1);
        btn_confirm1  = findViewById(R.id.btn_bookViewconfirm1);
        btn_delete1 = findViewById(R.id.btn_bookViewdelete1);
        bookpic = findViewById(R.id.ivAddBookViewPic);

        name = getIntent().getStringExtra("keybooksname");
        id = getIntent().getStringExtra("keybooksid");
        price = getIntent().getStringExtra("keybooksprice");
        description = getIntent().getStringExtra("keybooksdes");

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        final StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Books").child(id).child("images").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(bookpic);
            }
        });

        bookpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"),PICK_IMAGE);
            }
        });

        et_BookViewDesciption.setText(description);
        et_BookViewName.setText(name);
        et_BookViewPrcie.setText(price);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Books").child(id);

        btn_confirm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newprice, newdes, newnname;
                newprice = et_BookViewPrcie.getText().toString();
                newdes = et_BookViewDesciption.getText().toString();
                newnname = et_BookViewName.getText().toString();

                if(!newprice.isEmpty() && !newdes.isEmpty() && !newnname.isEmpty() && bookimagePath != null){
                    Double priceDouble = Double.parseDouble(price);
                    if (priceDouble >0){
                        AdminBook adminBook = new AdminBook(newnname, newprice, newdes, id);
                        databaseReference.setValue(adminBook);
                        startActivity(new Intent(ChangeBook.this, ViewBook.class));

                        StorageReference imageReference = storageReference.child("Books").child(id).child("images");   //User id/Images/Profile Pic
                        UploadTask uploadTask = imageReference.putFile(bookimagePath);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChangeBook.this, "Upload failed.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(ChangeBook.this, "Upload successfully.",Toast.LENGTH_SHORT).show();

                            }
                        });

                        finish();
                    }else{
                        Toast.makeText(ChangeBook.this, "Please enter price more than 0", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChangeBook.this, "Please enter all information", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeBook.this);
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("Deleting this book will result in completely removing this voucher from the system");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ChangeBook.this, "Account deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(ChangeBook.this, ChangeVoucher.class));
                                }else{
                                    Toast.makeText(ChangeBook.this, "cannot delete", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ChangeBook.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
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



    }
}