package com.example.macintosh.moviesprojectstage1;

import android.arch.lifecycle.LiveData;
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


    private TextView tvReleaseDateValue;
    private TextView tvTitleValue;
    private TextView tvPlotVotes;
    private TextView tvPlotSynopsis;
    private ImageView posterView;

    private ImageButton favouriteImageButton;
    private AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        assignViews();

        Intent intent = getIntent();
        final Movie movie = intent.getParcelableExtra("Movie");

        String title = movie.getTitle();
        String imageUrl = movie.getImageUrl();
        String plotSynopsis = movie.getPlotSynopsis();
        int plotAverage = movie.getPlotAverage();
        String releaseDate = movie.getReleaseDate();

        setupViews(title,imageUrl,plotSynopsis,plotAverage,releaseDate);

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


    public boolean checkIfIdmatches(final int id){



        int num = mDb.movieDao().getRowCount();

//        int numRows = numberOfRowsInTable[0];

        if(num==0) {
            Log.e("Detail Activity",num+" " + num);
            return false;
        }

        else{

            Log.e("Detail Activity",num+ " " + num);


            Movie movieFromDb = mDb.movieDao().getMovieById(id);
            if(movieFromDb == null) {
                return false;
            }

            int movieId = movieFromDb.getId();

            if(movieId == id){
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


    private void assignViews(){
        tvPlotSynopsis = findViewById(R.id.tvPlotSynopsisValue);
        tvTitleValue = findViewById(R.id.tvTitle);
        tvReleaseDateValue = findViewById(R.id.releaseDateValue);
        tvPlotVotes = findViewById(R.id.plotAvgValue);
        posterView = findViewById(R.id.posterId);
        favouriteImageButton = findViewById(R.id.favouriteBtnId);
    }

    private void setupViews(String title,String img, String plotsyn, int plotAvg, String relDate){
        Picasso.with(this).load(img).into(posterView);
        tvTitleValue.setText(title);
        tvReleaseDateValue.setText(relDate);
        tvPlotVotes.setText(String.valueOf(plotAvg));
        tvPlotSynopsis.setText(plotsyn);
    }


}
