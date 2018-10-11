package com.example.macintosh.moviesprojectstage1;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.macintosh.moviesprojectstage1.database.AppDatabase;
import com.example.macintosh.moviesprojectstage1.database.AppExecutors;
import com.example.macintosh.moviesprojectstage1.database.Movie;
import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private Parcelable mListState;
    private final String PARCEL_KEY = "ListState";
    private final String URL_KEY = "URL_KEY";
    private TextView mErrorMessagetv;

    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private GridLayoutManager gridLayoutManager;


    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("OncREATE","Activity created");
        mErrorMessagetv = findViewById(R.id.tv_error_message_display);
        progressBar = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.rv_main_act);


        gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this,getApplicationContext());

        mDb = AppDatabase.getsInstance(getApplicationContext());
        loadMovieData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("OnSaveInstanceState", "Saving State..");
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(PARCEL_KEY,mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null){
            Log.e("OnRestoreInstanceState", "Restoring State..");
            mListState = savedInstanceState.getParcelable(PARCEL_KEY);
        }
    }

    private void loadMovieData(){

        showJsonDataView();

        String sharedPreference = getSharedPreferenceValue();

        if(sharedPreference.equals(getString(R.string.favourites))){


            //database
//            final LiveData<List<Movie>> movieList = mDb.movieDao().loadAllFavouriteMovies();
            MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
            viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    Log.e("MainACTIVITY: ", "Receving database update from LIVEDATA");
                    setMovies(movies);
                }
            });

        }else {
            //network
            final URL searchURL = NetworkUtils.buildUrl(sharedPreference);

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
                                List<Movie> movies = NetworkUtils.getJSONData(httpResponse[0]);
                                setMovies(movies);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        }
    }

    private void setMovies(List<Movie> movies) {
        mMovieAdapter.setMovieData(movies);

        if(mListState!=null){
            Log.e("OnResume", "Retreiving State..");
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }
    }

    /**
     * Show the Json data and hide the error message
     * */
    private void showJsonDataView(){
        mErrorMessagetv.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mErrorMessagetv.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemClickedID = item.getItemId();

        switch (itemClickedID){
            case R.id.sort_by_id: startActivity(new Intent(this,PreferenceSortActivity.class)); return true;
            default:return true;
        }

    }

    @Override
    public void onClickHandler(Movie movie) {
        Class detailActivityClass = DetailActivity.class;

        Intent intent = new Intent(this,detailActivityClass);

        intent.putExtra("Movie",movie);
        startActivity(intent);
    }

    private String getSharedPreferenceValue(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String END_POINT = sharedPreferences.getString(getString(R.string.Pref_Key),getString(R.string.popular));
        return END_POINT;
    }

}
