package com.ui.book;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.koolearn.klibrary.ui.android.R;
import com.squareup.picasso.Picasso;
import com.ui.AdminStaff.AdminBook;
import com.ui.AdminStaff.BookAdapter;
import com.ui.AdminStaff.ChangeBook;

import java.util.ArrayList;

public class SelectBookAdapter extends RecyclerView.Adapter<SelectBookAdapter.SelectBooKViewHolder>{

    Context context;
    ArrayList<AdminBook> list;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    public static String tempselectbookid;


    public SelectBookAdapter(Context context, ArrayList<AdminBook> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public SelectBookAdapter.SelectBooKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_showbook, parent, false);
        return new SelectBookAdapter.SelectBooKViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectBookAdapter.SelectBooKViewHolder holder, int position) {
        AdminBook adminBook = list.get(position);
        holder.name.setText(adminBook.getAdminBookName());
        holder.title.setText(adminBook.getAdminBookName());
        holder.price.setText(adminBook.getAdminbookPrice());
        holder.des.setText(adminBook.getAdminBookDescription());

        firebaseStorage = FirebaseStorage.getInstance();

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Books").child(adminBook.getAdminBookID()).child("images").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(holder.pic);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                Context context = v.getContext();
                Intent intent = new Intent(context , Booking.class);
                intent.putExtra("keysbooksname", adminBook.getAdminBookName());
                intent.putExtra("keysbooksprice", adminBook.getAdminbookPrice());
                intent.putExtra("keysbooksdes", adminBook.getAdminBookDescription());
                intent.putExtra("keysbooksid", adminBook.getAdminBookID());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Booking.tempselectbook = adminBook.getAdminBookName();
                tempselectbookid = adminBook.getAdminBookID();
                context.startActivity(intent);




                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SelectBooKViewHolder extends RecyclerView.ViewHolder {
        TextView  name, price, title, des;
        ImageView pic;

        public SelectBooKViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textsbook22);
            price = itemView.findViewById(R.id.textsbook32);
            title = itemView.findViewById(R.id.titlebook1);
            pic = itemView.findViewById(R.id.ivBookPic1);
            des = itemView.findViewById(R.id.textsbook42);
        }
    }
}
