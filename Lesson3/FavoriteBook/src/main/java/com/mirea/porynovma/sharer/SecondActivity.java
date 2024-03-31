package com.mirea.porynovma.sharer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {
    private EditText edit;
    private TextView bookText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        edit = findViewById(R.id.editText);
        bookText = findViewById(R.id.textView2);

        String bookName = getIntent().getStringExtra(MainActivity.KEY);
        bookText.setText(String.format("Любимая книга разработчика: %s", bookName));
    }

    public void onSend(View view) {
        String bookName = edit.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(MainActivity.USER_MESSAGE, bookName);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}