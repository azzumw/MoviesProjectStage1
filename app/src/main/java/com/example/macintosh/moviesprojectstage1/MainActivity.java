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

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEditText;
    private TextView mURLResults;
    private TextView mSearchResults;
    private TextView mErrorMessagetv;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = findViewById(R.id.et_search);
        mURLResults = findViewById(R.id.tv__url_result);
        mSearchResults = findViewById(R.id.tv_search_results);
        mErrorMessagetv = findViewById(R.id.tv_error_message_display);
        progressBar = findViewById(R.id.pb_loading_indicator);
    }

    private void makeTMDBSearchQuery()  {

        URL url = null;

        String keyword = mSearchEditText.getText().toString();
        if (keyword.isEmpty()){
            url = NetworkUtils.buildUrl();
        }

         url = NetworkUtils.buildUrl(keyword);

        mURLResults.setText(url.toString());


        MoviesAsyncTask moviesAsyncTask = new MoviesAsyncTask();
        moviesAsyncTask.execute(url);
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
            case R.id.search_id: makeTMDBSearchQuery(); return false;
            default:return true;
        }

    }

    public class MoviesAsyncTask extends AsyncTask<URL,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;

            try {
                result = NetworkUtils.getResponseFromHttpUrl(searchURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);

            if(s!= null && s!=""){
                showJsonDataView();
                mSearchResults.setText(s);
            }
            else{
                showErrorMessage();
            }
        }
    }


}
