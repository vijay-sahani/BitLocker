package com.bitlocker.android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bitlocker.android.Adapters.NoteListAdapter;
import com.bitlocker.android.DatabaseUtil.BuildRoomDB;
import com.bitlocker.android.Listeners.NotesClickListener;
import com.bitlocker.android.Models.Notes;
import com.bitlocker.android.databinding.SecureNotesViewBinding;
import com.bitlocker.android.databinding.ToolbarViewBinding;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SecureNotesView extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    SecureNotesViewBinding binding;
    RecyclerView recyclerView;
    NoteListAdapter noteListAdapter;
    List<Notes> notes = new ArrayList<>();
    BuildRoomDB database;
    Notes selectedNote;
    Context context;
    private static int pinnedItemsSize;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=SecureNotesViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=getApplicationContext();
        setSupportActionBar(binding.toolbar.getRoot());
        binding.toolbar.getRoot().setTitle("Secure Notes");
        recyclerView = binding.notesRecyclerView;
        database = BuildRoomDB.getInstance(this);

        // Updating the recyclerview
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                int position = (int) result.getData().getSerializableExtra("noteId");
                if (position == -1) {
                    Notes new_notes = (Notes) result.getData().getSerializableExtra("notes");
                    database.notesDAO().insert(new_notes);
                    new_notes.setID(database.notesDAO().getLastRecordId());
                    notes.add(pinnedItemsSize, new_notes);
                    noteListAdapter.notifyItemInserted(pinnedItemsSize);
                } else {
                    Notes old_note = (Notes) result.getData().getSerializableExtra("notes");
                    database.notesDAO().update(old_note.getID(), old_note.getTitle(), old_note.getNotes());
                    notes.set(position, old_note);
                    noteListAdapter.notifyItemChanged(position);
                }
            }
        });

        binding.fab.setOnClickListener(view -> {
            Intent intent=new Intent(context,AddSecureNote.class);
            intent.putExtra("noteId", -1);
            activityResultLauncher.launch(intent);
        });
        notes.addAll(database.notesDAO().getAll());

        updateRecycler();
        pinnedItemsSize = getPinnedItemsSize();
    }
    private void updateRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        noteListAdapter = new NoteListAdapter(SecureNotesView.this, notes, notesClickListener);
        recyclerView.setAdapter(noteListAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes, int position) {
            Intent intent = new Intent(context, AddSecureNote.class);
            intent.putExtra("notes", notes);
            intent.putExtra("noteId", position);
            activityResultLauncher.launch(intent);
        }

        @Override
        public void onLongClick(Notes notes, MaterialCardView cardView) {
            selectedNote = notes;
            showPopUp(cardView);
        }

        private void showPopUp(MaterialCardView cardView) {
            PopupMenu popupMenu = new PopupMenu(context, cardView);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(SecureNotesView.this);
            popupMenu.show();
        }
    };

    private int getPinnedItemsSize() {
        int pinnedItems = 0;
        while (pinnedItems < notes.size() && notes.get(pinnedItems).isPinned()) {
            pinnedItems++;
        }
        return pinnedItems;
    }

    /*
     * Build by Vijay Sahani
     * Using Binary search logic to find the exact position of the recently unpinned item
     */
    private int getOriginalIndex(int targetId) {
        if (notes.size() <= 1) return 0;
        int low = pinnedItemsSize;
        int high = notes.size() - 1;
        while (low < high) {// To ensure that there are at least two elements in the arr
            int mid = (low + high) >> 1;
            notes.forEach(notes1 -> System.out.println(notes1.getID()));
            if (targetId > notes.get(mid).getID() && targetId > notes.get(low).getID())
                return low - 1;
            if (targetId < notes.get(mid).getID() && targetId > notes.get(mid + 1).getID()) {
                return mid;
            } else if (targetId > notes.get(mid).getID()) {
                high = mid - 1;
            } else low = mid + 1;
        }
        return high;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int prevIdx = notes.indexOf(selectedNote);
        switch (menuItem.getItemId()) {
            case R.id.pin:
                database.notesDAO().pin(selectedNote.getID(), !selectedNote.isPinned());
                Toast.makeText(context, selectedNote.isPinned() ? "Note Unpinned" : "Note Pinned", Toast.LENGTH_SHORT).show();
                int newIdx = selectedNote.isPinned() ? getOriginalIndex(selectedNote.getID()) : 0;
                if (!selectedNote.isPinned()) pinnedItemsSize++;
                else pinnedItemsSize--;
                selectedNote.setPinned(!selectedNote.isPinned()); // change the pinned tag
                recyclerView.smoothScrollToPosition(newIdx);
                notes.remove(prevIdx);// remove the previous item first
                notes.add(newIdx, selectedNote);// now add the updated pinned note
                noteListAdapter.notifyItemChanged(prevIdx);
                noteListAdapter.notifyItemMoved(prevIdx, newIdx);
                return true;
            case R.id.delete:
                database.notesDAO().delete(selectedNote);
                if (selectedNote.isPinned()) {
                    pinnedItemsSize--;
                }
                recyclerView.removeViewAt(prevIdx);
                notes.remove(prevIdx);
                noteListAdapter.notifyItemRemoved(prevIdx);
                return true;
        }
        return false;
    }
}