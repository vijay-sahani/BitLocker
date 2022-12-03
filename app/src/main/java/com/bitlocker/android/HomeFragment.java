package com.bitlocker.android;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitlocker.android.Adapters.LoginAdapter;
import com.bitlocker.android.Listeners.LoginViewClickListener;
import com.bitlocker.android.Models.LoginCredential;
import com.bitlocker.android.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private static final String ARG_PARAM1 = "username";
    private static final String ARG_PARAM2 = "title";
    private static final String ARG_PARAM3 = "password";
    private static final String ARG_PARAM4 = "url";
    RecyclerView recyclerView;
    LoginAdapter loginAdapter;
    List<LoginCredential> loginCredentials = new ArrayList<>();
    Context context;
    FloatingActionButton add_new;

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        context = getContext();
//        add_new = findViewById(R.id.fab);
        recyclerView = binding.recyclerView;
//        add_new.setOnClickListener(view -> handleBottomInput(null));
        loginCredentials.add(new LoginCredential("Microsoft", "microsofl@soft.com", "mypasswrod", "microsof.com"));
        loginCredentials.add(new LoginCredential("Facebook", "facebook@book.com", "faceishit", "facebook.com"));
        loginCredentials.add(new LoginCredential("Google", "google@gmail.com", "show are you", "gogo.com"));
        updateRecycler(loginCredentials);
        return binding.getRoot();
    }

    private void updateRecycler(List<LoginCredential> pass_info) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        loginAdapter = new LoginAdapter(context, pass_info, loginViewClickListener);
        recyclerView.setAdapter(loginAdapter);
    }

    private void handleBottomInput(LoginCredential loginCredential) {
        BottomLoginInput bottomInput = new BottomLoginInput();
        if (loginCredential != null) {
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, loginCredential.getUsername());
            args.putString(ARG_PARAM2, loginCredential.getTitle());
            args.putString(ARG_PARAM3, loginCredential.getPassword());
            args.putString(ARG_PARAM4, loginCredential.getUrl());
            bottomInput.setArguments(args);
        }
        bottomInput.setInputListener((title, username, password, url, action) -> {
            if (action.equals("new")) {
                loginCredentials.add(new LoginCredential(title, username, password, url));
                loginAdapter.notifyItemInserted(loginCredentials.size() - 1);
            } else {
                loginCredential.setTitle(title);
                loginCredential.setPassword(password);
                loginCredential.setUsername(username);
                loginCredential.setUrl(url);
                loginAdapter.notifyItemChanged(loginCredentials.indexOf(loginCredential));
            }
        });
        bottomInput.show(requireActivity().getSupportFragmentManager(), "ModalBottomSheet");
    }

    private final LoginViewClickListener loginViewClickListener = (info, position) -> {
        handleBottomInput(info);
    };
}