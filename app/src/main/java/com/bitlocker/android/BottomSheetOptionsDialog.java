package com.bitlocker.android;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitlocker.android.Listeners.BottomSheetOptionsListener;
import com.bitlocker.android.databinding.BottomsheetOptionsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetOptionsDialog extends BottomSheetDialogFragment {

    private BottomSheetOptionsListener bottomSheetListener;

    public void setBottomSheetListener(BottomSheetOptionsListener listener) {
        this.bottomSheetListener = listener;
    }

    BottomsheetOptionsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomsheetOptionsBinding.inflate(inflater, container, false);

        binding.secureNoteButton.setOnClickListener(view -> {
            bottomSheetListener.selectedItem(1);
            dismiss();
        });

        binding.loginCredentialsButton.setOnClickListener(view -> {
            bottomSheetListener.selectedItem(2);
            dismiss();
        });

        return binding.getRoot();
    }
}
