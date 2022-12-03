package com.bitlocker.android.Adapters;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitlocker.android.Listeners.LoginViewClickListener;
import com.bitlocker.android.Models.LoginCredential;
import com.bitlocker.android.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.PasswordViewHolder> {

    Context context;
    List<LoginCredential> list;
    LoginViewClickListener listener;
    int previousOpenIndex = -1;

    public LoginAdapter(Context context, List<LoginCredential> list, LoginViewClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PasswordViewHolder(LayoutInflater.from(context).inflate(R.layout.login_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_username.setText(list.get(position).getUsername());
        holder.view_only_password.setText(list.get(position).getPassword());
        holder.view_only_username.setText(list.get(position).getUsername());
        holder.view_only_url.setText(list.get(position).getUrl());
        // Expandable listener
        if (!list.get(position).isVisibility()) {
            TransitionManager.beginDelayedTransition(holder.login_cardview, new AutoTransition());
            holder.hiddenView.setVisibility(View.GONE);
            holder.arrow_button.setImageResource(R.drawable.ic_baseline_expand_more_24);
        } else {
            TransitionManager.beginDelayedTransition(holder.login_cardview, new AutoTransition());
            holder.hiddenView.setVisibility(View.VISIBLE);
            holder.arrow_button.setImageResource(R.drawable.ic_baseline_expand_less_24);
        }

        // copy button
        holder.edit_button.setOnClickListener(view -> {
            listener.onClick(list.get(holder.getAdapterPosition()), position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PasswordViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView login_cardview;
        TextView textView_title, textView_username;
        ImageView arrow_button, edit_button;
        LinearLayout hiddenView;
        TextInputEditText view_only_password, view_only_username,view_only_url;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            login_cardview = itemView.findViewById(R.id.pass_view);
            textView_title = itemView.findViewById(R.id.textview_title);
            textView_username = itemView.findViewById(R.id.example_username);
            arrow_button = itemView.findViewById(R.id.arrow_button);
            hiddenView = itemView.findViewById(R.id.hidden_view);
            edit_button = itemView.findViewById(R.id.edit_button);
            view_only_password = itemView.findViewById(R.id.show_only_password);
            view_only_username = itemView.findViewById(R.id.show_only_username);
            view_only_url=itemView.findViewById(R.id.show_only_url);

            arrow_button.setOnClickListener(view -> {
                LoginCredential pass = list.get(getAdapterPosition());
                pass.setVisibility(!pass.isVisibility());
                // If this is the first item which is expanding
                if (pass.isVisibility() && previousOpenIndex == -1) {
                    previousOpenIndex = getAdapterPosition();
                }
                // If previous item is expanded then close it
                else if (pass.isVisibility() && previousOpenIndex != -1) {
                    list.get(previousOpenIndex).setVisibility(false);
                    notifyItemChanged(previousOpenIndex);
                    previousOpenIndex = getAdapterPosition();
                }
                // The first item is getting closed
                 else previousOpenIndex = -1; // No item is expanded
                notifyItemChanged(getAdapterPosition());
            });
        }
    }

}
