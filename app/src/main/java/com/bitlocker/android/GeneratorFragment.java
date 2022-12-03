package com.bitlocker.android;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitlocker.android.AlgorithmUtil.PasswordGenerator;
import com.bitlocker.android.databinding.FragmentGeneratorBinding;

import android.content.ClipboardManager;

public class GeneratorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GeneratorFragment() {
        // Required empty public constructor
    }

    FragmentGeneratorBinding binding;
    PasswordGenerator passwordGenerator;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGeneratorBinding.inflate(inflater, container, false);
        context = getContext();
        binding.lengthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.lengthText.setText("Length: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        passwordGenerator= new PasswordGenerator();
        password = passwordGenerator.getPassword(binding.lengthSeekBar.getProgress(), true, false, false);
        binding.generatedPasswordTextView.setText(password);
        updatePasswordStrengthView(password);
        binding.generatePassword.setOnClickListener(view -> {
            int len = binding.lengthSeekBar.getProgress();
            boolean capital = binding.capitalsSwitch.isChecked();
            boolean numbers = binding.numberSwitch.isChecked();
            boolean special = binding.symbolSwitch.isChecked();
            password = passwordGenerator.getPassword(len, capital, numbers, special);
            binding.generatedPasswordTextView.setText(password);
            updatePasswordStrengthView(password);
        });
        binding.copyPasswordButton.setOnClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("password", password);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Password copied!", Toast.LENGTH_SHORT).show();
        });
        return binding.getRoot();
    }


    private void updatePasswordStrengthView(String password) {
        ProgressBar progressBar = binding.progressBar;
        TextView strengthView = binding.passwordStrength;

        int strength = passwordGenerator.passwordStrength(password);
        if (strength == 0) {
            updateStrength(strengthView, progressBar, strength, "Week", 25);
        } else if (strength == 1) {
            updateStrength(strengthView, progressBar, strength, "Medium", 50);
        } else if (strength == 2) {
            updateStrength(strengthView, progressBar, strength, "Strong", 75);
        } else {
            updateStrength(strengthView, progressBar, strength, "Super Strong", 100);
        }
    }

    int[] passwordColors = {R.color.week, R.color.moderate, R.color.strong};
    String password;

    private void updateStrength(TextView strengthView, ProgressBar progressBar, int points, String strength, int fillBar) {
        strengthView.setTextColor(getResources().getColor(passwordColors[points], null));
        strengthView.setText(strength);
        progressBar.setProgress(fillBar);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(passwordColors[points], null), android.graphics.PorterDuff.Mode.SRC_IN);
    }
}