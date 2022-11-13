package com.ui.AdminStaff;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.koolearn.klibrary.ui.android.R;

import java.util.ArrayList;

public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.StaffListViewHolder>  {

    Context context;

    ArrayList<StaffList> list;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public StaffListAdapter(Context context, ArrayList<StaffList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StaffListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.staff_list_item, parent, false);
        return new StaffListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffListViewHolder holder, int position) {

        StaffList staffList = list.get(position);

        holder.AllUserName.setText(staffList.getUserName());
        holder.AllUserRole.setText(staffList.getUserRole());
        holder.AllUserEmail.setText(staffList.getUserEmail());

        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference viewUserProfile = FirebaseDatabase.getInstance().getReference("User Info").
                child(firebaseAuth.getUid());

        Log.i(TAG,"user id is " + staffList.getUserId());

        holder.tvEditUserOption.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.tvEditUserOption);
            popupMenu.inflate(R.menu.edit_user_menu);
            popupMenu.setOnMenuItemClickListener(item ->{
                switch (item.getItemId()){
                    case R.id.menu_view:

                        Log.i(TAG,"user name " + staffList.getUserName());
                        Intent i = new Intent(view.getContext(), ViewUserProfile.class);
                        i.putExtra("keyusername", staffList.getUserName());
                        i.putExtra("keyuserid", staffList.getUserId());
                        i.putExtra("keyuseremail", staffList.getUserEmail());
                        i.putExtra("keyuserrole", staffList.getUserRole());
                        i.putExtra("keyuserage", staffList.getUserAge());
                        i.putExtra("keyuserStudent", staffList.getUserStudent());
                        context.startActivity(i);

                        /*((Activity)context).finish();
                        Intent intent = new Intent(view.getContext(), AllUserProfile.class );
                        context.startActivity(intent);*/
                        /*Intent i2 = new Intent(view.getContext(), UpdateAllUserProfile.class);
                        i.putExtra("keyeusername", staffList.getUserName());
                        i.putExtra("keyeuserid", staffList.getUserId());
                        i.putExtra("keyeuseremail", staffList.getUserEmail());
                        i.putExtra("keyeuserrole", staffList.getUserRole());
                        i.putExtra("keyeuserage", staffList.getUserAge());
                        context.startActivity(i2);*/



                        break;
                }
                return false;
            });
            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StaffListViewHolder extends RecyclerView.ViewHolder{
        TextView AllUserName, AllUserRole, AllUserEmail, tvEditUserOption;

        public StaffListViewHolder(@NonNull View itemView) {
            super(itemView);


            AllUserEmail = itemView.findViewById(R.id.tvAllUserEmail);
            AllUserName = itemView.findViewById(R.id.tvAllUserName);
            AllUserRole = itemView.findViewById(R.id.tvAllUserRole);
            tvEditUserOption = itemView.findViewById(R.id.txt_EditUserOption);
            tvEditUserOption = itemView.findViewById(R.id.txt_EditUserOption);

        }
    }

    /*public void showPopUp(View view){
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }*/
}



