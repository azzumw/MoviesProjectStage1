package com.example.macintosh.moviesprojectstage1;

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
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Movie> movies = AppDatabase.getsInstance(FavouritesActivity.this).movieDao().loadAllFavouriteMovies();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!movies.isEmpty()){
                            textView.setText(movies.get(0).getId() + movies.get(0).getTitle());
                        }

                    }
                });
            }
        });
    }


}
