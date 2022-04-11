package com.example.atigah_jefferson_s1918454;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.atigah_jefferson_s1918454.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class  MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new SearchFragment());


        binding.bottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.nav_search:
                replaceFragment(new SearchFragment());
                break;
            case R.id.nav_map:
                Intent switchActivityIntent = new Intent(this, MapFragment.class);
                startActivity(switchActivityIntent);
                break;
            case R.id.nav_plan:
                replaceFragment(new PlanFragment());
                break;
            case R.id.nav_construction:
               switchActivityIntent = new Intent(this, ConstructionFragment.class);
                startActivity(switchActivityIntent);
                break;
        }

        return true;
    }
}
