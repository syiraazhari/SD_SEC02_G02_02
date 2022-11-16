package com.ui.AdminStaff;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koolearn.klibrary.ui.android.R;

public class MyViewHolder extends RecyclerView.ViewHolder {


    TextView stuName, stulink, stuid;
    Button btnDown, btnViewStudent;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        stuName = itemView.findViewById(R.id.textd12);
        stulink = itemView.findViewById(R.id.textd22);
        btnDown = itemView.findViewById(R.id.btnStudentDown);
        btnViewStudent = itemView.findViewById(R.id.btnViewStudent);
        stuid = itemView.findViewById(R.id.textd32);

    }
}
