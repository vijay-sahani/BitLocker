package com.bitlocker.android.Listeners;

import com.bitlocker.android.Models.Notes;
import com.google.android.material.card.MaterialCardView;

public interface NotesClickListener {
    void onClick(Notes notes, int position);

    void onLongClick(Notes notes, MaterialCardView cardView);
}
