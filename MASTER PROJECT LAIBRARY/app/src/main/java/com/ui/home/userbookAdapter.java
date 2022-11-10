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

public class userbookAdapter extends RecyclerView.Adapter<userbookAdapter.userbookViewHolder>{

    Context context;
    ArrayList<AdminBook> list;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    public userbookAdapter(FirebaseRecyclerOptions<AdminBook> options) {
    }

    public userbookAdapter(Context context, ArrayList<AdminBook> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public userbookAdapter.userbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_listofbook, parent, false);
        return new userbookAdapter.userbookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull userbookAdapter.userbookViewHolder holder, int position) {
        AdminBook adminBook = list.get(position);
        holder.name.setText(adminBook.getAdminBookName());
        holder.title.setText(adminBook.getAdminBookName());
        holder.price.setText(adminBook.getAdminbookPrice());

        firebaseStorage = FirebaseStorage.getInstance();

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Books").child(adminBook.getAdminBookID()).child("images").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(holder.pic);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class userbookViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, title;
        ImageView pic;

        public userbookViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textbook22);
            price = itemView.findViewById(R.id.textbook32);
            title = itemView.findViewById(R.id.titlebook);
            pic = itemView.findViewById(R.id.ivBookPic);

        }

    }

}
