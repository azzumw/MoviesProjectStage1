package com.example.macintosh.moviesprojectstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.moviesprojectstage1.database.AppDatabase;
import com.example.macintosh.moviesprojectstage1.database.AppExecutors;
import com.example.macintosh.moviesprojectstage1.database.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    private ImageButton favouriteImageButton;
    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvReleaseDateValue;
        TextView tvTitleValue;
        TextView tvPlotVotes;
        TextView tvPlotSynopsis;
        ImageView posterView;


        tvPlotSynopsis = findViewById(R.id.tvPlotSynopsisValue);
        tvTitleValue = findViewById(R.id.tvTitle);
        tvReleaseDateValue = findViewById(R.id.releaseDateValue);
        tvPlotVotes = findViewById(R.id.plotAvgValue);
        posterView = findViewById(R.id.posterId);
        favouriteImageButton = findViewById(R.id.favouriteBtnId);

        Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra("Movie");

        String title = movie.getTitle();
        String imageUrl = movie.getImageUrl();
        String plotSynopsis = movie.getPlotSynopsis();
        int plotAverage = movie.getPlotAverage();
        String releaseDate = movie.getReleaseDate();

        Picasso.with(this).load(imageUrl).into(posterView);
        tvTitleValue.setText(title);
        tvReleaseDateValue.setText(releaseDate);
        tvPlotVotes.setText(String.valueOf(plotAverage));
        tvPlotSynopsis.setText(plotSynopsis);

        mDb = AppDatabase.getsInstance(this);
        Log.e("Detail Activity: ",movie.toString());

        if(checkIfIdmatches(movie.getId())){
            favouriteImageButton.setImageResource(R.drawable.baseline_favorite_black_18dp);
            movie.setIsFavourite(true);

        }else{
            favouriteImageButton.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
            movie.setIsFavourite(false);
        }


        favouriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavouriteButtonClicked(movie);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkIfIdmatches(final int id){
        final int[] numberOfRowsInTable = new int[1];
        final int[] movieId = new int[1];

        numberOfRowsInTable[0] = mDb.movieDao().getRowCount();

        int numRows = numberOfRowsInTable[0];

        if(numRows==0) {
            Log.e("Detail Activity",numRows+" " + numberOfRowsInTable[0]);
            return false;
        }

        else{

            Log.e("Detail Activity",numRows+" " + numberOfRowsInTable[0]);
            Movie movieFromDb = mDb.movieDao().getMovieById(id);
            if(movieFromDb == null) {
                return false;
            }

            movieId[0] = movieFromDb.getId();

            if(movieId[0] == id){
                return true;
            }
            else {
                Log.e("DetailActivity: ","final else");
                return false;
            }
        }
    }

    public void onFavouriteButtonClicked(final Movie movie){
        //check if this movie object isFavourite.
        //if no, set isFavourite  = true;
        //else, set isFavourite = false;

        if(movie.getFavourite()){
            //set isFavourite = false;
            //remove from database
            favouriteImageButton.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
            Log.v("DetailActivity","Favourtie set to true: "+ movie.toString());
            movie.setIsFavourite(false);

            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteMovie(movie);
                }
            });
            Log.v("DetailActivity",movie.toString());
            Toast.makeText(DetailActivity.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();


        }
        else{
            //set isFavourite = true
            //add to database
            favouriteImageButton.setImageResource(R.drawable.baseline_favorite_black_18dp);
            Log.v("DetailActivity","Favourtie set to default false: "+ movie.toString());
            movie.setIsFavourite(true);


            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(movie);

                }
            });

            Toast.makeText(DetailActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
        }
    }




}
