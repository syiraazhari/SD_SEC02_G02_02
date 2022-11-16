package com.ui.AdminStaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class ViewStudent extends AppCompatActivity {

    FirebaseFirestore db;
    RecyclerView rcv_student;
    ArrayList<DownModel> downModels = new ArrayList<>();
    StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);




        setupFB();
        setupRV();
        dataFromFirebase();

    }

    private void dataFromFirebase(){
        if (downModels.size()>0){
            downModels.clear();
        }

        //db = FirebaseFirestore.getInstance();

        db.collection("StudentVeri")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot: task.getResult()){

                            DownModel downModel  = new DownModel(documentSnapshot.getString("name"), documentSnapshot.getString("link")
                            , documentSnapshot.getString("UserUid"));
                            downModels.add(downModel);

                        }

                        studentAdapter = new StudentAdapter(ViewStudent.this, downModels);
                        rcv_student.setAdapter(studentAdapter);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ViewStudent.this, "Error ;-.-;", Toast.LENGTH_SHORT).show();


                    }
                });



    }


    private void setupFB(){
        db = FirebaseFirestore.getInstance();
    }

    private void setupRV(){
        rcv_student = findViewById(R.id.rcv_student);
        rcv_student.setHasFixedSize(true);
        rcv_student.setLayoutManager(new LinearLayoutManager(this));
    }

}