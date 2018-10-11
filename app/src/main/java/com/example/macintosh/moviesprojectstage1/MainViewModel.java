package com.example.macintosh.moviesprojectstage1;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.macintosh.moviesprojectstage1.database.AppDatabase;
import com.example.macintosh.moviesprojectstage1.database.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    //data to cache
    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
         movies = AppDatabase.getsInstance(this.getApplication()).movieDao().loadAllFavouriteMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
