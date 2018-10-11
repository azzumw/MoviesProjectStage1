package com.example.macintosh.moviesprojectstage1;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.database.AppDatabase;
import com.example.macintosh.moviesprojectstage1.database.AppExecutors;
import com.example.macintosh.moviesprojectstage1.database.Movie;

import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        textView = findViewById(R.id.textview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LiveData<List<Movie>> movies = AppDatabase.getsInstance(FavouritesActivity.this).movieDao().loadAllFavouriteMovies();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                textView.setText(movies.get(0).getId() + movies.get(0).getTitle());
            }
        });
    }


}
