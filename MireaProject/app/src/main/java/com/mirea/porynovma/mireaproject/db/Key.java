package com.mirea.porynovma.mireaproject.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Key {
    @PrimaryKey()
    @NonNull
    public String file = "//";
    public byte[] key;
}
