package com.example.macintosh.moviesprojectstage1;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.model.Movie;
import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mURLResults;
    private TextView mSearchResults;
    private TextView mErrorMessagetv;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mURLResults = findViewById(R.id.tv__url_result);
        mSearchResults = findViewById(R.id.tv_search_results);
        mErrorMessagetv = findViewById(R.id.tv_error_message_display);
        progressBar = findViewById(R.id.pb_loading_indicator);

        loadMovieData();

    }


    private void loadMovieData(){
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        String END_POINT = pref.getString(getString(R.string.Pref_Key), "");
        new MoviesAsyncTask().execute(END_POINT);
    }

    /**
     * Show the Json data and hide the error message
     * */
    private void showJsonDataView(){
        mErrorMessagetv.setVisibility(View.INVISIBLE);
        mSearchResults.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mErrorMessagetv.setVisibility(View.VISIBLE);
        mSearchResults.setVisibility(View.INVISIBLE);
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

    public class MoviesAsyncTask extends AsyncTask<String,Void,ArrayList<Movie>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... urls) {

            final String searchURL = urls[0];

            URL movieRequestURL = NetworkUtils.buildUrl(searchURL);
            mURLResults.setText(movieRequestURL.toString());

            String jsonResult = null;
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
                for (Movie movie : movieData){
                    mSearchResults.append(movie.getTitle() + " " + movie.getid() + "\n");
                }
            }
            else{
                showErrorMessage();
            }
        }
    }


}
