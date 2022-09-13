package com.example.laibrary.ui.AdminStaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.laibrary.R;
import com.example.laibrary.databinding.FragmentAdminstaffBinding;

public class AdminStaffFramgnet extends Fragment {

    private FragmentAdminstaffBinding binding;
    private CardView ChangeBookRanking, AddNewBook, EditBookInfo, EditStaff;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdminstaffBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_adminstaff, container,false);

        ChangeBookRanking = view.findViewById(R.id.cvAdminChangeBookInfo);
        AddNewBook = view.findViewById(R.id.cvAdminChangeBookRanking);
        EditBookInfo = view.findViewById(R.id.cvAdminChangeBookInfo);
        EditStaff = view.findViewById(R.id.cvAdminEditStaff);

        EditStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StafflistsActivity.class));
            }
        });




        return view;
    }
}