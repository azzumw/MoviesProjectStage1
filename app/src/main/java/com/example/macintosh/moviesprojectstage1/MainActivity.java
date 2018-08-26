package com.example.macintosh.moviesprojectstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.macintosh.moviesprojectstage1.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchEditText;
    private TextView mURLResults;
    private TextView mSearchResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchEditText = findViewById(R.id.et_search);
        mURLResults = findViewById(R.id.tv__url_result);
        mSearchResults = findViewById(R.id.tv_search_results);

        makeTMDBSearchQuery();
    }

    private void makeTMDBSearchQuery()  {
        String keyword = mSearchEditText.getText().toString();
        if (keyword.isEmpty()){
            NetworkUtils.buildUrl();
        }

        URL url = NetworkUtils.buildUrl(keyword);

        mURLResults.setText(url.toString());
        try {
            mSearchResults.setText(NetworkUtils.getResponseFromHttpUrl(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
