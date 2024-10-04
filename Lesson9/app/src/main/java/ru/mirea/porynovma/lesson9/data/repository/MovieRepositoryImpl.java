package ru.mirea.porynovma.lesson9.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.porynovma.lesson9.domain.models.Movie;
import ru.mirea.porynovma.lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {
    private Context context;
    public MovieRepositoryImpl(Context context) {
        this.context = context;
    }
    @Override
    public boolean saveMovie(Movie movie){
        SharedPreferences pref = this.context.getSharedPreferences("movie", Context.MODE_PRIVATE);
        pref.edit().putString("favName", movie.getName()).apply();
        return true;
    }
    @Override
    public Movie getMovie(){
        return new Movie(1, "Game of throne");
    }
}
