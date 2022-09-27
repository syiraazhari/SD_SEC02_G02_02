package com.example.laibraryadminstaff.ui.AdminStaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.laibraryadminstaff.R;
import com.example.laibraryadminstaff.databinding.FragmentAdminstaffBinding;
import com.example.laibraryadminstaff.ui.profile.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminStaffFramgnet extends Fragment {

    private FragmentAdminstaffBinding binding;
    private CardView ChangeBookRanking, AddNewBook, EditBookInfo, EditStaff;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    TextView staffAdminName;
    ArrayList<StaffList> list;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdminstaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_adminstaff, container,false);

        ChangeBookRanking = view.findViewById(R.id.cvAdminChangeBookInfo);
        AddNewBook = view.findViewById(R.id.cvAdminChangeBookRanking);
        EditBookInfo = view.findViewById(R.id.cvAdminChangeBookInfo);
        EditStaff = view.findViewById(R.id.cvAdminEditStaff);
        staffAdminName = view.findViewById(R.id.AdminName);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("User Info").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile = snapshot.getValue(UserProfile.class);
                staffAdminName.setText( userProfile.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        EditStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StafflistsActivity.class));
            }
        });


        return view;
    }
}