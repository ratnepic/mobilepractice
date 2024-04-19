package ru.mirea.porynovma.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.porynovma.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String data = msg.getData().getString("result");
                Log.d(MainActivity.class.getSimpleName(), data);
            }
        };
        MyLooper looper = new MyLooper(mainThreadHandler);
        looper.start();

        binding.button.setOnClickListener(v -> {
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("age", binding.editTextAge.getText().toString());
            bundle.putString("prof", binding.editTextProf.getText().toString());
            msg.setData(bundle);
            looper.mHandler.sendMessage(msg);
        });
    }
}