package com.mirea.porynovma.mireaproject.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface KeysDao {
    @Query("SELECT * FROM `Key`")
    List<Key> getAll();
    @Query("SELECT * FROM `Key` WHERE file = :id")
    Key getById(String id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Key employee);
    @Update
    void update(Key employee);
    @Delete
    void delete(Key employee);
}
