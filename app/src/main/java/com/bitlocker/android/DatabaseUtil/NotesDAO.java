package com.bitlocker.android.DatabaseUtil;


import static androidx.room.OnConflictStrategy.REPLACE;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.bitlocker.android.Models.Notes;

import java.util.List;

@Dao
public interface NotesDAO {
    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes order by pinned desc, id desc")
    List<Notes> getAll();

    @Query("update notes set title=:title, note=:note where ID=:id")
    void update(int id, String title, String note);

    @Delete
    void delete(Notes notes);

    @Query("Update notes set pinned=:pin where ID=:id")
    void pin(int id, boolean pin);

    @Query("select max(ID) from notes")
    int getLastRecordId();
}
