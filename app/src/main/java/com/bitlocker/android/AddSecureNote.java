package com.bitlocker.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bitlocker.android.Models.Notes;
import com.bitlocker.android.databinding.AddSecureNoteBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSecureNote extends AppCompatActivity {
    Notes notes;
    boolean old_note = false;
    int noteId;
    AddSecureNoteBinding binding;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=AddSecureNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=getApplicationContext();

        noteId = (int) getIntent().getSerializableExtra("noteId");
        if (noteId != -1) {
            notes = (Notes) getIntent().getSerializableExtra("notes");
            binding.edittextTitle.setText(notes.getTitle());
            binding.edittextNote.setText(notes.getNotes());
            old_note = true;
        }
        binding.saveButton.setOnClickListener(view -> {
            String title = binding.edittextTitle.getText().toString();
            String note = binding.edittextNote.getText().toString();
            if (note.isEmpty()) {
                Toast.makeText(context, "Please enter something", Toast.LENGTH_SHORT).show();
            } else {

                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
                Date date = new Date();
                if (!old_note) {
                    notes = new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(note);
                notes.setDate(formatter.format(date));

                Intent intent = new Intent(context, SecureNotesView.class);
                intent.putExtra("noteId", noteId);
                intent.putExtra("notes", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}