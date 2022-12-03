package com.bitlocker.android;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bitlocker.android.Listeners.BottomLoginInputListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

public class BottomLoginInput extends BottomSheetDialogFragment {
    TextInputEditText title, password, url,username;
    BottomLoginInputListener listener;
    String action;
    public void setInputListener(BottomLoginInputListener listener){
        this.listener=listener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_login_input,
                container, false);
        title = v.findViewById(R.id.title_input);
        username=v.findViewById(R.id.username_input);
        password = v.findViewById(R.id.password_input);
        url = v.findViewById(R.id.url_input);
        Bundle receivedData = getArguments();
        if(receivedData!=null){
            action="update";
            title.setText(receivedData.getString("title"));
            username.setText(receivedData.getString("username"));
            password.setText(receivedData.getString("password"));
            url.setText(receivedData.getString("url"));
        }
        else action="new";
        Button cancelBtn = v.findViewById(R.id.cancel_button);
        Button saveBtn = v.findViewById(R.id.save_button);
        cancelBtn.setOnClickListener(view -> {
            dismiss();
        });
        saveBtn.setOnClickListener(view -> {
            String title_text=title.getText().toString();
            String username_text=username.getText().toString();
            String password_text=password.getText().toString();
            String url_text=url.getText().toString();
            if(!title_text.isEmpty()){
                listener.sendData(title_text,username_text,password_text,url_text,action);
                dismiss();
            }
            else Toast.makeText(getContext(),"Please enter details",Toast.LENGTH_SHORT).show();
        });

        return v;
    }
}