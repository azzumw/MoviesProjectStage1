package com.example.macintosh.moviesprojectstage1;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.moviesprojectstage1.database.AppDatabase;
import com.example.macintosh.moviesprojectstage1.database.AppExecutors;
import com.example.macintosh.moviesprojectstage1.database.Movie;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView tvReleaseDateValue;
    private TextView tvTitleValue;
    private TextView tvPlotVotes;
    private TextView tvPlotSynopsis;
    private ImageView posterView;

    private ImageButton favouriteImageButton;
    private AppDatabase mDb;

    private Movie movie;
    private final String MOVIE_KEY = "Movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.custom_theme_color));

        assignViews();

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(MOVIE_KEY);

        String title = movie.getTitle();
        String imageUrl = movie.getImageUrl();
        String plotSynopsis = movie.getPlotSynopsis();
        int plotAverage = movie.getPlotAverage();
        String releaseDate = movie.getReleaseDate();

        setupViews(title,imageUrl,plotSynopsis,plotAverage,releaseDate);

        mDb = AppDatabase.getsInstance(this);
        setFavouriteState(movie.getId());

        favouriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavouriteButtonClicked(movie);
            }
        });

        sendDataToFragments();

    }

    private void sendDataToFragments(){

        final CustomViewPager viewPager = findViewById(R.id.viewPagerId);
        FragmentManager fm = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fm);
        viewPagerAdapter.addFragment(FragmentTrailer.getInstance(movie.getId()),getString(R.string.trailerPageTitle));
        viewPagerAdapter.addFragment(FragmentReviews.getInstance(movie.getId()),getString(R.string.reviewPageTitle));

        TabLayout tabLayout = findViewById(R.id.tablayout_id);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }


    public void setFavouriteState(final int id){

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {

                Movie movieFromDb = mDb.movieDao().getMovieById(id);

                if (movieFromDb == null) {
                    movie.setIsFavourite(false);
                } else {
                    int movieId = movieFromDb.getId();

                    if (movieId == id) {
                        movie.setIsFavourite(true);
                    } else {
                        movie.setIsFavourite(false);
                    }
                }

                updateFavouriteImage();
            }
        });
    }

    private void updateFavouriteImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Boolean.TRUE.equals(movie.getFavourite())) {
                    favouriteImageButton.setImageResource(R.drawable.baseline_favorite_black_18dp);
                } else {
                    favouriteImageButton.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
                }

            }
        });
    }

    public void onFavouriteButtonClicked(final Movie movie){

        if(movie.getFavourite()){
            //set isFavourite = false;
            //remove from database
            movie.setIsFavourite(false);

            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteMovie(movie);
                }
            });
            Toast.makeText(DetailActivity.this, R.string.removedfromfavmsg, Toast.LENGTH_SHORT).show();
        }
        else{
            //set isFavourite = true
            //add to database
            movie.setIsFavourite(true);

            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(movie);

                }
            });

            Toast.makeText(DetailActivity.this, R.string.addToFavMsg, Toast.LENGTH_SHORT).show();
        }

        updateFavouriteImage();
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
        tvPlotVotes.setText(String.valueOf(plotAvg).concat("/10"));
        tvPlotSynopsis.setText(plotsyn);
        favouriteImageButton.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
    }
}
