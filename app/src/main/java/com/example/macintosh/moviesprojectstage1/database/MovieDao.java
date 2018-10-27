package com.example.macintosh.moviesprojectstage1.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie order by title ASC")
    LiveData<List<Movie>> loadAllFavouriteMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("Select * from movie where id = :id")
    Movie getMovieById(int id);
}
