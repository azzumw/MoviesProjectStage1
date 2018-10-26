package com.example.macintosh.moviesprojectstage1;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.moviesprojectstage1.database.AppDatabase;
import com.example.macintosh.moviesprojectstage1.database.AppExecutors;
import com.example.macintosh.moviesprojectstage1.database.Movie;
import com.example.macintosh.moviesprojectstage1.database.Review;
import com.example.macintosh.moviesprojectstage1.database.Trailer;
import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    private TextView tvReleaseDateValue;
    private TextView tvTitleValue;
    private TextView tvPlotVotes;
    private TextView tvPlotSynopsis;
    private ImageView posterView;

    private ImageButton favouriteImageButton;
    private AppDatabase mDb;

    private Movie movie;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mRecyclerView;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.custom_theme_color));

        assignViews();

        Intent intent = getIntent();
        movie = intent.getParcelableExtra("Movie");

        String title = movie.getTitle();
        String imageUrl = movie.getImageUrl();
        String plotSynopsis = movie.getPlotSynopsis();
        int plotAverage = movie.getPlotAverage();
        String releaseDate = movie.getReleaseDate();

        setupViews(title,imageUrl,plotSynopsis,plotAverage,releaseDate);

        mDb = AppDatabase.getsInstance(this);
        Log.e("Detail Activity: ",movie.toString());

        setFavouriteState(movie.getId());

        favouriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavouriteButtonClicked(movie);
            }
        });


//        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

//        mRecyclerView.setLayoutManager(linearLayoutManager);

//        mRecyclerView.setHasFixedSize(true);

//        mTrailerAdapter = new TrailerAdapter(getApplicationContext(),this);
//        mReviewAdapter = new ReviewAdapter();
        // make network request to get the video links

//        mRecyclerView.setAdapter(mTrailerAdapter);

//        if(isOnline()){
//            loadTrailers();
//        }else{
//            displayErrorMessage();
//        }

        //viewpager and tablayout



        sendDataToFragments();

    }

    private void sendDataToFragments(){

        CustomViewPager viewPager = findViewById(R.id.viewPagerId);
        FragmentManager fm = getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fm);
        viewPagerAdapter.addFragment(FragmentTrailer.getInstance(movie.getId()),getString(R.string.trailerPageTitle));
        viewPagerAdapter.addFragment(FragmentReviews.getInstance(movie.getId()),getString(R.string.reviewPageTitle));


//        Bundle bundle = new Bundle();
//        bundle.putInt("movie_id",movie.getId());
        TabLayout tabLayout = findViewById(R.id.tablayout_id);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(viewPagerAdapter);

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
                        Log.e("DetailActivity: ", "final else");
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
        //check if this movie object isFavourite.
        //if no, set isFavourite  = true;
        //else, set isFavourite = false;

        if(movie.getFavourite()){
            //set isFavourite = false;
            //remove from database
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

        updateFavouriteImage();
    }


    private void assignViews(){
        tvPlotSynopsis = findViewById(R.id.tvPlotSynopsisValue);
        tvTitleValue = findViewById(R.id.tvTitle);
        tvReleaseDateValue = findViewById(R.id.releaseDateValue);
        tvPlotVotes = findViewById(R.id.plotAvgValue);
        posterView = findViewById(R.id.posterId);
        favouriteImageButton = findViewById(R.id.favouriteBtnId);
//        mRecyclerView = findViewById(R.id.rv_detailAct_trailerslist);
    }

    private void setupViews(String title,String img, String plotsyn, int plotAvg, String relDate){
        Picasso.with(this).load(img).into(posterView);
        tvTitleValue.setText(title);
        tvReleaseDateValue.setText(relDate);
        tvPlotVotes.setText(String.valueOf(plotAvg));
        tvPlotSynopsis.setText(plotsyn);
        favouriteImageButton.setImageResource(R.drawable.baseline_favorite_border_black_18dp);
    }

    private void loadTrailers(){

        int id = movie.getId();
        final URL searchURL = NetworkUtils.buildUrl(id,EndPoints.VIDEOS.getType());

        final String[] httpResponse = new String[1];
        AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    httpResponse[0] = NetworkUtils.getResponseFromHttpUrl(searchURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Trailer> trailersList = NetworkUtils.getJSONTrailerData(httpResponse[0]);
                            setTrailers(trailersList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }



    private void setTrailers(List<Trailer> trailers) {
            mTrailerAdapter.setTrailerData(trailers);
    }


//    @Override
//    public void onClickHandler(Trailer trailer) {

//        String videoId = trailer.getKey();
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+videoId));
//        intent.putExtra("VIDEO_ID", videoId);
//        startActivity(intent);
//    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void displayErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);

    }



}
