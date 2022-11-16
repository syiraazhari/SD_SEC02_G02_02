package com.ui.AdminStaff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.koolearn.klibrary.ui.android.R;

public class ViewBooking extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private VeiwPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        tabLayout = findViewById(R.id.tablayout_booking);
        viewPager = findViewById(R.id.viewpager_booking);
        viewPageAdapter = new VeiwPageAdapter(getSupportFragmentManager());

        viewPageAdapter.AddFragment(new FragmentBookingBook(),"Book");
        viewPageAdapter.AddFragment(new FragmentBoookingRoom(),"Room");
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_book);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_room);




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}