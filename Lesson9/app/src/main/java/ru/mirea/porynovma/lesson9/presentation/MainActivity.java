package ru.mirea.porynovma.lesson9.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.porynovma.lesson9.R;
import ru.mirea.porynovma.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.porynovma.lesson9.domain.repository.MovieRepository;
import ru.mirea.porynovma.lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.porynovma.lesson9.domain.usecases.SaveFilmToFavoriteUseCase;
import ru.mirea.porynovma.lesson9.domain.models.Movie;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);
        MovieRepository movieRepository = new MovieRepositoryImpl(this);
        findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new SaveFilmToFavoriteUseCase(movieRepository)
                        .execute(new Movie(2, text.getText().toString()));
                textView.setText(String.format("Save result %s", result));
            }
        });
        findViewById(R.id.buttonGetMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(
                     String.format("Save result %s",
                     movie.getName())
                );
            }
        });
    }
}