package ru.mirea.porynovma.lesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.porynovma.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        SharedPreferences pref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();

        String group = pref.getString("group", "");
        String student = pref.getString("student", "");
        String movie = pref.getString("movie", "");

        bind.editTextText1.setText(group);
        bind.editTextText2.setText(student);
        bind.editTextText3.setText(movie);

        bind.button.setOnClickListener(v -> {
            Toast.makeText(this,"SAVED", Toast.LENGTH_SHORT).show();
            prefEditor
                    .putString("group", bind.editTextText1.getText().toString())
                    .putString("student", bind.editTextText2.getText().toString())
                    .putString("movie", bind.editTextText3.getText().toString())
                    .apply();
        });
    }
}