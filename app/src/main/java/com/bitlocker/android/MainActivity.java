package com.bitlocker.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.bitlocker.android.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.search_button:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.generator_button:
                    replaceFragment(new GeneratorFragment());
                    break;
                case R.id.settings_button:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });
        binding.navigateFab.setOnClickListener(view -> {
            BottomSheetOptionsDialog bottomSheet = new BottomSheetOptionsDialog();
            bottomSheet.show(getSupportFragmentManager(),
                    "ModalBottomSheet");
            bottomSheet.setBottomSheetListener(position -> {
                switch (position) {
                    case 1:
                        Intent intent = new Intent(getApplicationContext(), SecureNotesView.class);
                        startActivity(intent);
                        break;
                    case 2:
                        BottomLoginInput bottomInput = new BottomLoginInput();

                        bottomInput.show(getSupportFragmentManager(), "ModalBottomSheet");
                        break;
                }
            });
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}