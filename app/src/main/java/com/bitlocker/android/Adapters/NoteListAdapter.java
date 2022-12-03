package com.bitlocker.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitlocker.android.Listeners.NotesClickListener;
import com.bitlocker.android.Models.Notes;
import com.bitlocker.android.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NotesViewHolder> {

    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NoteListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteListAdapter.NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_note.setText(list.get(position).getNotes());
        holder.textView_date.setText(list.get(position).getDate());

        if (list.get(position).isPinned()) {
            holder.imageView_pin.setImageResource(R.drawable.pin);
        } else {
            holder.imageView_pin.setImageResource(0);
        }
        holder.notes_container.setStrokeColor(holder.itemView.getResources().getColor(getRandomColor(), null));
        holder.notes_container.setOnClickListener(view -> listener.onClick(list.get(holder.getAdapterPosition()), position));


        holder.notes_container.setOnLongClickListener(view -> {
            listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
            return true;
        });
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        // pick a random color
        Random random = new Random();
        int random_color = random.nextInt(colorCode.size());
        return colorCode.get(random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class NotesViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView notes_container;
        TextView textView_title, textView_date, textView_note;
        ImageView imageView_pin;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_container = itemView.findViewById(R.id.note_container);
            textView_title = itemView.findViewById(R.id.textview_title);
            textView_note = itemView.findViewById(R.id.textview_note);
            textView_date = itemView.findViewById(R.id.textview_date);
            imageView_pin = itemView.findViewById(R.id.imageView_pin);

        }
    }
}
