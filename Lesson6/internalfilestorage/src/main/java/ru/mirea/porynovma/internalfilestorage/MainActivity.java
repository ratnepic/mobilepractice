package ru.mirea.porynovma.internalfilestorage;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;

import ru.mirea.porynovma.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        bind.button.setOnClickListener(v -> {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = openFileOutput("mirea.txt", Context.MODE_PRIVATE);
                fileOutputStream.write((bind.editDate.getText().toString() + "\n" + bind.editDesc.getText().toString()).getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}