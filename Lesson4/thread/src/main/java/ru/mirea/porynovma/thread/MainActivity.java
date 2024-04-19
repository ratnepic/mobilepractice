package ru.mirea.porynovma.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

import ru.mirea.porynovma.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView textView;
    private Button button;
    private EditText textLessons;
    private EditText textDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        textView = binding.textView;
        button = binding.button;
        textLessons = binding.editTextLessons;
        textDays = binding.editTextDays;

        setContentView(binding.getRoot());
    }

    public void onButtonClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        int lessons = Integer.parseInt(textLessons.getText().toString());
                        int days = Integer.parseInt(textDays.getText().toString());
                        textView.setText("Среднее число пар в день: " + (lessons / days));

                    } catch (Exception e) {
                        Log.e(MainActivity.class.getSimpleName(), e.toString());
                    }
                }
            }
        }).start();
    }
}