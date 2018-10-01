package com.example.macintosh.moviesprojectstage1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.database.Movie;
import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private final static int LOADER_ID = 11;
    private final String URL_KEY = "URL_KEY";
    private TextView mErrorMessagetv;

    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mErrorMessagetv = findViewById(R.id.tv_error_message_display);
        progressBar = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.rv_main_act);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this,getApplicationContext());

        mRecyclerView.setAdapter(mMovieAdapter);


        loadMovieData();

    }


    private void loadMovieData(){

        showJsonDataView();

//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());
//
//        String END_POINT = pref.getString(getString(R.string.Pref_Key),getString(R.string.popular));


        //new MoviesAsyncTask().execute(END_POINT);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Movie>> movieLoader = loaderManager.getLoader(LOADER_ID);

        if(movieLoader==null){
            loaderManager.initLoader(LOADER_ID,null,this).forceLoad();
        }else{
            loaderManager.restartLoader(LOADER_ID,null,this);
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

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie>  movieArrayList = null;

            @Override
            protected void onStartLoading() {


                if (args != null) {
                    deliverResult(movieArrayList);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    /*Force an asynchronous load. Unlike startLoading() this will ignore
                    a previously loaded data set and load a new one. This simply calls through to the implementation's onForceLoad().
                    You generally should only call this when the loader is started -- that is, isStarted() returns true.*/
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable ArrayList<Movie> data) {
                super.deliverResult(data);
                movieArrayList = data;
            }

            @Nullable
            @Override
            public ArrayList<Movie> loadInBackground() {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String END_POINT = sharedPreferences.getString(getString(R.string.Pref_Key),getString(R.string.popular));

                URL searchURL = NetworkUtils.buildUrl(END_POINT);

                try {
                    String httpResponse = NetworkUtils.getResponseFromHttpUrl(searchURL);

                    movieArrayList = NetworkUtils.getJSONData(httpResponse);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException je){
                    je.printStackTrace();
                }
//                movieArrayList.add(new Movie("Dilbar",567,567,"http://image.tmdb.org/t/p/w500/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg","releasedate",89,"description"));
                return movieArrayList;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        progressBar.setVisibility(View.INVISIBLE);

        if (data == null) {
            showErrorMessage();
        } else {
            mMovieAdapter.setMovieData(data);
            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {

    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        get the current preference
//        SharedPreferences pref = PreferenceManager
//                .getDefaultSharedPreferences(getApplicationContext());
//
//        String END_POINT = pref.getString(getString(R.string.Pref_Key),getString(R.string.popular));
//        URL url = NetworkUtils.buildUrl(END_POINT);
//        String stringUrl = url.toString();
//
//
//        put it in the state bundle
//        outState.putString(URL_KEY,stringUrl);


//    }
}
