package com.ui.AdminStaff;

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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koolearn.klibrary.ui.android.R;
import com.koolearn.klibrary.ui.android.databinding.FragmentAdminstaffBinding;
import com.ui.profile.UserProfile;

import java.util.ArrayList;

public class AdminStaffFramgnet extends Fragment {

    private FragmentAdminstaffBinding binding;
    private CardView AdminViewBookingInfo, AdminAddVoucher, AdminEditBookInfo, EditStaff, AdminFeedback, AdminStudent, bookingbook;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    TextView staffAdminName;
    ArrayList<StaffList> list;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdminstaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_adminstaff, container,false);

        AdminViewBookingInfo = view.findViewById(R.id.cvAdminViewBookingInfo);
        AdminAddVoucher = view.findViewById(R.id.AdminAddVoucher);
        AdminEditBookInfo = view.findViewById(R.id.cvAdminChangeBookInfo);
        EditStaff = view.findViewById(R.id.cvAdminEditStaff);
        staffAdminName = view.findViewById(R.id.AdminName);
        AdminFeedback = view.findViewById(R.id.cvAdminFeedback);
        AdminStudent = view.findViewById(R.id.cvAdminStudent);
        bookingbook = view.findViewById(R.id.cvBookingBook);

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

        AdminAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ChangeVoucher.class);

                startActivity(intent);
            }
        });

        AdminFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewFeedback.class));
            }
        });

        AdminStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewStudent.class));

            }
        });

        AdminEditBookInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewBook.class));

            }
        });

        AdminViewBookingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewBookingroom.class));

            }
        });

        bookingbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewBookingBook.class));

            }
        });


        return view;
    }
}