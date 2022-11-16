package com.ui.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koolearn.klibrary.ui.android.R;
import com.squareup.picasso.Picasso;
import com.ui.AdminStaff.AdminBook;

import java.util.ArrayList;
import java.util.Objects;

public class userbookAdapter extends FirebaseRecyclerAdapter<AdminBook,userbookAdapter.userbookViewHolder> {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    public userbookAdapter(@NonNull FirebaseRecyclerOptions<AdminBook> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull userbookAdapter.userbookViewHolder holder, int position, @NonNull AdminBook model) {
        holder.name.setText(model.getAdminBookName());
        holder.title.setText(model.getAdminBookName());
        holder.price.setText(model.getAdminbookPrice());
        holder.des.setText(model.getAdminBookDescription());

        firebaseStorage = FirebaseStorage.getInstance();

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Books").child(model.getAdminBookID()).child("images").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(holder.pic);

            }
        });
    }

    @NonNull
    @Override
    public userbookAdapter.userbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listofbook,parent,false);
        return new userbookAdapter.userbookViewHolder(view);
    }

    class userbookViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, title, des;
        ImageView pic;

        public userbookViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textbook22);
            price = itemView.findViewById(R.id.textbook32);
            title = itemView.findViewById(R.id.titlebook);
            pic = itemView.findViewById(R.id.ivBookPic);
            des = itemView.findViewById(R.id.textbook42);
        }
    }
}
