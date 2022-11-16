package com.ui.AdminStaff;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<MyViewHolder> {

    ViewStudent viewStudent;
    Context context;
    ArrayList<DownModel> downModels;

    public StudentAdapter(ViewStudent viewStudent, ArrayList<DownModel> downModels) {
        this.viewStudent = viewStudent;
        this.downModels = downModels;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewStudent.getBaseContext());
        View view = layoutInflater.inflate(R.layout.elements, null, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.stuName.setText(downModels.get(position).getName());
        holder.stulink.setText(downModels.get(position).getLink());
        holder.stuid.setText(downModels.get(position).getId());
        holder.btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFiles(holder.stuName.getContext(), downModels.get(position).getName(),".pdf",DIRECTORY_DOWNLOADS ,downModels.get(position).getLink());
            }
        });

        holder.btnViewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(viewStudent.getBaseContext(), CheckVerification.class);
                intent.putExtra("keystudentid", downModels.get(position).getId());
                viewStudent.startActivity(intent);
            }
        });

    }

    private void downloadFiles(Context context, String filename, String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, filename + fileExtension);

        downloadManager.enqueue(request);

    }

    @Override
    public int getItemCount() {
        return downModels.size();
    }
}
