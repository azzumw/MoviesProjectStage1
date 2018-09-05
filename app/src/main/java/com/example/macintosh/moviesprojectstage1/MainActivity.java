package com.example.macintosh.moviesprojectstage1;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.model.Movie;
import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {


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

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        String END_POINT = pref.getString(getString(R.string.Pref_Key),getString(R.string.popular));


        new MoviesAsyncTask().execute(END_POINT);
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

        String title = movie.getTitle();
        int id = movie.getid();
        int voteCount = movie.getVoteCount();
        String imageUrl = movie.getImage();
        String plotSynopsis = movie.getPlotSynopsis();
        int plotAverage = movie.getPlotAverage();
        String releaseDate = movie.getReleaseDate();

        intent.putExtra("title", title);
        intent.putExtra("id",id);
        intent.putExtra("image",imageUrl);
        intent.putExtra("releaseDate",releaseDate);
        intent.putExtra("plotSynopsis",plotSynopsis);
        intent.putExtra("plotAverage",plotAverage);

        startActivity(intent);


    }

    public class MoviesAsyncTask extends AsyncTask<String,Void,ArrayList<Movie>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... urls) {

            final String searchURL = urls[0];

            URL movieRequestURL = null;

                movieRequestURL = NetworkUtils.buildUrl(searchURL);


            String jsonResult;
            if(urls.length==0){
                return null;
            }

            try {
                jsonResult = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);

                ArrayList<Movie> simpleJsonMovieData = NetworkUtils.getJSONData(jsonResult);


                return simpleJsonMovieData;

            } catch (IOException e) {
                e.printStackTrace(); return null;
            } catch (JSONException je){
                je.getStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {
            progressBar.setVisibility(View.INVISIBLE);

            if(movieData!= null){
                showJsonDataView();
                mMovieAdapter.setMovieData(movieData);
            }
            else{
                showErrorMessage();
            }
        }
    }


}
