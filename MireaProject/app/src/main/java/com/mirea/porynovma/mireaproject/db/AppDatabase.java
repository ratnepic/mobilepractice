package com.mirea.porynovma.mireaproject.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Key.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract KeysDao keysDao();
}
