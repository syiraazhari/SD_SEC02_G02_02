package com.ui.AdminStaff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koolearn.klibrary.ui.android.R;
import com.ui.home.FeedbackForm;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{

    Context context;
    ArrayList<FeedbackForm> list;


    public FeedbackAdapter(Context context, ArrayList<FeedbackForm> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public FeedbackAdapter.FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackAdapter.FeedbackViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.FeedbackViewHolder holder, int position) {
        FeedbackForm feedbackForm = list.get(position);
        holder.feedback.setText(feedbackForm.getUserFDfeedback());
        holder.email.setText(feedbackForm.getUserFDemail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView feedback, email;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            feedback = itemView.findViewById(R.id.textfd22);
            email = itemView.findViewById(R.id.textfd12);

        }

    }

}
