package com.mirea.porynovma.toastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.editText);
    }

    public void onClickToast(View view) {
        String chars = String.valueOf(txt.getText().length());
        Toast toast = Toast.makeText(
                getApplicationContext(),
                "СТУДЕНТ №21 ГРУППА БСБО-10-21 Количество символов - " + chars,
                Toast.LENGTH_SHORT
                );
        toast.show();
    }
}