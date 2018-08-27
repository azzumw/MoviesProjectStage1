package com.example.macintosh.moviesprojectstage1;

import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mURLResults;
    private TextView mSearchResults;
    private TextView mErrorMessagetv;

    private ProgressBar progressBar;

    private final String API_KEY = "51ed01ec1db0ac9a518638cb27934aec";
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
        new MoviesAsyncTask().execute(API_KEY);
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
            case R.id.search_id: loadMovieData(); return false;
            default:return true;
        }

    }

    public class MoviesAsyncTask extends AsyncTask<String,Void,String[]>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... urls) {
            String searchURL = urls[0];
            URL movieRequestURL = NetworkUtils.buildUrl(searchURL);
            mURLResults.setText(movieRequestURL.toString());
            String jsonResult = null;
            if(urls.length==0){
                return null;
            }

            try {
                jsonResult = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);

                String [] simpleJsonMovieData = NetworkUtils.getJSONData(jsonResult);


                return simpleJsonMovieData;

            } catch (IOException e) {
                e.printStackTrace(); return null;
            } catch (JSONException je){
                je.getStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
            progressBar.setVisibility(View.INVISIBLE);

            if(movieData!= null){
                showJsonDataView();
                for (String movie : movieData){
                    mSearchResults.append(movie + "\n");
                }
            }
            else{
                showErrorMessage();
            }
        }
    }


}
