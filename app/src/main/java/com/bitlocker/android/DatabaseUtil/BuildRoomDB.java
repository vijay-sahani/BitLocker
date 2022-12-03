package com.bitlocker.android.DatabaseUtil;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bitlocker.android.Models.LoginCredential;
import com.bitlocker.android.Models.Notes;


@Database(entities = {Notes.class, LoginCredential.class}, version = 1,exportSchema = false)
public abstract class BuildRoomDB extends RoomDatabase {

    private static BuildRoomDB database;
    private static String DATABASE_NAME = "BitlockerDb";

    public synchronized static BuildRoomDB getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                            BuildRoomDB.class, DATABASE_NAME).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract NotesDAO notesDAO();
    public abstract LoginDAO loginDAO();
}

