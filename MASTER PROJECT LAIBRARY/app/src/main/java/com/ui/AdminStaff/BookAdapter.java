package com.ui.AdminStaff;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BooKViewHolder> {

    Context context;
    ArrayList<AdminBook> list;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;


    public BookAdapter(Context context, ArrayList<AdminBook> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookAdapter.BooKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookAdapter.BooKViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BooKViewHolder holder, int position) {
        AdminBook adminBook = list.get(position);
        holder.uid.setText(adminBook.getAdminBookID());
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


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context , ChangeBook.class);
                intent.putExtra("keybooksname", adminBook.getAdminBookName());
                intent.putExtra("keybooksprice", adminBook.getAdminbookPrice());
                intent.putExtra("keybooksdes", adminBook.getAdminBookDescription());
                intent.putExtra("keybooksid", adminBook.getAdminBookID());
                context.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BooKViewHolder extends RecyclerView.ViewHolder {
        TextView uid, name, price, title;
        ImageView pic;

        public BooKViewHolder(@NonNull View itemView) {
            super(itemView);
            uid = itemView.findViewById(R.id.textbook12);
            name = itemView.findViewById(R.id.textbook22);
            price = itemView.findViewById(R.id.textbook32);
            title = itemView.findViewById(R.id.titlebook);
            pic = itemView.findViewById(R.id.ivBookPic);

        }

    }

}
