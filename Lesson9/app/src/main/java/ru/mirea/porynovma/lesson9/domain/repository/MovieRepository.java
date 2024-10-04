package ru.mirea.porynovma.lesson9.domain.repository;

import ru.mirea.porynovma.lesson9.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();
}
